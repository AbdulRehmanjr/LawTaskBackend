package com.lawstack.app.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.ChatUserList;
import com.lawstack.app.model.User;
import com.lawstack.app.model.UserChat;
import com.lawstack.app.repository.ChatUserListRepository;
import com.lawstack.app.repository.MessageRepository;
import com.lawstack.app.service.ChatUserListService;
import com.lawstack.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatUserListImp implements ChatUserListService {

    @Autowired
    private ChatUserListRepository culRepo;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Override
    public ChatUserList addNewChatList(String userId) {

        log.info("Making a chat ist for new user", userId);

        ChatUserList chat = this.culRepo.findByUserId(userId);
        if (chat != null) {
            log.info("User list already existed");
            return null;
        }
        String id = UUID.randomUUID().toString();
        ChatUserList user = new ChatUserList();

        user.setId(id);
        user.setUserId(userId);

        return this.culRepo.save(user);
    }

    @Override
    public ChatUserList addUserToChatList(String userId, String receiverId) {

        log.info("Add new user to list {} , {}", userId, receiverId);
        boolean userFlag = false;

        ChatUserList userList = this.culRepo.findByUserId(userId);

        List<String> append = new ArrayList<>();

        List<String> list = new ArrayList<>();

        if (userList.getUsersTo() == null) {

            list.add(receiverId);
            userList.setUsersTo(list);

            userList = this.culRepo.save(userList);

            userFlag = true;

        }
        if (userFlag == false) {
            boolean isReceiverInList = userList.getUsersTo().stream()
                    .anyMatch(user -> user.equals(receiverId));
            if (!isReceiverInList) {

                append = userList.getUsersTo();
                append.add(receiverId);
                userList.setUsersTo(append);

                this.culRepo.save(userList);
            }

        }
        this.updateReceiverList(receiverId, userId);
        return userList;
    }

    private void updateReceiverList(String receiverId, String userId) {

        boolean receiverFlag = false;
        ChatUserList receiverList = this.culRepo.findByUserId(receiverId);

        List<String> list = new ArrayList<>();
        List<String> append = new ArrayList<>();

        if (receiverList.getUsersTo() == null) {

            list.add(userId);
            receiverList.setUsersTo(list);

            this.culRepo.save(receiverList);

            receiverFlag = true;
        }

        if (receiverFlag == false) {
            boolean isUserInReceiverList = receiverList.getUsersTo().stream()
                    .anyMatch(user -> user.equals(userId));
            if (!isUserInReceiverList) {

                append = receiverList.getUsersTo();
                append.add(userId);
                receiverList.setUsersTo(append);

                this.culRepo.save(receiverList);
            }
        }
    }

    @Override
    public List<UserChat> getChatListById(String userId) {
        log.info("Getting chat list user Id");
        ChatUserList userList = this.culRepo.findByUserId(userId);

        List<UserChat> users  = new ArrayList<>();
        User receiver = this.userService.getUserById(userId);

        if(receiver ==null || userList == null){
            return null;
        }

        try {
            userList.getUsersTo().stream().forEach(id -> {
                UserChat chat = new UserChat();
                chat.setId(userList.getId());
                chat.setReceiver(receiver);
                chat.setSender(this.userService.getUserById(id));
               chat.setUnRead(this.messageRepository.findByReceiverNameAndSenderNameAndIsRead(userId,id, false).size());
                users.add(chat);
            });
        } catch (Exception e) {
           log.error("No user in chat list");
        }
       

        try {
            if (users.isEmpty()) {
                return null;
            }    
        } catch (Exception e) {
            log.error("User list is empty");
            return null;
        }
        
        return users;
    }

    @Override
    public void removeUserFromChatList(String receiverId) {

    }

    @Override
    public int getCountMessages(String userId) {
        log.info("Getting Messages Count.");
        
        ChatUserList userList = this.culRepo.findByUserId(userId);
        User receiver = this.userService.getUserById(userId);

        AtomicInteger count = new AtomicInteger(0);
        
        if(receiver ==null || userList == null){
            return 0;
        }

        try {
            userList.getUsersTo().stream().forEach(id -> {
              
                int  response =  this.messageRepository.findByReceiverNameAndSenderNameAndIsRead(userId, id, false).size();
                  count.addAndGet(response);
                  log.info("Count {}",count.get());
              });  
        } catch (Exception e) {
            log.error("Error {}",e.getMessage());
        }
             
        return count.get();
    }

}

package com.lawstack.app.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.ChatUserList;
import com.lawstack.app.model.User;
import com.lawstack.app.repository.ChatUserListRepository;
import com.lawstack.app.service.ChatUserListService;
import com.lawstack.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatUserListImp implements ChatUserListService {

    @Autowired
    private ChatUserListRepository culRepo;

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
    public List<User> getChatListById(String userId) {
        log.info("Getting chat list user Id");
        List<User> users = new ArrayList<>();
        ChatUserList userList = this.culRepo.findByUserId(userId);

        userList.getUsersTo().stream().forEach(id -> {
            log.info("User Id to search", id);
            users.add(this.userService.getUserById(id));
        });

        log.info("User Info ===> {}", users.toString());
        if (users.isEmpty()) {
            return null;
        }
        return users;
    }

    @Override
    public void removeUserFromChatList(String receiverId) {

    }

}

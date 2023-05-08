package com.lawstack.app.service.implementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.ChatUserList;
import com.lawstack.app.repository.ChatUserListRepository;
import com.lawstack.app.service.ChatUserListService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatUserListImp implements ChatUserListService {

    @Autowired
    private ChatUserListRepository culRepo;

    @Override
    public ChatUserList addNewChatList(String userId) {

        log.info("Making a chat ist for new user", userId);

        ChatUserList list = this.culRepo.findByUserId(userId);
        if(list!=null){
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
    public void addUserToChatList(String userId, String receiverId) {
        log.info("Add new user to list");

        ChatUserList list = this.culRepo.findByUserId(userId);

        boolean isReceiverInList = list.getUsersTo().stream()
                .anyMatch(user -> user.equals(receiverId));

        if (!isReceiverInList) {
            log.info("Adding new reciver to database");
            list.getUsersTo().add(receiverId);
            this.culRepo.save(list);
        }

    }

    @Override
    public void removeUserFromChatList(String receiverId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeUserFromChatList'");
    }

}

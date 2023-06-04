package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.ChatUserList;
import com.lawstack.app.model.UserChat;

public interface ChatUserListService {

    ChatUserList addNewChatList(String userId);

    ChatUserList addUserToChatList(String userId,String receiverId);

    void removeUserFromChatList(String receiverId);

    List<UserChat> getChatListById(String userId);

    int getCountMessages(String userId);
}

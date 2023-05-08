package com.lawstack.app.service;

import com.lawstack.app.model.ChatUserList;

public interface ChatUserListService {

    ChatUserList addNewChatList(String userId);

    void addUserToChatList(String userId,String receiverId);

    void removeUserFromChatList(String receiverId);
}

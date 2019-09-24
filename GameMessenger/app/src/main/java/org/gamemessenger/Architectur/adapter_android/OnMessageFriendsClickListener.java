package org.gamemessenger.Architectur.adapter_android;

import org.gamemessenger.Architectur.MVC.model.MessageFriends;

public interface OnMessageFriendsClickListener {
    void onMessageClick(MessageFriends messageUser, int position);
    void onLongMessageClick(MessageFriends messageUser, int position);
}

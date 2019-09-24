package org.gamemessenger.Architectur.adapter_android;

import android.view.View;

import org.gamemessenger.Architectur.MVC.model.Friends;

public interface OnFriendClickListener {
    void onFriendClick(View v, Friends friends, int position);
    void onLongFriendClick(View v,Friends friends, int position);
}

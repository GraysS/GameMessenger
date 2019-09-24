package org.gamemessenger.Architectur.Factory;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import org.gamemessenger.Architectur.MVC.model.Friends;
import org.gamemessenger.Architectur.MVC.model.MessageFriends;

public abstract class ViewAdapters extends RecyclerView.ViewHolder {
    public ViewAdapters(@NonNull View itemView) {
        super(itemView);
    }

    public  void bind(MessageFriends messageUser) {
    }

    public  void bind(Friends messageUser, int position) {
    }

}

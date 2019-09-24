package org.gamemessenger.Architectur.adapter_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.gamemessenger.Architectur.Factory.RowsAdapter;
import org.gamemessenger.Architectur.Factory.ViewAdapters;
import org.gamemessenger.Architectur.MVC.model.MessageFriends;
import org.gamemessenger.Architectur.app.Config;
import org.gamemessenger.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

public class MessageFriendsAdapter extends RowsAdapter {
    private List<MessageFriends> userList = new ArrayList<>();

    private String my_id;
    private OnMessageFriendsClickListener onMessageFriendsClickListener;

    private int SELF = 100;

    public MessageFriendsAdapter(OnMessageFriendsClickListener onMessageFriendsClickListener) {
        super();
        this.onMessageFriendsClickListener = onMessageFriendsClickListener;
    }

    @Override
    public MessageFriendsAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(viewType == SELF) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_item_view_self, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_item_view_friend, parent, false);
        }

        return new MessageFriendsAdapter.MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewAdapters holder, int position) {
        MessageFriends user = userList.get(position);
        super.onBindViewHolder(holder,position);
        holder.bind(user);
    }

    public void addItems(MessageFriends messageFriends) {
        userList.add(messageFriends);
        notifyDataSetChanged();
    }

    public void deleteItems(MessageFriends messageFriends) {
        userList.remove(messageFriends);
        notifyDataSetChanged();
    }

    public void setItems(Collection<MessageFriends> users) {
        userList.addAll(users);
        notifyDataSetChanged();
    }

    public void setMyId(String my_id) {
        this.my_id = my_id;
    }

    public void clearItems() {
        userList.clear();
        notifyDataSetChanged();
    }

   /* public void removeItems(int position) {
        userList.remove(position);
        notifyDataSetChanged();
    }*/

    @Override
    public int getItemViewType(int position) {
        MessageFriends messageUser = userList.get(position);

        if(messageUser.getFrId().equals(my_id)) {
            return SELF;
        }
        return position;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public MessageFriends getItem(int position) {
        return userList.get(position);
    }



     class MessageViewHolder extends ViewAdapters {
        private ImageView userImageView;
        private TextView messageTextView;
        private TextView messageTextViewTime;
        private TextView messageTextViewDate;


        public MessageViewHolder(View itemView) {
            super(itemView);

            //userImageView = itemView.findViewById(R.id.profile_image_view);
            messageTextView = itemView.findViewById(R.id.tv_message);
            messageTextViewTime = itemView.findViewById(R.id.tv_message_time);
          //  messageTextViewDate = itemView.findViewById(R.id.tv_message_date);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageFriends user = userList.get(getLayoutPosition());
                    onMessageFriendsClickListener.onMessageClick(user,getAdapterPosition());

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    MessageFriends user = userList.get(getLayoutPosition());
                    onMessageFriendsClickListener.onLongMessageClick(user,getAdapterPosition());
                    return true;
                }
            });
        }

        public void bind(MessageFriends user) {

            DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm - dd.MM.yyyy");

            simpleDateFormat.setTimeZone(TimeZone.getDefault());

            messageTextView.setText(user.getMessage());


            messageTextViewTime.setHint(simpleDateFormat.format(user.getMessage_time()));


            //Picasso.with(itemView.getContext()).load(user.getImageUrl()).into(userImageView);
        }
    }
}


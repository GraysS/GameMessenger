package org.gamemessenger.Architectur.adapter_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.gamemessenger.Architectur.Factory.RowsAdapter;
import org.gamemessenger.Architectur.Factory.ViewAdapters;
import org.gamemessenger.Architectur.MVC.model.Friends;
import org.gamemessenger.Architectur.app.Config;
import org.gamemessenger.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class FriendsAdapter extends RowsAdapter {
    private List<Friends> userList = new ArrayList<>();

    private String my_id;
    private OnFriendClickListener onUserClickListener;
    private int SELF = 100;


    public FriendsAdapter(OnFriendClickListener onUserClickListener) {
        super();
        this.onUserClickListener = onUserClickListener;
    }

    @Override
    public FriendsAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_item_view, parent, false);
        return new FriendsAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewAdapters holder, int position) {
        Friends user = userList.get(position);
        super.onBindViewHolder(holder,position);
        holder.bind(user,position);
    }

    public void addItems(Friends friends) {
        userList.add(friends);
        notifyDataSetChanged();
    }
    public void updateItems(Friends friends) {
        deleteFriendItems(friends.getUid());
        userList.add(0,friends);
        notifyDataSetChanged();
    }
    public void setItems(Collection<Friends> users) {
        userList.addAll(users);
        notifyDataSetChanged();
    }

    public void clearItems() {
        userList.clear();
        notifyDataSetChanged();
    }

    public void deleteItems(Friends friends) {
        userList.remove(friends);
        notifyDataSetChanged();
    }

    public void deleteFriendItems(int user_id) {
        Iterator<Friends> iterator = userList.iterator();
        while (iterator.hasNext()) {
            synchronized (iterator){
                if(iterator.next().getUid() == user_id) {
                    iterator.remove();
                }
            }
        }
        //Friends friends = getItem(user_id);

    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public Friends getItem(int position) {
        return userList.get(position);
    }

    public void setMyId(String my_id) {
        this.my_id = my_id;
    }


    class UserViewHolder extends ViewAdapters {
        private ImageView userImageView;
        private TextView nickTextView;
        private TextView messageTextView;
        private TextView timeTextView;
        private TextView countTextView;

        public UserViewHolder(final View itemView) {
            super(itemView);

            userImageView = itemView.findViewById(R.id.profile_image_view);
            nickTextView = itemView.findViewById(R.id.tv_friends_nick);
            messageTextView = itemView.findViewById(R.id.tv_friends_message);
            timeTextView = itemView.findViewById(R.id.tv_friends_time);
            countTextView = itemView.findViewById(R.id.tv_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Friends user = userList.get(getLayoutPosition());
                    onUserClickListener.onFriendClick(v,user,getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Friends user = userList.get(getLayoutPosition());
                    onUserClickListener.onLongFriendClick(v,user,getAdapterPosition());
                    return true;
                }
            });
        }

        public void bind(Friends user, int position) {

            DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm - dd.MM.yyyy");

            simpleDateFormat.setTimeZone(TimeZone.getDefault());

            nickTextView.setText(user.getNick());
            if(user.getLast_message_time() > 0) {
                timeTextView.setHint(simpleDateFormat.format(user.getLast_message_time()));
            }

            if(user.getCount() > 0) {
                countTextView.setText(user.getCount());
            }

            if (my_id.equals(user.getFrId())) {
                messageTextView.setText("You: " + user.getLast_message());
            } else {
                messageTextView.setText(user.getLast_message());
            }




            //Picasso.with(itemView.getContext()).load(user.getImageUrl()).into(userImageView);
        }
    }
}

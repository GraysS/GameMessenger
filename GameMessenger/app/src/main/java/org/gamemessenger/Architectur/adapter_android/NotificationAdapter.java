package org.gamemessenger.Architectur.adapter_android;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.gamemessenger.Architectur.MVC.model.Notification;
import org.gamemessenger.Architectur.MVC.model.User;
import org.gamemessenger.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<Notification> userNotif = new ArrayList<>();

    private OnNotificationClickListener onBtnAddClickListener;
    private OnNotificationClickListener onBtnSpamClickListener;
    private OnNotificationClickListener onBtnCancelClickListener;

    public NotificationAdapter(OnNotificationClickListener onBtnAddClickListener,
                               OnNotificationClickListener onBtnSpamClickListener,
                               OnNotificationClickListener onBtnCancelClickListener)
    {
        this.onBtnAddClickListener = onBtnAddClickListener;
        this.onBtnSpamClickListener = onBtnSpamClickListener;
        this.onBtnCancelClickListener = onBtnCancelClickListener;
    }

    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item_view, parent, false);
        return new NotificationAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.NotificationViewHolder holder, int position) {
        Notification notification = userNotif.get(position);
        holder.bind(notification);
    }

    public void setItems(Collection<Notification> notifications) {
        userNotif.addAll(notifications);
        notifyDataSetChanged();
    }

    public void clearItems() {
        userNotif.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userNotif.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImageView;
        private TextView nickTextView;
        private Button btn_notificationAdd;
        private Button btn_notificationSpam;
        private Button btn_notificationCancel;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.profile_image_view);
            nickTextView = itemView.findViewById(R.id.tv_notification_nick);
            btn_notificationAdd = itemView.findViewById(R.id.btn_notification_add);
            btn_notificationSpam = itemView.findViewById(R.id.btn_notification_spam);
            btn_notificationCancel = itemView.findViewById(R.id.btn_notification_cancel);


            btn_notificationAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notification notification = userNotif.get(getLayoutPosition());
                    onBtnAddClickListener.onNotificationClick(notification);

                }
            });

            btn_notificationSpam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notification notification = userNotif.get(getLayoutPosition());
                    onBtnSpamClickListener.onNotificationClick(notification);
                }
            });

            btn_notificationCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notification notification = userNotif.get(getLayoutPosition());
                    onBtnCancelClickListener.onNotificationClick(notification);
                }
            });

        }

        public void bind(final Notification notification) {
            nickTextView.setText(notification.getNick());

            //Picasso.with(itemView.getContext()).load(user.getImageUrl()).into(userImageView);
        }
    }

}
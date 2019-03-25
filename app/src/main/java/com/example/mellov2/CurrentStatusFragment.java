package com.example.mellov2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CurrentStatusFragment extends Fragment {

    //=============================================

    //read in value from device
    private int percentBladderFullness = 43;

    private static String notification_title = "Bladder Fullness Status";
    private String notification_content;
    private final String CHANNEL_ID = "mello_notifications";
    private final int NOTIFICATION_ID = 001;

    //=============================================

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_status, container, false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean switch50Value = prefs.getBoolean("switch_status_50", true);
        boolean switch80Value = prefs.getBoolean("switch_status_80", true);
        boolean switch100Value = prefs.getBoolean("switch_status_100", true);

        while(true) {
            //fetch percentage from bladder

            ImageView currentStatusImage = (ImageView) view.findViewById(R.id.current_status_drop_image);

            if (percentBladderFullness > 90) {
                currentStatusImage.setImageResource(R.drawable.current_status_drop_100);
                if (switch100Value == true) {
                    notification_content = "Your bladder is at 100% fullness";
                    displayNotification(view);
                }
            } else if (percentBladderFullness > 80) {
                currentStatusImage.setImageResource(R.drawable.current_status_drop_90);
            } else if (percentBladderFullness > 70) {
                currentStatusImage.setImageResource(R.drawable.current_status_drop_80);
                if (switch80Value == true) {
                    notification_content = "Your bladder is at 80% fullness";
                    displayNotification(view);
                }
            } else if (percentBladderFullness > 60) {
                currentStatusImage.setImageResource(R.drawable.current_status_drop_70);
            } else if (percentBladderFullness > 50) {
                currentStatusImage.setImageResource(R.drawable.current_status_drop_60);
            } else if (percentBladderFullness > 40) {
                currentStatusImage.setImageResource(R.drawable.current_status_drop_50);
                if (switch50Value == true) {
                    notification_content = "Your bladder is at 50% fullness";
                    displayNotification(view);
                }
            } else if (percentBladderFullness > 30) {
                currentStatusImage.setImageResource(R.drawable.current_status_drop_40);
            } else if (percentBladderFullness > 20) {
                currentStatusImage.setImageResource(R.drawable.current_status_drop_30);
            } else if (percentBladderFullness > 10) {
                currentStatusImage.setImageResource(R.drawable.current_status_drop_20);
            } else if (percentBladderFullness > 5) {
                currentStatusImage.setImageResource(R.drawable.current_status_drop_10);
            } else {
                currentStatusImage.setImageResource(R.drawable.current_status_drop_0);
            }
            return view;

        }

    }

    public void displayNotification(View view) {
        createNotificationChannel();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID);

        notificationBuilder.setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.dashboard_icon_current)
                .setContentTitle(notification_title)
                .setContentText(notification_content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Mello Notifications";
            String description = "Bladder Fullness Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
package com.example.mellov2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CurrentStatusFragment extends Fragment {

    //=============================================

    //read in value from device
    private int percentBladderFullness = 78;


    //=============================================

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_status, container, false);

        ImageView currentStatusImage = (ImageView) view.findViewById(R.id.current_status_drop_image);

        if (percentBladderFullness > 90) {
            currentStatusImage.setImageResource(R.drawable.current_status_drop_100);
        } else if (percentBladderFullness > 80) {
            currentStatusImage.setImageResource(R.drawable.current_status_drop_90);
        } else if (percentBladderFullness > 70) {
            currentStatusImage.setImageResource(R.drawable.current_status_drop_80);
        } else if (percentBladderFullness > 60) {
            currentStatusImage.setImageResource(R.drawable.current_status_drop_70);
        } else if (percentBladderFullness > 50) {
            currentStatusImage.setImageResource(R.drawable.current_status_drop_60);
        } else if (percentBladderFullness > 40) {
            currentStatusImage.setImageResource(R.drawable.current_status_drop_50);
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

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("My notification")
//                .setContentText("50% Bladder Capacity Reached")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
}

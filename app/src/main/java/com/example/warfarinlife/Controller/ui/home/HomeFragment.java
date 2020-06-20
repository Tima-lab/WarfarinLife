package com.example.warfarinlife.Controller.ui.home;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.warfarinlife.Controller.LoginActivity;
import com.example.warfarinlife.Controller.MainMenuActivity;
import com.example.warfarinlife.Controller.ui.settingProfile.SettingProfileFragmentDirections;
import com.example.warfarinlife.Database.DBFunction;
import com.example.warfarinlife.Database.DBHelper;
import com.example.warfarinlife.Model.ProfileModel;
import com.example.warfarinlife.R;

import java.util.ArrayList;
import java.util.List;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class HomeFragment extends Fragment {

    private static final String ACTION_SNOOZE = "action";
    private TextView textMNO;
    private TextView textData;
    private TextView textTime;
    private TextView textName;
    private LinearLayout insertLayoutHome;
    private int idProfile;
    private List<View> viewList;
    private DBFunction dbFunction;
    private DBHelper dbHelper;

    private static String CHANNEL_TIME_ID = "Time channel";
    private static final int NOTIFY_TIME_ID = 100;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);



        textMNO = root.findViewById(R.id.textMNO);
        textData = root.findViewById(R.id.textData);
        textTime = root.findViewById(R.id.textTime);
        textName = root.findViewById(R.id.text_name_profile);
        insertLayoutHome = root.findViewById(R.id.insertLayoutHome);

        viewList = new ArrayList<>();

        // Инициализация бд
        dbHelper = new DBHelper(getActivity());
        dbFunction = new DBFunction(dbHelper);


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        idProfile = getActivity().getIntent().getIntExtra("id",-1);
        insertLayoutHome.removeAllViews();
        ProfileUserOnDisplay();

    }

    private void ProfileUserOnDisplay() {

        final ProfileModel profileModel = dbFunction.ProfileUserSearch(idProfile);

        textName.setText(profileModel.getName());

        if (profileModel.getMno() != 0) {
            textMNO.setText(String.valueOf(profileModel.getMno()));
        }

        if (profileModel.getDate() != null) {
            if (profileModel.getDate().length() != 0)
                textData.setText(profileModel.getDate());
        }

        if (profileModel.getTime() != null) {
            if (profileModel.getTime().length() != 0) {
                textTime.setText(profileModel.getTime());
                CreateNotificationChannel();
            }
        }

        if (profileModel.getMno() == 0 || profileModel.getDate() == null || profileModel.getDate().length() == 0 || profileModel.getTime() == null || profileModel.getTime().length() == 0)
            NotifyOnDisplay();

    }


    private void NotifyOnDisplay() {
        final View view = getLayoutInflater().inflate(R.layout.notification_profile_information_loyaut, null);
        Button btnAddInformation = view.findViewById(R.id.btnNotifySettingProfile);
        TextView textInformation = view.findViewById(R.id.textNotifySettingProfile);
        textInformation.setText("В вашем профиле не хватает данных");
        btnAddInformation.setText("Заполнить");
        viewList.add(view);

        btnAddInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenProfileSetting();
            }
        });

        insertLayoutHome.addView(view);
    }


    private void OpenProfileSetting() {
        Navigation.findNavController(getView()).navigate(R.id.action_nav_home_to_settingProfileFragment);
    }

    private void NotificationTime() {

        Intent snoozeIntent = new Intent(getContext(), MainMenuActivity.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(getContext(), 0, snoozeIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),CHANNEL_TIME_ID)
                .setSmallIcon(R.drawable.border_background_btn)
                .setContentTitle("Напоминание")
                .setContentText("Не забудьте принять лекарство")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("Напоминание")
                .setAutoCancel(true)
                .addAction(R.drawable.border_background_btn, getString(R.string.snooze), snoozePendingIntent);

        builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
        managerCompat.notify(NOTIFY_TIME_ID, builder.build());


    }

    private void AlarmClock() {
        AlarmManager manager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        
    }

    private void CreateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_time_title);
            String description = getString(R.string.notification_time_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_TIME_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
            //notificationManager.createNotificationChannel(channel);
        } else {
            //NotificationTime();
            AlarmClock();
        }
    }
}

package com.example.warfarinlife.Controller.ui.settingProfile;

import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.warfarinlife.Database.DBFunction;
import com.example.warfarinlife.Database.DBHelper;
import com.example.warfarinlife.Model.ProfileModel;
import com.example.warfarinlife.R;

public class SettingProfileFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private EditText editTextNameProfile;
    private EditText editTextMnoProfile;
    private Button btnTextDateProfile;
    private Button btnTextTimeProfile;
    private Button btnSaveProfile;
    private Button btnSettingsRecoverList;

    private int idProfile;
    String dateLoad;
    String timeLoad;

    private DBFunction dbFunction;
    private DBHelper dbHelper;

    private ProfileModel profileModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_setting, container, false);

        editTextNameProfile = view.findViewById(R.id.edit_text_profile_name);
        editTextMnoProfile = view.findViewById(R.id.edit_text_profile_mno);
        btnTextDateProfile = view.findViewById(R.id.edit_text_profile_date);
        btnTextTimeProfile = view.findViewById(R.id.edit_text_profile_time);
        btnSaveProfile = view.findViewById(R.id.btn_profile_save);
        btnSettingsRecoverList = view.findViewById(R.id.btn_settings_recover_list);

        btnSaveProfile.setOnClickListener(this);

        // Инициализация бд
        dbHelper = new DBHelper(getActivity());
        dbFunction = new DBFunction(dbHelper);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        idProfile = getActivity().getIntent().getIntExtra("id",-1);
        profileModel = dbFunction.ProfileUserSearch(idProfile);

        OutputProfileOnDisplay();

        btnSettingsRecoverList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbFunction.RecoveryTableList();
                Toast.makeText(getContext(), "Данные списка восстановлены", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigate(R.id.action_settingProfileFragment_to_nav_home);
            }
        });

        btnTextDateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDateDialog();
            }
        });

        btnTextTimeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenTimeDialog();
            }
        });
    }

    private void OutputProfileOnDisplay() {
        editTextNameProfile.setText(profileModel.getName());
        editTextMnoProfile.setText(String.valueOf(profileModel.getMno()));
        btnTextDateProfile.setText(profileModel.getDate());
        btnTextTimeProfile.setText(profileModel.getTime());
    }

    @Override
    public void onClick(View v) {
        SaveProfile();
    }

    private void SaveProfile() {
        String name = String.valueOf(editTextNameProfile.getText());
        String mno = String.valueOf(editTextMnoProfile.getText());
        dateLoad = String.valueOf(btnTextDateProfile.getText());
        timeLoad = String.valueOf(btnTextTimeProfile.getText());

        if (dbFunction.UpdateProfile(idProfile, name, mno, dateLoad, timeLoad)) {
            Toast.makeText(getContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.action_settingProfileFragment_to_nav_home);
        } else Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
    }

    private void OpenTimeDialog() {
        int min=0;
        int hour=0;
        new TimePickerDialog(getContext(), this, 12, 20,true).show();
    }

    private void OpenDateDialog() {
        new DatePickerDialog(getContext(),this,2020,1,1).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        view.setIs24HourView(true);
        btnTextTimeProfile.setText(hourOfDay + ":" + minute);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        btnTextDateProfile.setText(dayOfMonth + "/" + month + "/" + year);
    }

}

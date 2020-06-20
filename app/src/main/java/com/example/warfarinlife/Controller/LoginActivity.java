package com.example.warfarinlife.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.warfarinlife.Database.DBHelper;
import com.example.warfarinlife.Database.DBFunction;
import com.example.warfarinlife.Model.ProfileModel;
import com.example.warfarinlife.R;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private HorizontalScrollView horizontalScrollViewProfiles;
    private LinearLayout layoutCreateProfiles;
    private EditText editTextName;
    private DBHelper dbHelper;
    private List<View> viewList;
    private LinearLayout linearLayoutInto;
    private DBFunction dbFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Поиск по id
        layoutCreateProfiles = findViewById(R.id.layoutCreateProfile);
        horizontalScrollViewProfiles = findViewById(R.id.scrollViewProfiles);
        editTextName = findViewById(R.id.editTextName);
        linearLayoutInto = findViewById(R.id.linear);

        viewList = new ArrayList<>();

        // Инициализация бд
        dbHelper = new DBHelper(this);
        dbFunction = new DBFunction(dbHelper);
        SelectAllOnDisplay();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ClearAll();
        SelectAllOnDisplay();
    }

    public void CreateProfileButton(View view) {
        horizontalScrollViewProfiles.setVisibility(View.GONE);
        layoutCreateProfiles.setVisibility(View.VISIBLE);
    }
    
    public void AddProfileButton(View view) {

        if (editTextName.getText().length() == 0) {
            editTextName.setError("Введите имя!!");
        } else {
            InsertDB();
            ClearAll();
            SelectAllOnDisplay();
        }
    }
    
    private void ClearAll() {
        editTextName.setText("");
        layoutCreateProfiles.setVisibility(View.GONE);
        horizontalScrollViewProfiles.setVisibility(View.VISIBLE);
        viewList.clear();
        linearLayoutInto.removeAllViews();
    }

    private void InsertDB() {
        String name = editTextName.getText().toString();
        dbFunction.InsertUser(name);
        dbHelper.close();
    }

    private void SelectAllOnDisplay() {
        final ProfileModel[] profileModels = dbFunction.SelectAllProfile();

        if (profileModels.length > 1) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) linearLayoutInto.getLayoutParams();
            lp.gravity = Gravity.START;
            lp.leftMargin = 30;
            linearLayoutInto.setLayoutParams(lp);
        }

        for (int i = 0; i < profileModels.length; i++) {
            final View view = getLayoutInflater().inflate(R.layout.profile_layout, null);
            ImageButton selectProfile = view.findViewById(R.id.btnSelectProfile);
            TextView textNameProfile = view.findViewById(R.id.text_name_profile);
            textNameProfile.setText(profileModels[i].getName());
            viewList.add(view);

            final int id = profileModels[i].getId();
            final String name = profileModels[i].getName();

            linearLayoutInto.addView(viewList.get(i));

            selectProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickOnProfile(id, name);
                }
            });
        }
    }

    private void ClickOnProfile(int id, String name) {
        Toast.makeText(LoginActivity.this, "Name: " + name, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        startActivity(intent);
    }

}

package com.example.warfarinlife.Controller.ui.addSubstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.warfarinlife.Database.DBFunction;
import com.example.warfarinlife.Database.DBHelper;
import com.example.warfarinlife.R;

public class AddSubstanceFragment extends Fragment implements View.OnClickListener{

    private int id_profile;
    private EditText textName;
    private EditText textInfo;
    private Button btnCreate;
    private DBFunction dbFunction;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_substance, container, false);

        id_profile = AddSubstanceFragmentArgs.fromBundle(getArguments()).getIdProfile();
        textName = view.findViewById(R.id.edit_text_name_substance);
        textInfo = view.findViewById(R.id.edit_text_info);
        btnCreate = view.findViewById(R.id.btn_add_substance);

        dbHelper = new DBHelper(getContext());
        dbFunction = new DBFunction(dbHelper);

        btnCreate.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (dbFunction.InsertSubstance(id_profile, String.valueOf(textName.getText()), String.valueOf(textInfo.getText())))
            Toast.makeText(getContext(), textName.getText() + " добавлено", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(v).navigate(R.id.action_addSubstanceFragment_to_nav_gallery);
    }
}

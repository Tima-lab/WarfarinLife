package com.example.warfarinlife.Controller.ui.list;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.warfarinlife.Controller.ui.addSubstance.AddSubstanceFragmentDirections;
import com.example.warfarinlife.Database.DBFunction;
import com.example.warfarinlife.Database.DBHelper;
import com.example.warfarinlife.Model.ListModel;
import com.example.warfarinlife.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private ListView listView;
    private TextView textName;
    private TextView textInfo;
    private TextView textNotFound;
    private Button btnBack;
    private LinearLayout linearLayoutList;
    private LinearLayout linearLayoutInfo;
    private EditText searchEditText;
    private FloatingActionButton fab;
    private Button btnListItemDelete;
    private Button btnListItemCancel;
    private LinearLayout linearLayoutSettingsList;

    private DBFunction dbFunction;
    private DBHelper db;

    private int idProfile;
    private ListModel[] listModels;
    private String[] nameSubstance;
    private boolean isCheckedList = false;
    private SparseBooleanArray chosen;
    private int countDel = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_list, container, false);

        listView = root.findViewById(R.id.listView);
        textName = root.findViewById(R.id.text_name_substance);
        textInfo = root.findViewById(R.id.text_info_substance);
        linearLayoutList = root.findViewById(R.id.linearList);
        linearLayoutInfo = root.findViewById(R.id.linearInfo);
        btnBack = root.findViewById(R.id.btnBack);
        searchEditText = root.findViewById(R.id.search_edit_text);
        textNotFound = root.findViewById(R.id.text_not_found);
        btnListItemDelete = root.findViewById(R.id.btn_list_item_delete);
        btnListItemCancel = root.findViewById(R.id.btn_list_item_cancel);
        linearLayoutSettingsList = root.findViewById(R.id.linear_layout_settings_list);

        db = new DBHelper(getActivity());
        dbFunction = new DBFunction(db);

        btnBack.setOnClickListener(this);

        idProfile = getActivity().getIntent().getIntExtra("id",-1);

        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);

        fab = root.findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickFabButton();
            }
        });

        SelectAllSubstanceOnDisplay();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    SelectAllSubstanceOnDisplay();
                    textNotFound.setVisibility(View.GONE);
                }
                else
                    if (SearchResOnDisplay(s.toString()) == 0)
                        textNotFound.setVisibility(View.VISIBLE);
                    else textNotFound.setVisibility(View.GONE);
            }
        });


        btnListItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < chosen.size(); i++) {
                    int idSubstance = listModels[chosen.keyAt(i)].getId_list();
                    String nameSubstance = listModels[chosen.keyAt(i)].getName();
                    if (dbFunction.DeleteSubstance(idSubstance,nameSubstance)) countDel++;
                }

                Toast.makeText(getContext(), "Удалено " + countDel + "!", Toast.LENGTH_SHORT).show();
                countDel = 0;
                SelectAllSubstanceOnDisplay();
                isCheckedList = false;

                linearLayoutSettingsList.setVisibility(View.GONE);
            }
        });

        btnListItemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSettingsList.setVisibility(View.GONE);
                isCheckedList = false;
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, nameSubstance);
                listView.setAdapter(adapter);
            }
        });

    }

    private void SelectAllSubstanceOnDisplay() {
        fab.setVisibility(View.VISIBLE);
        linearLayoutSettingsList.setVisibility(View.GONE);
        listModels = dbFunction.SelectAllSubstance(idProfile);
        nameSubstance = new String[listModels.length];

        for (int i = 0; i < listModels.length; i++) {
            nameSubstance[i] = listModels[i].getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, nameSubstance);
        listView.setAdapter(adapter);

    }

    private int SearchResOnDisplay(String text) {
        fab.setVisibility(View.GONE);
        linearLayoutSettingsList.setVisibility(View.GONE);
        listModels = dbFunction.Search(idProfile,text);
        nameSubstance = new String[listModels.length];

        for (int i = 0; i < listModels.length; i++) {
            nameSubstance[i] = listModels[i].getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, nameSubstance);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckListItem(parent,position);
                fab.setVisibility(View.GONE);
            }
        });

        return listModels.length;

    }

    @Override
    public void onClick(View v) {
        ReplaceInfoOnList();
    }

    private void ReplaceListOnInfo(int position, ListModel[] listModels) {
        String name = listModels[position].getName();
        String info = listModels[position].getInfo();
        linearLayoutList.setVisibility(View.GONE);
        linearLayoutInfo.setVisibility(View.VISIBLE);
        textName.setText(name);
        textInfo.setText(info);
    }

    private void ReplaceInfoOnList() {
        fab.setVisibility(View.VISIBLE);
        linearLayoutInfo.setVisibility(View.GONE);
        linearLayoutList.setVisibility(View.VISIBLE);
    }

    private void ClickFabButton() {
        ListFragmentDirections.ActionNavGalleryToAddSubstanceFragment action = ListFragmentDirections.actionNavGalleryToAddSubstanceFragment();
        action.setIdProfile(idProfile);
        Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        isCheckedList = true;
        fab.setVisibility(View.GONE);
        linearLayoutSettingsList.setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_checked, nameSubstance);
        listView.setAdapter(adapter);


        listView.setItemChecked(position, true);
        chosen = ((ListView) parent).getCheckedItemPositions();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckListItem(parent,position);
    }

    private void CheckListItem(AdapterView<?> parent,int position) {
        if (!isCheckedList) {
            fab.setVisibility(View.GONE);
            ReplaceListOnInfo(position, listModels);
        } else {
            if (listView.getCheckedItemCount() == 0) {
                ArrayAdapter<String> adapterDefault = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, nameSubstance);
                listView.setAdapter(adapterDefault);
                isCheckedList = false;
                fab.setVisibility(View.VISIBLE);
                linearLayoutSettingsList.setVisibility(View.GONE);
            } else {
                chosen = ((ListView) parent).getCheckedItemPositions();
            }
        }
    }
}

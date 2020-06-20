package com.example.warfarinlife.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListModel {

    private int id_list;
    private String name;
    private String info;

    public ListModel(int id_list, String name, String info) {
        this.id_list = id_list;
        this.name = name;
        this.info = info;
    }

    public int getId_list() {
        return id_list;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }
}
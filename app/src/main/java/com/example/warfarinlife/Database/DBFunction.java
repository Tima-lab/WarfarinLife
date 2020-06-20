package com.example.warfarinlife.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.warfarinlife.Model.ProfileModel;
import com.example.warfarinlife.Model.ListModel;

public class DBFunction {

    private DBHelper dbHelper;

    public DBFunction(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Создание нового пользователя
    public void InsertUser(String name) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, name);

        database.insert(DBHelper.TABLE_PROFILE, null, contentValues);
        dbHelper.close();
    }

    // Вывод всех пользователей
    public ProfileModel[] SelectAllProfile() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int i = 0;

        final Cursor cursor = database.query(DBHelper.TABLE_PROFILE, null, null, null, null, null, null);
        ProfileModel[] profileModels = new ProfileModel[cursor.getCount()];

        if (cursor.moveToFirst()) {

            final int idIndex = cursor.getColumnIndex(DBHelper.KEY_PROFILE_ID);
            final int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            final int mnoIndex = cursor.getColumnIndex(DBHelper.KEY_MNO);
            final int dataIndex = cursor.getColumnIndex(DBHelper.KEY_DATE_ANALYSIS);
            final int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME_RECEPTION);

            do {
                final int id = cursor.getInt(idIndex);
                final String name = cursor.getString(nameIndex);
                final double mno = cursor.getDouble(mnoIndex);
                final String date = cursor.getString(dataIndex);
                final String time = cursor.getString(timeIndex);

                profileModels[i] = new ProfileModel(id, name, mno, date, time);
                i++;

            } while (cursor.moveToNext());
        }

        cursor.close();
        return profileModels;
    }

    public ListModel[] SelectAllSubstance(int idProfile) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        int i = 0;

        //final Cursor cursor = database.query(DBHelper.TABLE_LIST_BAN, null, null, null, null, null, null);
        //final Cursor cursor = database.rawQuery("select rowid,name_substance, info from " + dbHelper.TABLE_LIST_BAN + "," + dbHelper.TABLE_PROFILE_LIST_BAN, null);
        final Cursor cursor = database.rawQuery("select rowid,name_substance, info from " + dbHelper.TABLE_LIST_BAN + "" +
                " union select rowid,name_substance, info from " + dbHelper.TABLE_PROFILE_LIST_BAN + " where profile_id_list match ?", new String[] {String.valueOf(idProfile)});
        //database.query(DBHelper.TABLE_PROFILE_LIST_BAN + DBHelper.TABLE_LIST_BAN,);
        //final Cursor cursor = database.rawQuery("select rowid,name_substance, info from " + DBHelper.TABLE_PROFILE_LIST_BAN + " where " + DBHelper.KEY_PROFILE_ID_LIST +" = ?", new String[] {String.valueOf(idProfile)});
        //final Cursor cursor = database.query(DBHelper.TABLE_PROFILE_LIST_BAN, new String[]{"rowid", "name_substance", "info"}, "profile_id_list like ?", new String[]{String.valueOf(idProfile)},null,null,null);
        ListModel[] listModels = new ListModel[cursor.getCount()];

        if (cursor.moveToFirst()) {
            final int idIndex = cursor.getColumnIndex("rowid");
            final int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME_SUBSTANCE);
            final int infoIndex = cursor.getColumnIndex(DBHelper.KEY_INFO);

            do {

                final int id_list = cursor.getInt(idIndex);
                final String name_substance = cursor.getString(nameIndex);
                final String info = cursor.getString(infoIndex);

                listModels[i] = new ListModel(id_list, name_substance, info);
                i++;

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return listModels;
    }

    public ListModel[] Search(int idProfile, String text) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        int i = 0;

        final Cursor cursor = database.rawQuery("select rowid,name_substance,info from " + DBHelper.TABLE_LIST_BAN + " where name_substance like ?" +
                " union select rowid,name_substance,info from " + dbHelper.TABLE_PROFILE_LIST_BAN + " where profile_id_list match ? and name_substance like ?", new String[] {"%" + text + "%",String.valueOf(idProfile),"%" + text + "%"});
        ListModel[] listModels = new ListModel[cursor.getCount()];

        if (cursor.moveToFirst()) {

            final int idIndex = cursor.getColumnIndex("rowid");
            final int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME_SUBSTANCE);
            final int infoIndex = cursor.getColumnIndex(DBHelper.KEY_INFO);

            do {

                final int id_list = cursor.getInt(idIndex);
                final String name_substance = cursor.getString(nameIndex);
                final String info = cursor.getString(infoIndex);

                listModels[i] = new ListModel(id_list, name_substance, info);
                i++;

            } while (cursor.moveToNext());

        }

        cursor.close();
        database.close();
        return listModels;
    }


    
    public ProfileModel ProfileUserSearch(int id) {
        ProfileModel profileModel = null;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String profile_id = String.valueOf(id);
        final Cursor cursor = database.rawQuery("select * from " + dbHelper.TABLE_PROFILE + " where profile_id = ?", new String[]{profile_id});
        
        if (cursor.moveToFirst()) {
            final int idIndex = cursor.getColumnIndex(DBHelper.KEY_PROFILE_ID);
            final int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            final int mnoIndex = cursor.getColumnIndex(DBHelper.KEY_MNO);
            final int dataIndex = cursor.getColumnIndex(DBHelper.KEY_DATE_ANALYSIS);
            final int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME_RECEPTION);

            final int id_profile = cursor.getInt(idIndex);
            final String name = cursor.getString(nameIndex);
            final double mno = cursor.getDouble(mnoIndex);
            final String date = cursor.getString(dataIndex);
            final String time = cursor.getString(timeIndex);
                
            profileModel = new ProfileModel(id_profile, name, mno, date, time);
                
        }

        cursor.close();
        database.close();
        return profileModel;
    }

    public boolean InsertSubstance(int idProfile, String name, String info) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int i = 0;

        try {
            dbHelper.InsertInProfileList(database, idProfile, name, info);

            final Cursor cursor = database.rawQuery("select rowid,profile_id_list,name_substance, info from " + DBHelper.TABLE_PROFILE_LIST_BAN, new String[] {});
            ListModel[] listModels = new ListModel[cursor.getCount()];

            if (cursor.moveToFirst()) {
                final int idIndex = cursor.getColumnIndex("rowid");
                final int profileidIndex = cursor.getColumnIndex(DBHelper.KEY_PROFILE_ID_LIST);
                final int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME_SUBSTANCE);
                final int infoIndex = cursor.getColumnIndex(DBHelper.KEY_INFO);

                do {

                    final int id_list = cursor.getInt(idIndex);
                    final int profile = cursor.getInt(profileidIndex);
                    final String name_substance = cursor.getString(nameIndex);
                    final String info_substance = cursor.getString(infoIndex);

                    listModels[i] = new ListModel(id_list, name_substance, info_substance);
                    Log.d("2", "InsertSubstance: " + id_list + " ," + profile + " ," + name_substance + " ," + info_substance);
                    i++;

                } while (cursor.moveToNext());
            }

            cursor.close();

            database.close();
            return true;
        } catch (Exception e) {
            database.close();
            return false;
        }
    }


    public boolean UpdateProfile(int idProfile, String name, String mno, String date, String time) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.KEY_NAME,name);
        contentValues.put(dbHelper.KEY_MNO, mno);
        contentValues.put(dbHelper.KEY_DATE_ANALYSIS, date);
        contentValues.put(dbHelper.KEY_TIME_RECEPTION, time);

        try {
            database.update(dbHelper.TABLE_PROFILE, contentValues, dbHelper.KEY_PROFILE_ID +" = ?", new String[] {String.valueOf(idProfile)});
            database.close();
            return true;
        } catch (Exception e) {
            database.close();
            return false;
        }
    }

    public boolean DeleteSubstance(int idSubstance, String nameSubstance) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        try {
            database.delete(DBHelper.TABLE_PROFILE_LIST_BAN,  "rowid = ? and " + DBHelper.KEY_NAME_SUBSTANCE + " = ?", new String[]{String.valueOf(idSubstance), nameSubstance});
            database.delete(DBHelper.TABLE_LIST_BAN, "rowid = ? and " + DBHelper.KEY_NAME_SUBSTANCE + " = ?", new String[]{String.valueOf(idSubstance), nameSubstance});
            database.close();
            return true;
        } catch (Exception e) {
            database.close();
            return false;
        }
    }

    public void RecoveryTableList() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        database.delete(DBHelper.TABLE_LIST_BAN, null, null);
        dbHelper.ListReducing(database);
        dbHelper.ListIncrease(database);
        dbHelper.ListIncreaseOrReducing(database);

        database.close();
    }


}

package com.aprr.biotracer.alumni;

import android.content.Context;
import android.content.SharedPreferences;

public class ListHandler {

    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;
    private Context mContext;

    public ListHandler(Context mContext){
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences("id_makul",Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    public ListItem getMakul(){
        ListItem listItem = new ListItem();
        listItem.setId_makul(mPreferences.getString("makul_id",""));

        return listItem;
    }

    public void setId(String id_mk){
        this.mEditor.putString("makul_id", id_mk);
        mEditor.commit();
    }
}

package com.aprr.biotracer.alumni;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.aprr.biotracer.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BergunaAdapter extends RecyclerView.Adapter<BergunaAdapter.MyViewHolder> {

    private HashMap<Integer, Boolean> isChecked = new HashMap<>();
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<String> bergunaList;
    private ArrayList<String> makulIdList;
    private ArrayList<String> id;
    ListHandler listItem;

    public BergunaAdapter(Context context, ArrayList<String> bergunaList, ArrayList<String> makulIdList){
        this.mContext = context;
        this.bergunaList = bergunaList;
        this.makulIdList = makulIdList;
        inflater = LayoutInflater.from(context);

        Log.d("makul", makulIdList.get(0));
    }

    @Override
    public BergunaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.list_item_cb, parent, false);

        MyViewHolder holder = new MyViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final BergunaAdapter.MyViewHolder holder, final int position) {
        holder.cbMakul.setText(bergunaList.get(position)+" - "+ makulIdList.get(position));

        if (isChecked.containsKey(position)){
            holder.cbMakul.setChecked(isChecked.get(position));
        } else {
            holder.cbMakul.setChecked(false);
        }

        holder.cbMakul.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // KONDISI PADA SAAT CEKLIS
                    id.add(makulIdList.get(position));
                    listItem.setId(String.valueOf(id));
                } else {
                    // KONDISI PADA SAAT CEKLIS DIHILANGKAN
                    id.remove(makulIdList.get(position));
                    listItem.setId(String.valueOf(id));
                }
                Log.d("idnya", String.valueOf(listItem.getMakul().getId_makul()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return bergunaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox cbMakul;

        public MyViewHolder(View itemView) {
            super(itemView);
            listItem = new ListHandler(mContext);
            id = new ArrayList<>();
            cbMakul = itemView.findViewById(R.id.cb_makul);

        }
    }

}


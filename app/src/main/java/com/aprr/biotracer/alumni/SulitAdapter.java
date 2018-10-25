package com.aprr.biotracer.alumni;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.aprr.biotracer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SulitAdapter extends RecyclerView.Adapter<SulitAdapter.MyViewHolder> {

    private HashMap<Integer, Boolean> isChecked = new HashMap<>();
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<String> sulitList;
    private ArrayList<String> makulIdList;
    private ArrayList<String> id;
    ListHandler listItem;



    public SulitAdapter(Context context, ArrayList<String> sulitList, ArrayList<String> makulIdList){
        this.mContext = context;
        this.sulitList = sulitList;
        this.makulIdList = makulIdList;
        inflater = LayoutInflater.from(context);

//        Log.d("makul", makulIdList.get(0));
    }

    @Override
    public SulitAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.list_item_cb, parent, false);

        SulitAdapter.MyViewHolder holder = new SulitAdapter.MyViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(SulitAdapter.MyViewHolder holder, final int position) {
        holder.cbMakul.setText(sulitList.get(position));
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
        return sulitList.size();
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

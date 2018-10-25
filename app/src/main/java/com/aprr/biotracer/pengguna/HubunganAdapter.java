package com.aprr.biotracer.pengguna;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aprr.biotracer.R;

import java.util.ArrayList;
import java.util.List;

public class HubunganAdapter extends ArrayAdapter<Hubungan> {
    private List<Hubungan> hubunganList = new ArrayList<>();

    HubunganAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<Hubungan> hubunganList) {
        super(context, resource, spinnerText, hubunganList);
        this.hubunganList = hubunganList;
    }

    @Override
    public Hubungan getItem(int position) {
        return hubunganList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    /**
     * Gets the state object by calling getItem and
     * Sets the state name to the drop-down TextView.
     *
     * @param position the position of the item selected
     * @return returns the updated View
     */
    private View initView(int position) {
        Hubungan hub = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_informasi_profil, null);
        TextView textView =  v.findViewById(R.id.spinner_profil_text);
        textView.setText(hub.getHub_nama());
        return v;

    }


}

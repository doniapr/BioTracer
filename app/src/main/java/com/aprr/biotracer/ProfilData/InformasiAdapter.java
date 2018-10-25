package com.aprr.biotracer.ProfilData;

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

public class InformasiAdapter extends ArrayAdapter<Informasi> {
    private List<Informasi> informasiList = new ArrayList<>();

    InformasiAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<Informasi> informasiList) {
        super(context, resource, spinnerText, informasiList);
        this.informasiList = informasiList;
    }

    @Override
    public Informasi getItem(int position) {
        return informasiList.get(position);
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
        Informasi info = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_informasi_profil, null);
        TextView textView =  v.findViewById(R.id.spinner_profil_text);
        textView.setText(info.getInfo_nama());
        return v;

    }


}

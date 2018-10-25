package com.aprr.biotracer.konstanta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.aprr.biotracer.R;

import java.util.ArrayList;

public class ProfilAdapter extends RecyclerView.Adapter<ProfilAdapter.ProfilViewHolder>{

    private ArrayList<ProfilHandler> profilList;

    public ProfilAdapter(ArrayList<ProfilHandler> profilList) {
        this.profilList = profilList;
    }

    @Override
    public ProfilAdapter.ProfilViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.detail_profil, parent, false);
        return new ProfilAdapter.ProfilViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfilAdapter.ProfilViewHolder holder, int position) {
        holder.rvHeader.setText(profilList.get(position).getJudul());
        holder.rvDetail.setText(profilList.get(position).getDetail());
    }

    @Override
    public int getItemCount() {
        return (profilList != null) ? profilList.size() : 0;
    }

    public class ProfilViewHolder extends RecyclerView.ViewHolder{
        private TextView rvHeader, rvDetail;

        public ProfilViewHolder(View itemView) {
            super(itemView);
             rvHeader= itemView.findViewById(R.id.rv_header_detail);
             rvDetail= itemView.findViewById(R.id.rv_detail_akun);
        }
    }
}


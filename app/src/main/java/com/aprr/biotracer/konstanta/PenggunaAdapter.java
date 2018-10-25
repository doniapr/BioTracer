package com.aprr.biotracer.konstanta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aprr.biotracer.R;

import java.util.ArrayList;

public class PenggunaAdapter extends RecyclerView.Adapter<PenggunaAdapter.PenggunaViewHolder>{

    private ArrayList<PenggunaHandler> penggunaList;

    public PenggunaAdapter(ArrayList<PenggunaHandler> penggunaList) {
        this.penggunaList = penggunaList;
    }

    @Override
    public PenggunaAdapter.PenggunaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.detail_profil, parent, false);
        return new PenggunaAdapter.PenggunaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PenggunaAdapter.PenggunaViewHolder holder, int position) {
        holder.rvHeader.setText(penggunaList.get(position).getJudul());
        holder.rvDetail.setText(penggunaList.get(position).getDetail());
    }

    @Override
    public int getItemCount() {
        return (penggunaList != null) ? penggunaList.size() : 0;
    }

    public class PenggunaViewHolder extends RecyclerView.ViewHolder{
        private TextView rvHeader, rvDetail;

        public PenggunaViewHolder(View itemView) {
            super(itemView);
            rvHeader= itemView.findViewById(R.id.rv_header_detail);
            rvDetail= itemView.findViewById(R.id.rv_detail_akun);
        }
    }
}

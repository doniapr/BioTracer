package com.aprr.biotracer.konstanta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aprr.biotracer.R;

import java.util.ArrayList;

public class HasilMakulAdapter extends RecyclerView.Adapter<HasilMakulAdapter.HasilMakulViewHolder>{

    private ArrayList<HasilMakulHandler> jawabanList;

    public HasilMakulAdapter(ArrayList<HasilMakulHandler> jawabanList) {
        this.jawabanList = jawabanList;
    }

    @Override
    public HasilMakulAdapter.HasilMakulViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hasil_tanggapan_makul, parent, false);
        return new HasilMakulAdapter.HasilMakulViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HasilMakulAdapter.HasilMakulViewHolder holder, int position) {
        holder.rvJawab.setText(jawabanList.get(position).getJawaban());
    }

    @Override
    public int getItemCount() {
        return (jawabanList != null) ? jawabanList.size() : 0;
    }

    public class HasilMakulViewHolder extends RecyclerView.ViewHolder{
        private TextView rvJawab;

        public HasilMakulViewHolder(View itemView) {
            super(itemView);
            rvJawab = itemView.findViewById(R.id.rv_makul);
        }
    }
}

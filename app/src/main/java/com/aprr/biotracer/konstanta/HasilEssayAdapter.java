package com.aprr.biotracer.konstanta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aprr.biotracer.R;

import java.util.ArrayList;

public class HasilEssayAdapter extends RecyclerView.Adapter<HasilEssayAdapter.HasilEssayViewHolder>{

    private ArrayList<HasilEssayHandler> soalList;
    private ArrayList<HasilEssayHandler> jawabanList;

    public HasilEssayAdapter(ArrayList<HasilEssayHandler> soalList, ArrayList<HasilEssayHandler> jawabanList) {
        this.soalList = soalList;
        this.jawabanList = jawabanList;
    }

    @Override
    public HasilEssayAdapter.HasilEssayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hasil_tanggapan, parent, false);
        return new HasilEssayAdapter.HasilEssayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HasilEssayAdapter.HasilEssayViewHolder holder, int position) {
        holder.rvSoal.setText(soalList.get(position).getSoal());
        holder.rvJawab.setText(jawabanList.get(position).getJawaban());
    }

    @Override
    public int getItemCount() {
        return (soalList != null) ? soalList.size() : 0;
    }

    public class HasilEssayViewHolder extends RecyclerView.ViewHolder{
        private TextView rvSoal, rvJawab;

        public HasilEssayViewHolder(View itemView) {
            super(itemView);
            rvSoal = itemView.findViewById(R.id.rv_soal);
            rvJawab = itemView.findViewById(R.id.rv_jawaban);
        }
    }
}

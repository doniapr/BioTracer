package com.aprr.biotracer.alumni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.aprr.biotracer.R;
import com.aprr.biotracer.konstanta.HasilEssayAdapter;
import com.aprr.biotracer.konstanta.HasilEssayHandler;
import com.aprr.biotracer.konstanta.HasilMakulAdapter;
import com.aprr.biotracer.konstanta.HasilMakulHandler;
import com.aprr.biotracer.konstanta.HasilPgAdapter;
import com.aprr.biotracer.konstanta.HasilPgHandler;

import java.util.ArrayList;

public class HasilAlumniActivity extends AppCompatActivity {
    private RecyclerView rvHasilPg, rvHasilEssay, rvHasilBerguna,rvHasilPerlu, rvHasilSulit;
    private TextView tvSaran;
    HasilEssayAdapter EssayAdapter;
    HasilPgAdapter pgAdapter;
    HasilMakulAdapter makulAdapterB, makulAdapterP, makulAdapterS;
    int x;

    String saranAlumni,temp_Saran;
    ArrayList<HasilEssayHandler> soalHasilEssay = new ArrayList<>();
    ArrayList<HasilPgHandler> soalHasilPg = new ArrayList<>();
    ArrayList<HasilEssayHandler> HasilEssay = new ArrayList<>();
    ArrayList<HasilPgHandler> HasilPg = new ArrayList<>();
    ArrayList<HasilMakulHandler> MakulBerguna = new ArrayList<>();
    ArrayList<HasilMakulHandler> MakulPerlu = new ArrayList<>();
    ArrayList<HasilMakulHandler> MakulSulit = new ArrayList<>();

    ArrayList<Object> temp_soalHasilEssay = new ArrayList<>();
    ArrayList<Object> temp_soalHasilPg = new ArrayList<>();
    ArrayList<Object> temp_HasilEssay = new ArrayList<>();
    ArrayList<Object> temp_HasilPg = new ArrayList<>();
    ArrayList<Object> temp_MakulBerguna = new ArrayList<>();
    ArrayList<Object> temp_MakulPerlu = new ArrayList<>();
    ArrayList<Object> temp_MakulSulit = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_alumni);

        Bundle bnd = getIntent().getBundleExtra("soal_essay");
        Bundle bnd1 = getIntent().getBundleExtra("soal_pg");
        Bundle bnd2 = getIntent().getBundleExtra("hasil_essay");
        Bundle bnd3 = getIntent().getBundleExtra("hasil_pg");
        Bundle bnd4 = getIntent().getBundleExtra("makul_berguna");
        Bundle bnd5 = getIntent().getBundleExtra("makul_perlu");
        Bundle bnd6 = getIntent().getBundleExtra("makul_sulit");
        Bundle bnd7 = getIntent().getBundleExtra("saran");

        temp_soalHasilEssay = (ArrayList<Object>) bnd.getSerializable("soal_essay");
        temp_soalHasilPg = (ArrayList<Object>) bnd1.getSerializable("soal_pg");
        temp_HasilEssay = (ArrayList<Object>) bnd2.getSerializable("hasil_essay");
        temp_HasilPg = (ArrayList<Object>) bnd3.getSerializable("hasil_pg");
        temp_MakulBerguna = (ArrayList<Object>) bnd4.getSerializable("makul_berguna");
        temp_MakulPerlu = (ArrayList<Object>) bnd5.getSerializable("makul_perlu");
        temp_MakulSulit = (ArrayList<Object>) bnd6.getSerializable("makul_sulit");
        temp_Saran = (String) bnd7.getSerializable("saran");


        for (x=0;x<temp_soalHasilEssay.size();x++){
            soalHasilEssay.add(new HasilEssayHandler(temp_soalHasilEssay.get(x).toString(),temp_HasilEssay.get(x).toString()));
        }
        for (x=0;x<temp_HasilEssay.size();x++){
            HasilEssay.add(new HasilEssayHandler(temp_soalHasilEssay.get(x).toString(),temp_HasilEssay.get(x).toString()));
        }

        for (x=0;x<temp_soalHasilPg.size();x++){
            soalHasilPg.add(new HasilPgHandler(temp_soalHasilPg.get(x).toString() ,temp_HasilPg.get(x).toString()));
        }
        for (x=0;x<temp_HasilPg.size();x++){
            HasilPg.add(new HasilPgHandler(temp_soalHasilPg.get(x).toString() ,temp_HasilPg.get(x).toString()));
        }

        for (x=0;x<temp_MakulBerguna.size();x++){
            MakulBerguna.add(new HasilMakulHandler(temp_MakulBerguna.get(x).toString()));
        }

        for (x=0;x<temp_MakulPerlu.size();x++){
            MakulPerlu.add(new HasilMakulHandler(temp_MakulPerlu.get(x).toString()));
        }

        for (x=0;x<temp_MakulSulit.size();x++){
            MakulSulit.add(new HasilMakulHandler(temp_MakulSulit.get(x).toString()));
        }

        saranAlumni = temp_Saran;

//        Log.d("soalnya", String.valueOf(soalHasilEssay));
//        Log.d("soalnya", String.valueOf(HasilEssay));

        rvHasilPg = findViewById(R.id.tanggapan_alumni_pg);
        rvHasilEssay = findViewById(R.id.tanggapan_alumni_essay);
        rvHasilBerguna = findViewById(R.id.tanggapan_alumni_berguna);
        rvHasilPerlu = findViewById(R.id.tanggapan_alumni_perlu);
        rvHasilSulit = findViewById(R.id.tanggapan_alumni_sulit);

        tvSaran = findViewById(R.id.hasil_saran_alumni);

        //Hasil Essay
        EssayAdapter = new HasilEssayAdapter(soalHasilEssay, HasilEssay);
        RecyclerView.LayoutManager lmEssay = new LinearLayoutManager(HasilAlumniActivity.this);
        rvHasilEssay.setLayoutManager(lmEssay);
        rvHasilEssay.setAdapter(EssayAdapter);

        //Hasil Pg
        pgAdapter = new HasilPgAdapter(soalHasilPg, HasilPg);
        RecyclerView.LayoutManager lmPg = new LinearLayoutManager(HasilAlumniActivity.this);
        rvHasilPg.setLayoutManager(lmPg);
        rvHasilPg.setAdapter(pgAdapter);

        //Hasil Makul Berguna
        makulAdapterB = new HasilMakulAdapter(MakulBerguna);
        RecyclerView.LayoutManager lmBerguna = new LinearLayoutManager(HasilAlumniActivity.this);
        rvHasilBerguna.setLayoutManager(lmBerguna);
        rvHasilBerguna.setAdapter(makulAdapterB);

        //Hasil Makul Perlu
        makulAdapterP = new HasilMakulAdapter(MakulPerlu);
        RecyclerView.LayoutManager lmPerlu = new LinearLayoutManager(HasilAlumniActivity.this);
        rvHasilPerlu.setLayoutManager(lmPerlu);
        rvHasilPerlu.setAdapter(makulAdapterP);

        //Hasil Makul Sulit
        makulAdapterS = new HasilMakulAdapter(MakulSulit);
        RecyclerView.LayoutManager lmSulit = new LinearLayoutManager(HasilAlumniActivity.this);
        rvHasilSulit.setLayoutManager(lmSulit);
        rvHasilSulit.setAdapter(makulAdapterS);

        tvSaran.setText(saranAlumni);
    }
}

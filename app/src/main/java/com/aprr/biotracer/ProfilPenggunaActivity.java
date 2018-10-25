package com.aprr.biotracer;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.aprr.biotracer.app.Profil;
import com.aprr.biotracer.app.SessionHandler;
import com.aprr.biotracer.konstanta.PenggunaAdapter;
import com.aprr.biotracer.konstanta.PenggunaHandler;
import com.aprr.biotracer.konstanta.ProfilAdapter;
import com.aprr.biotracer.konstanta.ProfilHandler;

import java.util.ArrayList;

public class ProfilPenggunaActivity extends AppCompatActivity {


    private RecyclerView rvDetailPengguna;
    private PenggunaAdapter adapter;
    TextView tvNama, tvEmail,tvAkses;
    private ArrayList<PenggunaHandler> penggunaArrayList;
    private ProgressDialog pDialog;

    ArrayList<Object> DetailPengguna = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bnd = getIntent().getBundleExtra("detailPengguna");
        DetailPengguna = (ArrayList<Object>) bnd.getSerializable("detailPengguna");

        setContentView(R.layout.activity_profil_pengguna);
        SessionHandler session = new SessionHandler(getApplicationContext());

        tvNama = findViewById(R.id.pengguna_nama);
        tvEmail = findViewById(R.id.pengguna_email);
        tvAkses = findViewById(R.id.pengguna_akses);

        tvNama.setText(session.getUserDetails().getFullName());
        tvEmail.setText(session.getUserDetails().getEmail());
        if (session.getUserDetails().getRole()==2){
            tvAkses.setText("Alumni");
        } else {
            tvAkses.setText("Pengguna Alumni");
        }
        penggunaArrayList = new ArrayList<>();

        addData("Hp", DetailPengguna.get(1).toString());
        addData("Instansi", DetailPengguna.get(2).toString());
        addData("Alamat", DetailPengguna.get(3).toString());


//        LoadDetail(session.getUserDetails().getUser_id());
//        System.out.println();
//        System.out.println(pr.getJk());


        rvDetailPengguna = findViewById(R.id.detail_pengguna_alumni);

        adapter = new PenggunaAdapter(penggunaArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfilPenggunaActivity.this);

        rvDetailPengguna.setLayoutManager(layoutManager);

        rvDetailPengguna.setAdapter(adapter);
    }


    public void addData(String judul, String detail) {
        System.out.println(judul);
        System.out.println(detail);

        penggunaArrayList.add(new PenggunaHandler(judul, detail));
    }
}

package com.aprr.biotracer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aprr.biotracer.ProfilData.ProfilUbahActivity;
import com.aprr.biotracer.app.MySingleton;
import com.aprr.biotracer.app.Profil;
import com.aprr.biotracer.app.SessionHandler;
import com.aprr.biotracer.konstanta.ProfilAdapter;
import com.aprr.biotracer.konstanta.ProfilHandler;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfilActivity extends AppCompatActivity {

    private RecyclerView rvDetailProfil;
    private ProfilAdapter adapter;
    TextView tvNama, tvEmail,tvAkses;
    Button btnUbah;
    private ArrayList<ProfilHandler> profilArrayList;
    private ProgressDialog pDialog;

    ArrayList<Object> DetailProf = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bnd = getIntent().getBundleExtra("detailProf");
        DetailProf = (ArrayList<Object>) bnd.getSerializable("detailProf");

        setContentView(R.layout.activity_profil);
        setTitle("Profil");

        SessionHandler session = new SessionHandler(getApplicationContext());



        tvNama = findViewById(R.id.profil_nama);
        tvEmail = findViewById(R.id.profil_email);
        tvAkses = findViewById(R.id.profil_akses);
        btnUbah = findViewById(R.id.btn_ubah_profil);


        tvNama.setText(session.getUserDetails().getFullName());
        tvEmail.setText(session.getUserDetails().getEmail());
        if (session.getUserDetails().getRole()==2){
            tvAkses.setText("Alumni");
        } else {
            tvAkses.setText("Pengguna Alumni");
        }
        profilArrayList = new ArrayList<>();

        addData("Jenis Kelamin", DetailProf.get(0).toString());
        addData("Tanggal Lahir", DetailProf.get(2).toString());
        addData("Tempat Lahir", DetailProf.get(1).toString());
        addData("Masuk Kuliah", DetailProf.get(3).toString());
        addData("Lulus Kuliah", DetailProf.get(4).toString());
        addData("IPK", DetailProf.get(5).toString());
        addData("Alamat Rumah", DetailProf.get(6).toString());
        addData("Nama Instansi", DetailProf.get(7).toString());
        addData("Alamat Instansi", DetailProf.get(8).toString());
        addData("HP", DetailProf.get(9).toString());
        addData("Jabatan", DetailProf.get(10).toString());
        addData("Media Memperoleh Informasi Almamater", DetailProf.get(11).toString());
        addData("Pendidikan Terakhir", DetailProf.get(12).toString());
        addData("Pekerjaan Pertama", DetailProf.get(13).toString());
        addData("Lama Waktu Menunggu Pekerjaan pertama", DetailProf.get(14).toString());
        addData("Gaji Pertama", DetailProf.get(15).toString());

//        LoadDetail(session.getUserDetails().getUser_id());
//        System.out.println();
//        System.out.println(pr.getJk());


        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfilActivity.this,ProfilUbahActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("profil",DetailProf);
                i.putExtra("profil",bundle);
                startActivity(i);
                finish();
            }
        });

        rvDetailProfil = findViewById(R.id.detail_profil_alumni);
        adapter = new ProfilAdapter(profilArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfilActivity.this);
        rvDetailProfil.setLayoutManager(layoutManager);
        rvDetailProfil.setAdapter(adapter);
    }


    public void addData(String judul, String detail) {
        System.out.println(judul);
        System.out.println(detail);

        profilArrayList.add(new ProfilHandler(judul, detail));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}

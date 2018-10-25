package com.aprr.biotracer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aprr.biotracer.ProfilData.Informasi;
import com.aprr.biotracer.ProfilData.InformasiAdapter;
import com.aprr.biotracer.ProfilData.ProfilAddActivity;
import com.aprr.biotracer.ProfilData.ProfilPenggunaAddActivity;
import com.aprr.biotracer.alumni.AlumniActivity;
import com.aprr.biotracer.alumni.HasilAlumniActivity;
import com.aprr.biotracer.app.MySingleton;
import com.aprr.biotracer.app.SessionHandler;
import com.aprr.biotracer.pengguna.AlumniData;
import com.aprr.biotracer.pengguna.AlumniDataAdapter;
import com.aprr.biotracer.pengguna.HasilPenggunaActivity;
import com.aprr.biotracer.pengguna.PenggunaActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainPenggunaActivity extends AppCompatActivity {

    ImageView ivPengguna,ivPetunjuk, ivBantuan, ivProfil, ivKeluar2;
    private SessionHandler session1;
    private ProgressDialog pDialog;

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USER_ID = "user_id";
    public String key = "@gY=iF=&.YoZO[in%^";

    private String detail_url = "https://boyojnck.000webhostapp.com/member/profil_pengguna.php";
    private String get_pengguna_kuis_url = "https://boyojnck.000webhostapp.com/member/get_data_pengguna.php";

    ArrayList<Object> ProfilPengguna = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pengguna);

        session1 = new SessionHandler(getApplicationContext());
        //User user = session1.getUserDetails();

        ivPengguna = findViewById(R.id.iv_pengguna);
        ivPetunjuk = findViewById(R.id.iv_petunjuk);
        ivBantuan = findViewById(R.id.iv_bantuan);
        ivProfil = findViewById(R.id.iv_profil);
        ivKeluar2 = findViewById(R.id.iv_keluar2);

        ivPengguna.setOnClickListener(new View.OnClickListener(
        ) {
            @Override
            public void onClick(View v) {
                displayLoader();
                cekKuisPengguna(session1.getUserDetails().getUser_id());

            }
        });

        ivPetunjuk.setOnClickListener(new View.OnClickListener(
        ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainPenggunaActivity.this,PetunjukActivity.class);
                startActivity(i);
            }
        });


        ivBantuan.setOnClickListener(new View.OnClickListener(
        ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainPenggunaActivity.this,BantuanActivity.class);
                startActivity(i);
            }
        });

        ivProfil.setOnClickListener(new View.OnClickListener(
        ) {
            @Override
            public void onClick(View v) {
                displayLoader();
                LoadDetail(session1.getUserDetails().getUser_id(),1);
            }
        });

        ivKeluar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session1.logoutUser();
                Intent i = new Intent(MainPenggunaActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(MainPenggunaActivity.this);
        pDialog.setMessage("Memuat.. Silakan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void LoadDetail(int id, final int load){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USER_ID, id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, detail_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                ProfilPengguna.add(response.getString("id"));
                                ProfilPengguna.add(response.getString("hp"));
                                ProfilPengguna.add(response.getString("instansi"));
                                ProfilPengguna.add(response.getString("alamat"));


                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                                if (load == 1){
                                    PindahProfilPengguna();
                                } else {
                                    PindahQuestPengguna();
                                }

                            }else{
                                Intent i = new Intent(getApplicationContext(), ProfilPenggunaAddActivity.class);
                                startActivity(i);

                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);

    }

    private void PindahProfilPengguna(){
        Log.d("profilPengguna", ProfilPengguna.get(0).toString());
        Intent i = new Intent(MainPenggunaActivity.this,ProfilPenggunaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("detailPengguna",ProfilPengguna);
        i.putExtra("detailPengguna",bundle);
        startActivity(i);
    }

    private void PindahQuestPengguna(){
//        Log.d("hehehe", ProfilPengguna.get(0).toString());
        Intent i = new Intent(MainPenggunaActivity.this,PenggunaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("detailPengguna",ProfilPengguna);
        i.putExtra("detailPengguna",bundle);
        startActivity(i);
    }

    private void cekKuisPengguna(int id){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USER_ID, id);
            request.put("key", key);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, get_pengguna_kuis_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully
                            if (response.getInt(KEY_STATUS) == 1) {

                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                                LoadDetail(session1.getUserDetails().getUser_id(),0);
                            }else if (response.getInt(KEY_STATUS) == 0) {
                                loadHasil();

                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void loadHasil(){
        Intent i = new Intent(MainPenggunaActivity.this,HasilPenggunaActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("detailPengguna",ProfilPengguna);
//        i.putExtra("detailPengguna",bundle);
        startActivity(i);
    }

}

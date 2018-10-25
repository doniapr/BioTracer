package com.aprr.biotracer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aprr.biotracer.ProfilData.ProfilAddActivity;
import com.aprr.biotracer.alumni.AlumniActivity;
import com.aprr.biotracer.alumni.HasilAlumniActivity;
import com.aprr.biotracer.app.MySingleton;
import com.aprr.biotracer.app.Profil;
import com.aprr.biotracer.app.SessionHandler;
import com.aprr.biotracer.app.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity {

    ImageView ivAlumni,ivPetunjuk, ivBantuan, ivProfil, ivKeluar;
    private SessionHandler session;
    private static final String KEY_STATUS = "status";
    private static final String KEY_JK = "jk";
    Profil p = new Profil();
    private static final String KEY_KEY = "key";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USER_ID = "user_id";
    public String key = "@gY=iF=&.YoZO[in%^";
    private ProgressDialog pDialog;
    private String quest_url = "https://tracer.fadlidony.com/api/alumni/get/quest";
    private String detail_url = "https://tracer.fadlidony.com/api/alumni/getprofile";
    private String get_alumni_kuis_url = "https://tracer.fadlidony.com/api/alumni/post/checkquest";

    ArrayList<Object> ProfilAlumni = new ArrayList<>();
    ArrayList<String> QuestAlumni = new ArrayList<>();
    ArrayList<String> QuestId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        ivAlumni = findViewById(R.id.iv_alumni);
        ivPetunjuk = findViewById(R.id.iv_petunjuk);
        ivBantuan = findViewById(R.id.iv_bantuan);
        ivProfil = findViewById(R.id.iv_profil);
        ivKeluar = findViewById(R.id.iv_keluar);

        ivAlumni.setOnClickListener(new OnClickListener(
        ) {
            @Override
            public void onClick(View v) {
                displayLoader();
                cekKuisAlumni(session.getUserDetails().getUser_id());
            }
        });

        ivPetunjuk.setOnClickListener(new OnClickListener(
        ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,PetunjukActivity.class);
                startActivity(i);
            }
        });


        ivBantuan.setOnClickListener(new OnClickListener(
        ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,BantuanActivity.class);
                startActivity(i);
            }
        });

        ivProfil.setOnClickListener(new OnClickListener(
        ) {
            @Override
            public void onClick(View v) {
                displayLoader();
                LoadDetail(session.getUserDetails().getUser_id());
            }
        });

        ivKeluar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Memuat.. Silakan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void LoadDetail(int id){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters

            request.put(KEY_USER_ID, id);
            request.put(KEY_KEY, key);


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

                                ProfilAlumni.add(response.getString(KEY_JK));
                                ProfilAlumni.add(response.getString("tempat_lahir"));
                                ProfilAlumni.add(response.getString("tanggal_lahir"));
                                ProfilAlumni.add(response.getString("masuk_kuliah"));
                                ProfilAlumni.add(response.getString("lulus_kuliah"));
                                ProfilAlumni.add(response.getString("ipk"));
                                ProfilAlumni.add(response.getString("alamat_rumah"));
                                ProfilAlumni.add(response.getString("nama_instansi"));
                                ProfilAlumni.add(response.getString("alamat_instansi"));
                                ProfilAlumni.add(response.getString("hp"));
                                ProfilAlumni.add(response.getString("jabatan"));
                                ProfilAlumni.add(response.getString("informasi"));
                                ProfilAlumni.add(response.getString("pendidikan"));
                                ProfilAlumni.add(response.getString("pekerjaan"));
                                ProfilAlumni.add(response.getString("waktu"));
                                ProfilAlumni.add(response.getString("gaji"));

                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                                PindahProfil();
                            }else{
                                Intent i = new Intent(getApplicationContext(), ProfilAddActivity.class);
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
                                "Periksa Koneksi Internet Anda", Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);

    }

    private void cekKuisAlumni(int id){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USER_ID, id);
            request.put(KEY_KEY, key);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, get_alumni_kuis_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully
                            if (response.getInt(KEY_STATUS) == 1) {

                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                                loadQuest(1);

                            }else if (response.getInt(KEY_STATUS) == 0) {
                                loadQuest(2);

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

    private void loadQuest(final int id) {
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, quest_url, null , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        pDialog.dismiss();
//                        Log.d("respon","s");
                        try {
                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String quest = response.getString("quest_nama");
                                QuestAlumni.add(quest);
                            }
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String quest_id = response.getString("quest_id");
                                QuestId.add(quest_id);
                            }

                            if (id==1){
                                pindahQuestPg();
                            } else if (id==2){
                                loadHasilKuis();
                            }
//                            pindahQuestPg();

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

    private void PindahProfil(){
//        Log.d("hehehe", ProfilAlumni.get(0).toString());
        Intent i = new Intent(MainActivity.this,ProfilActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("detailProf",ProfilAlumni);
        i.putExtra("detailProf",bundle);
        startActivity(i);
        finish();
    }

    private void pindahQuestPg(){
//        pDialog.dismiss();
//        Log.d("hehehe", QuestAlumni.get(0));
        Intent i = new Intent(MainActivity.this,AlumniActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("kues_nama", QuestAlumni);
        bundle.putSerializable("kues_id", QuestId);
        i.putExtra("kues_nama",bundle);
        i.putExtra("kues_id",bundle);
        startActivity(i);
    }

    private void loadHasilKuis(){
//        pDialog.dismiss();
        Intent i = new Intent(MainActivity.this,HasilAlumniActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("soal_essay", QuestAlumni);
        bundle.putSerializable("soal_pg", QuestAlumni);
        bundle.putSerializable("hasil_essay", QuestAlumni);
        bundle.putSerializable("hasil_pg", QuestAlumni);
        bundle.putSerializable("makul_berguna", QuestAlumni);
        bundle.putSerializable("makul_perlu", QuestAlumni);
        bundle.putSerializable("makul_sulit", QuestAlumni);
        bundle.putSerializable("saran", "saraann");

        i.putExtra("soal_essay",bundle);
        i.putExtra("soal_pg",bundle);
        i.putExtra("hasil_essay",bundle);
        i.putExtra("hasil_pg",bundle);
        i.putExtra("makul_berguna",bundle);
        i.putExtra("makul_perlu",bundle);
        i.putExtra("makul_sulit",bundle);
        i.putExtra("saran",bundle);

        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);

    }
}

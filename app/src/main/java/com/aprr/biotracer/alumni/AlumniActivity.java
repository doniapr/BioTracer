package com.aprr.biotracer.alumni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aprr.biotracer.R;
import com.aprr.biotracer.app.MySingleton;
import com.aprr.biotracer.app.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlumniActivity extends AppCompatActivity {

    TextView tvSoal;
    RadioGroup rgPilihanJawaban;
    RadioButton rbPilihanKurang, rbPilihanCukup, rbPilihanBaik, rbPilihanSangatBaik;
    Button btnSubmitPg;
    ArrayList<String> soalArrayList , soalIdArray;
    ArrayList<Integer> jawabanId,soalJawabanId;
    int user_id;
    private String tambah_quest_url = "https://tracer.fadlidony.com/api/alumni/post/fillquest";
    private String quest_essay_url = "https://tracer.fadlidony.com/api/alumni/get/essay";
    private static final String KEY_STATUS = "status";
    private static final String KEY_KEY = "key";
    public String key = "@gY=iF=&.YoZO[in%^";
    private static final String KEY_MESSAGE = "message";

    ArrayList<String> QuestEssayAlumni = new ArrayList<>();
    ArrayList<String> QuestEssayId = new ArrayList<>();

    private ProgressDialog pDialog;

    SessionHandler session;
    int arr; //untuk menampung nilai panjang array
    int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soalArrayList = new ArrayList<>();
        soalIdArray = new ArrayList<>();

        jawabanId = new ArrayList<>();
        soalJawabanId = new ArrayList<>();

        session = new SessionHandler(getApplicationContext());
        user_id = session.getUserDetails().getUser_id();

        Bundle bnd = getIntent().getBundleExtra("kues_nama");
        soalArrayList = (ArrayList<String>) bnd.getSerializable("kues_nama");
        Bundle bndId = getIntent().getBundleExtra("kues_id");
        soalIdArray = (ArrayList<String>) bndId.getSerializable("kues_id");

//        Log.d("soal", String.valueOf(soalIdArray.size()));
//        Log.d("soal",soalIdArray.get(19));

        setContentView(R.layout.activity_alumni);


        tvSoal = findViewById(R.id.tv_soal_pg);
        rgPilihanJawaban = findViewById(R.id.rgPilihanJawaban);
        rbPilihanBaik = findViewById(R.id.rb_pilihan_baik);
        rbPilihanKurang = findViewById(R.id.rb_pilihan_kurang);
        rbPilihanCukup = findViewById(R.id.rb_pilihan_cukup);
        rbPilihanSangatBaik = findViewById(R.id.rb_pilihan_sangat_baik);

        btnSubmitPg = findViewById(R.id.btnSubmitPg);

        setKonten();

        btnSubmitPg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekJawaban();
            }
        });
    }

    public void setKonten(){
        rgPilihanJawaban.clearCheck();
        arr = soalArrayList.size();
        if(x >= arr){ //jika nilai x melebihi nilai arr(panjang array) maka akan pindah activity
//            Intent i = new Intent(AlumniActivity.this, );
//            startActivity(i);
//            Toast.makeText(getApplicationContext(),"Soal Habis", Toast.LENGTH_SHORT).show();
            Log.d("jawaban", String.valueOf(jawabanId));
            Log.d("soal", String.valueOf(soalJawabanId));
            TambahPgAlumni();
            //displayLoader();
            //loadQuestEssay();
        }else{
            //setting text dengan mengambil text dari method getter di kelas SoalPilihanGanda
            tvSoal.setText(soalArrayList.get(x));
        }
        x++;
    }

    public void cekJawaban() {
        if (rbPilihanKurang.isChecked()) {
            jawabanId.add(1);
            soalJawabanId.add(Integer.valueOf(soalIdArray.get(x-1)));
            setKonten();
        } else if (rbPilihanCukup.isChecked()) {
            jawabanId.add(2);
            soalJawabanId.add(Integer.valueOf(soalIdArray.get(x-1)));
            setKonten();
        } else if (rbPilihanBaik.isChecked()) {
            jawabanId.add(3);
            soalJawabanId.add(Integer.valueOf(soalIdArray.get(x-1)));
            setKonten();
        } else if (rbPilihanSangatBaik.isChecked()) {
            jawabanId.add(4);
            soalJawabanId.add(Integer.valueOf(soalIdArray.get(x-1)));
            setKonten();

        } else {
            Toast.makeText(this, "Silahkan pilih jawaban dulu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(AlumniActivity.this);
        pDialog.setMessage("Menambahkan Data.. Silakan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void TambahPgAlumni() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("jawaban", jawabanId);
            request.put("soal_id", soalJawabanId);
            request.put("user_id", user_id);
            request.put(KEY_KEY, key);

            Log.d("rekues", String.valueOf(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, tambah_quest_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        pDialog.dismiss();
                        try {
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                loadQuestEssay();

                            }else if(response.getInt(KEY_STATUS) == 1){
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

        //Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void loadQuestEssay() {
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, quest_essay_url, null , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        pDialog.dismiss();
//                        Log.d("respon","s");
                        try {
                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String quest = response.getString("quest_nama");
                                QuestEssayAlumni.add(quest);
                            }
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String quest_id = response.getString("quest_id");
                                QuestEssayId.add(quest_id);
                            }

                            pindahQuestEssay();
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

    private void pindahQuestEssay(){
//        Log.d("hehehe", QuestAlumni.get(0));
        Intent i = new Intent(AlumniActivity.this, AlumniEssayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("kues_nama", QuestEssayAlumni);
        bundle.putSerializable("kues_id", QuestEssayId);
        i.putExtra("kues_nama",bundle);
        i.putExtra("kues_id",bundle);
        startActivity(i);
        finish();
    }

    public void onBackPressed(){
        Toast.makeText(this, "Selesaikan Pertanyaan", Toast.LENGTH_SHORT).show();
    }
}

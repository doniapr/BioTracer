package com.aprr.biotracer.alumni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AlumniEssayActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_KEY = "key";
    public String key = "@gY=iF=&.YoZO[in%^";
    private static final String KEY_MESSAGE = "message";

    TextView tvSoalEssay;
    EditText etJawabanEssay;
    Button btnSubmitEssay;
    ArrayList<String> soalEssayList , soalIdEssay;
    ArrayList<String> jawabanEssay,soalJawabanEssayId;
    int user_id;
    int x=0;
    int arr;

    private ProgressDialog pDialog;

    ArrayList<String> MakulNama = new ArrayList<>();
    ArrayList<String> MakulId = new ArrayList<>();

    private String get_makul_url = "https://tracer.fadlidony.com/api/alumni/get/makul";
    private String tambah_quest_essay_url = "https://tracer.fadlidony.com/api/alumni/post/essay";

    SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soalEssayList = new ArrayList<>();
        soalIdEssay = new ArrayList<>();

        jawabanEssay = new ArrayList<>();
        soalJawabanEssayId = new ArrayList<>();

        session = new SessionHandler(getApplicationContext());
        user_id = session.getUserDetails().getUser_id();

        Bundle bnd = getIntent().getBundleExtra("kues_nama");
        soalEssayList = (ArrayList<String>) bnd.getSerializable("kues_nama");
        Bundle bndId = getIntent().getBundleExtra("kues_id");
        soalIdEssay = (ArrayList<String>) bndId.getSerializable("kues_id");

        setContentView(R.layout.activity_alumni_essay);

        tvSoalEssay = findViewById(R.id.tv_soal_essay);
        etJawabanEssay = findViewById(R.id.et_jawaban_essay);
        btnSubmitEssay = findViewById(R.id.btn_submit_essay);

        setKonten();

        btnSubmitEssay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekJawaban();
            }
        });

    }

    public void setKonten(){
        etJawabanEssay.setText(null);
        arr = soalEssayList.size();
        if(x >= arr){ //jika nilai x melebihi nilai arr(panjang array) maka akan pindah activity (kuis selesai)
            //Toast.makeText(getApplicationContext(),"Soal habis", Toast.LENGTH_SHORT).show();
            Log.d("jawaban", String.valueOf(jawabanEssay));
            Log.d("jawaban", String.valueOf(soalJawabanEssayId));
            TambahEssayAlumni();
        }else{
            tvSoalEssay.setText(soalEssayList.get(x));
        }
        x++;
    }

    public void cekJawaban(){
        if(!etJawabanEssay.getText().toString().isEmpty()){ //jika edit text TIDAK kosong
            jawabanEssay.add(etJawabanEssay.getText().toString());
            soalJawabanEssayId.add(soalIdEssay.get(x-1));
            setKonten();
        }else{
            Toast.makeText(this, "Silahkan pilih jawaban dulu!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(){
        Toast.makeText(this, "Selesaikan Pertanyaan", Toast.LENGTH_SHORT).show();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(AlumniEssayActivity.this);
        pDialog.setMessage("Menambahkan Data.. Silakan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void TambahEssayAlumni() {
        displayLoader();
        JSONArray request2 = new JSONArray();
        JSONArray request3 = new JSONArray();
        JSONObject request = new JSONObject();
        try {

            for (x=0;x<jawabanEssay.size();x++){
                request2.put(jawabanEssay.get(x));
            }

            for (x=0;x<soalJawabanEssayId.size();x++){
                request3.put(soalJawabanEssayId.get(x));
            }


            request.put("jawaban",request2);
            request.put("soal_id", request3);
            request.put("user_id", user_id);
            request.put(KEY_KEY, key);

            Log.d("rekues", String.valueOf(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, tambah_quest_essay_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                loadMakul();
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

    private void loadMakul() {
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, get_makul_url, null , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        pDialog.dismiss();
//                        Log.d("respon","s");
                        try {
                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String quest = response.getString("makul_nama");
                                MakulNama.add(quest);
                            }
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String quest_id = response.getString("makul_id");
                                MakulId.add(quest_id);
                            }
                            pindahBerguna();
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

    private void pindahBerguna(){
        Intent i = new Intent(AlumniEssayActivity.this, AlumniMakulBergunaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("makul_nama", MakulNama);
        bundle.putSerializable("makul_id", MakulId);
        i.putExtra("makul_nama",bundle);
        i.putExtra("makul_id",bundle);
        startActivity(i);
        finish();
    }
}

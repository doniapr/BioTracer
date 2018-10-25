package com.aprr.biotracer.pengguna;

import android.app.ProgressDialog;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.aprr.biotracer.R;
import com.aprr.biotracer.app.MySingleton;
import com.aprr.biotracer.app.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PenggunaEssayActivity extends AppCompatActivity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_KEY = "key";
    public String key = "@gY=iF=&.YoZO[in%^";
    private static final String KEY_MESSAGE = "message";

    TextView tvSoalEssay;
    EditText etJawabanEssay;
    Button btnSubmitEssay;
    String JawabanSaran;
    int user_id;

    private ProgressDialog pDialog;

    private String tambah_quest_essay_url = "https://tracer.fadlidony.com/api/alumni/postprofile";

    SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionHandler(getApplicationContext());
        user_id = session.getUserDetails().getUser_id();
        setContentView(R.layout.activity_pengguna_essay);

        tvSoalEssay = findViewById(R.id.tv_soal_pengguna);
        etJawabanEssay = findViewById(R.id.et_jawaban_essay1);
        btnSubmitEssay = findViewById(R.id.btn_submit_essay1);


        btnSubmitEssay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JawabanSaran = etJawabanEssay.getText().toString();
                cekJawaban();
            }
        });

    }


    public void cekJawaban(){
        if(!etJawabanEssay.getText().toString().isEmpty()){ //jika edit text TIDAK kosong
            displayLoader();
            TambahEssayPengguna();
        }else{
            Toast.makeText(this, "Silahkan pilih jawaban dulu!", Toast.LENGTH_SHORT).show();
            etJawabanEssay.setError("Tidak Boleh Kosong");
        }
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(PenggunaEssayActivity.this);
        pDialog.setMessage("Menambahkan Data.. Silakan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void TambahEssayPengguna() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("jawaban", JawabanSaran);
            request.put("user_id", user_id);
            request.put(KEY_KEY, key);

//            Log.d("rekues", String.valueOf(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, tambah_quest_essay_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        pDialog.dismiss();
                        try {
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
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
}

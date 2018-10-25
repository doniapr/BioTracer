package com.aprr.biotracer.alumni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aprr.biotracer.MainActivity;
import com.aprr.biotracer.MainPenggunaActivity;
import com.aprr.biotracer.R;
import com.aprr.biotracer.app.MySingleton;
import com.aprr.biotracer.app.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class SaranAlumniActivity extends AppCompatActivity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_KEY = "key";
    public String key = "@gY=iF=&.YoZO[in%^";
    private static final String KEY_MESSAGE = "message";

    TextView tvSoalSaran;
    EditText etJawabanSaran;
    Button btnSubmitSaran;
    String JawabanSaran;
    int user_id;

    private ProgressDialog pDialog;

    private String tambah_saran_alumni_url = "https://tracer.fadlidony.com/api/alumni/post/saran";

    SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saran_alumni);

        session = new SessionHandler(getApplicationContext());
        user_id = session.getUserDetails().getUser_id();

        tvSoalSaran = findViewById(R.id.tv_saran_alumni);
        etJawabanSaran = findViewById(R.id.et_saran_alumni);
        btnSubmitSaran = findViewById(R.id.btn_saran_alumni);

        tvSoalSaran.setText("Apa saran Saudara untuk pengembangan dan kemajuan Departemen Biologi?");
        btnSubmitSaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JawabanSaran = etJawabanSaran.getText().toString();

                cekJawaban();
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(SaranAlumniActivity.this);
        pDialog.setMessage("Menambahkan Data.. Silakan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void cekJawaban(){
        if(!etJawabanSaran.getText().toString().isEmpty()){ //jika edit text TIDAK kosong
//            displayLoader();
            TambahSaranAlumni();
//            pindahMain();
        }else{
            Toast.makeText(this, "Silahkan pilih jawaban dulu!", Toast.LENGTH_SHORT).show();
            etJawabanSaran.setError("Tidak Boleh Kosong");
        }
    }

    private void pindahMain() {
        pDialog.dismiss();
        Intent i = new Intent(SaranAlumniActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    private void TambahSaranAlumni() {
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
                (Request.Method.POST, tambah_saran_alumni_url, request, new Response.Listener<JSONObject>() {
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
                            pindahMain();
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

    public void onBackPressed(){
        Toast.makeText(this, "Selesaikan Pertanyaan", Toast.LENGTH_SHORT).show();
    }

}

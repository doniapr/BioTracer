package com.aprr.biotracer.ProfilData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ProfilPenggunaAddActivity extends AppCompatActivity {
    EditText etHP,etInstansi, etAlamat;
    String hp,instansi, alamat;
    Button btnSubmitPengguna;
    int user_id;
    SessionHandler session;
    private ProgressDialog pDialog;
    private static final String KEY_HP = "hp";
    private static final String KEY_INSTANSI = "instansi";
    private static final String KEY_ALAMAT = "alamat";
    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "status";
    private static final String KEY_KEY = "key";
    private static final String KEY_MESSAGE = "message";
    private String tambah_pengguna_url = "https://boyojnck.000webhostapp.com/member/tambah_profil_pengguna.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_pengguna_add);

        etHP = findViewById(R.id.et_pengguna_hp);
        etInstansi = findViewById(R.id.et_pengguna_instansi);
        etAlamat = findViewById(R.id.et_pengguna_alamat);

        btnSubmitPengguna = findViewById(R.id.submit_pengguna);

        session = new SessionHandler(getApplicationContext());
        user_id = session.getUserDetails().getUser_id();

        btnSubmitPengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hp = etHP.getText().toString();
                instansi = etInstansi.getText().toString();
                alamat = etAlamat.getText().toString();

                if (validateInputs()) {
                    TambahPengguna();
                }
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(ProfilPenggunaAddActivity.this);
        pDialog.setMessage("Menambahkan Data.. Silakan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void TambahPengguna() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_HP, hp);
            request.put(KEY_INSTANSI,instansi );
            request.put(KEY_ALAMAT, alamat);
            request.put("user_id", user_id);
//            Log.d("rekues", request.getString("informasi_id"));
//            Log.d("rekues", request.getString("pendidikan_id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, tambah_pengguna_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                                loadMain();
                            }else if(response.getInt(KEY_STATUS) == 1){
                                //Display error message if username is already existsing
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }else{
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


    private boolean validateInputs() {
        if (KEY_EMPTY.equals(hp)) {
            etHP.setError("Nomor HP Harus Diisi");
            etHP.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(alamat)) {
            etAlamat.setError("Alamat Harus Diisi");
            etAlamat.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(instansi)) {
            etInstansi.setError("Nama Instansi Harus Diisi");
            etInstansi.requestFocus();
            return false;
        }
        return true;
    }

    private void loadMain(){
        Intent i = new Intent(getApplicationContext(), MainPenggunaActivity.class);
        startActivity(i);
        finish();
    }
}

package com.aprr.biotracer.alumni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aprr.biotracer.R;
import com.aprr.biotracer.app.MySingleton;
import com.aprr.biotracer.app.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlumniMakulBergunaActivity extends AppCompatActivity {

    RecyclerView rvBerguna;
    Button btnBergunaSbmt;
    private ArrayList<String> MakulList;
    private ArrayList<String> MakulIdList;
    ListHandler listItem;
    String makulChecked;
    int user_id;

    private String tambah_berguna_url = "https://tracer.fadlidony.com/api/alumni/post/berguna";
    private static final String KEY_STATUS = "status";
    private static final String KEY_KEY = "key";
    public String key = "@gY=iF=&.YoZO[in%^";
    private static final String KEY_MESSAGE = "message";

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumni_makul_berguna);

        MakulList = new ArrayList<>();
        MakulIdList = new ArrayList<>();

        final SessionHandler session = new SessionHandler(getApplicationContext());

        Bundle bnd = getIntent().getBundleExtra("makul_nama");
        MakulList = (ArrayList<String>) bnd.getSerializable("makul_nama");
        Bundle bndId = getIntent().getBundleExtra("makul_id");
        MakulIdList = (ArrayList<String>) bndId.getSerializable("makul_id");

        listItem = new ListHandler(getApplicationContext());

        btnBergunaSbmt = findViewById(R.id.btn_berguna_sbmt);
        rvBerguna = findViewById(R.id.rvListBerguna);

        rvBerguna.setLayoutManager(new LinearLayoutManager(this));
        rvBerguna.setAdapter(new BergunaAdapter(this,MakulList, MakulIdList));

        btnBergunaSbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makulChecked = String.valueOf(listItem.getMakul().getId_makul());
                user_id = session.getUserDetails().getUser_id();
                displayLoader();
                TambahBerguna();

//                pindahPerlu();
                Log.d("idnya", String.valueOf(listItem.getMakul().getId_makul()));
            }
        });
    }

    private void pindahPerlu(){
//        pDialog.dismiss();
        Intent i = new Intent(AlumniMakulBergunaActivity.this, AlumniMakulPerluActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("makul_nama", MakulList);
        bundle.putSerializable("makul_id", MakulIdList);
        i.putExtra("makul_nama",bundle);
        i.putExtra("makul_id",bundle);
        startActivity(i);
        finish();
    }

    private void TambahBerguna() {
//        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("jawaban", makulChecked);
            request.put("user_id", user_id);
            request.put(KEY_KEY, key);

            Log.d("rekues", String.valueOf(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, tambah_berguna_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                                pindahPerlu();
                            }else {
                                Toast.makeText(getApplicationContext(),
                                        "gagal", Toast.LENGTH_SHORT).show();
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

    private void displayLoader() {
        pDialog = new ProgressDialog(AlumniMakulBergunaActivity.this);
        pDialog.setMessage("Menambahkan Data.. Silakan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    public void onBackPressed(){
        Toast.makeText(this, "Selesaikan Pertanyaan", Toast.LENGTH_SHORT).show();
    }
}

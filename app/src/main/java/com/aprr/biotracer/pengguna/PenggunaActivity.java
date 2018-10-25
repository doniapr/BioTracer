package com.aprr.biotracer.pengguna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aprr.biotracer.ProfilData.ProfilPenggunaAddActivity;
import com.aprr.biotracer.R;
import com.aprr.biotracer.app.MySingleton;
import com.aprr.biotracer.app.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PenggunaActivity extends AppCompatActivity {


    private String alumni_url = "https://boyojnck.000webhostapp.com/member/get_user.php";
    private String hubungan_url = "https://boyojnck.000webhostapp.com/member/get_hubungan.php";
    private String tambah_hubungan_url = "";
    private String quest_pg_url = "https://boyojnck.000webhostapp.com/member/get_quest_pengguna.php";
    private ProgressDialog pDialog;
    TextView tvNamaAlumni, tvHubungan;
    Button btnHubungan;
    Spinner spAlumni, spHubungan;
    int UserAlumniId, HubunganId, profilPenggunaId, userId;

    private static final String KEY_STATUS = "status";
    private static final String KEY_KEY = "key";
    public String key = "@gY=iF=&.YoZO[in%^";
    private static final String KEY_MESSAGE = "message";

    ArrayList<String> QuestPengguna = new ArrayList<>();
    ArrayList<String> QuestPenggunaId = new ArrayList<>();

    ArrayList<Object> DetailPengguna = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bnd = getIntent().getBundleExtra("detailPengguna");
        DetailPengguna = (ArrayList<Object>) bnd.getSerializable("detailPengguna");

        setContentView(R.layout.activity_pengguna);

        profilPenggunaId = Integer.parseInt(DetailPengguna.get(0).toString());

        SessionHandler session = new SessionHandler(getApplicationContext());
        userId = session.getUserDetails().getUser_id();

        tvNamaAlumni = findViewById(R.id.tv_name_alumni);
        tvHubungan = findViewById(R.id.tv_hubungan);
        btnHubungan = findViewById(R.id.btn_submit_hubungan);
        spAlumni = findViewById(R.id.sp_nama_alumni);
        spHubungan = findViewById(R.id.sp_hubungan);

        displayLoader();
        loadDataAlumni();
//        loadHubungan();
//        LoadProfilPenggunaId(userId);

        btnHubungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    Log.d("hoho", String.valueOf(UserAlumniId));
                    Log.d("hoho", String.valueOf(HubunganId));
//                    LoadProfilPenggunaId(userId);
                    tambahhubungan();
//                    displayLoader();
//                    loadQuestPengguna();
                }
            }
        });


    }

    private boolean validateInputs() {
        if (UserAlumniId== 0) {
            tvNamaAlumni.setError("Pilih Alumni");
            tvNamaAlumni.requestFocus();
            return false;
        }

        if (HubunganId== 0) {
            tvHubungan.setError("Pilih Hubungan");
            tvHubungan.requestFocus();
            return false;
        }

        return true;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Memuat Data.. Silakan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void loadDataAlumni() {
        final List<AlumniData> alumniList = new ArrayList<>();
        final List<String> alumni = new ArrayList<>();
        final List<AlumniData> alumniIdList = new ArrayList<>();
        final List<String> alumni_id = new ArrayList<>();

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, alumni_url, null , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
//                        pDialog.dismiss();
//                        Log.d("respon","s");
                        try {
                            alumniList.add(new AlumniData("Pilih..."));
                            alumniIdList.add(new AlumniData("0"));
                            alumni.add("Pilih...");
                            alumni_id.add("0");

                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String alum = response.getString("user_nama");
//                                Log.d("hehehehe", informasi);
                                alumniList.add(new AlumniData(alum));
                                alumni.add(alum);
                            }
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String alum_id = response.getString("user_id");
//                                Log.d("hehehehe", informasi);
                                alumniIdList.add(new AlumniData(alum_id));
                                alumni_id.add(alum_id);
                            }
                            final AlumniDataAdapter alumniAdapter = new AlumniDataAdapter(getApplicationContext(),
                                    R.layout.list_informasi_profil, R.id.spinner_profil_text, alumniList);
                            spAlumni.setAdapter(alumniAdapter);

                            spAlumni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    Toast.makeText(getApplicationContext(),info_id.get(position),Toast.LENGTH_SHORT).show();
                                    UserAlumniId = Integer.parseInt(alumni_id.get(position));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            loadHubungan();

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

    private void loadHubungan() {
        final List<Hubungan> hubunganList = new ArrayList<>();
        final List<String> hub = new ArrayList<>();
        final List<Hubungan> hubunganIdList = new ArrayList<>();
        final List<String> hub_id = new ArrayList<>();

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, hubungan_url, null , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        pDialog.dismiss();
//                        Log.d("respon","s");
                        try {
                            hubunganList.add(new Hubungan("Pilih..."));
                            hubunganIdList.add(new Hubungan("0"));
                            hub.add("Pilih...");
                            hub_id.add("0");

                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String hubungan = response.getString("hubungan_nama");
//                                Log.d("hehehehe", informasi);
                                hubunganList.add(new Hubungan(hubungan));
                                hub.add(hubungan);
                            }
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String hubungan_id = response.getString("hubungan_id");
//                                Log.d("hehehehe", informasi);
                                hubunganIdList.add(new Hubungan(hubungan_id));
                                hub_id.add(hubungan_id);
                            }
                            final HubunganAdapter hubunganAdapter = new HubunganAdapter(getApplicationContext(),
                                    R.layout.list_informasi_profil, R.id.spinner_profil_text, hubunganList);
                            spHubungan.setAdapter(hubunganAdapter);

                            spHubungan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    Toast.makeText(getApplicationContext(),info_id.get(position),Toast.LENGTH_SHORT).show();
                                    HubunganId = Integer.parseInt(hub_id.get(position));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

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

    private void tambahhubungan() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("user_id", UserAlumniId);
            request.put("profilepengguna_id", profilPenggunaId);
            request.put("hubungan_id", HubunganId);
            request.put(KEY_KEY,key);

            Log.d("rekues",String.valueOf(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, tambah_hubungan_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

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

                        loadQuestPengguna();
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

    private void loadQuestPengguna() {
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, quest_pg_url, null , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        pDialog.dismiss();
//                        Log.d("respon","s");
                        try {
                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String quest = response.getString("quest_nama");
                                QuestPengguna.add(quest);
                            }
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String quest_id = response.getString("quest_id");
                                QuestPenggunaId.add(quest_id);
                            }

                            pindahQuestPgPengguna();
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

    private void pindahQuestPgPengguna(){
        Log.d("hehehe", QuestPengguna.get(0));
        Intent i = new Intent(PenggunaActivity.this,PenggunaQuestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("kues_nama", QuestPengguna);
        bundle.putSerializable("kues_id", QuestPenggunaId);
        i.putExtra("kues_nama",bundle);
        i.putExtra("kues_id",bundle);
        startActivity(i);
    }
}

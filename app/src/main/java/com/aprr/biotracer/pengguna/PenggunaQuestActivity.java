package com.aprr.biotracer.pengguna;

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
import com.aprr.biotracer.alumni.AlumniActivity;
import com.aprr.biotracer.app.MySingleton;
import com.aprr.biotracer.app.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PenggunaQuestActivity extends AppCompatActivity {

    TextView tvSoal;
    RadioGroup rgPilihanJawaban;
    RadioButton rbPilihanSangatKurang, rbPilihanKurang, rbPilihanCukup, rbPilihanBaik, rbPilihanSangatBaik;
    Button btnSubmitPg;
    ArrayList<String> soalArrayList , soalIdArray;
    ArrayList<Integer> jawabanId,soalJawabanId;

    ArrayList<String> QuestEssayPengguna = new ArrayList<>();
    ArrayList<String> QuestEssayPenggunaId = new ArrayList<>();

    int user_id;
    private String tambah_quest_pengguna_url = "";
    private String quest_essay_url = "https://boyojnck.000webhostapp.com/member/get_quest_essay_pengguna.php";
    private static final String KEY_STATUS = "status";
    private static final String KEY_KEY = "key";
    public String key = "@gY=iF=&.YoZO[in%^";
    private static final String KEY_MESSAGE = "message";

    private ProgressDialog pDialog;

    SessionHandler session;
    int arr; //untuk menampung nilai panjang array
    int x;
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

        setContentView(R.layout.activity_pengguna_quest);

        tvSoal = findViewById(R.id.tv_soal_pengguna);
        rgPilihanJawaban = findViewById(R.id.rg1PilihanJawaban);
        rbPilihanBaik = findViewById(R.id.rb1_pilihan_baik);
        rbPilihanSangatKurang = findViewById(R.id.rb1_pilihan_sangat_kurang);
        rbPilihanKurang = findViewById(R.id.rb1_pilihan_kurang);
        rbPilihanCukup = findViewById(R.id.rb1_pilihan_cukup);
        rbPilihanSangatBaik = findViewById(R.id.rb1_pilihan_sangat_baik);

        btnSubmitPg = findViewById(R.id.btn1SubmitPg);

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
            Toast.makeText(getApplicationContext(),"Soal Habis", Toast.LENGTH_SHORT).show();
            Log.d("jawaban", String.valueOf(jawabanId));
            Log.d("soal", String.valueOf(soalJawabanId));
            TambahPenggunaPg();
//            displayLoader();
        }else{
            //setting text dengan mengambil text dari method getter di kelas SoalPilihanGanda
            tvSoal.setText(soalArrayList.get(x));
        }
        x++;
    }

    public void cekJawaban() {
        if (rbPilihanSangatKurang.isChecked()) {
            jawabanId.add(1);
            soalJawabanId.add(Integer.valueOf(soalIdArray.get(x-1)));
            setKonten();
        } else if (rbPilihanKurang.isChecked()) {
            jawabanId.add(2);
            soalJawabanId.add(Integer.valueOf(soalIdArray.get(x-1)));
            setKonten();
        } else if (rbPilihanCukup.isChecked()) {
            jawabanId.add(3);
            soalJawabanId.add(Integer.valueOf(soalIdArray.get(x-1)));
            setKonten();
        } else if (rbPilihanBaik.isChecked()) {
            jawabanId.add(4);
            soalJawabanId.add(Integer.valueOf(soalIdArray.get(x-1)));
            setKonten();
        } else if (rbPilihanSangatBaik.isChecked()) {
            jawabanId.add(5);
            soalJawabanId.add(Integer.valueOf(soalIdArray.get(x-1)));
            setKonten();
        } else {
            Toast.makeText(this, "Silahkan pilih jawaban dulu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void TambahPenggunaPg() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("jawaban", jawabanId);
            request.put("soal_id", soalJawabanId);
            request.put("user_id", user_id);
            request.put(KEY_KEY, key);

//            Log.d("rekues", String.valueOf(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, tambah_quest_pengguna_url, request, new Response.Listener<JSONObject>() {
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

    private void displayLoader() {
        pDialog = new ProgressDialog(PenggunaQuestActivity.this);
        pDialog.setMessage("Menambahkan Data.. Silakan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void pindahQuestEssay(){
        Intent i = new Intent(PenggunaQuestActivity.this, PenggunaEssayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("kues_nama", QuestEssayPengguna);
        bundle.putSerializable("kues_id", QuestEssayPenggunaId);
        i.putExtra("kues_nama",bundle);
        i.putExtra("kues_id",bundle);
        startActivity(i);
    }

}

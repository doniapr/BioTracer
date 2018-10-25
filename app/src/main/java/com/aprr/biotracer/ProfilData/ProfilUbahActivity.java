package com.aprr.biotracer.ProfilData;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aprr.biotracer.MainActivity;
import com.aprr.biotracer.R;
import com.aprr.biotracer.app.MySingleton;
import com.aprr.biotracer.app.SessionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfilUbahActivity extends AppCompatActivity {

    EditText etJk,etTempatLahir, etTanggalLahir, etMasukKuliah, etLulusKuliah, etIpk,etAlamatRumah, etNamaInstansi, etAlamatInstansi,etHp,etJabatan;
    TextView tvTl,tvName,tvInformasi,tvGaji,tvPekerjaan, tvWaktu, tvPendidikan;
    String jk,tempatLahir, tanggalLahir,masukKuliah, lulusKuliah, ipk,alamatRumah, namaInstansi, alamatInstansi,hp,jabatan;
    String id_info, id_pend, id_kerja, id_waktu, id_gaji;
    Button btnUbahProfil;
    String[] tglLahir;
    int user_id;
    SessionHandler session;
    private ProgressDialog pDialog;
    private static final String KEY_INFO = "info_name";
    private static final String KEY_PEND = "pendidikan_name";
    private static final String KEY_KERJA = "pekerjaan_name";
    private static final String KEY_WAKTU = "waktu_name";
    private static final String KEY_GAJI = "gaji_name";
    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "status";
    private static final String KEY_KEY = "key";
    public String key = "@gY=iF=&.YoZO[in%^";
    private static final String KEY_MESSAGE = "message";
    Spinner informasiSpinner, pendidikanSpinner, pekerjaanSpinner,waktuSpinner, gajiSpinner;
    private String info_url = "https://tracer.fadlidony.com/api/get/informasi";
    private String waktu_url = "https://tracer.fadlidony.com/api/get/waktu";
    private String gaji_url = "https://tracer.fadlidony.com/api/get/gaji";
    private String kerja_url = "https://tracer.fadlidony.com/api/get/pekerjaan";
    private String pend_url = "https://tracer.fadlidony.com/api/get/pendidikan";
    private String tambah_profil_url = "https://tracer.fadlidony.com/api/alumni/postprofile";

    ArrayList<Object> Profil = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bnd = getIntent().getBundleExtra("profil");
        Profil = (ArrayList<Object>) bnd.getSerializable("profil");

        Log.d("profil", String.valueOf(Profil));

        setContentView(R.layout.activity_profil_ubah);

        tvName = findViewById(R.id.tv_name_profil);
        tvInformasi = findViewById(R.id.tv_informasi_edit);
        tvPekerjaan = findViewById(R.id.tv_pekerjaan_edit);
        tvPendidikan = findViewById(R.id.tv_pendidikan_edit);
        tvGaji = findViewById(R.id.tv_gaji_edit);
        tvWaktu = findViewById(R.id.tv_waktu_edit);

        informasiSpinner = findViewById(R.id.sp_info_edit);
        pendidikanSpinner = findViewById(R.id.sp_pendidikan_edit);
        pekerjaanSpinner = findViewById(R.id.sp_pekerjaan_edit);
        waktuSpinner = findViewById(R.id.sp_waktu_edit);
        gajiSpinner = findViewById(R.id.sp_gaji_edit);
        etJk = findViewById(R.id.et_jk_edit);
        etTempatLahir = findViewById(R.id.et_tempat_lahir_edit);
        etTanggalLahir = findViewById(R.id.et_tanggal_lahir_edit);
        etMasukKuliah = findViewById(R.id.et_masuk_kuliah_edit);
        etLulusKuliah = findViewById(R.id.et_lulus_kuliah_edit);
        etIpk = findViewById(R.id.et_IPK_edit);
        etAlamatRumah = findViewById(R.id.et_alamat_rumah_edit);
        etAlamatInstansi = findViewById(R.id.et_alamat_instansi_edit);
        etNamaInstansi = findViewById(R.id.et_nama_instansi_edit);
        etHp = findViewById(R.id.et_hp_edit);

        etJabatan = findViewById(R.id.et_jabatan_edit);
        session = new SessionHandler(getApplicationContext());

        tvName.setText(session.getUserDetails().getFullName());
        displayLoader();

        tglLahir = Profil.get(2).toString().split(" ");

        etJk.setText(Profil.get(0).toString());
        etTempatLahir.setText(Profil.get(1).toString());
        etTanggalLahir.setText(tglLahir[0]+"-"+tglLahir[1]+"-"+tglLahir[2]);
        etMasukKuliah.setText(Profil.get(3).toString());
        etLulusKuliah.setText(Profil.get(4).toString());
        etIpk.setText(Profil.get(5).toString());
        etAlamatRumah.setText(Profil.get(6).toString());
        etNamaInstansi.setText(Profil.get(7).toString());
        etAlamatInstansi.setText(Profil.get(8).toString());
        etHp.setText(Profil.get(9).toString());
        etJabatan.setText(Profil.get(10).toString());
        loadInformasi();
        loadPendidikan();
        loadPekerjaan();
        loadWaktu();
        loadGaji();


        btnUbahProfil = findViewById(R.id.submit_profil_edit);

        btnUbahProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jk = etJk.getText().toString();
                tempatLahir = etTempatLahir.getText().toString();
                tanggalLahir = etTanggalLahir.getText().toString();
                masukKuliah = etMasukKuliah.getText().toString();
                lulusKuliah = etLulusKuliah.getText().toString();
                ipk = etIpk.getText().toString();
                alamatRumah = etAlamatRumah.getText().toString();
                alamatInstansi = etAlamatInstansi.getText().toString();
                namaInstansi = etNamaInstansi.getText().toString();
                hp = etHp.getText().toString();
                jabatan = etJabatan.getText().toString();

                if (validateInputs()) {
                    TambahProfil();
                }
            }
        });


        session = new SessionHandler(getApplicationContext());
        user_id = session.getUserDetails().getUser_id();


    }


    private void displayLoader() {
        pDialog = new ProgressDialog(ProfilUbahActivity.this);
        pDialog.setMessage("Memuat.. Silakan Tunggu...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void TambahProfil() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("jk", jk);
            request.put("tempat_lahir", tempatLahir);
            request.put("tanggal_lahir", tanggalLahir);
            request.put("masuk_kuliah", masukKuliah);
            request.put("lulus_kuliah", lulusKuliah);
            request.put("ipk", ipk);
            request.put("alamat_rumah", alamatRumah);
            request.put("alamat_instansi", alamatInstansi);
            request.put("nama_instansi", namaInstansi);
            request.put("hp", hp);
            request.put("user_id", user_id);
            request.put("jabatan", jabatan);
            request.put("informasi_id", id_info);
            request.put("pendidikan_id", id_pend);
            request.put("pekerjaan_id", id_kerja);
            request.put("waktu_id", id_waktu);
            request.put("gaji_id", id_gaji);
            request.put(KEY_KEY, key);

            Log.d("rekues", String.valueOf(request));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, tambah_profil_url, request, new Response.Listener<JSONObject>() {
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
        if (KEY_EMPTY.equals(jk)) {
            etJk.setError("Jenis Kelamin Harus Diisi");
            etJk.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(tempatLahir)) {
            etTempatLahir.setError("Tempat Lahir Harus Diisi");
            etTempatLahir.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(tanggalLahir)) {
            etTanggalLahir.setError("Tanggal Lahir Harus Diisi");
            etTanggalLahir.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(masukKuliah)) {
            etMasukKuliah.setError("Masuk Kuliah Harus Diisi");
            etMasukKuliah.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(lulusKuliah)) {
            etLulusKuliah.setError("Lulus Kuliah Harus Diisi");
            etLulusKuliah.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(ipk)) {
            etIpk.setError("Ipk Harus Diisi");
            etIpk.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(alamatInstansi)) {
            etAlamatInstansi.setError("Alamat Instansi Harus Diisi");
            etAlamatInstansi.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(alamatRumah)) {
            etAlamatRumah.setError("Alamat Rumah Harus Diisi");
            etAlamatRumah.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(hp)) {
            etHp.setError("No HP Harus Diisi");
            etHp.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(namaInstansi)) {
            etNamaInstansi.setError("Nama Instansi Harus Diisi");
            etNamaInstansi.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(jabatan)) {
            etJabatan.setError("Jabatan Harus Diisi");
            etJabatan.requestFocus();
            return false;
        }
        if (id_info== "0") {
            tvInformasi.setError("Pilih Informasi");
            tvInformasi.requestFocus();
            return false;
        }
        if (id_gaji== "0") {
            tvGaji.setError("Pilih Gaji");
            tvGaji.requestFocus();
            return false;
        }

        if (id_waktu== "0") {
            tvWaktu.setError("Pilih Waktu");
            tvWaktu.requestFocus();
            return false;
        }

        if (id_pend== "0") {
            tvPendidikan.setError("Pilih Pendidikan");
            tvPendidikan.requestFocus();
            return false;
        }

        if (id_kerja== "0") {
            tvPekerjaan.setError("Pilih Pekerjaan");
            tvPekerjaan.requestFocus();
            return false;
        }

        return true;
    }

    private void loadMain(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void loadInformasi() {
        final List<Informasi> informasiList = new ArrayList<>();
        final List<String> info = new ArrayList<>();
        final List<Informasi> informasiIdList = new ArrayList<>();
        final List<String> info_id = new ArrayList<>();
//
//        JSONArray request = new JSONArray();
//        try {
//            //Populate the request parameters
//            request.put(KEY_KEY, key);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, info_url, null , new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
//                        pDialog.dismiss();
//                        Log.d("respon","s");
                        try {
                            informasiList.add(new Informasi("Pilih..."));
                            informasiIdList.add(new Informasi("0"));
                            info.add("Pilih...");
                            info_id.add("0");

                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String informasi = response.getString(KEY_INFO);
//                                Log.d("hehehehe", informasi);
                                informasiList.add(new Informasi(informasi));
                                info.add(informasi);
                            }
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String informasi_id = response.getString("info_id");
//                                Log.d("hehehehe", informasi);
                                informasiIdList.add(new Informasi(informasi_id));
                                info_id.add(informasi_id);
                            }
                            final InformasiAdapter infoAdapter = new InformasiAdapter(ProfilUbahActivity.this,
                                    R.layout.list_informasi_profil, R.id.spinner_profil_text, informasiList);
                            informasiSpinner.setAdapter(infoAdapter);

                            informasiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    Toast.makeText(getApplicationContext(),info_id.get(position),Toast.LENGTH_SHORT).show();
                                    id_info = info_id.get(position);
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

    private void loadPendidikan() {
        final List<Pendidikan> pendidikanList = new ArrayList<>();
        final List<String> pend = new ArrayList<>();
        final List<Pendidikan> pendidikanIdList = new ArrayList<>();
        final List<String> pend_id = new ArrayList<>();

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, pend_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
//                        pDialog.dismiss();
                        pendidikanList.add(new Pendidikan("Pilih..."));
                        pendidikanIdList.add(new Pendidikan("0"));
                        pend.add("Pilih...");
                        pend_id.add("0");
                        try {
                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String pendidikan = response.getString(KEY_PEND);
//                                Log.d("hehehehe", informasi);
                                pendidikanList.add(new Pendidikan(pendidikan));
                                pend.add(pendidikan);
                            }
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String pendidikan_id = response.getString("pendidikan_id");
//                                Log.d("hehehehe", informasi);
                                pendidikanIdList.add(new Pendidikan(pendidikan_id));
                                pend_id.add(pendidikan_id);
                            }
                            final PendidikanAdapter pendAdapter = new PendidikanAdapter(ProfilUbahActivity.this,
                                    R.layout.list_pendidikan_profil, R.id.spinner_pend_text, pendidikanList);
                            pendidikanSpinner.setAdapter(pendAdapter);

                            pendidikanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    Toast.makeText(getApplicationContext(),pend_id.get(position),Toast.LENGTH_SHORT).show();
                                    id_pend = pend_id.get(position);
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

    private void loadPekerjaan() {
        final List<Pekerjaan> pekerjaanList = new ArrayList<>();
        final List<String> kerja = new ArrayList<>();
        final List<Pekerjaan> pekerjaanIdList = new ArrayList<>();
        final List<String> kerja_id = new ArrayList<>();
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, kerja_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
//                        pDialog.dismiss();
                        pekerjaanList.add(new Pekerjaan("Pilih..."));
                        pekerjaanIdList.add(new Pekerjaan("0"));
                        kerja.add("Pilih...");
                        kerja_id.add("0");
                        try {
                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String pekerjaan = response.getString(KEY_KERJA);
//                                Log.d("hehehehe", informasi);
                                pekerjaanList.add(new Pekerjaan(pekerjaan));
                                kerja.add(pekerjaan);
                            }
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String pekerjaan_id = response.getString("pekerjaan_id");
//                                Log.d("hehehehe", informasi);
                                pekerjaanIdList.add(new Pekerjaan(pekerjaan_id));
                                kerja_id.add(pekerjaan_id);
                            }
                            final PekerjaanAdapter kerjaAdapter = new PekerjaanAdapter(ProfilUbahActivity.this,
                                    R.layout.list_informasi_profil, R.id.spinner_profil_text, pekerjaanList);
                            pekerjaanSpinner.setAdapter(kerjaAdapter);

                            pekerjaanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    Toast.makeText(getApplicationContext(),kerja_id.get(position),Toast.LENGTH_SHORT).show();
                                    id_kerja = kerja_id.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            loadPendidikan();

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

    private void loadWaktu() {
        final List<Waktu> waktuList = new ArrayList<>();
        final List<String> wak = new ArrayList<>();
        final List<Waktu> waktuIdList = new ArrayList<>();
        final List<String> wak_id = new ArrayList<>();
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, waktu_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
//                        pDialog.dismiss();
                        waktuList.add(new Waktu("Pilih..."));
                        waktuIdList.add(new Waktu("0"));
                        wak.add("Pilih...");
                        wak_id.add("0");
                        try {
                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String waktu = response.getString(KEY_WAKTU);
//                                Log.d("hehehehe", informasi);
                                waktuList.add(new Waktu(waktu));
                                wak.add(waktu);
                            }
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String waktu_id = response.getString("waktu_id");
//                                Log.d("hehehehe", informasi);
                                waktuIdList.add(new Waktu(waktu_id));
                                wak_id.add(waktu_id);
                            }
                            final WaktuAdapter waktuAdapter = new WaktuAdapter(ProfilUbahActivity.this,
                                    R.layout.list_informasi_profil, R.id.spinner_profil_text, waktuList);
                            waktuSpinner.setAdapter(waktuAdapter);

                            waktuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    Toast.makeText(getApplicationContext(),wak_id.get(position),Toast.LENGTH_SHORT).show();
                                    id_waktu = wak_id.get(position);
                                }


                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            loadPendidikan();

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

    private void loadGaji() {
        final List<Gaji> gajiList = new ArrayList<>();
        final List<String> gaj = new ArrayList<>();
        final List<Gaji> gajiIdList = new ArrayList<>();
        final List<String> gaj_id = new ArrayList<>();
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, gaji_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        pDialog.dismiss();
                        gajiList.add(new Gaji("Pilih..."));
                        gajiIdList.add(new Gaji("0"));
                        gaj.add("Pilih...");
                        gaj_id.add("0");
                        try {
                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String gaji = response.getString(KEY_GAJI);
//                                Log.d("hehehehe", informasi);
                                gajiList.add(new Gaji(gaji));
                                gaj.add(gaji);
                            }
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String gaji_id = response.getString("gaji_id");
//                                Log.d("hehehehe", informasi);
                                gajiIdList.add(new Gaji(gaji_id));
                                gaj_id.add(gaji_id);
                            }
                            final GajiAdapter gajiAdapter = new GajiAdapter(ProfilUbahActivity.this,
                                    R.layout.list_informasi_profil, R.id.spinner_profil_text, gajiList);
                            gajiSpinner.setAdapter(gajiAdapter);

                            gajiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    Toast.makeText(getApplicationContext(),gaj_id.get(position),Toast.LENGTH_SHORT).show();
                                    id_gaji = gaj_id.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            loadPendidikan();

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

    @Override
    public void onBackPressed() {
        loadMain();
    }
}

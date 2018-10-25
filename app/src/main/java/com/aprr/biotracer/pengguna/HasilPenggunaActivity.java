package com.aprr.biotracer.pengguna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aprr.biotracer.R;
import com.aprr.biotracer.app.SessionHandler;

public class HasilPenggunaActivity extends AppCompatActivity {

    int userId;
    SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_pengguna);

        userId = session.getUserDetails().getUser_id();
    }
}

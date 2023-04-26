package com.skytel.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skytel.quizapp.Model.PrizeModel;
import com.skytel.quizapp.Networking.FingerRequest;
import com.skytel.quizapp.Networking.SkyRequestError;

public class TanCodeActivity extends AppCompatActivity {

    private Button play, againTanCode;
    private EditText tanCode;
    private TextView back;
    private String phonenumber;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tan_code);

        play = findViewById(R.id.play);
        tanCode = findViewById(R.id.tanCode);
        back = findViewById(R.id.back);
        againTanCode = findViewById(R.id.againTanCode);
        progressBar = findViewById(R.id.progress_bar);

        phonenumber = getIntent().getStringExtra("phonenumber");

        againTanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTanCode(phonenumber);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tanCode.getText().length() > 0) {
                    checkTanCode(tanCode.getText().toString());
                } else
                    Toast.makeText(TanCodeActivity.this, "Баталгаажуулах кодоо оруулна уу", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getTanCode(String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);
        FingerRequest.FingerRequestEvent<String> event = new FingerRequest.FingerRequestEvent<String>() {
            @Override
            public void onSuccess(String result) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TanCodeActivity.this, result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SkyRequestError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        FingerRequest.getTanCode(event, getApplicationContext(), phoneNumber);
    }

    public void checkTanCode(String tanCode) {
        progressBar.setVisibility(View.VISIBLE);
        FingerRequest.FingerRequestEvent<String> event = new FingerRequest.FingerRequestEvent<String>() {
            @Override
            public void onSuccess(String result) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TanCodeActivity.this, result, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ChooseTypeActivity.class).putExtra("phonenumber", phonenumber));
                finish();
            }

            @Override
            public void onError(SkyRequestError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        FingerRequest.checkTanCode(event, getApplicationContext(), phonenumber, tanCode);
    }
}
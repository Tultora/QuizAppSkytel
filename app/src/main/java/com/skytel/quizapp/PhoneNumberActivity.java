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

public class PhoneNumberActivity extends AppCompatActivity {

    private Button next;
    private EditText phoneNumber;
    private TextView back;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_phone_number);

        next = findViewById(R.id.next);
        phoneNumber = findViewById(R.id.phoneNumber);
        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progress_bar);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNumber.getText().length() == 8) {
                    getTanCode(phoneNumber.getText().toString());
                } else {
                    Toast.makeText(PhoneNumberActivity.this, "ДУГААРАА ЗӨВ ОРУУЛНА УУ", Toast.LENGTH_SHORT).show();
                }
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
                Toast.makeText(PhoneNumberActivity.this, result, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), TanCodeActivity.class).putExtra("phonenumber", phoneNumber));
                finish();
            }

            @Override
            public void onError(SkyRequestError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PhoneNumberActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        FingerRequest.getTanCode(event, getApplicationContext(), phoneNumber);
    }
}
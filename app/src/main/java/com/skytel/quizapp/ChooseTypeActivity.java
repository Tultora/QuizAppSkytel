package com.skytel.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ChooseTypeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button music, movie, science;
    private TextView back;
    private String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_type);

        music = findViewById(R.id.music);
        movie = findViewById(R.id.movie);
        science = findViewById(R.id.science);
        back = findViewById(R.id.back);

        music.setOnClickListener(this);
        movie.setOnClickListener(this);
        science.setOnClickListener(this);
        back.setOnClickListener(this);
        //.putExtra("phonenumber", phonenumber)
        phonenumber = getIntent().getStringExtra("phonenumber");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.movie:
                startActivity(new Intent(getApplicationContext(), QAActivity.class)
                        .putExtra("type", 3)
                        .putExtra("phonenumber", phonenumber));
                finish();
                break;
            case R.id.music:
                startActivity(new Intent(getApplicationContext(), QAActivity.class)
                        .putExtra("type", 1)
                        .putExtra("phonenumber", phonenumber));
                finish();
                break;
            case R.id.science:
                startActivity(new Intent(getApplicationContext(), QAActivity.class)
                        .putExtra("type", 2)
                        .putExtra("phonenumber", phonenumber));
                finish();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
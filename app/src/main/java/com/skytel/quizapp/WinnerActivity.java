package com.skytel.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WinnerActivity extends AppCompatActivity {

    private Button finish;
    private int point;
    private String prizeId, prizeName;
    private TextView correctAnswer, prizeMessage;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_winner);

        finish = findViewById(R.id.finish);
        correctAnswer = findViewById(R.id.correctAnswer);
        image = findViewById(R.id.image);
        prizeMessage = findViewById(R.id.prizeMessage);

        point = getIntent().getIntExtra("point", 0);
        prizeId = getIntent().getStringExtra("prizeId");
        prizeName = getIntent().getStringExtra("prizeName");
        correctAnswer.setText("Зөв хариулсан асуултын тоо - " + point);
        prizeMessage.setText(prizeName);

        if (prizeId.equals("20")) {
            image.setImageResource(R.drawable.sticker);
        } else if (prizeId.equals("21")) {
            image.setImageResource(R.drawable.badge);
        } else if (prizeId.equals("22")) {
            image.setImageResource(R.drawable.inlive_sticker);
        } else if (prizeId.equals("59")) {
            image.setImageResource(R.drawable.go_basic);
        } else if (prizeId.equals("60")) {
            image.setImageResource(R.drawable.go_plus);
        }

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
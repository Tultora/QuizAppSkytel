package com.skytel.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skytel.quizapp.Model.PrizeModel;
import com.skytel.quizapp.Model.QuestionsList;
import com.skytel.quizapp.Networking.FingerRequest;
import com.skytel.quizapp.Networking.SkyRequestError;

import java.util.ArrayList;
import java.util.List;

public class QAActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView answer1, answer2, answer3, question, point, minute;
    private View progressBar;
    private List<QuestionsList> questionsLists;
    private int index = 0;
    private String phonenumber;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_qaactivity);

        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        question = findViewById(R.id.question);
        point = findViewById(R.id.point);
        minute = findViewById(R.id.minute);
        progressBar = findViewById(R.id.progress_bar);

        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);

        phonenumber = getIntent().getStringExtra("phonenumber");
        getQuestions(getIntent().getIntExtra("type", 1));

        timer = new CountDownTimer(16000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                minute.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                getPrize(index);
                timer.cancel();
            }
        };
    }

    public void getQuestions(final int type) {
        progressBar.setVisibility(View.VISIBLE);
        FingerRequest.FingerRequestEvent<List<QuestionsList>> event = new FingerRequest.FingerRequestEvent<List<QuestionsList>>() {
            @Override
            public void onSuccess(List<QuestionsList> result) {
                progressBar.setVisibility(View.GONE);
                questionsLists = result;
                setQuestions(index);
            }

            @Override
            public void onError(SkyRequestError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        };

        FingerRequest.getQuestions(event, getApplicationContext(), type);
    }

    public void getPrize(final int point) {
        progressBar.setVisibility(View.VISIBLE);
        FingerRequest.FingerRequestEvent<PrizeModel> event = new FingerRequest.FingerRequestEvent<PrizeModel>() {
            @Override
            public void onSuccess(PrizeModel result) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), WinnerActivity.class)
                        .putExtra("point", point)
                        .putExtra("prizeId", result.getPrizeId())
                        .putExtra("prizeName", result.getMessage()));
                finish();
            }

            @Override
            public void onError(SkyRequestError error) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), LoserActivity.class).putExtra("point", point));
                finish();
            }
        };

        FingerRequest.getPrize(event, getApplicationContext(), point, phonenumber);
    }

    public void setQuestions(int indexx) {
        answer1.setBackgroundResource(R.drawable.edittext_bg);
        answer2.setBackgroundResource(R.drawable.edittext_bg);
        answer3.setBackgroundResource(R.drawable.edittext_bg);
        point.setText("15-" + indexx);

        question.setText(questionsLists.get(indexx).getName());
        answer1.setText(questionsLists.get(indexx).getAnswer1());
        answer2.setText(questionsLists.get(indexx).getAnswer2());
        if (questionsLists.get(indexx).getAnswer3().equals("")) answer3.setVisibility(View.GONE);
        else {
            answer3.setVisibility(View.VISIBLE);
            answer3.setText(questionsLists.get(indexx).getAnswer3());
        }

        timer.start();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.answer1:
                timer.cancel();
                if (questionsLists.get(index).getCorrect().equals("1")) {
                    answer1.setBackgroundResource(R.drawable.button_bg);
                    index++;
                    if (index == 15) {
                        getPrize(index);
                    } else
                        setQuestions(index);
                } else {
                    answer1.setBackgroundResource(R.drawable.red_button);
                    getPrize(index);
                }
                break;
            case R.id.answer2:
                timer.cancel();
                if (questionsLists.get(index).getCorrect().equals("2")) {
                    answer2.setBackgroundResource(R.drawable.button_bg);
                    index++;
                    if (index == 15) {
                        getPrize(index);
                    } else
                        setQuestions(index);
                } else {
                    answer2.setBackgroundResource(R.drawable.red_button);
                    getPrize(index);
                }
                break;
            case R.id.answer3:
                timer.cancel();
                if (questionsLists.get(index).getCorrect().equals("3")) {
                    answer3.setBackgroundResource(R.drawable.button_bg);
                    index++;
                    if (index == 15) {
                        getPrize(index);
                    } else
                        setQuestions(index);
                } else {
                    answer3.setBackgroundResource(R.drawable.red_button);
                    getPrize(index);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            return false;
        }
        return false;
    }
}
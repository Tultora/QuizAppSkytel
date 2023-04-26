package com.skytel.quizapp.util;

import android.content.Context;

import com.skytel.quizapp.Model.PrizeModel;
import com.skytel.quizapp.Model.QuestionsList;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONParse {

    private Context context;

    public JSONParse() {

    }

    public JSONParse(Context context) {
        this.context = context;
    }

    //
    public QuestionsList convertJsonToQuestions(JSONObject c) throws JSONException {

        QuestionsList result = new QuestionsList();

        if (!c.isNull("id"))
            result.setId(c.getString("id"));
        if (!c.isNull("name"))
            result.setName(c.getString("name"));
        if (!c.isNull("answer1"))
            result.setAnswer1(c.getString("answer1"));
        if (!c.isNull("answer2"))
            result.setAnswer2(c.getString("answer2"));
        if (!c.isNull("answer3"))
            result.setAnswer3(c.getString("answer3"));
        if (!c.isNull("correct"))
            result.setCorrect(c.getString("correct"));
        if (!c.isNull("type"))
            result.setType(c.getString("type"));

        return result;
    }

    public PrizeModel convertJsonToPrize(JSONObject c) throws JSONException {

        PrizeModel result = new PrizeModel();

        if (!c.isNull("prize"))
            result.setPrizeId(c.getString("prize"));
        if (!c.isNull("prize_name"))
            result.setPrizeName(c.getString("prize_name"));
        if (!c.isNull("message"))
            result.setMessage(c.getString("message"));

        return result;
    }
}

package com.skytel.quizapp.Model;

public class QuestionsList {

    private String id, name, answer1, answer2, answer3, correct, type;

    public String getName() {
        return name;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getCorrect() {
        return correct;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public void setType(String type) {
        this.type = type;
    }
}

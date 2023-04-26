package com.skytel.quizapp.Model;

public class PrizeModel {
    private String prizeId, prizeName, message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }
}

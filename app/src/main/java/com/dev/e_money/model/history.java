package com.dev.e_money.model;

public class history {
    String Amount,SendFrom,SendTo,Status,Time;

    public history(String amount, String sendFrom, String sendTo, String status, String time) {
        Amount = amount;
        SendFrom = sendFrom;
        SendTo = sendTo;
        Status = status;
        Time = time;
    }

    public history() {
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getSendFrom() {
        return SendFrom;
    }

    public void setSendFrom(String sendFrom) {
        SendFrom = sendFrom;
    }

    public String getSendTo() {
        return SendTo;
    }

    public void setSendTo(String sendTo) {
        SendTo = sendTo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}

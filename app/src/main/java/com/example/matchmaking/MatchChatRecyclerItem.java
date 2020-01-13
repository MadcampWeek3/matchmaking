package com.example.matchmaking;

public class MatchChatRecyclerItem {

    private String userid;
    private String text;
    private String texttime;

    public MatchChatRecyclerItem(String userid, String text, String texttime) {
        this.userid = userid;
        this.text = text;
        this.texttime = texttime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTexttime() {
        return texttime;
    }

    public void setTexttime(String texttime) {
        this.texttime = texttime;
    }
}

package com.example.matchmaking;


public class MatchRoomRecyclerItem {

    private String nickname;
    private String tiertxt;
    private String positiontxt;
    private String voice;
    private String id;

    public MatchRoomRecyclerItem(String nickname, String tiertxt, String positiontxt, String voice) {
        this.nickname = nickname;
        this.tiertxt = tiertxt;
        this.positiontxt = positiontxt;
        this.voice = voice;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTiertxt() {
        return tiertxt;
    }

    public void setTiertxt(String tiertxt) {
        this.tiertxt = tiertxt;
    }

    public String getPositiontxt() {
        return positiontxt;
    }

    public void setPositiontxt(String positiontxt) {
        this.positiontxt = positiontxt;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}

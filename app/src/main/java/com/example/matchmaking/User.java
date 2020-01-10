package com.example.matchmaking;

public class User {
    private String id;
    private String password;
    private String nickname;
    private String tier;
    private String position;
    private String voice;
    private Evaluation userEval;
    private String hope_tendency;
    private int hope_num;
    private String hope_voice;

    public User(String id, String password, String nickname, String tier, String position, String voice) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.tier = tier;
        this.position = position;
        this.voice = voice;
        this.userEval = new Evaluation();
    }

    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
    public String getTier() { return tier; }
    public String getPosition() { return position; }
    public String getVoice() { return voice; }
    public Evaluation getHonor() { return userEval; }
    public String getHope_tendency() { return hope_tendency; }
    public int getHope_num() { return hope_num; }
    public String isHope_voice() { return hope_voice; }

    public void setId(String id) { this.id = id; }
    public void setPassword(String password) { this.password = password; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setTier(String tier) { this.tier = tier; }
    public void setPosition(String position) { this.position = position; }
    public void setVoice(String voice) { this.voice = voice; }
    public void setUserEval(String honor) { this.userEval = userEval; }
    public void setHope_tendency(String hope_tendency) { this.hope_tendency = hope_tendency; }
    public void setHope_num(int hope_num) { this.hope_num = hope_num; }
    public void setHope_voice(String hope_voice) { this.hope_voice = hope_voice; }
}

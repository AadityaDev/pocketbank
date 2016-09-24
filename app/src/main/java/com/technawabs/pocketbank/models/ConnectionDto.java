package com.technawabs.pocketbank.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionDto {

    private long id;
    private String name;
    private String email;
    private String mobileNo;
    private String googleProfilePic;
    private float score;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getGoogleProfilePic() {
        return googleProfilePic;
    }

    public void setGoogleProfilePic(String googleProfilePic) {
        this.googleProfilePic = googleProfilePic;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public static ConnectionDto buildFromJson(JSONObject contactJson) throws JSONException {
        ConnectionDto contact = new ConnectionDto();
        contact.setName(contactJson.getString("name"));
        contact.setEmail(contactJson.getString("email"));
        contact.setGoogleProfilePic(contactJson.getString("googleProfilePic"));
        return contact;
    }
}

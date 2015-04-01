package com.gt.foodflip;

/**
 * The User class will hold data for the current user of the app.
 */
public class User {
    private String user_id;
    private String karma;

    public User() {
    }

    public String getId() {
        return this.user_id;
    }

    public String getKarma() {
        return this.karma;
    }

    public void setId(String user_id) {
        this.user_id = user_id;
    }

    public void setKarma(String karma) {
        this.karma = karma;
    }

    public boolean isSet() {
        return this.user_id != null && !this.user_id.equals("") &&
                this.karma != null && !this.karma.equals("");
    }
}

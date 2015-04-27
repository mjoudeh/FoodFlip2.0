package com.gt.foodflip;

/**
 * This class is used to save each food entries data.
 */
public class FoodEntry {
    private String building = "";
    private String location = "";
    private String type = "";
    private String price ="";
    private String description = "";
    private int votes;
    private int vote;
    private int id;
    private boolean hasVoted;
    private String timestamp;
    private double rating;

    /* Setters */
    public void setBuilding(String building)
    {
        this.building = building;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public void setRating(double rating) { this.rating = rating; }

    /* Getters */
    public String getBuilding()
    {
        return this.building;
    }

    public String getLocation()
    {
        return this.location;
    }

    public String getType() {
        return this.type;
    }

    public String getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }

    public int getVotes() {
        return this.votes;
    }

    public int getVote() {
        return this.vote;
    }

    public int getId() {
        return this.id;
    }

    public boolean getHasVoted() {
        return this.hasVoted;
    }

    public String getTimestamp() { return this.timestamp; }

    public double getRating() { return this.rating; }

}
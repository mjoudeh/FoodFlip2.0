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
    private int id;
    private boolean hasVoted;

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

    public void setId(int id) {
        this.id = id;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

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

    public int getId() {
        return this.id;
    }

    public boolean getHasVoted() {
        return this.hasVoted;
    }
}
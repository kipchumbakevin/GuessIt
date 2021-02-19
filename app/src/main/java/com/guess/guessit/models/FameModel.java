package com.guess.guessit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FameModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("femaleMusic")
    @Expose
    private Integer femaleMusic;
    @SerializedName("maleMusic")
    @Expose
    private Integer maleMusic;
    @SerializedName("femaleActor")
    @Expose
    private Integer femaleActor;
    @SerializedName("maleActor")
    @Expose
    private Integer maleActor;
    @SerializedName("president")
    @Expose
    private Integer president;
    @SerializedName("football")
    @Expose
    private Integer football;
    @SerializedName("business")
    @Expose
    private Integer business;
    @SerializedName("basketball")
    @Expose
    private Integer basketball;
    @SerializedName("models")
    @Expose
    private Integer models;
    @SerializedName("carlogos")
    @Expose
    private Integer carlogos;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getFemaleMusic() {
        return femaleMusic;
    }

    public void setFemaleMusic(Integer femaleMusic) {
        this.femaleMusic = femaleMusic;
    }

    public Integer getMaleMusic() {
        return maleMusic;
    }

    public void setMaleMusic(Integer maleMusic) {
        this.maleMusic = maleMusic;
    }

    public Integer getFemaleActor() {
        return femaleActor;
    }

    public void setFemaleActor(Integer femaleActor) {
        this.femaleActor = femaleActor;
    }

    public Integer getMaleActor() {
        return maleActor;
    }

    public void setMaleActor(Integer maleActor) {
        this.maleActor = maleActor;
    }

    public Integer getPresident() {
        return president;
    }

    public void setPresident(Integer president) {
        this.president = president;
    }

    public Integer getFootball() {
        return football;
    }

    public void setFootball(Integer football) {
        this.football = football;
    }

    public Integer getBusiness() {
        return business;
    }

    public void setBusiness(Integer business) {
        this.business = business;
    }

    public Integer getBasketball() {
        return basketball;
    }

    public void setBasketball(Integer basketball) {
        this.basketball = basketball;
    }

    public Integer getModels() {
        return models;
    }

    public void setModels(Integer models) {
        this.models = models;
    }

    public Integer getCarlogos() {
        return carlogos;
    }

    public void setCarlogos(Integer carlogos) {
        this.carlogos = carlogos;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

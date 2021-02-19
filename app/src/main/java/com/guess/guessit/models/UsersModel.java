package com.guess.guessit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsersModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("actor")
    @Expose
    private Integer actor;
    @SerializedName("billion")
    @Expose
    private Integer billion;
    @SerializedName("convict")
    @Expose
    private Integer convict;
    @SerializedName("virgin")
    @Expose
    private Integer virgin;
    @SerializedName("student")
    @Expose
    private Integer student;
    @SerializedName("car")
    @Expose
    private Integer car;
    @SerializedName("medicine")
    @Expose
    private Integer medicine;
    @SerializedName("plastic")
    @Expose
    private Integer plastic;
    @SerializedName("african")
    @Expose
    private Integer african;
    @SerializedName("jobless")
    @Expose
    private Integer jobless;
    @SerializedName("pet")
    @Expose
    private Integer pet;
    @SerializedName("pass")
    @Expose
    private Integer pass;
    @SerializedName("activity")
    @Expose
    private Integer activity;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getActor() {
        return actor;
    }

    public void setActor(Integer actor) {
        this.actor = actor;
    }

    public Integer getBillion() {
        return billion;
    }

    public void setBillion(Integer billion) {
        this.billion = billion;
    }

    public Integer getConvict() {
        return convict;
    }

    public void setConvict(Integer convict) {
        this.convict = convict;
    }

    public Integer getVirgin() {
        return virgin;
    }

    public void setVirgin(Integer virgin) {
        this.virgin = virgin;
    }

    public Integer getStudent() {
        return student;
    }

    public void setStudent(Integer student) {
        this.student = student;
    }

    public Integer getCar() {
        return car;
    }

    public void setCar(Integer car) {
        this.car = car;
    }

    public Integer getMedicine() {
        return medicine;
    }

    public void setMedicine(Integer medicine) {
        this.medicine = medicine;
    }

    public Integer getPlastic() {
        return plastic;
    }

    public void setPlastic(Integer plastic) {
        this.plastic = plastic;
    }

    public Integer getAfrican() {
        return african;
    }

    public void setAfrican(Integer african) {
        this.african = african;
    }

    public Integer getJobless() {
        return jobless;
    }

    public void setJobless(Integer jobless) {
        this.jobless = jobless;
    }

    public Integer getPet() {
        return pet;
    }

    public void setPet(Integer pet) {
        this.pet = pet;
    }

    public Integer getPass() {
        return pass;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }
    public Integer getActivity() {
        return activity;
    }

    public void setActivity(Integer activity) {
        this.activity = activity;
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

package com.lawstack.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="DASHBOARDTABLE")
public class Dashboard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int jobs =0;

    private int sellers = 0;

    private int dewDropper = 0;

    private int sprinkle = 0;

    private int rainmaker = 0;

    private int users = 0;

    private Double income = 0.0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobs() {
        return jobs;
    }

    public void setJobs(int jobs) {
        this.jobs = jobs;
    }

    public int getSellers() {
        return sellers;
    }

    public void setSellers(int sellers) {
        this.sellers = sellers;
    }

    public int getDewDropper() {
        return dewDropper;
    }

    public void setDewDropper(int dewDropper) {
        this.dewDropper = dewDropper;
    }

    public int getSprinkle() {
        return sprinkle;
    }

    public void setSprinkle(int sprinkle) {
        this.sprinkle = sprinkle;
    }

    public int getRainmaker() {
        return rainmaker;
    }

    public void setRainmaker(int rainmaker) {
        this.rainmaker = rainmaker;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

}

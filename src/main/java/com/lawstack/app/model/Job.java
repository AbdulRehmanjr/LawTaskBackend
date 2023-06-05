package com.lawstack.app.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="JOBTABLE")
public class Job {

    @Id
    private String jobId;
    private String jobName;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] jobImage;
    
    private int jobPrice=10;

    private String description;

    private String jobType;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    private List<Order> orders;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public byte[] getJobImage() {
        return jobImage;
    }

    public void setJobImage(byte[] jobImage) {
        this.jobImage = jobImage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getJobPrice() {
        return jobPrice;
    }

    public void setJobPrice(int jobPrice) {
        this.jobPrice = jobPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

package com.lawstack.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
    
    private Double likes = 0.0;

    private Double views =0.0;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

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

    public Double getLikes() {
        return likes;
    }

    public void setLikes(Double likes) {
        this.likes = likes;
    }

    public Double getViews() {
        return views;
    }

    public void setViews(Double views) {
        this.views = views;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

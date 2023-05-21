package com.lawstack.app.model;




import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="ORDERTABLE")
public  class Order {
    
    @Id
    private String id;

    private String customerName ="NONE";

    private String customerEmail="NONE";

    private String description;

    @Lob
    @Column(columnDefinition = "LONGBLOB",nullable = true)
    private byte[] requirementFile;

    private String documentType;
 
    private String startedDate;

    private String endedDate;

    private boolean confirmed =false;

    private Double price = 0.0;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Job job;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private User user;

    private String customerId;

   
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public byte[] getRequirementFile() {
        return requirementFile;
    }
    public void setRequirementFile(byte[] requirementFile) {
        this.requirementFile = requirementFile;
    }
    public Job getJob() {
        return job;
    }
    public void setJob(Job job) {
        this.job = job;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getStartedDate() {
        return startedDate;
    }
    public void setStartedDate(String startedDate) {
        this.startedDate = startedDate;
    }
    public String getEndedDate() {
        return endedDate;
    }
    public void setEndedDate(String endedDate) {
        this.endedDate = endedDate;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getDocumentType() {
        return documentType;
    }
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }


}

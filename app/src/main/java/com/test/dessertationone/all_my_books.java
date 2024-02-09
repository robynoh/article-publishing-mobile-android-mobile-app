package com.test.dessertationone;

public class all_my_books {

    private String id;
    private String topic;
    private String abstracts;
    private String category;
    private String firstname;
    private String lastname;
    private String title;
    private String work;
    private String date_posted;
    private String reads;
    private String likes;
    private String status;
    private String coauthors;
    private String filename;

    // private String distance;


    public all_my_books(String id,String topic,String abstracts,String category, String firstname,String lastname,String title,String work,String date_posted,String reads,String likes,String status,String coauthors,String filename) {
        this.id = id;
        this.topic= topic;
        this.abstracts = abstracts;
        this.category = category;
        this.firstname = firstname;
        this.lastname = lastname;
        this.title = title;
        this.work = work;
        this.date_posted = date_posted;
        this.reads = reads;
        this.likes = likes;
        this.status = status;
        this.coauthors = coauthors;
        this.filename = filename;

    }

    public String getId() {
        return id;
    }


    public String getTopic() { return topic; }


    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getTitle() {
        return title;
    }

    public String getDate_posted() {
        return date_posted;
    }

    public String getWork() {
        return work;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public String getCategory() {
        return category;
    }

    public String  getReads() {
        return  reads;
    }

    public String  getLikes() {
        return  likes;
    }

    public String  getStatus() {
        return  status;
    }

    public String  getCoauthors() {
        return  coauthors;
    }

    public String  getFilename() {
        return  filename;
    }




}







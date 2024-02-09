package com.test.dessertationone;

public class all_subcategory {

    private String id;
    private String topic;
    private String authorid;
    private String abstracts;
    private String category;
    private String firstname;
    private String lastname;
    private String title;
    private String work;
    private String date_posted;
    private String reads;
    private String likes;

    // private String distance;


    public all_subcategory(String id,String topic,String authorid,String abstracts,String category, String firstname,String lastname,String title,String work,String date_posted,String reads,String likes) {
        this.id = id;
        this.topic= topic;
        this.authorid= authorid;
        this.abstracts = abstracts;
        this.category = category;
        this.firstname = firstname;
        this.lastname = lastname;
        this.title = title;
        this.work = work;
        this.date_posted = date_posted;
        this.reads = reads;
        this.likes = likes;

    }

    public String getId() {
        return id;
    }


    public String getTopic() { return topic; }

    public String getAuthorid() { return authorid; }


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




}








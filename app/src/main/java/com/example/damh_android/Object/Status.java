package com.example.damh_android.Object;

public class Status {
    String name;
    String creatDate;
    String id;


    public Status(String name, String creatDate) {
        this.name = name;
        this.creatDate = creatDate;
    }

    public Status() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCreatDate() {
        return creatDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatDate(String creatDate) {
        this.creatDate = creatDate;
    }
}

package com.example.damh_android.Object;

public class Priority {
    String name;
    String creatDate;
    String id;

    public Priority(String name, String creatDate) {
        this.name = name;
        this.creatDate = creatDate;
    }

    public Priority() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatDate(String creatDate) {
        this.creatDate = creatDate;
    }

    public String getName() {
        return name;
    }

    public String getCreatDate() {
        return creatDate;
    }
}

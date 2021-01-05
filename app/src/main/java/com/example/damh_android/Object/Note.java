package com.example.damh_android.Object;

public class Note {
    String name;
    String creatDate;
    String statusId;
    String priorityId;
    String galleryId;
    String planDate;

    public Note(String name, String statusId) {
        this.name = name;
        this.statusId = statusId;
    }

    public Note(String name, String creatDate, String statusId, String priorityId, String galleryId, String planDate) {
        this.name = name;
        this.creatDate = creatDate;
        this.statusId = statusId;
        this.priorityId = priorityId;
        this.galleryId = galleryId;
        this.planDate = planDate;
    }

    public Note() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(String creatDate) {
        this.creatDate = creatDate;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }

    public String getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }
}

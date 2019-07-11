package com.nguyentinhdeveloper.ghichu.model;

public class ModelNote {
    public String id;
    public String subject;
    public String date;
    public String node;
    public String img;
    public String audio;
    public String star;

    public ModelNote() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public ModelNote(String id, String subject, String date, String node, String img, String audio, String star) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.node = node;
        this.img = img;
        this.audio = audio;
        this.star = star;
    }
}

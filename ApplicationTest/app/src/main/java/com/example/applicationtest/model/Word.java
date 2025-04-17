package com.example.applicationtest.model;

import java.sql.Date;

public class Word {
    private Integer id;
    private String word;
    private String picture;
    private String translations;
    private String ukphone;
    private String ukspeech;
    private String usphone;
    private String usspeech;
    private Integer type;
    private Date learnTime;

    public Word() {
    }

    public Word(Integer id, String word, String picture, String translations, String ukphone, String ukspeech, String usphone, String usspeech, Integer type, Date learnTime) {
        this.id = id;
        this.word = word;
        this.picture = picture;
        this.translations = translations;
        this.ukphone = ukphone;
        this.ukspeech = ukspeech;
        this.usphone = usphone;
        this.usspeech = usspeech;
        this.type = type;
        this.learnTime = learnTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTranslations() {
        return translations;
    }

    public void setTranslations(String translations) {
        this.translations = translations;
    }

    public String getUkphone() {
        return ukphone;
    }

    public void setUkphone(String ukphone) {
        this.ukphone = ukphone;
    }

    public String getUkspeech() {
        return ukspeech;
    }

    public void setUkspeech(String ukspeech) {
        this.ukspeech = ukspeech;
    }

    public String getUsphone() {
        return usphone;
    }

    public void setUsphone(String usphone) {
        this.usphone = usphone;
    }

    public String getUsspeech() {
        return usspeech;
    }

    public void setUsspeech(String usspeech) {
        this.usspeech = usspeech;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getLearnTime() {
        return learnTime;
    }

    public void setLearnTime(Date learnTime) {
        this.learnTime = learnTime;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", picture='" + picture + '\'' +
                ", translations='" + translations + '\'' +
                ", ukphone='" + ukphone + '\'' +
                ", ukspeech='" + ukspeech + '\'' +
                ", usphone='" + usphone + '\'' +
                ", usspeech='" + usspeech + '\'' +
                ", type=" + type +
                ", learnTime=" + learnTime +
                '}';
    }
}

package com.example.droodsunny.memorybook;

import org.litepal.crud.DataSupport;

/**
 * Created by DroodSunny on 2017/9/23.
 */

public class Note extends DataSupport {

    public Note(String year,String month,String day,String content,String title,String location){
       this.year=year;
        this.month=month;
        this.day=day;
        this.content=content;
        this.title=title;
        this.location=location;
    }
    public Note(){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int id;
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    String year;
    String month;
    String day;
    String date;
    String title;
    String content;
    String location;


}

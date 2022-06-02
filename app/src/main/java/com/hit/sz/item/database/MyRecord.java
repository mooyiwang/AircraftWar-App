package com.hit.sz.item.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "record_table")
public class MyRecord {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String date;
    public String userName;
    public int    score;
    public String level; //"easy","medium","hard"

    public MyRecord(String date, String userName, int score, String level){
        this.date =date;
        this.userName = userName;
        this.score = score;
        this.level = level;
    }


    public String getName(){
        return this.userName;
    }
    public String getDate() {return this.date;}
    public String getScore() {return String.valueOf(this.score);}
    public String getLevel(){
        return this.level;
    }
}
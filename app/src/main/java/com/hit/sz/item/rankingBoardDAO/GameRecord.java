package com.hit.sz.item.rankingBoardDAO;

public class GameRecord {
    private String name;
    private int score;
    private String time;

    public GameRecord(String name, int score, String time){

        this.name = name;
        this.score = score;
        this.time = time;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getScore(){
        return score;
    }
    public void setScore(int score){this.score = score;}

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }


}

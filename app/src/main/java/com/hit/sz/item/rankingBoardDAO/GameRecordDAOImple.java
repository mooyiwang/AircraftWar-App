package com.hit.sz.item.rankingBoardDAO;

import com.hit.sz.activity.LevelSoundActivity;



import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GameRecordDAOImple implements GameRecordDAO{


    private FileOutputStream fout;
    private FileInputStream fin;
    private OutputStreamWriter writer;
    private InputStreamReader reader;
    private static List<GameRecord> records = new LinkedList<GameRecord>();
    private SimpleDateFormat format;
    private BufferedReader bufferedReader;


    public GameRecordDAOImple() throws FileNotFoundException, UnsupportedEncodingException {
    }

    @Override
    public  List<GameRecord> getAllRecords(){
        return records;
    };


    @Override
    public void addRecord(int score, String name) {
        format = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        String inName = name;
        Date date = new Date();
        String time = format.format(date);
        int i = 0;
        for(GameRecord record:records){
            if(record.getScore() > score){
                i++;
            }
        }
        records.add(i, new GameRecord( inName, score, time));
    }

    @Override
    public void deleteRecordbyRank(int rank){
        records.remove(rank-1);
    };

    @Override
    public void clearAllRecords(){
        int num = records.size();
        for(int i=0; i<num; i++){
            records.remove(i);
        }
    }

    @Override
    public void exportRecords() throws IOException {
        switch (LevelSoundActivity.GAME_LEVEL){
            case 0:
                fout = new FileOutputStream("records/GameRecordsEasy.txt");
                break;
            case 1:
                fout = new FileOutputStream("records/GameRecordsMedium.txt");
                break;
            case 2:
                fout = new FileOutputStream("records/GameRecordsHard.txt");
                break;
            default:
        }
        writer = new OutputStreamWriter(fout,"UTF-8");
        for(GameRecord record:records){
            String s = record.getName()+" "+ Integer.toString(record.getScore())+" "+record.getTime()+'\n';
            writer.write(s);
        }
        writer.close();
        fout.close();
    };

    @Override
    public void importRecords() throws IOException {
        switch (LevelSoundActivity.GAME_LEVEL){
            case 0:
                fin = new FileInputStream("records/GameRecordsEasy.txt");
                break;
            case 1:
                fin = new FileInputStream("records/GameRecordsMedium.txt");
                break;
            case 2:
                fin = new FileInputStream("records/GameRecordsHard.txt");
                break;
            default:
        }
        reader = new InputStreamReader(fin, "UTF-8");
        bufferedReader = new BufferedReader(reader);
        while (bufferedReader.ready()){
            String line = bufferedReader.readLine();
            if(line.contentEquals("")) {
                break;
            }
            String[] segments = line.split(" ");
            String name = segments[0];
            int score = Integer.parseInt(segments[1]);
            String time = segments[2];
            records.add(new GameRecord(name,score,time));
        }
        bufferedReader.close();
        reader.close();
        fin.close();
    };

    @Override
    public void printRecords(){
        System.out.println("****************************************");
        System.out.println("                  排行榜                 ");
        System.out.println("排名         玩家          分数        时间");
        System.out.println("****************************************");
        int i = 1;
        for (GameRecord record : records) {
            String s = record.getName() + " " + Integer.toString(record.getScore()) + " " + record.getTime();
            System.out.printf("NO.%d ", i);
            System.out.println(s);
            i++;
        }
    }

    public void close() throws IOException {
        writer.close();
        fout.close();
        bufferedReader.close();
        reader.close();
        fin.close();
    }

}

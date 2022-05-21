package com.hit.sz.music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.hit.sz.R;

import java.util.HashMap;
import java.util.Objects;

public class MusicService extends Service {
    private static  final String TAG = "MusicService";
    private HashMap<String, Integer> soundID = new HashMap<String , Integer>();
    private SoundPool mSoundPool;
    private static MediaPlayer player;
    public MusicService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "==== MusicService onCreate ===");
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool
                = new SoundPool.Builder()
                .setMaxStreams(20)
                .setAudioAttributes(audioAttributes)
                .build();

        soundID.put("bullet_hit", mSoundPool.load(this, R.raw.bullet_hit, 1));
        soundID.put("game_over", mSoundPool.load(this, R.raw.game_over, 1));
        soundID.put("bgm", mSoundPool.load(this, R.raw.bgm, 1));
        soundID.put("bgm_boss", mSoundPool.load(this, R.raw.bgm_boss, 1));
        soundID.put("bomb_explosion", mSoundPool.load(this, R.raw.bomb_explosion, 1));
        soundID.put("bullet", mSoundPool.load(this, R.raw.bullet, 1));
        soundID.put("get_supply", mSoundPool.load(this, R.raw.get_supply, 1));
    }

    @Override
    public IBinder onBind(Intent intent){
        //playMusic();
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public void playSound(String s){
                mSoundPool.play(soundID.get(s), 1, 1, 0,0,1);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    //播放音乐
    public  void playMusic(String type){
        stopMusic();
        if (Objects.equals(type, "bgm"))
            player = MediaPlayer.create(this, R.raw.bgm);
        else
            player = MediaPlayer.create(this, R.raw.bgm_boss);
        player.setLooping(true);
        player.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
    }
    /**
     * 停止播放
     */
    public static void stopMusic() {
        if (player != null) {
            player.stop();
            player.reset();//重置
            player.release();//释放
            player = null;
        }
    }
}
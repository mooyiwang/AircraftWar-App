package com.hit.sz.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;


import com.hit.sz.R;
import com.hit.sz.activity.BoardActivity;
import com.hit.sz.activity.GameActivity;
import com.hit.sz.activity.LevelSoundActivity;
import com.hit.sz.application.ImageManager;
import com.hit.sz.item.aircraft.AbstractAircraft;
import com.hit.sz.item.aircraft.BossEnemy;
import com.hit.sz.item.aircraft.EliteEnemy;
import com.hit.sz.item.aircraft.HeroAircraft;
import com.hit.sz.item.aircraft.MobEnemy;
import com.hit.sz.item.basic.AbstractFlyingObject;
import com.hit.sz.item.bullet.BaseBullet;
import com.hit.sz.item.bullet.HeroBullet;
import com.hit.sz.item.factory.BossFactory;
import com.hit.sz.item.factory.EliteFactory;
import com.hit.sz.item.factory.EnemyFactory;
import com.hit.sz.item.factory.MobFactory;
import com.hit.sz.item.factory.PropFactory;
import com.hit.sz.item.factory.RandomPropFactory;
import com.hit.sz.item.prop.AbstractProp;
import com.hit.sz.item.prop.BombProp;
import com.hit.sz.item.strategy.ShootStrategy;
import com.hit.sz.music.MusicService;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public abstract class AbstractGameView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private int backGroundTop = 0;
    //屏幕宽高
    public static int screenWidth;
    public static int screenHeight;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;
    int shootInterval = 3;
    private int intervalCnt=0; //
    /**
     * 飞机，子弹，道具
     */
    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractProp> props;
    private  AbstractProp prop;

    /**
     * 工厂
     */
    private final EnemyFactory mobFactory;
    private final EnemyFactory eliteFactory;
    private final EnemyFactory bossFactory;
    private final PropFactory randomPropFactory;


    /**
     * enemyMaxNumber 普通机和精英机最大数量
     * curBossNum 当前boss数量
     * bossAppearCnt 记录boss出现的次数
     * timeCnt 时间计数器，每一个周期+1，用于控制精英机出现概率
     */
    private int enemyMaxNumber = 8;
    private int curBossNum = 0;
    private int bossAppearCnt = 0;
    private int timeCnt = 0;


    /**
     * 游戏配置
     */
    private boolean gameOverFlag = false;
    public static int score = 0;
    public int scoreThreshold = setScoreThreshold();
    public int lastScoreThreshold = 0;
    private int time = 0;
    private String userName="游客";
    private String formatDate;
    String gameLevel;


    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    /**
     * 绘制相关
     */
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private Canvas canvas;


    private Context context;

    /**
     *
     * 音乐相关
     */
    private MediaPlayer mediaPlayer;
    public MusicService.MyBinder myBinder;
    private Connect conn;

    public AbstractGameView(Context context){

        super(context);
        this.context = context;
        loading_img();

        screenWidth = LevelSoundActivity.screenWidth;
        screenHeight = LevelSoundActivity.screenHeight;

        heroAircraft = HeroAircraft.getHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        /**
         * 实例化 工厂类
         */
        mobFactory = new MobFactory();
        eliteFactory = new EliteFactory();
        bossFactory = new BossFactory();
        randomPropFactory = new RandomPropFactory();

        //绘制相关
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        mPaint = new Paint();
        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);

        //音乐相关
        Log.i("music demo","bind service");
        conn = new Connect();
        Intent intent = new Intent(context, MusicService.class);
        context.bindService(intent,conn, Context.BIND_AUTO_CREATE);

        //日期相关
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd", Locale.getDefault());
        formatDate = df.format(c);
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public final void action() {

        time += timeInterval;


        // 周期性执行（控制频率）
        if (timeCountAndNewCycleJudge()) {
            System.out.println(time);
            timeCnt++;

            // 控制mob敌机生产
            if (enemyAircrafts.size() < enemyMaxNumber) {
                AbstractAircraft mobEnemy = mobFactory.enemyCreator();
                //根据难度设置初始血量
                mobEnemy.setHp(setEnemyHp());
                enemyAircrafts.add(mobEnemy);
            }

            //控制Elite敌机生成
            if(isCreateElite(timeCnt)){
                AbstractAircraft eliteEnemy =  eliteFactory.enemyCreator();
                eliteEnemy.setHp(setEnemyHp());
                enemyAircrafts.add(eliteEnemy);
            }

            //控制boss敌机生成
            if(score > scoreThreshold && hasBoss()){
                if(curBossNum == 0 ){
                    AbstractAircraft bossEnemy = bossFactory.enemyCreator();
                    //设置boss敌机射击策略
                    bossEnemy.setStrategy(setBossStrategy());
                    //设置boss敌机血量
                    if(isBossHpUp()){
                        bossEnemy.setHp(bossEnemy.getHp()+bossAppearCnt*200);
                    }
                    enemyAircrafts.add(bossEnemy);
                    curBossNum = 1;
                    bossAppearCnt++;

                    if(LevelSoundActivity.GAME_SOUND){
                        stopMusic();
                        mediaPlayer = MediaPlayer.create(context, R.raw.bgm_boss);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }
                }

                //控制Boss出现阈值
                lastScoreThreshold = scoreThreshold;
                if(bossAppearCnt <= 5){
                    scoreThreshold += (1000-bossAppearCnt*50);
                }
                else {
                    scoreThreshold = 500;
                }

            }

            // 飞机射出子弹
            shootAction();
            //打印难度
            hardnessPrint(timeCnt);
        }

        // 子弹移动
        bulletsMoveAction();

        // 飞机移动
        aircraftsMoveAction();

        // 道具移动
        propsMoveAction();

        // 撞击检测
        crashCheckAction();

        // 后处理
        postProcessAction();

        //每个时刻重绘界面
        draw();




        // 游戏结束检查
        if (heroAircraft.getHp() <= 0) {
            // 游戏结束
//                executorService.shutdown();
            gameOverFlag = true;
            LevelSoundActivity.SCORE = score;
            System.out.println("Game Over!");

            if(LevelSoundActivity.GAME_SOUND){
                stopMusic();
                myBinder.playSound("game_over");
            }

            //设置游戏结束标志
            gameOverFlag = true;

            //从GameActivity跳转到BoardActivity(显示排行榜
            Intent intent = new Intent(context, BoardActivity.class);
            intent.putExtra("score",score);
            intent.putExtra("date",formatDate);
            intent.putExtra("name",userName);
            intent.putExtra("level",gameLevel);
            context.startActivity(intent);
        }

    }

    //***********************
    //      Action 各部分
    //***********************
    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        if(intervalCnt==0){
            intervalCnt = shootInterval;
            for(AbstractAircraft enemyAircraft : enemyAircrafts){
                if(!(enemyAircraft instanceof MobEnemy)){
                    List<BaseBullet> enemyBullet = enemyAircraft.shoot();
                    if(enemyBullet.size() != 0){
                        for(BaseBullet bullet:enemyBullets){
                            bullet.setPower(setEnemyBulletPower());
                        }
                        enemyBullets.addAll(enemyBullet);
                    }
                }

            }
        }
        else intervalCnt-=1;

        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());

        if(LevelSoundActivity.GAME_SOUND){
            myBinder.playSound("bullet");
        }

    }

    private void bulletsMoveAction() {
        if(heroBullets == null || heroBullets.size()==0){
            return;
        }
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        if(enemyBullets == null || enemyBullets.size()==0){
            return;
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        if(enemyAircrafts == null || enemyAircrafts.size()==0){
            return;
        }
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction(){
        if(props == null || props.size()==0){
            return;
        }
        for(AbstractProp prop:props){
            prop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {

        //敌机子弹击中英雄
        for(BaseBullet bullet : enemyBullets){
            if(bullet.notValid()){
                continue;
            }
            if(heroAircraft.notValid()){
                continue;
            }
            if(heroAircraft.crash(bullet) || bullet.crash(heroAircraft)){
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }

            if(enemyAircrafts == null || enemyAircrafts.size() == 0){
                return;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    if(LevelSoundActivity.GAME_SOUND){
                        myBinder.playSound("bullet_hit");

                    }
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();

                    //若击毁的是elite或boss随机掉落道具
                    if (enemyAircraft.notValid()) {
                        //击毁elite
                        if (enemyAircraft instanceof EliteEnemy) {
                            prop = randomPropFactory.randomPropCreator(enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                            if(prop != null){
                                props.add(prop);
                            }
                            score += 20;
                        }
                        //击毁boss
                        if (enemyAircraft instanceof BossEnemy) {
                            prop = randomPropFactory.randomPropCreator(enemyAircraft.getLocationX(), enemyAircraft.getLocationY());
                            if(prop != null){
                                props.add(prop);
                            }
                            curBossNum = 0;
                            score += 50;

                            if(LevelSoundActivity.GAME_SOUND){
                                stopMusic();
                                mediaPlayer = MediaPlayer.create(context, R.raw.bgm);
                                mediaPlayer.setLooping(true);
                                mediaPlayer.start();
                            }

                        }
                        score += 10;
                    }

                }
                // 英雄机 与 敌机 相撞，均损毁
                if (!enemyAircraft.notValid() && (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft))) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }


        if(props == null || props.size() == 0){
            return;
        }
        for(AbstractProp prop : props){
            if(prop.crash(heroAircraft) || heroAircraft.crash(prop)){
                if(prop.notValid()){
                    continue;
                }
                if(!prop.notValid()){
                    //获取到炸弹道具bomb
                    if(prop instanceof BombProp){

                        if(LevelSoundActivity.GAME_SOUND){
                            myBinder.playSound("bomb_explosion");
                        }
                        //观察者模式加入订阅者（包括mob，elite 和 enemy_bullet
                        for(AbstractAircraft enemy:enemyAircrafts){
                            if(!(enemy instanceof BossEnemy) && !enemy.notValid()){
                                ((BombProp) prop).addBombed(enemy);
                            }
                        }
                        for(BaseBullet enemybullet: enemyBullets){
                            if(!enemybullet.notValid()){
                                ((BombProp) prop).addBombed(enemybullet);
                            }
                        }
                    }
                    //获取到其他道具
                    else {
                        if(LevelSoundActivity.GAME_SOUND) {
                            myBinder.playSound("get_supply");
                        }
                    }

                    prop.function(heroAircraft);
                    score += 30;
                }
                prop.vanish();
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * 4. 删除无效的道具
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      draw 各部分
    //***********************
    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        if (objects != null) {
            for (int i=0; i<objects.size(); i++){
                AbstractFlyingObject obj = objects.get(i);
                Bitmap image = objects.get(i).getImage();
                canvas.drawBitmap(image, objects.get(i).getLocationX() - image.getWidth()/2, objects.get(i).getLocationY() - image.getHeight()/2, mPaint);
            }
        }
    }

    private void paintScoreAndLife(Canvas canvas) {
        Paint myPaint = new Paint();
        int x = 10;
        int y = 100;
        myPaint.setColor(Color.RED);
        canvas.drawText("SCORE:" + score, x, y, myPaint);
        y = y + 20;
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y, myPaint);
    }

    /**
     * 绘制图像
     */
    public void draw(){
        //通过SurfaceHolder对象的lockCanvans()方法，我们可以获取当前的Canvas绘图对象
        canvas = mSurfaceHolder.lockCanvas();
        if(mSurfaceHolder == null || canvas == null){
            return;
        }
        //绘制背景
        //先根据难度设置背景
        setBackgroundImage();
        canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop-ImageManager.BACKGROUND_IMAGE.getHeight(), mPaint);
        canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, mPaint);
        backGroundTop += 1;
        if(backGroundTop == screenHeight){
            this.backGroundTop = 0;
        }
        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(enemyBullets);
        paintImageWithPositionRevised(heroBullets);

        paintImageWithPositionRevised(props);

        paintImageWithPositionRevised(enemyAircrafts);

        canvas.drawBitmap(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, mPaint);

        //绘制得分和生命值(没用之前的方法
//        paintScoreAndLife(canvas);
        Paint myPaint = new Paint();
        int x = 10;
        int y = 50;
        myPaint.setColor(Color.RED);
        myPaint.setTextSize(50);
        canvas.drawText("SCORE:" + score, x, y, myPaint);
        y = y + 50;
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y, myPaint);

        //通过unlockCanvasAndPost(mCanvas)方法对画布内容进行提交
        if(canvas != null){
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * 设置难度背景
     */
    public abstract void setBackgroundImage();

    /**
     * hook函数，是否有BOSS
     * @return boolean
     */
    public abstract boolean hasBoss();

    /**
     * boss的血量是否随出现次数的增多而增加
     * @return boolean
     */
    public abstract boolean isBossHpUp();

    /**
     * 设置初始boss阈值
     * @return scoreThreshold
     */
    public abstract int setScoreThreshold();

    /**
     * 设置敌机血量
     * @return enemyHp
     */
    public abstract int setEnemyHp();

    /**
     * 打印难度（好像现在用不上了
     * @param timeCnt 时间计数器
     */
    public abstract void hardnessPrint(int timeCnt);

    /**
     * 设置Boss的射击策略（我之前设置的是普通模式散射3连发，困难散射5连发
     * @return shootStrategy
     */
    public abstract ShootStrategy setBossStrategy();

    /**
     * 设置敌机子弹伤害
     * @return enemyBulletPower
     */
    public abstract int setEnemyBulletPower();

    /**
     * 根据时间控制精英机产生
     * @param timeCnt 时间计数器
     * @return boolean
     */
    public abstract boolean isCreateElite(int timeCnt);


    /**
     *
     * 继承SurfaceView和实现Runnable要重写的方法
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameOverFlag = false;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        screenHeight = LevelSoundActivity.screenHeight;
        screenWidth = LevelSoundActivity.screenWidth;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        gameOverFlag = true;
    }

    @Override
    public void run() {
        if(LevelSoundActivity.GAME_SOUND) {
            if(mediaPlayer==null){
                mediaPlayer = MediaPlayer.create(context, R.raw.bgm);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        }
        while(!gameOverFlag){
            synchronized (mSurfaceHolder){
                loading_img();
                System.out.println(Thread.currentThread());
                this.action();
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    //跟踪手指，设置英雄机的位置
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                System.out.println("touch");
                heroAircraft.setLocation((double) event.getX(), (double) event.getY());
                break;
            default:
                break;
        }
        return true;
    }


    /**
     加载图片
     */
    public void loading_img(){
        ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        ImageManager.HERO_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        ImageManager.HERO_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_hero);
        ImageManager.ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_enemy);
        ImageManager.MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.mob);
        ImageManager.ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.elite);
        ImageManager.BOSS_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.boss);
        ImageManager.BLOOD_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_blood);
        ImageManager.BOMB_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bomb);
        ImageManager.FIRE_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bullet);
        ImageManager.updateAll();
    }

    /**
     *  音乐相关
     */
    class Connect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            Log.i("music demo","Service Connnected");
            myBinder = (MusicService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
    private void stopMusic(){
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
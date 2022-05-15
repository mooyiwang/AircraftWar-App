package com.hit.sz.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hit.sz.R;
import com.hit.sz.activity.BoardActivity;
import com.hit.sz.application.ImageManager;
import com.hit.sz.application.MainActivity;
import com.hit.sz.item.aircraft.AbstractAircraft;
import com.hit.sz.item.aircraft.BossEnemy;
import com.hit.sz.item.aircraft.EliteEnemy;
import com.hit.sz.item.aircraft.HeroAircraft;
import com.hit.sz.item.basic.AbstractFlyingObject;
import com.hit.sz.item.bullet.BaseBullet;
import com.hit.sz.item.factory.BossFactory;
import com.hit.sz.item.factory.EliteFactory;
import com.hit.sz.item.factory.EnemyFactory;
import com.hit.sz.item.factory.MobFactory;
import com.hit.sz.item.factory.PropFactory;
import com.hit.sz.item.factory.RandomPropFactory;
import com.hit.sz.item.prop.AbstractProp;
import com.hit.sz.item.prop.BombProp;
import com.hit.sz.item.strategy.ShootStrategy;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public abstract class AbstractGameView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private int backGroundTop = 0;
    //屏幕宽高
    public static int screenWidth;
    public static int screenHeight;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;


    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

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



    public AbstractGameView(Context context){

        super(context);
        this.context = context;

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



        //Scheduled 线程池，用于定时任务调度
        ThreadFactory gameThread = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("game thread");
                return t;
            }
        };
        executorService = new ScheduledThreadPoolExecutor(1,gameThread);

        //绘制相关
        loading_img();
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);
        mPaint = new Paint();
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public final void action() {

        if(MainActivity.GAME_SOUND) {
            //TODO 用Service或线程实现音乐播放

        }

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

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

                        if(MainActivity.GAME_SOUND){
                            //TODO 停止一般bgm，播放BOSS的bgm
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
                executorService.shutdown();
                gameOverFlag = true;
                MainActivity.SCORE = score;
                System.out.println("Game Over!");

                if(MainActivity.GAME_SOUND){
                    //TODO 游戏结束，bgm停止播放，播放game over提示音

                }

                //设置游戏结束标志
                gameOverFlag = true;

                //从GameActivity跳转到BoardActivity(显示排行榜
                Intent intent = new Intent(context, BoardActivity.class);
                context.startActivity(intent);
            }


        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);


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
        // TODO 敌机射击
        for(AbstractAircraft enemyAircraft : enemyAircrafts){
            List<BaseBullet> enemyBullet = enemyAircraft.shoot();
            for(BaseBullet bullet:enemyBullets){
                bullet.setPower(setEnemyBulletPower());
            }
            enemyBullets.addAll(enemyBullet);
        }

        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());

        if(MainActivity.GAME_SOUND){
            //TODO 英雄设计音乐
        }

    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction(){
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
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    if(MainActivity.GAME_SOUND){
                        //TODO 播放英雄机子弹击中敌机音乐
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

                            if(MainActivity.GAME_SOUND){
                                //TODO 将boss背景音切换回一般背景音

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


        for(AbstractProp prop : props){
            if(prop.crash(heroAircraft) || heroAircraft.crash(prop)){
                if(prop.notValid()){
                    continue;
                }
                if(!prop.notValid()){
                    //获取到炸弹道具bomb
                    if(prop instanceof BombProp){

                        if(MainActivity.GAME_SOUND){
                            //TODO 播放炸弹爆炸音效

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
                        if(MainActivity.GAME_SOUND) {
                            //TODO 播放道具生效音效

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
    private void paintImageWithPositionRevised(Canvas canvas, List<? extends AbstractFlyingObject> objects) {
        if (objects != null && objects.size() == 0) {
            return;
        }
        else {
            for (int i=0; i<objects.size(); i++){
                Bitmap image = objects.get(i).getImage();

                assert image != null : objects.getClass().getName() + " has no image! ";
                canvas.drawBitmap(image, objects.get(i).getLocationX() - image.getWidth()/2, objects.get(i).getLocationY() - image.getHeight()/2, mPaint);
            }
        }
    }

    private void paintScoreAndLife(Canvas canvas) {
        Paint mPaint = new Paint();
        int x = 10;
        int y = 25;
        mPaint.setColor(0x16711680);
        canvas.drawText("SCORE:" + score, x, y, mPaint);
        y = y + 20;
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y, mPaint);
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
        paintImageWithPositionRevised(canvas, enemyBullets);
        paintImageWithPositionRevised(canvas, heroBullets);

        paintImageWithPositionRevised(canvas, props);

        paintImageWithPositionRevised(canvas, enemyAircrafts);

        canvas.drawBitmap(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, mPaint);

        //绘制得分和生命值
        paintScoreAndLife(canvas);
        //通过unlockCanvasAndPost(mCanvas)方法对画布内容进行提交
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }


    public abstract void setBackgroundImage();
    public abstract boolean hasBoss();
    public abstract boolean isBossHpUp();
    public abstract int setScoreThreshold();
    public abstract int setEnemyHp();
    public abstract void hardnessPrint(int timeCnt);
    public abstract ShootStrategy setBossStrategy();
    public abstract int setEnemyBulletPower();
    public abstract boolean isCreateElite(int timeCnt);


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        screenHeight = height;
        screenWidth = width;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        Thread.interrupted();
    }

    @Override
    public void run() {
        this.action();
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

    }


}

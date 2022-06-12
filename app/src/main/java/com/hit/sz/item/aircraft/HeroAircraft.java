package com.hit.sz.item.aircraft;

import com.hit.sz.application.ImageManager;
import com.hit.sz.activity.LevelSoundActivity;
import com.hit.sz.item.strategy.ShootStrategy;
import com.hit.sz.item.strategy.StraightShootUp;



/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {



    /** 单例模式 静态变量定义*/
    private volatile static HeroAircraft heroAircraft;


    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp, ShootStrategy strategy) {
        super(locationX, locationY, speedX, speedY, hp, strategy);
    }

    /** 双重检查锁定创建 */
    public static HeroAircraft getHeroAircraft(){
        if(heroAircraft == null){
            synchronized (HeroAircraft.class){
                if(heroAircraft == null){
                    heroAircraft = new HeroAircraft(
                            LevelSoundActivity.screenWidth / 2,
                            LevelSoundActivity.screenHeight - ImageManager.HERO_IMAGE.getHeight() ,
                            0,
                            0,
                            100,
                            new StraightShootUp());
                }
            }
        }
        return heroAircraft;
    }
    /** 双重检查锁定创建 */
    public static HeroAircraft getHeroAircraft(int hp,int power,int num){
        if(heroAircraft == null){
            synchronized (HeroAircraft.class){
                if(heroAircraft == null){
                    heroAircraft = new HeroAircraft(
                            LevelSoundActivity.screenWidth / 2,
                            LevelSoundActivity.screenHeight - ImageManager.HERO_IMAGE.getHeight() ,
                            0,
                            0,
                            (int) (100.0*(10.0+hp)/10.0),
                            new StraightShootUp(power,num));
                }
            }
        }
        return heroAircraft;
    }
    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    /**
     * 英雄机获得加血道具后，回血
     * @param increase 定义的加血量
     */
    public void increaseHp(int increase){
        hp += increase;
    }

}

package com.hit.sz.webservice.data;

public class BattleData extends DataPackage {
    private boolean startBattleReq;
    private boolean battleState;
    private int curScore;
    private int curLife;

    public BattleData(int type, boolean startBattleReq, boolean battleState, int curScore, int curLife) {
        super(type);
        this.startBattleReq = startBattleReq;
        this.battleState = battleState;
        this.curScore = curScore;
        this.curLife = curLife;
    }

    public boolean isStartBattleReq() {
        return startBattleReq;
    }

    public void setStartBattleReq(boolean startBattleReq) {
        this.startBattleReq = startBattleReq;
    }

    public boolean isBattleState() {
        return battleState;
    }

    public void setBattleState(boolean battleState) {
        this.battleState = battleState;
    }

    public int getCurScore() {
        return curScore;
    }

    public void setCurScore(int curScore) {
        this.curScore = curScore;
    }

    public int getCurLife() {
        return curLife;
    }

    public void setCurLife(int curLife) {
        this.curLife = curLife;
    }
}

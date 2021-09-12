package com.mygdx.game.framework.debug;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.framework.debug.sprites.items.ItemPowerUp;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveGamePlayerDataHolderClass implements Serializable{

    private static final long serialVersionUID = 1;

    private float highScores;
    private Vector2 savePointPosition; // = new Vector2();


    private String world;
    private String level;
    private String savePointID;
    private int powerCrystalGreen, powerCrystalBlack, powerCrystalBlue, powerCrystalRed;
    private String powerCrystalInUse;
    private ArrayList<PowerUp> playerPowerUpList = new ArrayList<PowerUp>();

    int playerMainLife;

    public void init() {

        highScores = 0f;//GameManagerAssets.saveGamePlayerDataHolderClass.getSaveHighScores();
        this.savePointPosition = null; //mapLevelinUse = GameManagerAssets.instance.getCurrentWorld();
        this.world = null; //getMapWorldinUse();
        this.level = null;
        this.savePointID = null;
        this.playerMainLife = 0;
        this.powerCrystalInUse = "1";
    }

    public String getSavePointWorld() { return this.world; }
    public  String getSavePointLevel(){ return this.level; }
    public  String getSavePointMarker(){ return this.savePointID; }
    public Vector2 getSavePointPosition(){ return this.savePointPosition; }
    public ArrayList<PowerUp> getPlayerPowerUpList(){return this.playerPowerUpList; }

    public  String getSavePlayerPowerCrystalInUse(){ return this.powerCrystalInUse; }
    public void setSavePlayerPoserCrystalInUse(String v) { this.powerCrystalInUse = v; }

    public void setPlayerPowerTree(String value){

        playerPowerUpList.add( new PowerUp(value ) );
    }

    public int getSavePlayerMainLife(){return this.playerMainLife; }
    public  float getSaveHighScores() { return highScores; }

    public void setSavePointWorld(String w){ this.world = w;}
    public  void setSavePointLevel(String l){ this.level = l; }
    public  void setSavePointMarker(String s){ this.savePointID = s; }
    public void setSavePointPosition(Vector2 pos){ this.savePointPosition = pos; }

    public void setSavePlayerMainLife(int mainL){this.playerMainLife = mainL;}
    public void setSaveHighScores(float value) { this.highScores = value;}


    public class PowerUp implements Serializable {

        String powerName;

        public PowerUp(String pName){
            this.powerName = pName;
        }

        public String getPowerName(){ return this.powerName; }

    }

    public int getPowerCrystalGreen(){ return this.powerCrystalGreen;}
    public void setSavePointPowerCrystalGreen(int v){ this.powerCrystalGreen = v;}

    public int getPowerCrystalBlack(){ return this.powerCrystalBlack;}
    public void setSavePointPowerCrystalBlack(int v){ this.powerCrystalBlack = v;}

    public int getPowerCrystalBlue(){ return this.powerCrystalBlue;}
    public void setSavePointPowerCrystalBlue(int v){ this.powerCrystalBlue = v;}

    public int getPowerCrystalRed(){ return this.powerCrystalRed;}
    public void setSavePointPowerCrystalRed(int v){ this.powerCrystalRed = v;}

}

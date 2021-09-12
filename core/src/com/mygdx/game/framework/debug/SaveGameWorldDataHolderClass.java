package com.mygdx.game.framework.debug;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveGameWorldDataHolderClass implements Serializable {
    private static final long serialVersionUID = 1;

    private ArrayList<WorldSaveObject> knownWorlds;

    public void init() {
        knownWorlds = new ArrayList<WorldSaveObject>();
    }

    /** first init actual set the Save with first World , Level etc !!!*/
    public void setFirstWorld(String world, String level, String haveBoss, String isBossDead, String itemName, int itemID, String used ){
        this.knownWorlds.add( new WorldSaveObject( world, level, haveBoss, isBossDead, itemName, itemID, used ) );
    }

    /** Used with "MAP_INFO" called if no world found in method - addWorldAndLevelMapInfo */
    public void setWorldAndLevelInfo(String world, String level, String haveBoss, String isBossDead){
        this.knownWorlds.add( new WorldSaveObject(world, level, haveBoss, isBossDead));
    }

    public void addWorldAndLevelsMapInfo(String world, String level, String haveBoss, String isBossDead) {
        // Check how many worlds we know!!! for loop needed
        int foundWorld = 0;
        int foundLevel = 0;
        int indexFoundWorld = 0;


        for (int a = 0; a < knownWorlds.size(); a++) {


            if (this.knownWorlds.get(a).getWorldObjectWorld().equals(world) ) {
                foundWorld += 1;
                indexFoundWorld = a;
                //System.out.println( "WorldObject is: " + this.knownWorlds.get(a).getWorldObjectWorld() );
                //System.out.println( "World input is: " + world);
                //setFirstWorld(world, level, haveBoss, isBossDead);
            }
        }
        //System.out.println("found: " + found);
        if(foundWorld == 0){
            //System.out.println("add world!! ");
            //setFirstWorld(world, level, haveBoss, isBossDead, itemName, itemID, used );
            setWorldAndLevelInfo(world, level, haveBoss, isBossDead);
        }


        if(foundWorld == 1){

            //System.out.println("World is Known, check Level !!");
            //System.out.println("World found in location: " + indexFoundWorld);

            for (int b = 0; b < knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.size(); b++) {

                if (this.knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.get(b).getLevelObjectLevel().equals(level) ) {
                    foundLevel +=1;
                }
            }
            //System.out.println("foundLevels: " + foundLevel + " indexFoundLevel: " + indexFoundLevel);
            if(foundLevel == 0){
                //System.out.println("Level not found, add to that world!! ");
                knownWorlds.get(indexFoundWorld).addWorldObjectLevels(level,haveBoss,isBossDead);
            }

        }
    }
    public void addWorldAndLevelsSpawnItem(String world, String level, String itemName, int itemID, String used){
        int foundWorld = 0;
        int foundLevel = 0;
        int foundItem = 0;
        int indexFoundWorld = 0;
        int indexFoundLevel = 0;
        for (int a = 0; a < knownWorlds.size(); a++) {


            if (this.knownWorlds.get(a).getWorldObjectWorld().equals(world) ) {
                foundWorld += 1;
                indexFoundWorld = a;
                //System.out.println( "WorldObject is: " + this.knownWorlds.get(a).getWorldObjectWorld() );
                //System.out.println( "World input is: " + world);
                //setFirstWorld(world, level, haveBoss, isBossDead);
            }
        }
        //System.out.println("found: " + found);
        if(foundWorld == 0){
            //System.out.println("add world!! ");
            //setFirstWorld(world, level, haveBoss, isBossDead, itemName, itemID, used );
        }


        if(foundWorld == 1){

            //System.out.println("World is Known, check Level !!");
            //System.out.println("World found in location: " + indexFoundWorld);

            for (int b = 0; b < knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.size(); b++) {

                if (this.knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.get(b).getLevelObjectLevel().equals(level) ) {
                    foundLevel +=1;
                    indexFoundLevel = b; // use later with itemSpawn
                }
            }
            //System.out.println("foundLevels: " + foundLevel + " indexFoundLevel: " + indexFoundLevel);
            if(foundLevel == 0){
                //System.out.println("Level not found, add to that world!! ");
                //knownWorlds.get(indexFoundWorld).addWorldObjectLevels(level,haveBoss,isBossDead, itemName, itemID, used);
            }

            if(foundLevel == 1){

                for( int c = 0; c < knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().size(); c++ ){

                    if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemID() == itemID ){
                        foundItem +=1;
                    }
                }

                if(foundItem == 0){
                    knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().add(
                            new ItemSaveObject(itemName, itemID, used )
                    );
                }
            }
        }
    }

    public void addWorldAndLevels_Old(String world, String level, String haveBoss, String isBossDead, String itemName, int itemID, String used ) {
        // Check how many worlds we know!!! for loop needed
        int foundWorld = 0;
        int foundLevel = 0;
        int foundItem = 0;
        int indexFoundWorld = 0;
        int indexFoundLevel = 0;
        for (int a = 0; a < knownWorlds.size(); a++) {


            if (this.knownWorlds.get(a).getWorldObjectWorld().equals(world) ) {
                foundWorld += 1;
                indexFoundWorld = a;
                //System.out.println( "WorldObject is: " + this.knownWorlds.get(a).getWorldObjectWorld() );
                //System.out.println( "World input is: " + world);
                //setFirstWorld(world, level, haveBoss, isBossDead);
            }
        }
        //System.out.println("found: " + found);
        if(foundWorld == 0){
            //System.out.println("add world!! ");
            setFirstWorld(world, level, haveBoss, isBossDead, itemName, itemID, used );
        }


        if(foundWorld == 1){

            //System.out.println("World is Known, check Level !!");
            //System.out.println("World found in location: " + indexFoundWorld);

            for (int b = 0; b < knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.size(); b++) {

                if (this.knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.get(b).getLevelObjectLevel().equals(level) ) {
                        foundLevel +=1;
                        indexFoundLevel = b; // use later with itemSpawn
                }
            }
            //System.out.println("foundLevels: " + foundLevel + " indexFoundLevel: " + indexFoundLevel);
            if(foundLevel == 0){
                //System.out.println("Level not found, add to that world!! ");
                knownWorlds.get(indexFoundWorld).addWorldObjectLevels(level,haveBoss,isBossDead, itemName, itemID, used);
            }

            if(foundLevel == 1){

                for( int c = 0; c < knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().size(); c++ ){

                    if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemID() == itemID ){
                        foundItem +=1;
                    }
                }

                if(foundItem == 0){
                    knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().add(
                       new ItemSaveObject(itemName, itemID, used )
                    );
                }
            }


        }
    }

    /** Used int World contactListener - Contact   */
    public void lookUpItemSetItemUsedWorldContactListener(String world, String level, String itemName, int itemID ) {
        // Check how many worlds we know!!! for loop needed
        int foundWorld = 0;
        int foundLevel = 0;
        int indexFoundWorld = 0;
        int indexFoundLevel = 0;

        for (int a = 0; a < knownWorlds.size(); a++) {
            if (this.knownWorlds.get(a).getWorldObjectWorld().equals(world) ) {
                foundWorld += 1;
                indexFoundWorld = a;
            }
        }
        if(foundWorld == 1){
            for (int b = 0; b < knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.size(); b++) {

                if (this.knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.get(b).getLevelObjectLevel().equals(level) ) {
                    foundLevel +=1;
                    indexFoundLevel = b;
                }
            }
            if(foundLevel == 1){

                for( int c = 0; c < knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().size(); c++ ){

                    if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemID() == itemID ){
                        if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemName().equals(itemName) ){

                            //System.out.println("Found item: " + knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemName() );
                            //System.out.println("\tid: " + knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemID() );
                            //System.out.println("\tused: " + knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).taken );
                            knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).setItemUsed("true");
                        }
                    }
                }
            }
        }
    }
    /** Used inn B2WorldCreator add all doors switch to Save World Object Class */
    public void lookUpAddDoorSwitchToLevel(String world, String level, String switchID, String status, String key ) {
        // Check how many worlds we know!!! for loop needed
        int foundWorld = 0;
        int foundLevel = 0;
        int indexFoundWorld = 0;
        int indexFoundLevel = 0;
        int foundDoorSwith = 0;

        for (int a = 0; a < knownWorlds.size(); a++) {
            if (this.knownWorlds.get(a).getWorldObjectWorld().equals(world) ) {
                foundWorld += 1;
                indexFoundWorld = a;
            }
        }
        if(foundWorld == 1){
            for (int b = 0; b < knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.size(); b++) {

                if (this.knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.get(b).getLevelObjectLevel().equals(level) ) {
                    foundLevel +=1;
                    indexFoundLevel = b;
                }
            }
            if(foundLevel == 1){

                for( int c = 0; c < knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getKnownDoorSwitch().size(); c++ ){

                    if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getKnownDoorSwitch().get(c).getDoorSwichId().equals(switchID) ){
                        foundDoorSwith +=1;

                    }
                }
            }

            if(foundDoorSwith == 0 ){
                knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).addKnownDoorSwitchArray(
                        world, level, switchID, status, key
                );
            }
        }
    }

    public String lookUpItemSetCreationVarB2World(String world, String level, String itemName, int itemID) {
        // Check how many worlds we know!!! for loop needed
        int foundWorld = 0;
        int foundLevel = 0;
        int indexFoundWorld = 0;
        int indexFoundLevel = 0;
        String returnValue = "null";

        for (int a = 0; a < knownWorlds.size(); a++) {
            if (this.knownWorlds.get(a).getWorldObjectWorld().equals(world) ) {
                foundWorld += 1;
                indexFoundWorld = a;
            }
        }
        if(foundWorld == 1){
            for (int b = 0; b < knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.size(); b++) {

                if (this.knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.get(b).getLevelObjectLevel().equals(level) ) {
                    foundLevel +=1;
                    indexFoundLevel = b;
                }
            }
            if(foundLevel == 1){

                for( int c = 0; c < knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().size(); c++ ){

                    if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemID() == itemID ){
                        if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemName().equals(itemName) ){
                            //System.out.println("Found item: " + knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemName() );
                            //System.out.println("\tid: " + knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemID() );
                            //System.out.println("\tused: " + knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemUsed() );

                            returnValue = knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().get(c).getItemUsed();
                        }
                    }
                }
            }
        }
        return returnValue;
    }
    public void lookUpEnemyBossSetBossDefeated(String world, String level) {
        // Check how many worlds we know!!! for loop needed
        int foundWorld = 0;
        int foundLevel = 0;
        int indexFoundWorld = 0;
        int indexFoundLevel = 0;


        for (int a = 0; a < knownWorlds.size(); a++) {
            if (this.knownWorlds.get(a).getWorldObjectWorld().equals(world) ) {
                foundWorld += 1;
                indexFoundWorld = a;
            }
        }
        if(foundWorld == 1){
            for (int b = 0; b < knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.size(); b++) {

                if (this.knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.get(b).getLevelObjectLevel().equals(level) ) {
                    foundLevel +=1;
                    indexFoundLevel = b;
                }
            }
            if(foundLevel == 1){

                for( int c = 0; c < knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().size(); c++ ){

                    if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getLevelObjectHaveBoss().equals("true") ){
                        if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getLevelObjectBossDead().equals("false") ){

                            knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).setLevelSaveObjectBossDead("true");
                        }
                    }
                }
            }
        }
    }

    public String lookUpEnemyBossGetBossDefeated(String world, String level) {
        // Check how many worlds we know!!! for loop needed
        int foundWorld = 0;
        int foundLevel = 0;
        int indexFoundWorld = 0;
        int indexFoundLevel = 0;
        String returnBossDead = "null";

        for (int a = 0; a < knownWorlds.size(); a++) {
            if (this.knownWorlds.get(a).getWorldObjectWorld().equals(world) ) {
                foundWorld += 1;
                indexFoundWorld = a;
            }
        }
        if(foundWorld == 1){
            for (int b = 0; b < knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.size(); b++) {

                if (this.knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.get(b).getLevelObjectLevel().equals(level) ) {
                    foundLevel +=1;
                    indexFoundLevel = b;
                }
            }
            if(foundLevel == 1){
                returnBossDead = knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getLevelObjectBossDead();
            }
        }
    return returnBossDead;
    }

    public String lookUpEnemyBossSetCreationVarB2World(String world, String level) {
        // Check how many worlds we know!!! for loop needed
        int foundWorld = 0;
        int foundLevel = 0;
        int indexFoundWorld = 0;
        int indexFoundLevel = 0;
        String returnValue = "null";

        for (int a = 0; a < knownWorlds.size(); a++) {
            if (this.knownWorlds.get(a).getWorldObjectWorld().equals(world) ) {
                foundWorld += 1;
                indexFoundWorld = a;
            }
        }
        if(foundWorld == 1){
            for (int b = 0; b < knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.size(); b++) {

                if (this.knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.get(b).getLevelObjectLevel().equals(level) ) {
                    foundLevel +=1;
                    indexFoundLevel = b;
                }
            }
            if(foundLevel == 1){

                for( int c = 0; c < knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().size(); c++ ){

                    if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getLevelObjectHaveBoss().equals("true") ){
                        if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getLevelObjectBossDead().equals("false") ){

                            returnValue = knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getLevelObjectBossDead();
                        }
                    }
                }
            }
        }
        return returnValue;
    }

    public String lookUpLevelEnemyBossGetName(String world, String level) {
        // Check how many worlds we know!!! for loop needed
        int foundWorld = 0;
        int foundLevel = 0;
        int indexFoundWorld = 0;
        int indexFoundLevel = 0;
        String returnBossName = "";

        for (int a = 0; a < knownWorlds.size(); a++) {
            if (this.knownWorlds.get(a).getWorldObjectWorld().equals(world) ) {
                foundWorld += 1;
                indexFoundWorld = a;
            }
        }
        if(foundWorld == 1){
            for (int b = 0; b < knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.size(); b++) {

                if (this.knownWorlds.get(indexFoundWorld).levelObjectHolderClassArray.get(b).getLevelObjectLevel().equals(level) ) {
                    foundLevel +=1;
                    indexFoundLevel = b;
                }
            }
            if(foundLevel == 1){

                for( int c = 0; c < knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getItemSaveObjectsHolderClassArray().size(); c++ ){

                    if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getLevelObjectHaveBoss().equals("true") ){
                        if( knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).getLevelObjectBossDead().equals("false") ){

                            returnBossName = ""; //knownWorlds.get(indexFoundWorld).getWorldObjectListLevels().get(indexFoundLevel).
                        }
                    }
                }
            }
        }
        return returnBossName;
    }


    public ArrayList<WorldSaveObject> getKnownWorldsList(){ return this.knownWorlds;}

    /** World Class and Level Class */
    public class WorldSaveObject implements Serializable {
        private static final long serialVersionUID = 1;
        private String world;
        private ArrayList<LevelSaveObject> levelObjectHolderClassArray = new ArrayList<LevelSaveObject>();

        public WorldSaveObject(String world, String lev, String boss, String isDead, String itemName, int itemID, String used ){

            setWorldObjectWorld(world);
            levelObjectHolderClassArray.add( new LevelSaveObject( lev, boss, isDead, itemName, itemID, used ));
        }

        /** first entry - List should be empty */
        public WorldSaveObject(String world, String level, String boss, String isDead){

            setWorldObjectWorld(world);
            levelObjectHolderClassArray.add( new LevelSaveObject( level, boss, isDead));
        }

        public WorldSaveObject(String world, String level, String itemName, int itemID, String used){

        }


       public void setWorldObjectWorld(String value){ this.world = value;}
       public String getWorldObjectWorld(){ return this.world;}



       public ArrayList<LevelSaveObject> getWorldObjectListLevels(){ return this.levelObjectHolderClassArray; }

       public void addWorldObjectLevels(String level, String haveBoss, String isBossDead, String itemName, int itemId, String used ){
            this.levelObjectHolderClassArray.add(new LevelSaveObject(level,haveBoss, isBossDead, itemName, itemId, used));
       }

        public void addWorldObjectLevels(String level, String haveBoss, String isBossDead){
            this.levelObjectHolderClassArray.add(new LevelSaveObject(level,haveBoss, isBossDead));
        }
    }
    public class LevelSaveObject implements Serializable {
        private static final long serialVersionUID = 1;
        private String level;
        private String levelHaveBoss;
        //private String bossName;
        private String bossDead;
        private ArrayList<DoorSwitchSaveObject> knownDoorSwitch = new ArrayList<DoorSwitchSaveObject>();
        private ArrayList<ItemSaveObject> itemSaveObjectsHolderClassArray = new ArrayList<ItemSaveObject>();

        public LevelSaveObject(String level, String haveBoss, String bossIsDead, String itemName, int itemID, String used ){
            setLevelSaveObjectLevel(level);

            setLevelSaveObjectHaveBoss(haveBoss);
            setLevelSaveObjectBossDead(bossIsDead);
            itemSaveObjectsHolderClassArray.add( new ItemSaveObject(itemName, itemID, used));
        }

        public LevelSaveObject(String level, String haveBoss, String isBossDead){
            setLevelSaveObjectLevel(level);
            setLevelSaveObjectHaveBoss(haveBoss);
            setLevelSaveObjectBossDead(isBossDead);
        }

        public void setLevelSaveObjectLevel(String value){ this.level = value; }
        public void setLevelSaveObjectHaveBoss(String value){ this.levelHaveBoss = value;}
        public void setLevelSaveObjectBossDead(String value){ this.bossDead = value; }

        public void addKnownDoorSwitchArray(String world, String level, String id, String status, String key){
            this.knownDoorSwitch.add(new DoorSwitchSaveObject(world, level, id, status, key ));
        }

        public ArrayList<DoorSwitchSaveObject> getKnownDoorSwitch(){ return this.knownDoorSwitch; }

        public String getLevelObjectLevel(){ return this.level;}
        public String getLevelObjectHaveBoss(){ return this.levelHaveBoss; }
        public String getLevelObjectBossDead(){ return this.bossDead;}
        public ArrayList<ItemSaveObject> getItemSaveObjectsHolderClassArray(){ return this.itemSaveObjectsHolderClassArray;}
    }

    /** Item Class - Checks if it should be created in map creation */
    public class ItemSaveObject implements Serializable {
        private static final long serialVersionUID = 1;

        String itemName;
        int id;
        String taken;

        public ItemSaveObject(String name, int itemId, String used){

            this.itemName = name;
            this.id = itemId;
            this.taken = used;

        }

        public String getItemName(){ return this.itemName;}
        public int getItemID(){ return this.id;}
        public String getItemUsed(){ return this.taken; }
        public void setItemUsed(String value){this.taken = value;}

    }

    public class KeysSaveObject implements Serializable {
        String doorInnWorld;
        String doorInnLevel;

        String switchDoorID;
        String keyDoorStatus;
        String keyTypeNeeded;
        String haveKey;

        public KeysSaveObject(String world, String level, String id, String doorStatus, String key){

            /** Door is in this World -> Level. */
            this.doorInnWorld = world;
            this.doorInnLevel = level;

            this.switchDoorID = id;
            this.keyDoorStatus = doorStatus;
            this.keyTypeNeeded = key;

            this.haveKey = "false";
        }

        public String getHaveKey(){ return this.haveKey; }
    }

    /**  Switch Door Class */
    public class DoorSwitchSaveObject implements Serializable {


        String doorInnWorld;
        String doorInnLevel;

        String switchDoorID;
        String keyDoorStatus;
        String keyTypeNeeded;

        public DoorSwitchSaveObject(String world, String level, String id, String doorStatus, String key){

            /** Door is in this World -> Level. */
            this.doorInnWorld = world;
            this.doorInnLevel = level;

            this.switchDoorID = id;
            this.keyDoorStatus = doorStatus;
            this.keyTypeNeeded = key;
        }

        public String getDoorSwichInnWorld(){return this.doorInnWorld;}
        public String getDoorSwichInnLevel(){return this.doorInnLevel;}
        public String getDoorSwichId(){return this.switchDoorID; }
        public String getKeyDoorStatus(){ return this.keyDoorStatus; }
        public String getKeyTypeNeeded(){ return this.keyTypeNeeded; }
     }


}

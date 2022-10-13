package com.mygdx.game.framework.debug.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.framework.debug.controllers.ControllerJoyStickButtonStyle;
import com.mygdx.game.framework.debug.managers.GameManagerAI;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.sprites.Enemies.EnemyA;
import com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy.EnemyKnightDevil;
import com.mygdx.game.framework.debug.sprites.Enemies.EnemyB;
import com.mygdx.game.framework.debug.sprites.Enemies.MovingFallingEnemy.EnemyStalactite;
import com.mygdx.game.framework.debug.sprites.Enemies.SmallEnemyDef;
import com.mygdx.game.framework.debug.sprites.Enemies.StationaryEnemies.EnemyGraphicSensor;
import com.mygdx.game.framework.debug.sprites.items.DragonEggGameItem;
import com.mygdx.game.framework.debug.sprites.items.ExtraLifeGameItem;
import com.mygdx.game.framework.debug.sprites.items.ItemPowerUp;
import com.mygdx.game.framework.debug.sprites.items.PortalMapTransitionHidden;
import com.mygdx.game.framework.debug.sprites.items.TreasureChestGameItem;
import com.mygdx.game.framework.debug.sprites.powers.BalloneBullet;
import com.mygdx.game.framework.debug.sprites.powers.EnemyBullet;
import com.mygdx.game.framework.debug.sprites.powers.EnemyKnightPowerSword;
import com.mygdx.game.framework.debug.sprites.powers.EnemyKnightRangeAttack;
import com.mygdx.game.framework.debug.world.gameAiObjects.AiObjectDef;
import com.mygdx.game.framework.debug.world.gameAiObjects.GameAIObject;
import com.mygdx.game.framework.debug.world.gameObjects.GameObjectDef;
import com.mygdx.game.framework.debug.sprites.items.SavePoint;
import com.mygdx.game.framework.debug.util.GameUtility;
import com.mygdx.game.framework.debug.world.gameObjects.GameObjectSwitchDoor;
import com.mygdx.game.framework.debug.world.gameObjects.GameObjectSwitchHidden;

//import com.mygdx.game.objects.Portal;
//import com.mygdx.game.sprites.BubblePlayer;
//import com.mygdx.game.sprites.powers.BalloneBullet;

public class WorldContactListener implements ContactListener{

    B2WorldCreator b2WorldCreator;

    private GameManagerAssets gameManagerAssetsInstance;
    private GameManagerAI gameManagerAIInstance;

    private float enemy_close_attack_bit_Timer = 0;
    private float enemy_range_attack_bit_Timer = 0;
    public WorldContactListener(B2WorldCreator w, GameManagerAssets instanceAsset, GameManagerAI instanceAI){
        this.b2WorldCreator = w;
        this.gameManagerAssetsInstance = instanceAsset;
        this.gameManagerAIInstance = instanceAI;
    }

    @Override
    public void beginContact(Contact contact) {



        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
/*
        int checkAllCollisionWho_is_Who_test = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        System.out.println("checkAllCollision_Who_is_Who_test (short)int " + (short)checkAllCollisionWho_is_Who_test);
        switch (checkAllCollisionWho_is_Who_test){

            case GameUtility.ENEMY_BIT | GameUtility.GAME_AI_OBJECT_BIT:
                System.out.println("checkAllCollision_Who_is_Who_test inn case!! " + (short)checkAllCollisionWho_is_Who_test);
                System.out.println("fixA f/cBits " + fixA.getFilterData().categoryBits );
                System.out.println("fixA uData " + fixA.getUserData() );

                System.out.println("fixB f/cBits " + fixB.getFilterData().categoryBits );
                System.out.println("fixB uData " + fixB.getUserData() );
                break;
        }
*/
        // ToDo : fixA and fixB can change place's , control all the fixture A and B , that we check them if thy change places !!!!

        /** Player & Enemy Contact -With: Ground & Wall's */
        int checkBeginContactGroundAndWall = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (checkBeginContactGroundAndWall){

            case GameUtility.PLAYER_BIT | GameUtility.GROUND_BIT:
                /*
                System.out.println("WorldContactListener Class Ground Player Contact: fixA : " +
                        fixA.getUserData() + " fixB " + fixB.getUserData() );

                System.out.println("WorldContactListener Class Ground Player Contact: fixA : " +
                        fixA.getFilterData().groupIndex + " fixB " + fixB.getFilterData().groupIndex );
                */

                /** Player fixA  | Ground fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.PLAYER_BIT && fixB.getFilterData().groupIndex == GameUtility.GROUND_BIT ) {
                    //System.out.println("WorldContactListener Class Player Hit ground!!");
                    ((BubblePlayer) fixA.getUserData()).setPlayerOnGroundTrue();
                }

                /** Ground fixA  | Player fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.GROUND_BIT && fixB.getFilterData().groupIndex == GameUtility.PLAYER_BIT ) {
                    //System.out.println("WorldContactListener Class Player Hit ground!!");
                    ((BubblePlayer) fixB.getUserData()).setPlayerOnGroundTrue();
                }
                break;

            case GameUtility.PLAYER_BIT | GameUtility.WALL_JUMPING_BIT:
                /* System.out.println("WorldContactListener Class Player Hit wall fixA.Filter " +
                        fixA.getFilterData() + " fixB.Filter " +
                        fixB.getFilterData()
                        );
                */

                /** Player fixA  | Wall fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.PLAYER_BIT && fixB.getFilterData().groupIndex == GameUtility.WALL_JUMPING_BIT) {


                        System.out.println("WorldContactListener Class Player Hit wall!!");
                        ((BubblePlayer) fixB.getUserData()).setPlayerWallJumpTrue();

                }

                /** Wall fixA  | Player fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.WALL_JUMPING_BIT && fixB.getFilterData().groupIndex == GameUtility.PLAYER_BIT ) {

                        System.out.println("WorldContactListener Class Player Hit Wall!!");
                        ((BubblePlayer) fixB.getUserData()).setPlayerWallJumpTrue();

                }
                break;
            /** Changed Enemy groupIndex */
            case GameUtility.ENEMY_BIT | GameUtility.GROUND_BIT:
/*
                System.out.println("WorldContactListener Class Ground Player Contact: fixA : " +
                        fixA.getUserData() + " fixB " + fixB.getUserData() );

                System.out.println("WorldContactListener Class Ground Player Contact: fixA : " +
                        fixA.getFilterData().groupIndex + " fixB " + fixB.getFilterData().groupIndex );
*/
                /** SmallEnemyDef fixA  | Ground fixB*/
                //if (fixA.getFilterData().groupIndex == GameUtility.ENEMY_BIT && fixB.getFilterData().groupIndex == GameUtility.GROUND_BIT ) {
                if (fixA.getFilterData().categoryBits == GameUtility.ENEMY_BIT && fixB.getFilterData().groupIndex == GameUtility.GROUND_BIT ) {
                    //System.out.println("WorldContactListener Class SmallEnemyDef Hit ground!!");

                    if(fixA.getUserData().toString().contains("EnemyA")){
                        ((EnemyA)fixA.getUserData()).setEnemyHitGround();
                    }

                    if(fixA.getUserData().toString().contains("EnemyStalactite")){
                        ((EnemyStalactite)fixA.getUserData()).setEnemyHitGround();
                    }
                }

                /** Ground fixA  | SmallEnemyDef fixB*/
                //if (fixA.getFilterData().groupIndex == GameUtility.GROUND_BIT && fixB.getFilterData().groupIndex == GameUtility.ENEMY_BIT ) {
                if (fixA.getFilterData().groupIndex == GameUtility.GROUND_BIT && fixB.getFilterData().categoryBits == GameUtility.ENEMY_BIT ) {
                    //System.out.println("WorldContactListener Class SmallEnemyDef Hit ground!!");

                    if(fixB.getUserData().toString().contains("EnemyA")){
                        ((EnemyA)fixB.getUserData()).setEnemyHitGround();
                    }

                    if(fixB.getUserData().toString().contains("EnemyStalactite")){
                        ((EnemyStalactite)fixB.getUserData()).setEnemyHitGround();
                    }
                }
                break;
/**Begin Object On floor test*/
            case GameUtility.GAME_ITEM_BIT | GameUtility.GROUND_BIT:
/*
                System.out.println("WorldContactListener Class Ground Player Contact: fixA : " +
                        fixA.getUserData() + " fixB " + fixB.getUserData() );

                System.out.println("WorldContactListener Class Ground Player Contact: fixA : " +
                        fixA.getFilterData().groupIndex + " fixB " + fixB.getFilterData().groupIndex );
*/
                /** ItemObject fixA  | Ground fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.GAME_ITEM_BIT && fixB.getFilterData().groupIndex == GameUtility.GROUND_BIT ) {
                    //System.out.println("WorldContactListener Class SmallEnemyDef Hit ground!!");

                    if(fixA.getUserData().toString().contains("DragonEggGameItem")){
                        ((DragonEggGameItem)fixA.getUserData()).setEnemyHitGround();
                    }
                }

                /** Ground fixA  | ItemObject fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.GROUND_BIT && fixB.getFilterData().groupIndex == GameUtility.GAME_ITEM_BIT ) {
                    //System.out.println("WorldContactListener Class SmallEnemyDef Hit ground!!");

                    if(fixB.getUserData().toString().contains("DragonEggGameItem")){
                        ((DragonEggGameItem)fixB.getUserData()).setEnemyHitGround();
                    }
                }
                break;
/**END Object On floor test*/
            case GameUtility.ENEMY_BIT | GameUtility.WALL_JUMPING_BIT:

                /** SmallEnemyDef fixA  | Wall fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.ENEMY_BIT && fixB.getFilterData().groupIndex == GameUtility.WALL_JUMPING_BIT) {
                    //System.out.println("WorldContactListener Class SmallEnemyDef Hit wall!!");

                    if(fixA.getUserData().toString().contains("EnemyA")){
                        ((EnemyA)fixA.getUserData()).setEnemyHitWall();
                    }

                }

                /** Wall fixA  | SmallEnemyDef fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.WALL_JUMPING_BIT && fixB.getFilterData().groupIndex == GameUtility.ENEMY_BIT ) {
                    //System.out.println("WorldContactListener Class SmallEnemyDef Hit Wall!!");

                    if(fixB.getUserData().toString().contains("EnemyA")){
                        ((EnemyA)fixB.getUserData()).setEnemyHitWall();
                    }

                }
                break;

        }

        /** Player's Powers Contact -With Ground & Wall's */
        int checkBeginContactPlayerPowers = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (checkBeginContactPlayerPowers){

            case GameUtility.PLAYER_POWER_BIT | GameUtility.WALL_AND_SEALING_BIT:
            case GameUtility.PLAYER_POWER_BIT | GameUtility.WALL_JUMPING_BIT:
                //System.out.println("fixA : " + fixA.getFilterData().groupIndex + " fixB : " + fixB.getFilterData().groupIndex +
                //        " userDataA " + fixA.getUserData() + " userDataB " + fixB.getUserData() );


                if (fixA.getFilterData().groupIndex == GameUtility.PLAYER_POWER_BIT && fixB.getFilterData().groupIndex == GameUtility.WALL_AND_SEALING_BIT) {
                    ((BalloneBullet) fixB.getUserData()).setToDestroy();
                }

                if (fixB.getFilterData().groupIndex == GameUtility.PLAYER_POWER_BIT && fixA.getFilterData().groupIndex == GameUtility.WALL_AND_SEALING_BIT) {
                    ((BalloneBullet) fixB.getUserData()).setToDestroy();
                }


                if (fixA.getFilterData().groupIndex == GameUtility.PLAYER_POWER_BIT && fixB.getFilterData().groupIndex == GameUtility.WALL_JUMPING_BIT) {
                    ((BalloneBullet) fixB.getUserData()).setToDestroy();
                }

                if (fixB.getFilterData().groupIndex == GameUtility.PLAYER_POWER_BIT && fixA.getFilterData().groupIndex == GameUtility.WALL_JUMPING_BIT) {
                    ((BalloneBullet) fixB.getUserData()).setToDestroy();
                }



                break;
            case GameUtility.PLAYER_POWER_BIT | GameUtility.GROUND_BIT:
                //System.out.println("fixA : " + fixA.getFilterData().groupIndex + " fixB : " + fixB.getFilterData().groupIndex +
                //   " userDataA " + fixA.getUserData() + " userDataB " + fixB.getUserData() );

                if (fixA.getFilterData().groupIndex == GameUtility.PLAYER_POWER_BIT && fixB.getFilterData().groupIndex == GameUtility.GROUND_BIT ) {
                    ((BalloneBullet) fixB.getUserData()).setToDestroy();
                }

                if (fixB.getFilterData().groupIndex == GameUtility.PLAYER_POWER_BIT && fixA.getFilterData().groupIndex == GameUtility.GROUND_BIT ) {
                    ((BalloneBullet) fixB.getUserData()).setToDestroy();
                }
                break;
        }

        /** All other contact's for now !! */
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
        switch (cDef) {

//ToDo:: check remove it and use the new one, rename the get set method - old don't do any thing - history
            //ok No its wrong !!!
            case GameUtility.PLAYER_BIT | GameUtility.GROUND_BIT: // if player is on Ground or off it /falling to death
                //System.out.println("WorldContactListener Class Begin Contact : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                if (fixA.getFilterData().categoryBits == GameUtility.GROUND_BIT) {

                    //BubblePlayer.playerOnGround(); // Static boolean ok !!
                    //System.out.println("WorldContactListener Class Begin Contact : fixA : " + fixA.getUserData() );
                }
                break;
            case GameUtility.PLAYER_BIT | GameUtility.WALL_JUMPING_BIT:
                //System.out.println("WorldContactListener Class fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                if (fixA.getFilterData().categoryBits == GameUtility.WALL_JUMPING_BIT) {
                    //((InteractiveTileObject) fixB.getUserData()).onSavePointHit((BubblePlayer) fixA.getUserData());


                    //System.out.println("WorldContactListener Class fixA: WallJump True Begin Contact");
                    if(fixB.getUserData().toString().contains("BubblePlayer")){
                        //((BubblePlayer) fixB.getUserData()).setPlayerWallJumpTrue(); // Moved as sliding down on spikes error it get stuck in this mode ???
                    }


                }else{
                    //System.out.println("WorldContactListener Class fixB: WallJump True Begin Contact");
                    //((BubblePlayer) fixB.getUserData()).setPlayerWallJumpTrue();
                }

                //System.out.println("Contact player and ground!!");
                break;
//ToDo:: check if we can remove it ?

            case GameUtility.ENEMY_BOTTOM_BIT | GameUtility.PLAYER_BIT:
                //System.out.println("fixA : " + fixA.getUserData() + " fixB : " + fixB.getUserData());// .getFilterData().categoryBits);

                if(fixA.getFilterData().categoryBits == GameUtility.ENEMY_BOTTOM_BIT) {
                    ((EnemyKnightDevil) fixA.getUserData()).enemyFallsOrJumpsOnPlayer();

                    ((BubblePlayer) fixB.getUserData()).contactWithEnemyClose(
                            ((EnemyKnightDevil) fixA.getUserData()) );
                }
                if(fixB.getFilterData().categoryBits == GameUtility.ENEMY_BOTTOM_BIT) {
                    ((EnemyKnightDevil) fixB.getUserData()).enemyFallsOrJumpsOnPlayer();

                    ((BubblePlayer) fixA.getUserData()).contactWithEnemyClose(
                            ((EnemyKnightDevil) fixB.getUserData()) );
                }
                break;

            /** case | if player touch the SmallEnemyDef */
            case GameUtility.PLAYER_BIT | GameUtility.ENEMY_BIT:
            case GameUtility.PLAYER_BIT | GameUtility.ENEMY_LEGS_BIT:
                //System.out.println("fixA : " + fixA.getUserData() + " fixB : " + fixB.getUserData());// .getFilterData().categoryBits);

                // think ok!!
                if(fixA.getFilterData().categoryBits == GameUtility.PLAYER_BIT) {
                    //System.out.println("fixA is Player: " + fixA.getUserData() + " fixB : " + fixB.getUserData());

                    if(fixB.getUserData().toString().contains("EnemyA")){
                        //System.out.println("Contact with EnemyA");

                        ((BubblePlayer) fixA.getUserData()).contactWithEnemyClose(
                                ((EnemyA) fixB.getUserData()) );
                    }

                    if(fixB.getUserData().toString().contains("EnemyB")){
                        //System.out.println("Contact with EnemyA");

                        ((BubblePlayer) fixA.getUserData()).contactWithEnemyClose(
                                ((EnemyB) fixB.getUserData()) );
                    }

                    if(fixB.getUserData().toString().contains("EnemyKnightDevil")){
                        ((BubblePlayer) fixA.getUserData()).contactWithEnemyClose(
                                ((EnemyKnightDevil) fixB.getUserData())
                        );
                    }

                    if(fixB.getUserData().toString().contains("EnemyKnightDevil")){
                        ((BubblePlayer) fixA.getUserData()).contactWithEnemyClose(
                                ((EnemyKnightDevil) fixB.getUserData())
                        );
                    }

                    if(fixB.getUserData().toString().contains("EnemyGraphicSensor")){
                        ((BubblePlayer) fixA.getUserData()).contactWithEnemyClose(
                                ((EnemyGraphicSensor) fixB.getUserData())
                        );
                    }

                    if(fixB.getUserData().toString().contains("EnemyStalactite")){
                        ((BubblePlayer) fixA.getUserData()).contactWithEnemyClose(
                                ((EnemyStalactite) fixB.getUserData())
                        );
                    }

                }

                // think ok!!
                if (fixB.getFilterData().categoryBits == GameUtility.PLAYER_BIT) {
                    //System.out.println("fixB is Player: " + fixB.getUserData() + " fixA : " + fixA.getUserData());

                    if(fixA.getUserData().toString().contains("EnemyA")){
                        //System.out.println("Contact with EnemyA");

                        ((BubblePlayer) fixB.getUserData()).contactWithEnemyClose(
                                ((EnemyA) fixA.getUserData()) );
                    }

                    if(fixA.getUserData().toString().contains("EnemyB")){
                        //System.out.println("Contact with EnemyA");

                        ((BubblePlayer) fixB.getUserData()).contactWithEnemyClose(
                                ((EnemyB) fixA.getUserData()) );
                    }

                    if(fixA.getUserData().toString().contains("EnemyKnightDevil")){
                        ((BubblePlayer) fixB.getUserData()).contactWithEnemyClose(
                                ((EnemyKnightDevil) fixA.getUserData())
                        );
                    }

                    if(fixA.getUserData().toString().contains("EnemyKnightDevil")){
                        ((BubblePlayer) fixB.getUserData()).contactWithEnemyClose(
                                ((EnemyKnightDevil) fixA.getUserData())
                        );
                    }

                    if(fixA.getUserData().toString().contains("EnemyGraphicSensor")){
                        ((BubblePlayer) fixB.getUserData()).contactWithEnemyClose(
                                ((EnemyGraphicSensor) fixA.getUserData())
                        );
                    }

                    if(fixA.getUserData().toString().contains("EnemyStalactite")){
                        ((BubblePlayer) fixB.getUserData()).contactWithEnemyClose(
                                ((EnemyStalactite) fixA.getUserData())
                        );
                    }
                }
                break;
            /** A Trigger to Start: Range | attack */
            case GameUtility.ENEMY_RANGE_ATTACK_BIT | GameUtility.PLAYER_BIT:

                if (fixA.getFilterData().categoryBits == GameUtility.ENEMY_RANGE_ATTACK_BIT) {
                    ((EnemyKnightDevil) fixA.getUserData()).setRangeAttackActiveBool(true);
                    //System.out.println("WorldContactListener fixA Enemy_Range_attack: true");
                }

                if (fixB.getFilterData().categoryBits == GameUtility.ENEMY_RANGE_ATTACK_BIT) {
                    ((EnemyKnightDevil) fixB.getUserData()).setRangeAttackActiveBool(true);
                    //System.out.println("WorldContactListener fixB Enemy_Range_attack: true");
                }
                break;
            /** A Trigger to Start:  Close | attack */
            case GameUtility.ENEMY_CLOSE_ATTACK_BIT | GameUtility.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == GameUtility.ENEMY_CLOSE_ATTACK_BIT) {

                    if (fixA.getUserData().toString().contains("EnemyKnightDevil")) {
                        ((EnemyKnightDevil) fixA.getUserData()).closeAttack((EnemyKnightDevil) fixA.getUserData());
                    }

                    if (fixB.getUserData().toString().contains("EnemyKnightDevil")) {
                        ((EnemyKnightDevil) fixB.getUserData()).closeAttack((EnemyKnightDevil) fixB.getUserData());
                    }
                }
                break;
            /** SmallEnemyDef vrs Player - Think its ok - fixA and fixB can change place's */
            case GameUtility.ENEMY_POWER_BIT | GameUtility.PLAYER_BIT:
                //System.out.println("fixA : " + fixA.getUserData() + " fixB : " + fixB.getUserData());// .getFilterData().categoryBits);

                if (fixA.getFilterData().categoryBits == GameUtility.ENEMY_POWER_BIT) {
                    //System.out.println("CatA -fixA : " + fixA.getUserData() + " fixB : " + fixB.getUserData());// .getFilterData().categoryBits);

                    /** Close Attack - fixA = SmallEnemyDef / fixB = Player */
                    if (fixA.getUserData().toString().contains("EnemyKnightPowerSword")) {
                        ((BubblePlayer) fixB.getUserData()).contactWithEnemyClose(
                                ((EnemyKnightPowerSword) fixA.getUserData()));
                    }

                    /** Range Attack - fixA = SmallEnemyDef / fixB = Player */
                    if(fixA.getUserData().toString().contains("EnemyKnightRangeAttack")) {
                        ((BubblePlayer) fixB.getUserData()).contactWithEnemyRange(
                                ((EnemyKnightRangeAttack) fixA.getUserData()) );
                    }

                    /** EnemyB flying - fixA = SmallEnemyDef / fixB = Player */
                    if(fixA.getUserData().toString().contains("EnemyBullet")) {
                        ((BubblePlayer) fixB.getUserData()).contactWithEnemyBBullet(
                                ((EnemyBullet) fixA.getUserData()) );
                    }


                }

                if (fixB.getFilterData().categoryBits == GameUtility.ENEMY_POWER_BIT) {
                    //System.out.println("CatB -fixA : " + fixA.getUserData() + " fixB : " + fixB.getUserData());// .getFilterData().categoryBits);

                    /** Close Attack - fixB = SmallEnemyDef / fixA = Player */
                    if (fixB.getUserData().toString().contains("EnemyKnightPowerSword")) {
                        ((BubblePlayer) fixA.getUserData()).contactWithEnemyClose(
                                ((EnemyKnightPowerSword) fixB.getUserData()));
                    }

                    /** Range Attack - fixB = SmallEnemyDef / fixA = Player */
                    if(fixB.getUserData().toString().contains("EnemyKnightRangeAttack")) {
                        ((BubblePlayer) fixA.getUserData()).contactWithEnemyRange(
                                ((EnemyKnightRangeAttack) fixB.getUserData()) );
                    }

                    /** Range Attack - fixB = SmallEnemyDef / fixA = Player */
                    if(fixB.getUserData().toString().contains("EnemyBullet")) {
                        ((BubblePlayer) fixA.getUserData()).contactWithEnemyBBullet(
                                ((EnemyBullet) fixB.getUserData()) );
                    }
                }
                break;


            /** Player vrs SmallEnemyDef Think its ok - fixA and fixB can change place's */
            case GameUtility.PLAYER_POWER_BIT | GameUtility.ENEMY_BIT:
            case GameUtility.PLAYER_POWER_BIT | GameUtility.ENEMY_LEGS_BIT:
                //System.out.println("fixA : " + fixA.getUserData() + " fixB : " + fixB.getUserData());// .getFilterData().categoryBits);

                // BalloneBullet fixA Then EnemyA = fixB
                if(fixA.getUserData().toString().contains("BalloneBullet") && fixB.getUserData().toString().contains("EnemyA")){

                    // destroy enemy
                    ((EnemyA) fixB.getUserData()).hitWithBullet(
                            ((BalloneBullet) fixA.getUserData()).getBalloneBulletDamageColor());
                    // destroy bullet
                    //System.out.println("Contatct List, Player Power Bullet: " + ((BalloneBullet) fixA.getUserData()).getBalloneBulletDamageColor());
                    ((BalloneBullet) fixA.getUserData()).setToDestroy();



                }
                //EnemyB
                if(fixA.getUserData().toString().contains("BalloneBullet") && fixB.getUserData().toString().contains("EnemyB")){

                    // destroy enemy
                    ((EnemyB) fixB.getUserData()).hitWithBullet(
                            ((BalloneBullet) fixA.getUserData()).getBalloneBulletDamageColor());
                    // destroy bullet
                    //System.out.println("Contatct List, Player Power Bullet: " + ((BalloneBullet) fixA.getUserData()).getBalloneBulletDamageColor());
                    ((BalloneBullet) fixA.getUserData()).setToDestroy();
                }

                // EnemyA fixA Then BalloneBullet = fixB
                if(fixA.getUserData().toString().contains("EnemyA") && fixB.getUserData().toString().contains("BalloneBullet")){
                    // destroy enemy
                    ((EnemyA) fixA.getUserData()).hitWithBullet(
                            ((BalloneBullet) fixB.getUserData()).getBalloneBulletDamageColor());
                    // destroy bullet
                    //System.out.println("Contatct List, Player Power Bullet: " + ((BalloneBullet) fixB.getUserData()).getBalloneBulletDamageColor());
                    ((BalloneBullet) fixB.getUserData()).setToDestroy();
                }
                // EnemyB fixA Then BalloneBullet = fixB
                if(fixA.getUserData().toString().contains("EnemyB") && fixB.getUserData().toString().contains("BalloneBullet")){
                    // destroy enemy
                    ((EnemyB) fixA.getUserData()).hitWithBullet(
                            ((BalloneBullet) fixB.getUserData()).getBalloneBulletDamageColor());
                    // destroy bullet
                    //System.out.println("Contatct List, Player Power Bullet: " + ((BalloneBullet) fixB.getUserData()).getBalloneBulletDamageColor());
                    ((BalloneBullet) fixB.getUserData()).setToDestroy();
                }


                if(fixA.getUserData().toString().contains("EnemyKnightDevil") && fixB.getUserData().toString().contains("BalloneBullet")){

                    ((EnemyKnightDevil) fixA.getUserData()).hitWithBullet(
                            ((BalloneBullet) fixB.getUserData()));
                    //System.out.println("fixA : " + fixA.getUserData() + " fixB : " + fixB.getUserData());// .getFilterData().categoryBits);
                    ((BalloneBullet) fixB.getUserData()).setToDestroy();
                }

                if(fixA.getUserData().toString().contains("BalloneBullet") && fixB.getUserData().toString().contains("EnemyKnightDevil")){

                    ((EnemyKnightDevil) fixB.getUserData()).hitWithBullet(
                            ((BalloneBullet) fixA.getUserData()));
                    //System.out.println("fixA : " + fixA.getUserData() + " fixB : " + fixB.getUserData());// .getFilterData().categoryBits);
                    ((BalloneBullet) fixA.getUserData()).setToDestroy();
                }
                break;
                // ToDo have to change in case FixA and FixB change place!!!
            case GameUtility.GAME_OBJECT_BIT | GameUtility.PLAYER_BIT:
            case GameUtility.GAME_OBJECT_BIT | GameUtility.PLAYER_POWER_BIT:
                //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );

                if (fixA.getFilterData().categoryBits == GameUtility.GAME_OBJECT_BIT) {
                        //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
// ToDo: change , check if player has the key to open door, etc
                        // Switch's Open / Close Door's
                        if(((GameObjectDef) fixA.getUserData()).getObjectIdentity().equals("GAME_SWITCH_OPEN")) {


                           if( ((GameObjectSwitchDoor) fixA.getUserData()).getDoorKeyStatus().equals("OPEN")){

                               ((GameObjectDef) fixA.getUserData()).onGameObjectHitChangeTexture(true);
                               ((GameObjectDef) fixA.getUserData()).setRunDoors(true);
                           }else{

                               /** then it's "CLOSED" or Boss Door, Defeated - Closed up - */
                               /** Check for Key - if Key is found Open Door, Else - Print "YOU DON'T HAVE THE KEY FOR THIS DOOR" !!! */


                               if( gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpEnemyBossGetBossDefeated(
                                       gameManagerAssetsInstance.getCurrentWorld(),
                                       gameManagerAssetsInstance.getCurrentLevel() ).equals("false") ) {

                                   ((GameObjectDef) fixA.getUserData()).onGameObjectHitChangeTexture(true);
                                   ((GameObjectDef) fixA.getUserData()).setRunDoors(true);
                               }
                           }
                        }

                    if(((GameObjectDef) fixA.getUserData()).getObjectIdentity().equals("HIDDEN_SWITCH_OPEN")) {
                        if( ((GameObjectSwitchHidden) fixA.getUserData()).getIsBossDeadOpenDoorSwitch().equals("true") ) {
                            if( ((GameObjectSwitchHidden) fixA.getUserData()).getActivateSwitch().equals("true") ) {
                                ((GameObjectDef) fixA.getUserData()).setRunDoors(true);

                                /** Set boss dead Save World Object*/
                                gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpEnemyBossSetBossDefeated(
                                        gameManagerAssetsInstance.getCurrentWorld(),
                                        gameManagerAssetsInstance.getCurrentLevel()
                                );
                                /** Player is saving, We save Known World & Level's and used Item's to file */
                                gameManagerAssetsInstance.gameManagerWorldDataWriteToFile();

                            }
                        }
                    }

                        if(((GameObjectDef) fixA.getUserData()).getObjectIdentity().equals("GAME_SWITCH_CLOSE_HIDDEN")) {
                            //((GameObjectDef) fixA.getUserData()).onGameObjectHitChangeTexture(true); // don't use texture on this one
                            ((GameObjectDef) fixA.getUserData()).setRunDoors(true);
                            //((GameObjectDef) fixA.getUserData()).active(b2WorldCreator.activableObstacles);
                        }
                        // Save Game
                        if(((GameObjectDef) fixA.getUserData()).getObjectIdentity().equals("GAME_SAVE_POINT")) {
                            //System.out.println("WorldContactListener Class Position of SpawonPoint ; " + ((SavePoint)fixA.getUserData()).getPositionSavePoint() );
                            /*
                            System.out.println("WorldContactListener Class - World: " + ((SavePoint) fixA.getUserData()).getWorld() +
                                    " Level: " + ((SavePoint) fixA.getUserData()).getLevel() +
                                    " SavePoint: " + ((SavePoint) fixA.getUserData()).getSavePoint() );
                            */

                            //if( !((SavePoint) fixA.getUserData()).getSavePointUsed() ) {


                                //System.out.println("Saving the game Save Point hit!!!");
                                ((GameObjectDef) fixA.getUserData()).onSavePointHit(
                                        ((SavePoint) fixA.getUserData()).getPositionSavePoint(),
                                        ((SavePoint) fixA.getUserData()).getWorld(),
                                        ((SavePoint) fixA.getUserData()).getLevel(),
                                        ((SavePoint) fixA.getUserData()).getSavePoint());

                                ((GameObjectDef) fixA.getUserData()).onGameObjectHitChangeTexture(true);

                                /** Player is saving, We save Known World & Level's and used Item's to file */
                            gameManagerAssetsInstance.gameManagerWorldDataWriteToFile();
                            gameManagerAIInstance.setPlayerPowerUpTextToScreen("Saving Game!!",((BubblePlayer) fixB.getUserData()).getPlayerPosition(), 0, 50);
                            //System.out.println("WorldContactListener SaveGame fixB: " + fixB.getUserData().toString() );
                            //}
                        }

                        // Portal's
                        if(((GameObjectDef) fixA.getUserData()).getObjectIdentity().equals("GAME_PORTAL")) {
                            //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                            //if( ((GameObjectDef) fixA.getUserData()).getPortalMapTravelType().equals("WORLD") ) {
                                //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                                /*
                                ((Portal) fixA.getUserData()).onPortalTravelHit(
                                        ((Portal) fixA.getUserData()).getThisPortalMapWorld(),
                                        ((Portal) fixA.getUserData()).getPortalMapDestinationLevel());
                                */
                                // String travelType, String theMapW, String theMapL, String mapTravelTo
/*
                            GameManagerAssets.instance.onPortalTravelHit(
                                        ((Portal) fixA.getUserData()).getPortalMapTravelType(),
                                        ((Portal) fixA.getUserData()).getThisPortalMapWorld(),
                                        ((Portal) fixA.getUserData()).getThisPortalMapLevel(),
                                        ((Portal) fixA.getUserData()).getPortalMapDestination(),
                                        ((Portal) fixA.getUserData()).getMapPortalDirectionTraveld()
                                );

                                ((BubblePlayer)fixB.getUserData()).onPortalTravelHit( ((Portal) fixA.getUserData()).getPortalMapTravelType(),
                                        ((Portal) fixA.getUserData()).getPortalMapDestination()
                                );
*/
                            //}
                            //if( ((GameObjectDef) fixA.getUserData()).getPortalMapTravelType().equals("LEVEL") ) {

                            //}

                        }

                        // Portal's hidden transition
                        if(((GameObjectDef) fixA.getUserData()).getObjectIdentity().equals("GAME_PORTAL_HIDDEN_TRANSITION")) {
                            //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                            //if( ((GameObjectDef) fixA.getUserData()).getPortalMapTravelType().equals("START") ) {
                                //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                                    /*
                                    ((Portal) fixA.getUserData()).onPortalTravelHit(
                                            ((Portal) fixA.getUserData()).getThisPortalMapWorld(),
                                            ((Portal) fixA.getUserData()).getPortalMapDestinationLevel());
                                    */
                                //if(((PortalMapTransitionHidden) fixA.getUserData()).mapPortalActive.equals("true")) {
                                //if( ((PortalMapTransitionHidden) fixA.getUserData()).getActive().equals("true") ) {
                            gameManagerAssetsInstance.onPortalTravelHit(
                                            ((PortalMapTransitionHidden) fixA.getUserData()).getPortalMapTravelType(),
                                            ((PortalMapTransitionHidden) fixA.getUserData()).getThisPortalMapWorld(),
                                            ((PortalMapTransitionHidden) fixA.getUserData()).getThisPortalMapLevel(),
                                            ((PortalMapTransitionHidden) fixA.getUserData()).getPortalMapDestinationWorld(),
                                            ((PortalMapTransitionHidden) fixA.getUserData()).getPortalMapDestinationLevel(),
                                            ((PortalMapTransitionHidden) fixA.getUserData()).getMapPortalDirectionTraveld()
                                    );

                            gameManagerAssetsInstance.setPortalTravelDirection(
                                            ((PortalMapTransitionHidden) fixA.getUserData()).getMapPortalDirectionTraveld()
                                    );

                            gameManagerAssetsInstance.setMapTraveldSensorDirection(
                                            ((PortalMapTransitionHidden) fixA.getUserData()).getMapPortalSensorDirectionTraveld()
                                    );

                                    ((BubblePlayer) fixB.getUserData()).onPortalTravelHit(((PortalMapTransitionHidden) fixA.getUserData()).getPortalMapTravelType(),
                                            ((PortalMapTransitionHidden) fixA.getUserData()).getPortalMapDestinationWorld(),
                                            ((PortalMapTransitionHidden) fixA.getUserData()).getPortalMapDestinationLevel()
                                    );
                                //}
                                  //  ((PortalMapTransitionHidden) fixA.getUserData()).setMapPortalActive("false");
                                //}
                            //}
                        }
                    }
                break;
            /** GAME_AI_OBJECT_BIT  Player collide with AI Markers */
            case GameUtility.GAME_AI_OBJECT_BIT | GameUtility.PLAYER_BIT:
                //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                if (fixA.getFilterData().categoryBits == GameUtility.GAME_AI_OBJECT_BIT) {
                    // spawn Boss marker
                    if (((AiObjectDef) fixA.getUserData()).getObjectIdentity().equals("GAME_BOSS_SPAWN_MARKER")) {
                        ((AiObjectDef) fixA.getUserData()).getHitBossSpawn();
                    }
                }
                if (fixB.getFilterData().categoryBits == GameUtility.GAME_AI_OBJECT_BIT) {
                    if (((AiObjectDef) fixB.getUserData()).getObjectIdentity().equals("GAME_BOSS_SPAWN_MARKER")) {
                        ((AiObjectDef) fixB.getUserData()).getHitBossSpawn();
                    }
                }

                if (fixA.getFilterData().categoryBits == GameUtility.GAME_AI_OBJECT_BIT) {

                    if (((AiObjectDef) fixA.getUserData()).getObjectIdentity().equals("AI_STEERING")) {

                        System.out.println("WorldContactListener fixA AI_STEERING TYPE " + ((GameAIObject) fixA.getUserData()).getMapMarkerType());
                        if(((GameAIObject) fixA.getUserData()).getMapMarkerType().equals("Steering Stop")){

                            gameManagerAIInstance.setStopSteeringActivityFromContactWithAI(true);

                        }else if(((GameAIObject) fixA.getUserData()).getMapMarkerType().equals("Steering Start")){

                            gameManagerAIInstance.setStartSteeringActivityFromContactWithAI(true);
                        }

                    }
                }

                if (fixB.getFilterData().categoryBits == GameUtility.GAME_AI_OBJECT_BIT) {
                    if (((AiObjectDef) fixB.getUserData()).getObjectIdentity().equals("AI_STEERING")) {

                        System.out.println("WorldContactListener fixB AI_STEERING TYPE " + ((GameAIObject) fixB.getUserData()).getMapMarkerType());

                        if(((GameAIObject) fixB.getUserData()).getMapMarkerType().equals("Steering Stop")) {
                            gameManagerAIInstance.setStopSteeringActivityFromContactWithAI(true);
                        }else if (((GameAIObject) fixB.getUserData()).getMapMarkerType().equals("Steering Start")) {
                            gameManagerAIInstance.setStartSteeringActivityFromContactWithAI(true);
                        }

                    }
                }






//ToDo: taken away !!! not sure | only by the tiled map - if take away - delete in create all so.
                if (fixA.getFilterData().categoryBits == GameUtility.GAME_AI_OBJECT_BIT) {

                    if (((AiObjectDef) fixA.getUserData()).getObjectIdentity().equals("GAME_PORTAL_SET_ACTIVITY")) {
                        ((AiObjectDef) fixA.getUserData()).setPortalActivity("true");
                    }
                }
                if (fixB.getFilterData().categoryBits == GameUtility.GAME_AI_OBJECT_BIT) {

                    if (((AiObjectDef) fixB.getUserData()).getObjectIdentity().equals("GAME_PORTAL_SET_ACTIVITY")) {
                        ((AiObjectDef) fixB.getUserData()).setPortalActivity("true");
                    }
                }



                break;
            /** GAME_AI_OBJECT_BIT  Enemies collide with AI Markers */
           // case GameUtility.GAME_AI_OBJECT_BIT | GameUtility.ENEMY_BIT: // AI bit changed from GAME_OBJECT_BIT
           case GameUtility.ENEMY_BIT | GameUtility.GAME_AI_OBJECT_BIT: // AI bit changed from GAME_OBJECT_BIT
                //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );

            /** Case fixA */
                if (fixA.getFilterData().categoryBits == GameUtility.GAME_AI_OBJECT_BIT) {
                    //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );

                    /** EnemyA */
                    if(fixB.getUserData().toString().contains("EnemyA")) {
                        if (((AiObjectDef) fixA.getUserData()).getObjectIdentity().equals("AI_BODY_CHANGE")) { // GAME_ENEMY_A_REVERS_V old

                            System.out.println("MarkerType is " + ((GameAIObject) fixA.getUserData()).getMapMarkerType() );

                            if( ((GameAIObject) fixA.getUserData()).getMapMarkerType().equals("Velocity") ) {

                                ((EnemyA) fixB.getUserData()).reverseVelocity(true, false);
                                //System.out.println("reversVel times ???" );


                            }else if( ((GameAIObject) fixA.getUserData()).getMapMarkerType().equals("FacingDirection") ) {

                                if(((EnemyA) fixB.getUserData()).getIsRunningRight()){
                                    //System.out.println("EnemyA is running Right: ");
                                    ((EnemyA) fixB.getUserData()).setLinearVelocity();
                                    ((EnemyA) fixB.getUserData()).setIsRunningRight(false);
                                }else{
                                    //System.out.println("EnemyA is running Left: ");
                                    ((EnemyA) fixB.getUserData()).setLinearVelocity();
                                    ((EnemyA) fixB.getUserData()).setIsRunningRight(true);
                                }
                            }
                        }
                    }

                    /** EnemyB */
                    if(fixB.getUserData().toString().contains("EnemyB")) {
                        //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                        if (((AiObjectDef) fixA.getUserData()).getObjectIdentity().equals("AI_BODY_CHANGE")) { // GAME_ENEMY_A_REVERS_V old

                            if( ((GameAIObject) fixA.getUserData()).getMapMarkerType().equals("Velocity") ) {

                                //((EnemyB) fixB.getUserData()).reverseVelocity(true, false); //don't work at all


                                if(((EnemyB) fixB.getUserData()).getIsRunningRight()) {

                                    ((EnemyB) fixB.getUserData()).setIsRunningRight(false); // turn left

                                }else{

                                    ((EnemyB) fixB.getUserData()).setIsRunningRight(true); // turn right

                                }

                                /* with this ignored ai change !!!
                                if(((EnemyB) fixB.getUserData()).getIsRunningRight()) {
                                    ((EnemyB) fixB.getUserData()).setIsRunningRight(false);

                                    ((EnemyB) fixB.getUserData()).setFlyingDirectionRight(false);
                                    ((EnemyB) fixB.getUserData()).setFlyingDirectionLeft(true);

                                }
                                */
/*
                                if(!((EnemyB) fixB.getUserData()).getIsRunningRight()) {
                                    ((EnemyB) fixB.getUserData()).b2body.setLinearVelocity(new Vector2(1.2f, -0.4f));
                                    System.out.println("Contact EnemyB vel 1.2f -0.4f");
                                    //((EnemyB) fixB.getUserData()).reverseVelocity(true, false);
                                }else{
                                    ((EnemyB) fixB.getUserData()).b2body.setLinearVelocity(new Vector2(-1.2f, -0.4f));
                                    System.out.println("Contact EnemyB vel -1.2f -0.4f");
                                }
*/

                                //System.out.println("EnemyB should change direction!!!");

                            }else if( ((GameAIObject) fixA.getUserData()).getMapMarkerType().equals("FacingDirection") ) {

                                if(((EnemyB) fixB.getUserData()).getIsRunningRight()){
                                    //System.out.println("EnemyA is running Right: ");
                                    ((EnemyB) fixB.getUserData()).setLinearVelocity();
                                    ((EnemyB) fixB.getUserData()).setIsRunningRight(false);
                                }else{
                                    //System.out.println("EnemyA is running Left: ");
                                    ((EnemyB) fixB.getUserData()).setLinearVelocity();
                                    ((EnemyB) fixB.getUserData()).setIsRunningRight(true);
                                }
                            }
                        }
                    }

                    /** EnemyKnightDevil Boss */
                    if(fixB.getUserData().toString().contains("EnemyKnightDevil")) {

                        //System.out.println(" Identity: " + ((AiObjectDef) fixA.getUserData()).getObjectIdentity() );

                        if (((AiObjectDef) fixA.getUserData()).getObjectIdentity().equals("AI_JUMP")) {

                            if (((GameAIObject) fixA.getUserData()).getMapMarkerType().equals("Long")) {
                                ((EnemyKnightDevil) fixB.getUserData()).setJumpLongBool();
                            }
                            if (((GameAIObject) fixA.getUserData()).getMapMarkerType().equals("Short")) {
                                ((EnemyKnightDevil) fixB.getUserData()).setJumpShortBool();
                            }
                        }

                        if (((AiObjectDef) fixA.getUserData()).getObjectIdentity().equals("AI_BODY_CHANGE")) { // GAME_ENEMY_A_REVERS_V old

                            if (((GameAIObject) fixA.getUserData()).getMapMarkerType().equals("Velocity")) {

                                //((EnemyKnightDevil) fixB.getUserData()).reverseVelocity(true, false);
                                //System.out.println("EnemyKnightDevil changing Velocity: ");

                            } else if (((GameAIObject) fixA.getUserData()).getMapMarkerType().equals("FacingDirection")) {

                                if (((EnemyKnightDevil) fixB.getUserData()).getIsRunningRight()) {
                                    //System.out.println("EnemyKnightDevil is running Right: ");
                                    ((EnemyKnightDevil) fixB.getUserData()).b2body.setLinearVelocity(0f, 0f);
                                    ((EnemyKnightDevil) fixB.getUserData()).setIsRunningRight(false);
                                } else {
                                    //System.out.println("EnemyKnightDevil is running Left: ");
                                    ((EnemyKnightDevil) fixB.getUserData()).b2body.setLinearVelocity(0f, 0f);
                                    ((EnemyKnightDevil) fixB.getUserData()).setIsRunningRight(true);
                                }
                            }
                        }

                        if (((AiObjectDef) fixA.getUserData()).getObjectIdentity().equals("AI_STEERING")) {
                            if (((GameAIObject) fixA.getUserData()).getMapMarkerType().equals("Dash")) {
                                ((EnemyKnightDevil) fixB.getUserData()).setDashAssociationNumber(
                                        ((GameAIObject) fixA.getUserData()).getMapMarkerAssociationNumber());

                                //System.out.println("Collide Dash nr: " + ((GameAIObject) fixA.getUserData()).getMapMarkerAssociationNumber() );
                            }
                        }


                        if (((AiObjectDef) fixA.getUserData()).getObjectIdentity().equals("GAME_AI_MARKER")) {

                            //System.out.println(" Identity: " + ((AiObjectDef) fixA.getUserData()).getObjectIdentity() );
                            //System.out.println("type: " + ((GameAIObject) fixA.getUserData()).getMapMarkerType() ) ;
                            //System.out.println("Association Number: " + ((GameAIObject) fixA.getUserData()).getMapMarkerAssociationNumber() ) ;

                            //System.out.println("Collide -GAME_AI_MARKER- running: " + ((EnemyKnightDevil) fixB.getUserData()).getIsRunningRight() );
                            //System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                        }


                        //if (((AiObjectDef) fixA.getUserData()).getObjectIdentity().equals("GAME_ENEMY_A_REVERS_V")) { old
                        //if (((AiObjectDef) fixA.getUserData()).getObjectIdentity().equals("AI_BODY_CHANGE")) {
                        //((EnemyKnightDevil) fixB.getUserData()).reverseVelocity(true, false);

                        //System.out.println("Collide -GAME_ENEMY_A_REVERS_V- running: " + ((EnemyKnightDevil) fixB.getUserData()).getIsRunningRight() );
                            /*
                            if( !((EnemyKnightDevil) fixB.getUserData()).getIsRunningRight()){
                                ((EnemyKnightDevil) fixB.getUserData()).setIsRunningRight(true);
                            }
                            */
                        //}

                    }

                } // End of if fixA.getFilterData()

            /** Case fixB */
                if (fixB.getFilterData().categoryBits == GameUtility.GAME_AI_OBJECT_BIT) {
                    System.out.println("WorldContactListener Class : fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );

                    /** EnemyA */
                    if(fixA.getUserData().toString().contains("EnemyA")) {
                        if (((AiObjectDef) fixB.getUserData()).getObjectIdentity().equals("AI_BODY_CHANGE")) { // GAME_ENEMY_A_REVERS_V old

                            if( ((GameAIObject) fixB.getUserData()).getMapMarkerType().equals("Velocity") ) {
                                ((EnemyA) fixA.getUserData()).reverseVelocity(true, false);
                            }else if( ((GameAIObject) fixB.getUserData()).getMapMarkerType().equals("FacingDirection") ) {

                                if(((EnemyA) fixA.getUserData()).getIsRunningRight()){
                                    //System.out.println("EnemyA is running Right: ");
                                    ((EnemyA) fixA.getUserData()).setLinearVelocity();
                                    ((EnemyA) fixA.getUserData()).setIsRunningRight(false);
                                }else{
                                    //System.out.println("EnemyA is running Left: ");
                                    ((EnemyA) fixA.getUserData()).setLinearVelocity();
                                    ((EnemyA) fixA.getUserData()).setIsRunningRight(true);
                                }
                            }
                        }
                    }
                    /** EnemyB */
                    if(fixA.getUserData().toString().contains("EnemyB")) {
                        if (((AiObjectDef) fixB.getUserData()).getObjectIdentity().equals("AI_BODY_CHANGE")) { // GAME_ENEMY_A_REVERS_V old

                            if( ((GameAIObject) fixB.getUserData()).getMapMarkerType().equals("Velocity") ) {

                                //((EnemyB) fixA.getUserData()).reverseVelocity(true, false);
                                //System.out.println("fixA");
                                if(((EnemyB) fixA.getUserData()).getIsRunningRight()) {

                                    ((EnemyB) fixA.getUserData()).setIsRunningRight(false); // turn left

                                }else{

                                    ((EnemyB) fixA.getUserData()).setIsRunningRight(true); // turn right

                                }

                            }else if( ((GameAIObject) fixB.getUserData()).getMapMarkerType().equals("FacingDirection") ) {

                                if(((EnemyB) fixA.getUserData()).getIsRunningRight()){
                                    //System.out.println("EnemyA is running Right: ");
                                    ((EnemyB) fixA.getUserData()).setLinearVelocity();
                                    ((EnemyB) fixA.getUserData()).setIsRunningRight(false);
                                }else{
                                    //System.out.println("EnemyA is running Left: ");
                                    ((EnemyB) fixA.getUserData()).setLinearVelocity();
                                    ((EnemyB) fixA.getUserData()).setIsRunningRight(true);
                                }
                            }
                        }
                    }
                    /** EnemyKnightDevil Boss */
                    //ToDo:

                } // End of if fixB.getFilterData()
                break;
            // SmallEnemyDef is on the Ground!! Boss Devil Knight for now!!
            case GameUtility.ENEMY_LEGS_BIT | GameUtility.GROUND_BIT:
               // System.out.println("WorldContactListener Class Begin Contact: fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );


                if (fixA.getFilterData().categoryBits == GameUtility.GROUND_BIT) {
                    //((InteractiveTileObject) fixB.getUserData()).onSavePointHit((BubblePlayer) fixA.getUserData());
                    //BubblePlayer.playerLeftGround();
                    //((EnemyKnightDevil) fixB.getUserData()).setEnemyOnGround();

                    if(fixB.getUserData().toString().contains("EnemyKnightDevil")) {
                        ((EnemyKnightDevil) fixB.getUserData()).setEnemyOnGround(true);
                    }



                    //System.out.println("EnemyBoss is on the floor!!!");
                }
                break;

            /** All Items with in the game collision  */
            case GameUtility.PLAYER_BIT | GameUtility.GAME_ITEM_BIT:
                // ToDo : if we need extra life bars, remove hardcoding >= 5 || need to destroy pick up life nr. 4
                //System.out.println("WorldContactListener Class Begin Contact: fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );

                /** -FIX A- EX_LIFE */
                if (fixA.getUserData().toString().contains("ExtraLifeGameItem")){
                    if( !(((BubblePlayer) fixB.getUserData()).updatePlayerLifeToHudAndSaveOnExit() >= 4 )){

                        ((BubblePlayer) fixB.getUserData()).setExtraLife();

                        gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpItemSetItemUsedWorldContactListener(gameManagerAssetsInstance.getCurrentWorld(),
                                gameManagerAssetsInstance.getCurrentLevel(), "EX_LIFE", ((ExtraLifeGameItem) fixA.getUserData()).getItemSpawnIDorEnemyDeadID() );
                        gameManagerAssetsInstance.readFromSaveGameWorld();
                        ((ExtraLifeGameItem) fixA.getUserData()).setToDestroy();
                        //GameManagerAssets.instance.gameManagerWorldDataWriteToFile(); // do this on SavePoint action instead.
                        gameManagerAIInstance.setPlayerPowerUpTextToScreen("Extra life",((BubblePlayer) fixB.getUserData()).getPlayerPosition(), 0, 50);
                    }
                }
                /** -FIX B- EX_LIFE */
                if (fixB.getUserData().toString().contains("ExtraLifeGameItem")){
                    if( !(((BubblePlayer) fixA.getUserData()).updatePlayerLifeToHudAndSaveOnExit() >= 4 )){
                        ((BubblePlayer) fixA.getUserData()).setExtraLife();
                        gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpItemSetItemUsedWorldContactListener(gameManagerAssetsInstance.getCurrentWorld(),
                                gameManagerAssetsInstance.getCurrentLevel(), "EX_LIFE", ((ExtraLifeGameItem) fixB.getUserData()).getItemSpawnIDorEnemyDeadID() );
                        ((ExtraLifeGameItem) fixB.getUserData()).setToDestroy();
                        //GameManagerAssets.instance.gameManagerWorldDataWriteToFile(); // do this on SavePoint action instead.
                        gameManagerAIInstance.setPlayerPowerUpTextToScreen("Extra life",((BubblePlayer) fixA.getUserData()).getPlayerPosition(), 0, 50);
                    }
                }


                /** -FIX A- POWER_UP */
                if (fixA.getUserData().toString().contains("ItemPowerUp")){


                    switch ( ((ItemPowerUp) fixA.getUserData()).getItemPowerUpTypOfPower() ){

                        case "WALL JUMPING":
                            ((BubblePlayer) fixB.getUserData()).setPlayerPowerUp("WALL JUMPING");
                            gameManagerAIInstance.setPlayerPowerUpTextToScreen("You pick up A new Skill",((BubblePlayer) fixB.getUserData()).getPlayerPosition(), 0, 50);
                            break;
                        case "DASH":
                            ((BubblePlayer) fixB.getUserData()).setPlayerPowerUp("DASH");
                            gameManagerAIInstance.setPlayerPowerUpTextToScreen("You pick up A new Skill",((BubblePlayer) fixB.getUserData()).getPlayerPosition(), 0, 50);
                            break;
                        case "WEAPON BLUE":
                            ((BubblePlayer) fixB.getUserData()).setPlayerPowerUp("WEAPON BLUE");
                            //System.out.println("Position pick up: " + ((BubblePlayer) fixB.getUserData()).b2body.getPosition() );

                            gameManagerAIInstance.setPlayerPowerUpTextToScreen("You pick up A new Weapon",((BubblePlayer) fixB.getUserData()).getPlayerPosition(), 0, 50);
/*
                            gameController.initCreateFloatingText("Blue Weapon pick up",
                                    ((ItemPowerUp) fixA.getUserData()).b2body.getPosition().x,
                                    ((ItemPowerUp) fixA.getUserData()).b2body.getPosition().y,
                                    200);
*/
                            /*
                            gameController.initCreateFloatingText("Blue Weapon pick up",
                                    ((BubblePlayer) fixB.getUserData()).b2body.getPosition().x,
                                    ((BubblePlayer) fixB.getUserData()).b2body.getPosition().y,
                                    200);
                            */
                            break;
                    }

                    /** checks power item - sett's it used -true- */
                    gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpItemSetItemUsedWorldContactListener(gameManagerAssetsInstance.getCurrentWorld(),
                            gameManagerAssetsInstance.getCurrentLevel(), "POWER_UP", ((ItemPowerUp) fixA.getUserData()).getObjectID() );


                    /** set's Player's power's */
                    gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().setPlayerPowerTree(
                            ((ItemPowerUp) fixA.getUserData()).getItemPowerUpTypOfPower() ) ;

                    /** delete's player save file, save's a new one with Player Power */
                    gameManagerAssetsInstance.gameManagerPlayerDataWriteToFile();

                    // test
                    gameManagerAssetsInstance.gameManagerWorldDataWriteToFile();

                    /** set item to destroy */
                    ((ItemPowerUp) fixA.getUserData()).setToDestroy();

/** Debug */
gameManagerAssetsInstance.readFromSaveGameWorld();
gameManagerAssetsInstance.readFromSaveGamePlayer();

                }

                /** -FIX B- POWER_UP */
                if (fixB.getUserData().toString().contains("ItemPowerUp")){

                    switch ( ((ItemPowerUp) fixB.getUserData()).getItemPowerUpTypOfPower() ){

                        case "WALL JUMPING":
                            ((BubblePlayer) fixA.getUserData()).setPlayerPowerUp("WALL JUMPING");
                            gameManagerAIInstance.setPlayerPowerUpTextToScreen("You pick up A new Skill",((BubblePlayer) fixA.getUserData()).getPlayerPosition(), 0, 50);
                            break;
                        case "DASH":
                            ((BubblePlayer) fixA.getUserData()).setPlayerPowerUp("DASH");
                            gameManagerAIInstance.setPlayerPowerUpTextToScreen("You pick up A new Skill",((BubblePlayer) fixA.getUserData()).getPlayerPosition(), 0, 50);
                            break;
                         case "WEAPON BLUE":
                              ((BubblePlayer) fixA.getUserData()).setPlayerPowerUp("WEAPON BLUE");
                             gameManagerAIInstance.setPlayerPowerUpTextToScreen("You pick up A new Weapon",((BubblePlayer) fixA.getUserData()).getPlayerPosition(), 0, 50);
                              break;
                    }
                    /** checks power item - sett's it used -true- */
                    gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpItemSetItemUsedWorldContactListener(gameManagerAssetsInstance.getCurrentWorld(),
                            gameManagerAssetsInstance.getCurrentLevel(), "POWER_UP", ((ItemPowerUp) fixA.getUserData()).getObjectID() );

                    /** set's Player's power's */
                    gameManagerAssetsInstance.getSaveGamePlayerDataHolderClass().setPlayerPowerTree(
                            ((ItemPowerUp) fixB.getUserData()).getItemPowerUpTypOfPower() ) ;

                    /** delete's player save file, save's a new one with Player Power */
                    gameManagerAssetsInstance.gameManagerPlayerDataWriteToFile();

                    // test
                    gameManagerAssetsInstance.gameManagerWorldDataWriteToFile();

                    /** set item to destroy */
                    ((ItemPowerUp) fixB.getUserData()).setToDestroy();

 /** Debug */
gameManagerAssetsInstance.readFromSaveGameWorld();
gameManagerAssetsInstance.readFromSaveGamePlayer();

                }


                /** BLUE AMO */

                /** -FIX A- DRAGON EGG */
                if (fixA.getUserData().toString().contains("DragonEggGameItem")){
                    if(((DragonEggGameItem) fixA.getUserData()).getEggCoolor().equals("BLUE")){
                        ((BubblePlayer) fixB.getUserData()).addBallooneBulletBlue();
                        System.out.println("ContactListener Dragon Egg Blue fixA");
                        gameManagerAIInstance.setPlayerPowerUpTextToScreen("Amo",((BubblePlayer) fixB.getUserData()).getPlayerPosition() , 0 , 100 );
                    }
                    ((DragonEggGameItem) fixA.getUserData()).setToDestroy();

                }
                /** -FIX B- DRAGON EGG */
                if (fixB.getUserData().toString().contains("DragonEggGameItem")){

                    if(((DragonEggGameItem) fixB.getUserData()).getEggCoolor().equals("BLUE")){
                        ((BubblePlayer) fixA.getUserData()).addBallooneBulletBlue();
                        System.out.println("ContactListener Dragon Egg Blue fixB");
                        gameManagerAIInstance.setPlayerPowerUpTextToScreen("Amo",((BubblePlayer) fixA.getUserData()).getPlayerPosition() , 0 , 100 );
                    }
                    ((DragonEggGameItem) fixB.getUserData()).setToDestroy();

                }

                /** RED AMO */

                /** -FIX A- DRAGON EGG */
                if (fixA.getUserData().toString().contains("DragonEggGameItem")){
                    if(((DragonEggGameItem) fixA.getUserData()).getEggCoolor().equals("RED")){
                        ((BubblePlayer) fixB.getUserData()).addBallooneBulletRed();
                        gameManagerAIInstance.setPlayerPowerUpTextToScreen("Amo",((BubblePlayer) fixB.getUserData()).getPlayerPosition() , 0 , 100 );
                    }
                    ((DragonEggGameItem) fixA.getUserData()).setToDestroy();
                }
                /** -FIX B- DRAGON EGG */
                if (fixB.getUserData().toString().contains("DragonEggGameItem")){
                    if(((DragonEggGameItem) fixB.getUserData()).getEggCoolor().equals("RED")){
                        ((BubblePlayer) fixA.getUserData()).addBallooneBulletRed();
                        gameManagerAIInstance.setPlayerPowerUpTextToScreen("Amo",((BubblePlayer) fixA.getUserData()).getPlayerPosition() , 0 , 100 );
                    }
                    ((DragonEggGameItem) fixB.getUserData()).setToDestroy();
                }

                /** BLACK AMO */

                /** -FIX A- DRAGON EGG */
                if (fixA.getUserData().toString().contains("DragonEggGameItem")){
                    if(((DragonEggGameItem) fixA.getUserData()).getEggCoolor().equals("BLACK")){
                        ((BubblePlayer) fixB.getUserData()).addBallooneBulletBlack();
                        gameManagerAIInstance.setPlayerPowerUpTextToScreen("Amo",((BubblePlayer) fixB.getUserData()).getPlayerPosition() , 0 , 100 );
                    }
                    ((DragonEggGameItem) fixA.getUserData()).setToDestroy();
                }
                /** -FIX B- DRAGON EGG */
                if (fixB.getUserData().toString().contains("DragonEggGameItem")){
                    if(((DragonEggGameItem) fixB.getUserData()).getEggCoolor().equals("BLACK")){
                        ((BubblePlayer) fixA.getUserData()).addBallooneBulletBlack();
                        gameManagerAIInstance.setPlayerPowerUpTextToScreen("Amo",((BubblePlayer) fixA.getUserData()).getPlayerPosition() , 0 , 100 );
                    }
                    ((DragonEggGameItem) fixB.getUserData()).setToDestroy();
                }



                /** -FIX A- CHEST  */
                if (fixA.getUserData().toString().contains("TreasureChestGameItem")){
                    ((TreasureChestGameItem) fixA.getUserData()).setChangeTexture();
                    gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpItemSetItemUsedWorldContactListener(gameManagerAssetsInstance.getCurrentWorld(),
                            gameManagerAssetsInstance.getCurrentLevel(), "TREASURE_CHEST", ((TreasureChestGameItem) fixA.getUserData()).getObjectID() );
                }

                /** -FIX B- CHEST */
                if (fixB.getUserData().toString().contains("TreasureChestGameItem")){
                    ((TreasureChestGameItem) fixB.getUserData()).setChangeTexture();
                    gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpItemSetItemUsedWorldContactListener(gameManagerAssetsInstance.getCurrentWorld(),
                            gameManagerAssetsInstance.getCurrentLevel(), "TREASURE_CHEST", ((TreasureChestGameItem) fixA.getUserData()).getObjectID() );
                }

                break;

            /** EnemyA hit one and other reverse Velocity */

            case GameUtility.ENEMY_BIT | GameUtility.ENEMY_BIT:
                //System.out.println("WorldContactListener Class Begin Contact: fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );

                if(fixA.getUserData().toString().contains("EnemyA") && fixB.getUserData().toString().contains("EnemyA")){

                    //((SmallEnemyDef)fixA.getUserData()).reverseVelocity(true,false);
                    //((SmallEnemyDef)fixB.getUserData()).reverseVelocity(true,false);


                }

                break;
//ToDo:: check if we want EnemyB to collide with Walls and Sealing !!!!
            /** Change Velocity when enemy -SmallEnemyDef (EnemyA) hits wall's and Sealing Bit*/
            case GameUtility.ENEMY_BIT | GameUtility.WALL_AND_SEALING_BIT:
                //System.out.println("WorldContactListener Class Begin Contact: fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                //if (fixA.getFilterData().groupIndex == GameUtility.ENEMY_BIT && fixB.getFilterData().groupIndex == GameUtility.WALL_AND_SEALING_BIT) {
                if (fixA.getFilterData().categoryBits == GameUtility.ENEMY_BIT && fixB.getFilterData().groupIndex == GameUtility.WALL_AND_SEALING_BIT) {

                    if( fixA.getUserData().toString().contains("EnemyA")) {
                        ((SmallEnemyDef) fixA.getUserData()).reverseVelocity(true, false);
                    }
                    /** Not sure EnemyB should collide with Walls and Sealing */
                    if( fixA.getUserData().toString().contains("EnemyB")) {
                        if(((EnemyB) fixA.getUserData()).getIsRunningRight()) {
                            ((EnemyB) fixA.getUserData()).setIsRunningRight(false); // turn left
                        }else{
                            ((EnemyB) fixA.getUserData()).setIsRunningRight(true); // turn right
                        }
                    }
                }

                //if (fixB.getFilterData().groupIndex == GameUtility.ENEMY_BIT && fixA.getFilterData().groupIndex == GameUtility.WALL_AND_SEALING_BIT) {
                if (fixB.getFilterData().categoryBits == GameUtility.ENEMY_BIT && fixA.getFilterData().groupIndex == GameUtility.WALL_AND_SEALING_BIT) {

                    if(fixB.getUserData().toString().contains("EnemyA")) {
                        ((SmallEnemyDef) fixB.getUserData()).reverseVelocity(true, false);
                    }
                    /** Not sure EnemyB should collide with Walls and Sealing */
                    if(fixB.getUserData().toString().contains("EnemyB")) {
                        if(((EnemyB) fixB.getUserData()).getIsRunningRight()) {
                            ((EnemyB) fixB.getUserData()).setIsRunningRight(false); // turn left
                        }else{
                            ((EnemyB) fixB.getUserData()).setIsRunningRight(true); // turn right
                        }
                    }
                }
                break;
            case GameUtility.ENEMY_POWER_BIT | GameUtility.GROUND_BIT:
                //System.out.println("WorldContactListener Class Begin Contact: fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                break;
/*
            case GameUtility.ENEMY_BIT | GameUtility.GROUND_BIT:

                System.out.println("WorldContactListener Class Begin Contact: fixA : " +
                        fixA.getUserData() + "fiA.Bit: " + fixA.getFilterData().categoryBits +
                        " fixB " + fixB.getUserData() + " fixB.Bit: " + fixB.getFilterData().categoryBits );

                if( fixA.getFilterData().groupIndex == GameUtility.GROUND_BIT ){

                }

                break;
*/
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();


        int checkEndContactGroundAndWall = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (checkEndContactGroundAndWall){

            case GameUtility.PLAYER_BIT | GameUtility.GROUND_BIT:
                /*
                System.out.println("WorldContactListener Class Ground Player Contact: fixA : " +
                        fixA.getUserData() + " fixB " + fixB.getUserData() );

                System.out.println("WorldContactListener Class Ground Player Contact: fixA : " +
                        fixA.getFilterData().groupIndex + " fixB " + fixB.getFilterData().groupIndex );
                */
                /** Player fixA  | Ground fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.PLAYER_BIT && fixB.getFilterData().groupIndex == GameUtility.GROUND_BIT ) {
                    //System.out.println("WorldContactListener Class Player Left ground!!");
                    ((BubblePlayer) fixA.getUserData()).setPlayerOnGroundFalse();
                }

                /** Ground fixA  | Player fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.GROUND_BIT && fixB.getFilterData().groupIndex == GameUtility.PLAYER_BIT ) {
                    //System.out.println("WorldContactListener Class Player Left ground!!");
                    ((BubblePlayer) fixB.getUserData()).setPlayerOnGroundFalse();
                }
                break;

            case GameUtility.PLAYER_BIT | GameUtility.WALL_JUMPING_BIT:

                /** Player fixA  | Wall fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.PLAYER_BIT && fixB.getFilterData().groupIndex == GameUtility.WALL_JUMPING_BIT) {
                    System.out.println("WorldContactListener Class Player Left wall!!");
                    ((BubblePlayer) fixB.getUserData()).setPlayerWallJumpFalse();
                }

                /** Wall fixA  | Player fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.WALL_JUMPING_BIT && fixB.getFilterData().groupIndex == GameUtility.PLAYER_BIT ) {
                    System.out.println("WorldContactListener Class Player Left Wall!!");
                    ((BubblePlayer) fixB.getUserData()).setPlayerWallJumpFalse();
                }
                break;
            case GameUtility.ENEMY_BIT | GameUtility.GROUND_BIT:

                /** SmallEnemyDef fixA  | Ground fixB*/
                //if (fixA.getFilterData().groupIndex == GameUtility.ENEMY_BIT && fixB.getFilterData().groupIndex == GameUtility.GROUND_BIT ) {
                if (fixA.getFilterData().categoryBits == GameUtility.ENEMY_BIT && fixB.getFilterData().groupIndex == GameUtility.GROUND_BIT ) {
                    //System.out.println("WorldContactListener Class SmallEnemyDef left ground!!");

                    if(fixA.getUserData().toString().contains("EnemyA")){
                        ((EnemyA)fixA.getUserData()).setEnemyLeftGround();
                    }
                }

                /** Ground fixA  | SmallEnemyDef fixB*/
                //if (fixA.getFilterData().groupIndex == GameUtility.GROUND_BIT && fixB.getFilterData().groupIndex == GameUtility.ENEMY_BIT ) {
                if (fixA.getFilterData().groupIndex == GameUtility.GROUND_BIT && fixB.getFilterData().categoryBits == GameUtility.ENEMY_BIT ) {
                    //System.out.println("WorldContactListener Class SmallEnemyDef left ground!!");

                    if(fixB.getUserData().toString().contains("EnemyA")){
                        ((EnemyA)fixB.getUserData()).setEnemyLeftGround();
                    }

                }
                break;

            case GameUtility.ENEMY_BIT | GameUtility.WALL_JUMPING_BIT:

                /** SmallEnemyDef fixA  | Wall fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.ENEMY_BIT && fixB.getFilterData().groupIndex == GameUtility.WALL_JUMPING_BIT) {
                    //System.out.println("WorldContactListener Class SmallEnemyDef left wall!!");

                    if(fixA.getUserData().toString().contains("EnemyA")){
                        ((EnemyA)fixA.getUserData()).setEnemyLeftWall();
                    }

                }

                /** Wall fixA  | SmallEnemyDef fixB*/
                if (fixA.getFilterData().groupIndex == GameUtility.WALL_JUMPING_BIT && fixB.getFilterData().groupIndex == GameUtility.ENEMY_BIT ) {
                    //System.out.println("WorldContactListener Class SmallEnemyDef left Wall!!");

                    if(fixB.getUserData().toString().contains("EnemyA")){
                        ((EnemyA)fixB.getUserData()).setEnemyLeftWall();
                    }

                }
                break;
        }




        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case GameUtility.ENEMY_RANGE_ATTACK_BIT | GameUtility.PLAYER_BIT:
                //enemy_range_attack_bit_Timer += GameManagerAITimer.getCurrentTime();
                //System.out.println("WorldContactListener Enemy_Range_attack_bit timer: " + enemy_range_attack_bit_Timer);
                if (fixA.getFilterData().categoryBits == GameUtility.ENEMY_RANGE_ATTACK_BIT) {

                    ((EnemyKnightDevil) fixA.getUserData()).setRangeAttackActiveBool(false);

                }
                break;

            case GameUtility.ENEMY_CLOSE_ATTACK_BIT | GameUtility.PLAYER_BIT:
                //System.out.println(" updateAllSpawnLifeFromEnemy !! ");
                if(fixA.getFilterData().categoryBits == GameUtility.ENEMY_CLOSE_ATTACK_BIT) {
                    //System.out.println("fixA : " + fixA.getUserData() );// .getFilterData().categoryBits);

                    if(fixA.getUserData().equals( (EnemyKnightDevil)fixA.getUserData() )) {
                        //System.out.println("true Knight fixA - left fight close range");
                        //((EnemyKnightDevil) fixA.getUserData()).closeAttackEnd((EnemyKnightDevil) fixA.getUserData());
                        //((EnemyKnightDevil)fixA.getUserData()).reverseVelocity(true, false);
//                        ((EnemyKnightDevil)fixA.getUserData()).closeAttack((EnemyKnightDevil) fixA.getUserData());
                    }

                }else{
                    //System.out.println("fixB : " + fixB.getUserData() );
                    if(fixB.getUserData().equals( (EnemyKnightDevil)fixB.getUserData() )) {
                       // System.out.println("true Knight fixB - left fight close range");
                        //((EnemyKnightDevil)fixB.getUserData()).reverseVelocity(true, false);
//                       ((EnemyKnightDevil)fixB.getUserData()).closeAttack((EnemyKnightDevil) fixB.getUserData());
                    }

                }
                break;

            case GameUtility.ENEMY_BIT | GameUtility.NOTHING_BIT:


                // OBJECT_BIT TEST = GameMarker casting
                // ENEMY_ENEMYKNIGHTDEVIL_BIT = EnemyKnightDevil casting
                if(fixA.getFilterData().categoryBits == GameUtility.NOTHING_BIT) {
/*
                    if( ((GameMarker)fixA.getUserData()).getMarkerID().equals("MarkStartMove") ) {

                        //((EnemyKnightDevil)fixB.getUserData()).setPatroll_Marker_Start_False();
                    }

                    if( ((GameMarker)fixA.getUserData()).getMarkerID().equals("MarkStartLeft") ) {

                        //((EnemyKnightDevil)fixB.getUserData()).setPatroll_Marker_Left_False();
                    }

                    if( ((GameMarker)fixA.getUserData()).getMarkerID().equals("MarkStartRight") ) {

                        //((EnemyKnightDevil)fixB.getUserData()).setPatroll_Marker_Right_False();
                    }
*/
                }
/*
            case GameUtility.BALLONE_HEAD_BIT | GameUtility.PLAYER_BIT: // contact 1
                //case Utility.PLAYER_POWER_BIT | Utility.PLAYER_BIT: // contact 2
                //System.out.println("BALLON_HEAD_BIT | MARIO_BIT : " + Utility.BALLONE_HEAD_BIT + " : " + Utility.PLAYER_BIT );
                if (fixA.getFilterData().categoryBits == GameUtility.BALLONE_HEAD_BIT) {
                    //((BalloneBullet)fixA.getUserData()).endhitOnHead((BubblePlayer) fixB.getUserData());
                    //System.out.println("End Contact 1"); // BALLONE_HEAD_BIT | PLAYER_BIT
                } else {
                    //    ((BalloneBullet)fixB.getUserData()).hitOnHead((BubblePlayer) fixA.getUserData());
                    //System.out.println("End Contact 2"); // PLAYER_POWER_BIT | PALAYER_BIT - RIGHT AFTER SHOOTING!!
                }
                break;
*/
            //ok
            case GameUtility.PLAYER_BIT | GameUtility.GROUND_BIT:
                //System.out.println("WorldContactListener Class End Contact: fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                if (fixA.getFilterData().categoryBits == GameUtility.GROUND_BIT)
                    //((InteractiveTileObject) fixB.getUserData()).onSavePointHit((BubblePlayer) fixA.getUserData());
                    //System.out.println("WorldContactListener Class End Contact: fixA : " + fixA.getUserData() );
                    //BubblePlayer.playerLeftGround();


                //System.out.println("Contact player and ground!!");
                break;
            case GameUtility.PLAYER_BIT | GameUtility.WALL_JUMPING_BIT:
                //System.out.println("WorldContactListener Class End Contact: fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );
                if (fixA.getFilterData().categoryBits == GameUtility.WALL_JUMPING_BIT) {
                    //((InteractiveTileObject) fixB.getUserData()).onSavePointHit((BubblePlayer) fixA.getUserData());
                    //System.out.println("WorldContactListener Class : WallJump True Contact End");
                    //((BubblePlayer) fixB.getUserData()).setPlayerWallJumpFalse(); // moved sliding down spikes get stuck in this mode !!
                }

                //System.out.println("Contact player and ground!!");
                break;
            case GameUtility.ENEMY_LEGS_BIT | GameUtility.GROUND_BIT:
                //System.out.println("WorldContactListener Class End Contact: fixA : " + fixA.getUserData() + " fixB " + fixB.getUserData() );


                if (fixA.getFilterData().categoryBits == GameUtility.GROUND_BIT) {
                    //((InteractiveTileObject) fixB.getUserData()).onSavePointHit((BubblePlayer) fixA.getUserData());
                    //BubblePlayer.playerLeftGround();
                    if(fixB.getUserData().toString().contains("EnemyKnightDevil")) {
                        ((EnemyKnightDevil) fixB.getUserData()).setEnemyOnGround(false);
                    }
                    //System.out.println("EnemyBoss left the floor!!!");
                }
                break;

            case GameUtility.ENEMY_BIT | GameUtility.GROUND_BIT:
                //if(fixA == null || fixB == null) {
                /*
                System.out.println("WorldContactListener Class End Contact: fixA : " +
                            fixA.getUserData() + "fiA.Bit: " + fixA.getFilterData().categoryBits +
                            " fixB " + fixB.getUserData() + " fixB.Bit: " + fixB.getFilterData().categoryBits );
                */
                //}

                /*
                if( fixA.getFilterData().groupIndex == GameUtility.ENEMY_LEGS_BIT  ) {
                    System.out.println("groupIndex == legg bit");
                }

                if(fixA != null && fixA.getUserData() != null && fixB != null && fixB.getUserData() != null ){

                    if( fixA.getFilterData().categoryBits == GameUtility.GROUND_BIT ) {

                        if (!fixA.getUserData().toString().contains("GameAIObject") && !fixB.getUserData().toString().contains("GameAIObject")) {
                            if (fixB.getUserData().toString().contains("EnemyA")) {
                                System.out.println("FixA = Ground, FixB = EnemyA -> left Ground!!");
                            }
                        }
                    }

                    if( fixB.getFilterData().categoryBits == GameUtility.GROUND_BIT ) {
                        if (!fixA.getUserData().toString().contains("GameAIObject") && !fixB.getUserData().toString().contains("GameAIObject")) {
                            if (fixA.getUserData().toString().contains("EnemyA")) {
                                System.out.println("FixB = Ground, FixA = EnemyA -> left Ground!!");
                            }
                        }
                    }

                }
                */

        }


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub

    }


}


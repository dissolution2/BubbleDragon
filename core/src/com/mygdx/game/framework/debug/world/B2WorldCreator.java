package com.mygdx.game.framework.debug.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.PlayScreen;
import com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy.BossEnemyDef;
import com.mygdx.game.framework.debug.sprites.Enemies.EnemyB;
import com.mygdx.game.framework.debug.sprites.Enemies.MovingFallingEnemy.EnemyStalactite;
import com.mygdx.game.framework.debug.sprites.Enemies.MovingFallingEnemy.MovingFallingEnemyDef;
import com.mygdx.game.framework.debug.sprites.Enemies.SmallEnemyDef;
import com.mygdx.game.framework.debug.sprites.Enemies.EnemyA;
import com.mygdx.game.framework.debug.sprites.Enemies.BossEnemy.EnemyKnightDevil;
import com.mygdx.game.framework.debug.sprites.Enemies.StationaryEnemies.EnemyGraphicSensor;
//import com.mygdx.game.framework.debug.sprites.Enemies.StationaryEnemies.EnemyVineThorns;
import com.mygdx.game.framework.debug.sprites.GraphicsAnimations.AnimationItemDef;
import com.mygdx.game.framework.debug.sprites.GraphicsAnimations.Grass;
import com.mygdx.game.framework.debug.sprites.items.ItemPowerUp;
import com.mygdx.game.framework.debug.sprites.items.PortalMapTransitionHidden;
import com.mygdx.game.framework.debug.sprites.items.TreasureChestGameItem;
import com.mygdx.game.framework.debug.sprites.items.DragonEggGameItem;
import com.mygdx.game.framework.debug.sprites.items.ExtraLifeGameItem;
import com.mygdx.game.framework.debug.sprites.items.Portal;
import com.mygdx.game.framework.debug.world.gameAiObjects.GameAIObject;
import com.mygdx.game.framework.debug.sprites.items.SavePoint;
import com.mygdx.game.framework.debug.util.GameUtility;
import com.mygdx.game.framework.debug.world.gameLightObjcets.GameLightObject;
import com.mygdx.game.framework.debug.world.gameObjects.GameObjectSwitchHidden;
import com.mygdx.game.framework.debug.world.gameObjects.ItemObjectDef;
import com.mygdx.game.framework.debug.world.gameObstacles.Obstacle;
import com.mygdx.game.framework.debug.world.gameObstacles.ObstacleDoor;
import com.mygdx.game.framework.debug.world.gameObjects.GameObjectSwitchDoor;
import com.mygdx.game.framework.debug.world.gamePfxObject.GamePfxObject;

import static com.mygdx.game.framework.debug.managers.GameManagerAI.rnRange;

public class B2WorldCreator {

    private MapObjects objects, objItems;
    public Array<Obstacle> obstacles, obstaclesWithNinePatch;
    public Array<Obstacle> activableObstacles;
    public Array<ObstacleDoor> obstacleDoors;
    //private Array<MapObject> pistons;
    private Array<EnemyA> enemyA;
    private Array<EnemyB> enemyB;
    private Array<EnemyGraphicSensor> enemyGraphicSensors;
    //private Array<EnemyVineThorns> trapVines;
    private Array<EnemyKnightDevil> enemyKnight;

    //private Array<EnemySpringRobot> enemySpringRobot;
    private Array<MapInfo> gameMapInfoObject;
    private Array<GameAIObject> gameAISteeringObjects;
    private Array<GameAIObject> gameAIBossSpawnObjects;
    private Array<GameAIObject> gameAIEnemyReversObjects;
    private Array<GameAIObject> gameAIPortalActivity;
    private Array<TravelSpawnPoint> gameTravelSpawnPointObjects;
    private Array<ExtraLifeGameItem> extraLife; /** All ItemGameObjectDef extended from */
    private Array<ItemPowerUp> playerPowerUp;
    private Array<ExtraLifeGameItem> spawnPoolWhenEnemyIsDead;
    private Array<DragonEggGameItem> extraDragonEgg;
    private Array<TreasureChestGameItem> extraTreasureChest;
    private Array<Portal> portals;
    private Array<PortalMapTransitionHidden> portalsHiddenMapTransition;
    private Array<SavePoint> savePoints;
    private Array<GameObjectSwitchDoor> gameObjectSwitches;
    private Array<GameObjectSwitchHidden> gameObjectSwitchesBossDead;
    private Array<GameObjectSwitchHidden> gameObjectSwitchesHidden;

    private Array<AnimationItemDef> gameObjectGraphicsAnimationItemFront;
    private Array<AnimationItemDef> gameObjectGraphicsAnimationItemBack;

    private Array<GamePfxObject> gamePfxObjects;
    private Array<GameLightObject> gameLightObjects;

    private Array<EnemyStalactite> enemyStalactite;


    private int id = 0;
    private String shouldCreateBoss;
    private String shouldBeCreatedItem;

    private GameManagerAssets gameManagerAssetsInstance;

   public B2WorldCreator(PlayScreen screen, GameManagerAssets instance) {

       this.gameManagerAssetsInstance = instance;
       World world = screen.getWorld();
       TiledMap map = screen.getMap();

       shouldCreateBoss = "true";
       shouldBeCreatedItem = "true";
       gameMapInfoObject = new Array<MapInfo>();
       gameAISteeringObjects = new Array<GameAIObject>();
       gameAIBossSpawnObjects = new Array<GameAIObject>();
       gameAIEnemyReversObjects = new Array<GameAIObject>();
       gameAIPortalActivity = new Array<GameAIObject>();
       gameTravelSpawnPointObjects = new Array<TravelSpawnPoint>();
       obstacles = new Array<Obstacle>(); // when we us the layer for more then one type off obstacles
       activableObstacles = new Array<Obstacle>();
       obstacleDoors = new Array<ObstacleDoor>();
       portals = new Array<Portal>();
       portalsHiddenMapTransition = new Array<PortalMapTransitionHidden>();
       savePoints = new Array<SavePoint>();
       gameObjectSwitches = new Array<GameObjectSwitchDoor>();
       /** One for every Boss, A big boss World might have sever all that we get keys to  |||| for doors and can open in eny order !!??*/
       gameObjectSwitchesBossDead = new Array<GameObjectSwitchHidden>();
       gameObjectSwitchesHidden = new Array<GameObjectSwitchHidden>();

       /** All ItemGameObjectDef extended from */
       extraLife = new Array<ExtraLifeGameItem>();
       playerPowerUp = new Array<ItemPowerUp>();
       extraDragonEgg = new Array<DragonEggGameItem>();
       extraTreasureChest = new Array<TreasureChestGameItem>();

       gameObjectGraphicsAnimationItemFront = new Array<AnimationItemDef>();
       gameObjectGraphicsAnimationItemBack = new Array<AnimationItemDef>();


       gamePfxObjects = new Array<GamePfxObject>();
       gameLightObjects = new Array<GameLightObject>();


       /** Spesial Enemy Stalg */
       enemyStalactite = new Array<EnemyStalactite>();

       /** Enemy's */
       enemyA = new Array<EnemyA>();
       enemyB = new Array<EnemyB>();
       enemyGraphicSensors = new Array<EnemyGraphicSensor>();
       //trapVines = new Array<EnemyVineThorns>();
       spawnPoolWhenEnemyIsDead = new Array<ExtraLifeGameItem>();
       enemyKnight = new Array<EnemyKnightDevil>();

       /** make the world.dat file and read it into class World */
       if (!gameManagerAssetsInstance.gameManagerSaveFileWorldExists()) {
           gameManagerAssetsInstance.gameManagerWorldDataReadWriteSave();
       }else{

           //System.out.println("BwWorldCreator Class - KnownWorld Class Size: " +
           //        GameManagerAssets.instance.getSaveGameWorldDataHolderClass().getKnownWorldsList().size() );

           if( gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().getKnownWorldsList().size() == 0 ) {
               gameManagerAssetsInstance.gameManagerWorldDataReadToWorldClassObject();
           }
       }
       /** -get info from "GAME_INFO_MAP" - set Current World & Level */
       /** -Add world & level to Game World Object Save Class */
       /** -get Map spawn point */
       for(MapObject object : map.getLayers().get("GAME_INFO_MAP").getObjects().getByType(RectangleMapObject.class)) {
           Rectangle rect = ((RectangleMapObject) object).getRectangle();

           if(object.getName().equals("MAP_INFO")) {

               if(object.getProperties().containsKey("KEY")){
                   System.out.println("BwWorldCreator Class \n- Map key is: " + object.getProperties().get("KEY").toString() );
                   gameManagerAssetsInstance.setCurrentWorld(object.getProperties().get("MAP INFO WORLD").toString());
                   gameManagerAssetsInstance.setCurrentLevel(object.getProperties().get("MAP INFO LEVEL").toString());


                   /** ands first entry to world */
                   gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().addWorldAndLevelsMapInfo(
                           object.getProperties().get("MAP INFO WORLD").toString(),
                           object.getProperties().get("MAP INFO LEVEL").toString(),
                           object.getProperties().get("MAP INFO BOSS").toString(),
                           object.getProperties().get("MAP INFO IS DEAD").toString()
                   );
               }
           }
           if(object.getName().equals("MAP_SPAWN_POINT")) {
                gameTravelSpawnPointObjects.add( new TravelSpawnPoint(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object ));
            }
       }

       /** Add Item's to Save World Object Class */
       for(MapObject object : map.getLayers().get("SPAWN_LAYER").getObjects().getByType(RectangleMapObject.class)) { // 10

           id = object.getProperties().get("id", int.class);
           gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().addWorldAndLevelsSpawnItem(
                   gameManagerAssetsInstance.getCurrentWorld(),
                   gameManagerAssetsInstance.getCurrentLevel(),
                   object.getName(), id, "false"
           );
       }

       /** Add Door Switch's Status and Key to Save World Object Class */
       for (MapObject object : map.getLayers().get("GAME_OBJECT_LAYER").getObjects().getByType(RectangleMapObject.class)) {


           if(object.getName().equals("GAME_SWITCH_OPEN")) {

               /** Switch id */
               if (object.getProperties().containsKey("Association Number")) {
                   /*
                   System.out.println( "Door id: " + object.getProperties().get("Association Number").toString() +
                   " Status: " + object.getProperties().get("Key Door Status").toString() +
                   " Key Needed: " + object.getProperties().get("Key Type").toString() );
                   */
                   /** Add All the door Switch's to the level */
                   gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpAddDoorSwitchToLevel(
                           gameManagerAssetsInstance.getCurrentWorld(),
                           gameManagerAssetsInstance.getCurrentLevel(),
                           object.getProperties().get("Association Number").toString(),
                           object.getProperties().get("Key Door Status").toString(),
                           object.getProperties().get("Key Type").toString()
                   );
               }
           }
       }



       /** Debug read out World Save Class Object */
       System.out.println("BwWorldCreator Class - Debug System.out : readFromSaveGameWorld() ");
       gameManagerAssetsInstance.readFromSaveGameWorld();


       /** Floor body */
       BodyDef bdef = new BodyDef();
       PolygonShape shape = new PolygonShape();
       FixtureDef fdef = new FixtureDef();
       Body body;

       Shape shapePolyLine;
       Body polyBody;
       BodyDef polyBodydef = new BodyDef();

       /** floor's */
       for(MapObject object : map.getLayers().get("GAME_MAP_GROUND_LAYER").getObjects().getByType(RectangleMapObject.class)){
           Rectangle rect = ((RectangleMapObject) object).getRectangle();
 // new
           BodyDef bdefNew = new BodyDef();
           bdefNew.position.set((rect.getX() + rect.getWidth() / 2) / GameUtility.PPM, (rect.getY() + rect.getHeight() / 2) / GameUtility.PPM);
           bdefNew.type = BodyDef.BodyType.StaticBody;
           Body bodynew = world.createBody(bdefNew);

           FixtureDef fdefnew  = new FixtureDef();

           shape.setAsBox(rect.getWidth() / 2 / GameUtility.PPM, rect.getHeight() / 2 / GameUtility.PPM);

           fdefnew.shape = shape;
           fdefnew.filter.categoryBits = GameUtility.GROUND_BIT;

           fdefnew.filter.groupIndex = GameUtility.GROUND_BIT;

           fdefnew.filter.maskBits = GameUtility.ENEMY_BIT |
                   GameUtility.ENEMY_LEGS_BIT |
                   GameUtility.PLAYER_BIT |
                   GameUtility.PLAYER_POWER_BIT |
                   GameUtility.GAME_ITEM_BIT;

           bodynew.createFixture(fdefnew);



/*        //old
           bdef.type = BodyDef.BodyType.StaticBody;
           bdef.position.set((rect.getX() + rect.getWidth() / 2) / GameUtility.PPM, (rect.getY() + rect.getHeight() / 2) / GameUtility.PPM);

           body = world.createBody(bdef);

           shape.setAsBox(rect.getWidth() / 2 / GameUtility.PPM, rect.getHeight() / 2 / GameUtility.PPM);
           fdef.shape = shape;
           //fdef.friction = 0.45f;
           fdef.filter.categoryBits = GameUtility.GROUND_BIT;

           fdef.filter.groupIndex = GameUtility.GROUND_BIT;

           fdef.filter.maskBits = GameUtility.ENEMY_BIT |
                   GameUtility.ENEMY_LEGS_BIT |
                   GameUtility.PLAYER_BIT;

           body.createFixture(fdef);
*/
       }

       /**  GRAPIHCS_ITEM_LAYER    */ // Works but taking this away for now!!
       for(MapObject object : map.getLayers().get("GRAPHICS_ITEM_LAYER").getObjects().getByType(RectangleMapObject.class)) {
           Rectangle rect = ((RectangleMapObject) object).getRectangle();

           if(object.getName().equals("GRASS_A") && object.getProperties().containsKey("type")) {

               switch ( (String) object.getProperties().get("type")) {

                   case "FRONT":
                       //gameObjectGraphicsAnimationItemFront.addAll(new Grass(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object));
                       break;
                   case "BACK":
                       //gameObjectGraphicsAnimationItemBack.addAll(new Grass(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object));
                       break;
               }
           }
       }


       /** Wall's with Wall Jump */
       for(MapObject object : map.getLayers().get("GAME_MAP_WALL_JUMPING_LAYER").getObjects().getByType(RectangleMapObject.class)){
           Rectangle rect = ((RectangleMapObject) object).getRectangle();
// new
           BodyDef bdefNew = new BodyDef();
           bdefNew.position.set((rect.getX() + rect.getWidth() / 2) / GameUtility.PPM, (rect.getY() + rect.getHeight() / 2) / GameUtility.PPM);
           bdefNew.type = BodyDef.BodyType.StaticBody;
           Body bodynew = world.createBody(bdefNew);

           FixtureDef fdefnew  = new FixtureDef();

           shape.setAsBox(rect.getWidth() / 2 / GameUtility.PPM, rect.getHeight() / 2 / GameUtility.PPM);

           fdefnew.shape = shape;
           fdefnew.filter.categoryBits = GameUtility.WALL_JUMPING_BIT;

           fdefnew.filter.groupIndex = GameUtility.WALL_JUMPING_BIT;

           fdefnew.filter.maskBits = GameUtility.ENEMY_BIT |
                   GameUtility.ENEMY_LEGS_BIT |
                   GameUtility.PLAYER_BIT |
                   GameUtility.PLAYER_POWER_BIT;

           bodynew.createFixture(fdefnew);


/* //old
           bdef.type = BodyDef.BodyType.StaticBody;
           bdef.position.set((rect.getX() + rect.getWidth() / 2) / GameUtility.PPM, (rect.getY() + rect.getHeight() / 2) / GameUtility.PPM);

           body = world.createBody(bdef);

           shape.setAsBox(rect.getWidth() / 2 / GameUtility.PPM, rect.getHeight() / 2 / GameUtility.PPM);
           fdef.shape = shape;

           //fdef.friction = 10f;

           fdef.filter.categoryBits = GameUtility.WALL_JUMPING_BIT;
           fdef.filter.groupIndex = GameUtility.WALL_JUMPING_BIT; // changed from ground bit might cause error check

           body.createFixture(fdef);
*/


       }

       /** Wall's and Sealing  No -Wall Jump */
       for(MapObject object : map.getLayers().get("GAME_MAP_WALL_AND_SEALING_LAYER").getObjects().getByType(RectangleMapObject.class)){
           Rectangle rect = ((RectangleMapObject) object).getRectangle();
// new
           BodyDef bdefNew = new BodyDef();
           bdefNew.position.set((rect.getX() + rect.getWidth() / 2) / GameUtility.PPM, (rect.getY() + rect.getHeight() / 2) / GameUtility.PPM);
           bdefNew.type = BodyDef.BodyType.StaticBody;
           Body bodynew = world.createBody(bdefNew);

           FixtureDef fdefnew  = new FixtureDef();

           shape.setAsBox(rect.getWidth() / 2 / GameUtility.PPM, rect.getHeight() / 2 / GameUtility.PPM);

           fdefnew.shape = shape;
           fdefnew.filter.categoryBits = GameUtility.WALL_AND_SEALING_BIT;

           fdefnew.filter.groupIndex = GameUtility.WALL_AND_SEALING_BIT;

           fdefnew.filter.maskBits = GameUtility.ENEMY_BIT |
                   GameUtility.ENEMY_LEGS_BIT |
                   GameUtility.PLAYER_BIT |
                   GameUtility.PLAYER_POWER_BIT;

           bodynew.createFixture(fdefnew);


/* //old
           bdef.type = BodyDef.BodyType.StaticBody;
           bdef.position.set((rect.getX() + rect.getWidth() / 2) / GameUtility.PPM, (rect.getY() + rect.getHeight() / 2) / GameUtility.PPM);

           body = world.createBody(bdef);

           shape.setAsBox(rect.getWidth() / 2 / GameUtility.PPM, rect.getHeight() / 2 / GameUtility.PPM);
           fdef.shape = shape;

           //fdef.friction = 10f;

           fdef.filter.categoryBits = GameUtility.WALL_JUMPING_BIT;
           fdef.filter.groupIndex = GameUtility.WALL_JUMPING_BIT; // changed from ground bit might cause error check

           body.createFixture(fdef);
*/


       }

       /** Ground PoloLine bodies/fixtures */
       for(MapObject object : map.getLayers().get("GAME_MAP_POLYLINE_LAYER").getObjects().getByType(PolylineMapObject.class)){

           if(object instanceof PolylineMapObject) {
               shapePolyLine = createPolyLine((PolylineMapObject) object);
           }else{
               continue;
           }

           Body bodyPoly;
           BodyDef bdefPoly = new BodyDef();
           bdefPoly.type = BodyDef.BodyType.StaticBody;
           bodyPoly = world.createBody(bdefPoly);
           bodyPoly.createFixture(shapePolyLine,0f);
           shapePolyLine.dispose();

       }

       objects = map.getLayers().get("DOOR_OBJECT_LAYER").getObjects();
       for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

           for (MapObject object : map.getLayers().get("DOOR_OBJECT_LAYER").getObjects().getByType(RectangleMapObject.class)) {

               Rectangle rect = ((RectangleMapObject) object).getRectangle();

               if (object.getProperties().get("Type").equals("Door")) {
                   //System.out.println("WorldCreator Class: Objects Door made!!");
                   ObstacleDoor obstacle = new ObstacleDoor(screen, rect.getX(), rect.getY(), rectangleObject); //world, rectangleObject);
                   obstacles.add(obstacle);
                   activableObstacles.add(obstacle);
               }
           }
       }

//ToDo:
       for(MapObject object : map.getLayers().get("AI_LAYER").getObjects().getByType(RectangleMapObject.class)) {
           Rectangle rect = ((RectangleMapObject) object).getRectangle();

           if(object.getName().equals("AI_STEERING")) {
               gameAISteeringObjects.add( new GameAIObject(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object));
           }

           if(object.getName().equals("AI_JUMP")) {
               new GameAIObject(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object );
           }

           if(object.getName().equals("GAME_BOSS_SPAWN_MARKER")) {
               gameAIBossSpawnObjects.add(new GameAIObject(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object ));
           }

           if(object.getName().equals("GAME_PORTAL_SET_ACTIVITY")){
               gameAIPortalActivity.add(new GameAIObject(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object ));
           }

           if(object.getName().equals("AI_BODY_CHANGE")) {
               //gameAIEnemyReversObjects.add(new GameAIObject(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object ));
               new GameAIObject(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object );
               //System.out.println("B2WorldCreator Class : MARKER_BOSS MADE" );
           }

       }

       for(MapObject object : map.getLayers().get("GAME_OBJECT_LAYER").getObjects().getByType(RectangleMapObject.class)) { // 6

           Rectangle rect = ((RectangleMapObject) object).getRectangle();

           //System.out.println("B2WorldCreator Class : getProperties : Name: " + object.getName() +
           //        " Property level key: " +
           //        object.getProperties().get("WORLD") +
           //        " SavePoint: " + object.getProperties().get("SAVEPOINT") );


           if(object.getName().equals("GAME_PORTAL")) {
               // GameObjectDef extended
               portals.addAll(new Portal(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object));
               //savePoints.addAll( new SavePoint(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object) );
               //System.out.println("B2WorldCreator Class : GAME_PORTAL_BIT MADE: posX " + rect.getX() / GameUtility.PPM + " posY: " + rect.getY() / GameUtility.PPM );
           }

           if(object.getName().equals("GAME_PORTAL_HIDDEN_TRANSITION")) {
               // GameObjectDef extended
               portalsHiddenMapTransition.addAll(new PortalMapTransitionHidden(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object));
               //savePoints.addAll( new SavePoint(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object) );
               //System.out.println("B2WorldCreator Class : GAME_PORTAL_BIT MADE: posX " + rect.getX() / GameUtility.PPM + " posY: " + rect.getY() / GameUtility.PPM );
           }

           // need to identify the object like with type and numbers like switch and Obstacles !!??
           if(object.getName().equals("GAME_SAVE_POINT")) {
               // GameObjectDef extended
               savePoints.addAll( new SavePoint(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object, gameManagerAssetsInstance) );
               //System.out.println("B2WorldCreator Class : GAME_SAVE_POINT_BIT MADE: posX " + rect.getX() / GameUtility.PPM + " posY: " + rect.getY() / GameUtility.PPM );
           }

           //if(object.getName().equals("GAME_SPAWN_POINT_RIGHT")){
           //    spawnPointsRight.addAll(new SpawnPoint(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object) );
           //}

           //if(object.getName().equals("GAME_SPAWN_POINT_LEFT")){
           //    spawnPointsLeft.addAll(new SpawnPoint(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object) );
           //}

           if(object.getName().equals("GAME_SWITCH_OPEN")) {

               //if( object.getProperties().get("BossSwitchDead") != null ) {
               //    gameObjectSwitchesBossDead.addAll( new GameObjectSwitchDoor(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object));
               //}else {
               gameObjectSwitches.addAll( new GameObjectSwitchDoor(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object));
               //}
               //System.out.println("B2WorldCreator Class : GAME_SWITCH_OPEN MADE: posX " + rect.getX() / GameUtility.PPM + " posY: " + rect.getY() / GameUtility.PPM );
           }


           if(object.getName().equals("HIDDEN_SWITCH_OPEN")) {
               if( object.getProperties().get("BossSwitchDead") != null ) {
                   gameObjectSwitchesBossDead.addAll( new GameObjectSwitchHidden(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object));
               }
           }


           if(object.getName().equals("GAME_SWITCH_CLOSE_HIDDEN")) {

               //if(!GameManagerAssets.instance.getSaveGameWorldDataHolderClass().getLevelObjectBossDead().equals("true") ) {

               gameObjectSwitchesHidden.addAll(new GameObjectSwitchHidden(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object));
               //System.out.println("B2WorldCreator Class : GAME_SWITCH_CLOSE MADE: posX " + rect.getX() / GameUtility.PPM + " posY: " + rect.getY() / GameUtility.PPM );

               //}
           }
       }

       /** Pfx Layer testing !!! */
       for(MapObject object : map.getLayers().get("PFX_LAYER").getObjects().getByType(RectangleMapObject.class)) {
           Rectangle rect = ((RectangleMapObject) object).getRectangle();

           if(object.getName().equals("PFX")){
               gamePfxObjects.add(new GamePfxObject(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object));
               //System.out.println("Pfx is made!!");
           }
       }


       /** Light Layer testing !!! */
       /*
       for(MapObject object : map.getLayers().get("LIGHT_LAYER").getObjects().getByType(RectangleMapObject.class)) {
           Rectangle rect = ((RectangleMapObject) object).getRectangle();

           if(object.getName().equals("LIGHT")){
               gameLightObjects.add( new GameLightObject(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object));
               System.out.println("Light is made!! posX: " + (rect.getX() / GameUtility.PPM) + " posY: " + (rect.getY() / GameUtility.PPM));
           }
       }
*/
       /** -Check if we should create Object -if so/ create it */
       for(MapObject object : map.getLayers().get("SPAWN_LAYER").getObjects().getByType(RectangleMapObject.class)){
           Rectangle rect = ((RectangleMapObject) object).getRectangle();

           if(object.getName().equals("EX_LIFE")) {

               id = object.getProperties().get("id", int.class);

               shouldBeCreatedItem = gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpItemSetCreationVarB2World(gameManagerAssetsInstance.getCurrentWorld(),
                       gameManagerAssetsInstance.getCurrentLevel(), object.getName(), id );
               /** true = Been Used */
                if(shouldBeCreatedItem.equals("false")) {
                    extraLife.add(new ExtraLifeGameItem(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object, id));
                }

           }


           if(object.getName().equals("POWER_UP")) {
               id = object.getProperties().get("id", int.class);

               shouldBeCreatedItem = gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpItemSetCreationVarB2World(gameManagerAssetsInstance.getCurrentWorld(),
                       gameManagerAssetsInstance.getCurrentLevel(), object.getName(), id );
               /** true = Been Used */
               if(shouldBeCreatedItem.equals("false")) {
                   playerPowerUp.add(new ItemPowerUp(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object, id));
               }
           }

           /** IF WE CREATE AMO ON MAP FROM ON MAP CREATION START USE - Name AMO_BLUE, AMO_RED ETC. */
           if(object.getName().equals("AMO_BLUE")){
               id = object.getProperties().get("id", int.class);
               extraDragonEgg.add(new DragonEggGameItem(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object, id, "BLUE"));
           }

           if(object.getName().equals("AMO_RED")){
               id = object.getProperties().get("id", int.class);
               extraDragonEgg.add(new DragonEggGameItem(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object, id, "RED"));
           }

           if(object.getName().equals("AMO_BLACK")){
               id = object.getProperties().get("id", int.class);
               extraDragonEgg.add(new DragonEggGameItem(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object, id, "BLACK"));
           }

           if(object.getName().equals("TREASURE_CHEST")){
               id = object.getProperties().get("id", int.class);
               shouldBeCreatedItem = gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpItemSetCreationVarB2World(gameManagerAssetsInstance.getCurrentWorld(),
                       gameManagerAssetsInstance.getCurrentLevel(), object.getName(), id );
               /** true = Been Used */
               if(shouldBeCreatedItem.equals("false")) {
                   extraTreasureChest.add(new TreasureChestGameItem(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object, id));
               }
           }
       }


       for(MapObject object : map.getLayers().get("ENEMY_LAYER").getObjects().getByType(RectangleMapObject.class)){ // 3

           Rectangle rect = ((RectangleMapObject) object).getRectangle();

           //System.out.println("We make Enemy: " + object.getName());

           if(object.getName().contains("EnemyA")) {

               id = object.getProperties().get("id", int.class);
               enemyA.add(new EnemyA(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object, gameManagerAssetsInstance));
           }

           if(object.getName().contains("EnemyB")) {

               id = object.getProperties().get("id", int.class);
               enemyB.add(new EnemyB(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object, gameManagerAssetsInstance));
           }

           if(object.getName().contains("EnemySensor")){
               id = object.getProperties().get("id", int.class);
                enemyGraphicSensors.add(new EnemyGraphicSensor(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object, id));


           }

           if(object.getName().contains("EnemyStalagmit")) {

               id = object.getProperties().get("id", int.class);
               enemyStalactite.add(new EnemyStalactite(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, object, gameManagerAssetsInstance));
           }



       }

       //ToDo : check level against witch boss - have Level boss, is boss dead, if not witch boss is to be made ??!! change

       /** check if boss is defeated if not/ create it on boss level = true */
       String randomBossPosition = Integer.toString( rnRange(0,2) );
       for (MapObject object : map.getLayers().get("ENEMY_BOSS_LAYER").getObjects().getByType(RectangleMapObject.class)) { // 4
           Rectangle rect = ((RectangleMapObject) object).getRectangle();

           id = object.getProperties().get("id", int.class);
           shouldCreateBoss = gameManagerAssetsInstance.getSaveGameWorldDataHolderClass().lookUpEnemyBossSetCreationVarB2World(gameManagerAssetsInstance.getCurrentWorld(),
                   gameManagerAssetsInstance.getCurrentLevel() );

           /** not dead, true = dead */
           if(shouldCreateBoss.equals("false")) {

               if(object.getName().equals("EnemyKnightBoss")) {

                   if( randomBossPosition.equals(object.getProperties().get("RANDOM POSITION").toString()) ) {

                       enemyKnight.add(new EnemyKnightDevil(screen, rect.getX() / GameUtility.PPM, rect.getY() / GameUtility.PPM, id, gameManagerAssetsInstance));
                    }
               }

               if(object.getName().equals("boos name")) {
                   // create it here, add it to enemy like the other's
               }

           }
       }
    }

    private ChainShape createPolyLine(PolylineMapObject polyline) {

        float[] vertices = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for(int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / GameUtility.PPM, vertices[i * 2 + 1] / GameUtility.PPM);
        }

        ChainShape chainShape = new ChainShape();
        chainShape.createChain(worldVertices);
        return chainShape;

    }


    /** GET */
    public Array<SmallEnemyDef> getEnemies(){
        Array<SmallEnemyDef> enemies = new Array<SmallEnemyDef>();

        enemies.addAll(enemyA);
        enemies.addAll(enemyB);

    return enemies;
    }

    public Array<MovingFallingEnemyDef> getEnemyMovingFalling(){
        Array<MovingFallingEnemyDef> stalg = new Array<MovingFallingEnemyDef>();

        stalg.addAll(enemyStalactite);

    return  stalg;
    }


    public Array<BossEnemyDef> getEnemiesBoss(){
        Array<BossEnemyDef> enemies = new Array<BossEnemyDef>();

        enemies.addAll(enemyKnight);

    return enemies;
    }




    public Array<Obstacle> getObstacles() {
        Array<Obstacle> obstacles = new Array<Obstacle>();

        obstacles.addAll(obstacleDoors);

        return obstacles;
    }

    public Array<ItemObjectDef> getItemGameObjects(){
        Array<ItemObjectDef> itemObjectDefs = new Array<ItemObjectDef>();

        itemObjectDefs.addAll(extraLife);
        itemObjectDefs.addAll(extraDragonEgg);
        itemObjectDefs.addAll(extraTreasureChest);
        itemObjectDefs.addAll(playerPowerUp);

    return itemObjectDefs;
    }

    public Array<AnimationItemDef> getGameObjectGraphicsAnimationItemBackGround(){
        Array<AnimationItemDef> animationObjectsBack = new Array<AnimationItemDef>();

        animationObjectsBack.addAll(gameObjectGraphicsAnimationItemBack);

        return animationObjectsBack;
    }

    public Array<AnimationItemDef> getGameObjectGraphicsAnimationItemForGround(){
        Array<AnimationItemDef> animationObjectsFront = new Array<AnimationItemDef>();

        animationObjectsFront.addAll(gameObjectGraphicsAnimationItemFront);

        return animationObjectsFront;
    }

    public Array<Obstacle> getActiveAbleObstacles() {
        Array<Obstacle> obst = new Array<Obstacle>();
        obst.addAll(activableObstacles);

    return obst;
    }
    /** GameManagerAI */
    public Array<GameAIObject> getGameAISteeringObjects(){ return gameAISteeringObjects; }
    /** GameManagerAI */
    public Array<GameAIObject> getGameAIBossSpawnObjects(){ return gameAIBossSpawnObjects; }

    public Array<GamePfxObject> getGamePfxObjects(){ return gamePfxObjects; }

    public Array<GameLightObject> getGameLightObjects(){ return this.gameLightObjects; }

    public Array<MapInfo> getCurrentMapInfoObject() { return gameMapInfoObject; }
    public  Array<SavePoint> getSavePoints(){ return savePoints; }
    public Array<TravelSpawnPoint> getGameTravelSpawnPointObjects(){ return gameTravelSpawnPointObjects; }

    public Array<Portal> getPortals() { return portals; }
    public Array<PortalMapTransitionHidden> getPortalsHiddenMapTransition() { return portalsHiddenMapTransition; }
    /** PlayScreen */
    public Array<GameObjectSwitchDoor> getGameObjectSwitches() { return gameObjectSwitches; }

    /** PlayScreen to render */
    public Array<EnemyGraphicSensor> getEnemyGraphicSensors(){
        return this.enemyGraphicSensors;
    }
    //public Array<EnemyVineThorns> getTrapVines() {return this.trapVines; }


    /** update and activate with inn GameManagerAI boss is dead!!  Only holds Worlds Boss's Switch's */
    public Array<GameObjectSwitchHidden> getGameObjectSwitchesBossDead(){ return gameObjectSwitchesBossDead; }
    /** PlayScreen */
    public Array<GameObjectSwitchHidden> getGameObjectSwitchesHidden() { return gameObjectSwitchesHidden; }

}




























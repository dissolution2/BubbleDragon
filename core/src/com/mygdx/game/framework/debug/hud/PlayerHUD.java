package com.mygdx.game.framework.debug.hud;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.transitions.ScreenTransitionAction;
import com.mygdx.game.framework.debug.screens.transitions.ScreenTransitionActor;
import com.mygdx.game.framework.debug.util.GameUtility;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class PlayerHUD implements Disposable {

    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;





    //Mario score/time Tracking Variables
    private Integer worldTimer;
    private boolean timeUp; // true when the world timer reaches 0
    private float timeCount;
    private static Integer score;

    //Scene2D widgets
    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    private TextureAtlas textureAtlas;
    private Image imagePlayerLife_Up01, imagePlayerLife_Up02, imagePlayerLife_Up03, imagePlayerLife_Up04;
    private Image imagePlayerLife_Down01, imagePlayerLife_Down02, imagePlayerLife_Down03, imagePlayerLife_Down04;

    private Image imageCrystal_Red,imageCrystal_Blue, imageCrystal_Black, imageCrystal_Green;

    private TextureRegion switchTextureLife, switchTextureLife_Gone, powerCrystalTexture_Red, powerCrystalTexture_Blue, powerCrystalTexture_Black, powerCrystalTexture_Green;

    private TextureRegion test_layout_buttons;

    private Image imageTest_layout_buttons;

    private ScreenTransitionActor transitionActor;

    private Skin skin;

    private int playerLife;
    private boolean fadeout;

private GameManagerAssets gameManagerAssetsInstance;
    public PlayerHUD(SpriteBatch sb, int lifeOfPlayer, GameManagerAssets instance){

        this.gameManagerAssetsInstance = instance;

        //define our tracking variables
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        playerLife = lifeOfPlayer;

        // life Bare etc
        switchTextureLife = new TextureRegion( GameUtility.getTextureAtlas("gameUI/GameUI2.atlas").findRegion("BubblePlayer_Live"), 0, 0, 23, 20);
        switchTextureLife_Gone = new TextureRegion( GameUtility.getTextureAtlas("gameUI/GameUI2.atlas").findRegion("BubblePlayer_Live_Lost"), 0, 0, 23, 20);

        //old
        //powerCrystalTexture = new TextureRegion( GameUtility.getTextureAtlas("gameUI/GameUI2.atlas").findRegion("krystalPower2"), 0, 0, 7, 18);

        powerCrystalTexture_Red = new TextureRegion( GameUtility.getTextureAtlas("gameUI/GameUI2.atlas").findRegion("krystalPowerRed"), 0, 0, 7, 18);
        powerCrystalTexture_Blue = new TextureRegion( GameUtility.getTextureAtlas("gameUI/GameUI2.atlas").findRegion("krystalPowerBlue"), 0, 0, 7, 18);
        powerCrystalTexture_Black = new TextureRegion( GameUtility.getTextureAtlas("gameUI/GameUI2.atlas").findRegion("krystalPowerBlack"), 0, 0, 7, 18);
        //powerCrystalTexture_Green = new TextureRegion( GameUtility.getTextureAtlas("gameUI/GameUI2.atlas").findRegion("krystalPowerGreen"), 0, 0, 7, 18);

        //test_layout_buttons = new TextureRegion( GameUtility.getTextureAtlas("gameUI/GameUI2.atlas").findRegion("Temp_LayOut_Buttons"), 0, 0, 264, 284);

        //Testing on transition level / game start fade inn / out
        transitionActor = new ScreenTransitionActor();


        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);



        //define our labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel =new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("BubbleBubble", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        skin = new Skin();
        textureAtlas = GameUtility.getTextureAtlas("gameUI/GameUI2.atlas");
        skin.addRegions( textureAtlas );

/*
        imageTest_layout_buttons = new Image(skin.getDrawable("Temp_LayOut_Buttons"));
        imageTest_layout_buttons.setWidth(264);
        imageTest_layout_buttons.setHeight(284);
        imageTest_layout_buttons.setX(0);
        imageTest_layout_buttons.setY(0);
*/
        imageCrystal_Red = new Image(skin.getDrawable("krystalPowerRed"));
        imageCrystal_Red.setWidth(7);
        imageCrystal_Red.setHeight(18);
        imageCrystal_Red.setX(0);
        imageCrystal_Red.setY(0);

        imageCrystal_Blue = new Image(skin.getDrawable("krystalPowerBlue"));
        imageCrystal_Blue.setWidth(7);
        imageCrystal_Blue.setHeight(18);
        imageCrystal_Blue.setX(0);
        imageCrystal_Blue.setY(0);

        imageCrystal_Black  = new Image(skin.getDrawable("krystalPowerBlack"));
        imageCrystal_Black.setWidth(7);
        imageCrystal_Black.setHeight(18);
        imageCrystal_Black.setX(0);
        imageCrystal_Black.setY(0);
/*
        imageCrystal_Green  = new Image(skin.getDrawable("krystalPowerGreen"));
        imageCrystal_Green.setWidth(7);
        imageCrystal_Green.setHeight(18);
        imageCrystal_Green.setX(0);
        imageCrystal_Green.setY(0);
*/

//*********************************************** Up life
        imagePlayerLife_Up01 = new Image(skin.getDrawable("BubblePlayer_Live"));
        //imageOxygenLevel.setColor(0,0,1,1);
        imagePlayerLife_Up01.setWidth(23);
        imagePlayerLife_Up01.setHeight(20);
        imagePlayerLife_Up01.setX(0);
        imagePlayerLife_Up01.setY(0);

        imagePlayerLife_Up02 = new Image(skin.getDrawable("BubblePlayer_Live"));
        //imageOxygenLevel.setColor(0,0,1,1);
        imagePlayerLife_Up02.setWidth(23);
        imagePlayerLife_Up02.setHeight(20);
        imagePlayerLife_Up02.setX(0);
        imagePlayerLife_Up02.setY(0);

        imagePlayerLife_Up03 = new Image(skin.getDrawable("BubblePlayer_Live"));
        //imageOxygenLevel.setColor(0,0,1,1);
        imagePlayerLife_Up03.setWidth(23);
        imagePlayerLife_Up03.setHeight(20);
        imagePlayerLife_Up03.setX(0);
        imagePlayerLife_Up03.setY(0);

        imagePlayerLife_Up04 = new Image(skin.getDrawable("BubblePlayer_Live"));
        //imageOxygenLevel.setColor(0,0,1,1);
        imagePlayerLife_Up04.setWidth(23);
        imagePlayerLife_Up04.setHeight(20);
        imagePlayerLife_Up04.setX(0);
        imagePlayerLife_Up04.setY(0);
//*********************************************** Down life
        imagePlayerLife_Down01 = new Image(skin.getDrawable("BubblePlayer_Live_Lost"));
        //imageOxygenLevel.setColor(0,0,1,1);
        imagePlayerLife_Down01.setWidth(23);
        imagePlayerLife_Down01.setHeight(20);
        imagePlayerLife_Down01.setX(0);
        imagePlayerLife_Down01.setY(0);

        imagePlayerLife_Down02 = new Image(skin.getDrawable("BubblePlayer_Live_Lost"));
        //imageOxygenLevel.setColor(0,0,1,1);
        imagePlayerLife_Down02.setWidth(23);
        imagePlayerLife_Down02.setHeight(20);
        imagePlayerLife_Down02.setX(0);
        imagePlayerLife_Down02.setY(0);

        imagePlayerLife_Down03 = new Image(skin.getDrawable("BubblePlayer_Live_Lost"));
        //imageOxygenLevel.setColor(0,0,1,1);
        imagePlayerLife_Down03.setWidth(23);
        imagePlayerLife_Down03.setHeight(20);
        imagePlayerLife_Down03.setX(0);
        imagePlayerLife_Down03.setY(0);

        imagePlayerLife_Down04 = new Image(skin.getDrawable("BubblePlayer_Live_Lost"));
        //imageOxygenLevel.setColor(0,0,1,1);
        imagePlayerLife_Down04.setWidth(23);
        imagePlayerLife_Down04.setHeight(20);
        imagePlayerLife_Down04.setX(0);
        imagePlayerLife_Down04.setY(0);
/*
        Table table_test_buttons_layout = new Table();
        table_test_buttons_layout.bottom();
        table_test_buttons_layout.right();
        table_test_buttons_layout.setFillParent(true);
        table_test_buttons_layout.add(imageTest_layout_buttons);
        table_test_buttons_layout.row();
        table_test_buttons_layout.add().size(50);
*/
        Table tabel_Crystals = new Table();
        tabel_Crystals.left().top();
        tabel_Crystals.setFillParent(true);
        tabel_Crystals.add().size(16);
        tabel_Crystals.row();
        tabel_Crystals.add().size(16);
        tabel_Crystals.row();
        tabel_Crystals.add(imageCrystal_Red);
        tabel_Crystals.row();
        tabel_Crystals.add(imageCrystal_Blue);
        tabel_Crystals.row();
        tabel_Crystals.add(imageCrystal_Black);
//        tabel_Crystals.row();
//        tabel_Crystals.add(imageCrystal_Green);


        //define a table used to organize our hud's labels
        Table table2 = new Table();
        //Top-Align table
        table2.left().top();
        //make the table fill the entire stage
        table2.setFillParent(true);
        table2.add().size(14);
        table2.add(imagePlayerLife_Up01);
        table2.add(imagePlayerLife_Up02);
        table2.add(imagePlayerLife_Up03);
        table2.add(imagePlayerLife_Up04);
        table2.row();

        Table table3 = new Table();
        //Top-Align table
        table3.left().top();
        //make the table fill the entire stage
        table3.setFillParent(true);
        table3.add().size(14);
        table3.add(imagePlayerLife_Down01);
        table3.add(imagePlayerLife_Down02);
        table3.add(imagePlayerLife_Down03);
        table3.add(imagePlayerLife_Down04);
        table3.row();


        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);
        //add our labels to our table, padding the top, and giving them all equal width with expandX

        table.add().size(14);
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add().size(14);
        //add a second row to our table
        table.row();
        table.add().size(14);
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();
        table.add().size(14);

        //table2.debug();
        //table.debug();

        //add our table to the stage
        stage.addActor(table3);
        stage.addActor(table2);
        stage.addActor(tabel_Crystals);
        //stage.addActor(table_test_buttons_layout);

        stage.addActor(table);

        stage.addActor(transitionActor);
        transitionActor.setVisible(false);

        //AddTransitionToScreenFadeOut();
        addTransitionToScreen(); // 5 ok - 2



    }


    // change . Add Known Life = total life, so if we pick up a Extra Life Bare (not an extra life)
    // we then show extra life to the hud and change max life!!!
    public void setLifeVisible(int life){

        //System.out.println("My life is: " + life);

        playerLife = life;

        if(playerLife == GameUtility.MAX_PLAYER_MAIN_LIFE) {
            imagePlayerLife_Up01.setVisible(true);
            imagePlayerLife_Up02.setVisible(true);
            imagePlayerLife_Up03.setVisible(true);
            imagePlayerLife_Up04.setVisible(true);

            imagePlayerLife_Down01.setVisible(false);
            imagePlayerLife_Down02.setVisible(false);
            imagePlayerLife_Down03.setVisible(false);
            imagePlayerLife_Down04.setVisible(false);
            //System.out.println("PlayerHud setLife visual: all life Up!!");
        }else{

            switch (playerLife){
                case 3:
                    imagePlayerLife_Up01.setVisible(true);
                    imagePlayerLife_Up02.setVisible(true);
                    imagePlayerLife_Up03.setVisible(true);
                    imagePlayerLife_Up04.setVisible(false);
                    imagePlayerLife_Down04.setVisible(true);
                    //System.out.println("PlayerHud setLife visual: 3 Up - 1 Down");
                    break;
                case 2:
                    imagePlayerLife_Up01.setVisible(true);
                    imagePlayerLife_Up02.setVisible(true);
                    imagePlayerLife_Up03.setVisible(false);
                    imagePlayerLife_Down03.setVisible(true);
                    imagePlayerLife_Up04.setVisible(false);
                    imagePlayerLife_Down04.setVisible(true);
                    //System.out.println("PlayerHud setLife visual: 2 Up - 2 Down");
                    break;
                case 1:
                    imagePlayerLife_Up01.setVisible(true);
                    imagePlayerLife_Up02.setVisible(false);
                    imagePlayerLife_Down02.setVisible(true);
                    imagePlayerLife_Up03.setVisible(false);
                    imagePlayerLife_Down03.setVisible(true);
                    imagePlayerLife_Up04.setVisible(false);
                    imagePlayerLife_Down04.setVisible(true);
                    //System.out.println("PlayerHud setLife visual: 1 Up - 3 Down");
                    break;
                case 0:
                    imagePlayerLife_Up01.setVisible(false);
                    imagePlayerLife_Down01.setVisible(true);
                    imagePlayerLife_Up02.setVisible(false);
                    imagePlayerLife_Down02.setVisible(true);
                    imagePlayerLife_Up03.setVisible(false);
                    imagePlayerLife_Down03.setVisible(true);
                    imagePlayerLife_Up04.setVisible(false);
                    imagePlayerLife_Down04.setVisible(true);
                    //System.out.println("PlayerHud setLife visual: 0 Up - 4 Down");
                    break;
            }
        }

    }

    public void addTransitionToScreen() {
        transitionActor.setVisible(true);
        stage.addAction(
                Actions.sequence(
                        Actions.addAction(
                                ScreenTransitionAction.transition(
                                        ScreenTransitionAction.ScreenTransitionType.FADE_IN, 2.8f), transitionActor))); //.FADE_IN, 1), transitionActor)));
    }

    // ? Call when loading maps ?
    public void AddTransitionToScreenFadeOut() {
        System.out.println("Hud Class call -AddTransitionToScreenFadeOut");
        transitionActor.setVisible(true);
        stage.addAction(
                Actions.sequence(
                        Actions.addAction(
                                ScreenTransitionAction.transition(
                                        ScreenTransitionAction.ScreenTransitionType.FADE_OUT, 8), transitionActor))); //.FADE_IN, 1), transitionActor)));
    }


    public void update(float dt, int pLive){
        //System.out.println("Hud GameState : " + this.gameState);

        if(gameManagerAssetsInstance.getGameState() != GameManagerAssets.GameState.GAME_PAUSED ) {

            timeCount += dt;
            if(timeCount >= 1){
                if (worldTimer > 0) {
                    worldTimer--;
                } else {
                    timeUp = true;
                }
                countdownLabel.setText(String.format("%03d", worldTimer));
                timeCount = 0;
            }


        // lifeUpdate
        setLifeVisible(pLive);

        }
    }

    // Needed this to get AddTransitionToScreen to work in Init
    public void render(float dt) {
        update(dt, playerLife);

        stage.act(dt);
        stage.draw();

    }



    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }



    @Override
    public void dispose() {

        stage.dispose();
    }

    public boolean isTimeUp() { return timeUp; }
}

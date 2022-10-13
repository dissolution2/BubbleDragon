package com.mygdx.game.framework.debug.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.GameScreen;
import com.mygdx.game.framework.debug.screens.GameTime;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.util.FloatingText;
import com.mygdx.game.framework.debug.util.GameUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class ControllerJoyStickButtonStyle extends Table implements GestureDetector.GestureListener  {

    private boolean gameMapShow = false;

    private Map<Button, SpellBarImageButton> spellBarButtons;
    private Map<Button, QuickSpellBarImageButton> quickBarButtons;

    private Map<Button, QuickSpellBarImageButton> imageButton_Two;

    private boolean power_Change_from_green_pressed = false;
    private boolean power_Change_from_blue_pressed = false;

    /** Add more for each power we bring in to the game!! */
    private String powerInUse;

    private boolean power_IsBlueActive = false; // as default set by GUI.
    private boolean power_IsGreenActive = true; // as default set by GUI.

    private Touchpad touchpad;
    private TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    private Viewport viewport;
    private Stage stage;
    private Game game;

    OrthographicCamera cam;

    private boolean fireButton, jumpButton;
    private BubblePlayer player;

    // static final
    private int MAX_SLOTS = 10;
    private String message = "";

    //private boolean boolPowerChangeButtonPressedTrue = false;

    //private List<Button> button_list;
    private ArrayList<Button> button_list;
    private ArrayList<Button> quick_Button_list;

    //GestureDetector gestureDetector; // fling etc
    private TextureAtlas textureAtlas;

    private boolean jumpButtonIsPressed;
    private boolean  shotButtonIsPressed;


    private FloatingText floatingText;
    private MapProperties mapProps;
    private int mapLevelWidth;
    private int mapLevelHeight;


    //private OrthographicCamera gameCam;

    public ControllerJoyStickButtonStyle(NameGame gameName, ArrayList<Button> from_PlayScreen_button_list, ArrayList<Button> from_PlayScreen_quickButton_list, BubblePlayer p, com.badlogic.gdx.maps.Map map) {

        this.textureAtlas = new TextureAtlas(Gdx.files.internal(GameManagerAssets.TEXTURE_ATLAS_HUD));
        this.spellBarButtons = new HashMap<Button, SpellBarImageButton>();
        this.quickBarButtons = new HashMap<Button, QuickSpellBarImageButton>();

        this.game = gameName;
        this.player = p;
        this.powerInUse = this.player.getPlayerActvieShootingPower();
        this.mapProps = map.getProperties();
        this.mapLevelWidth = mapProps.get("width", Integer.class);
        this.mapLevelHeight = mapProps.get("height", Integer.class);

// testing new power button to be added later with inn the game -running!!!
        this.button_list = from_PlayScreen_button_list;
        this.quick_Button_list = from_PlayScreen_quickButton_list;

        viewport = new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, gameName.batch);
/*      System.out.println("ControllerJoyStick ViewPort Position " + viewport.getCamera().position +
                " viewPortCamera W " + viewport.getCamera().viewportWidth +
                " viewPortCamera H " + viewport.getCamera().viewportHeight );

        System.out.println("ControllerJoyStick ViewPort ScreenW " + viewport.getScreenWidth() +
                " ScreenH " + viewport.getScreenHeight() +
                " WorldW " +  viewport.getWorldWidth() +
                " WorldH " +  viewport.getWorldHeight() );
*/
        initJoystick();
        initButtonBar();
        initQuickButtonBar();
        //gestureDetector = new GestureDetector(this); // fling etc


    }

   public Stage getControllerJoyStickStyleStage(){
        return this.stage;
   }

    public void initCreateFloatingText(String text, float posX, float posY, float deltaX, float deltaY){

        Vector3 positionProsjected = new Vector3(posX,posY,0);
        viewport.unproject(positionProsjected); // map 40 - 20 with pros from ManagerAI
        this.floatingText = new FloatingText(text, TimeUnit.SECONDS.toMillis(2));
        this.floatingText.setPosition(positionProsjected.x - 50f, positionProsjected.y);
        this.floatingText.setDeltaY(deltaY); // float strait up.
        floatingText.setDeltaX(deltaX); // float the text diagonaly up.
        this.stage.addActor(floatingText);
    }

    public void initJoystick(){

        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("gameController/Back_08.png"));//touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("gameController/Joystick_08.png"));//touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);


        //setBounds(x,y,width,height)
        touchpad.setBounds(60, 30, 150, 150); // (5, 30, 150, 150); (60, 30, 150, 150);
//touchpad.setDebug(true);
        stage.addActor(touchpad);

    }

    public void initButtonBar(){

        //Button button;
        for (int count = 0; count < Math.min(button_list.size(), MAX_SLOTS); count ++) {

            //final Button button = ....
            final Button button = button_list.get(count);
            final String buttonName = button.getName().toLowerCase();

            //final Button button = new Button("attack", 2, new Action());
            //final Button b = button.get; // = spells.get(count);

            //String buttonName = "attack";

            ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
            imageButtonStyle.imageUp = new TextureRegionDrawable( textureAtlas.findRegion( buttonName +  GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_UP_SUFFIX ));
            imageButtonStyle.imageDown = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));
            imageButtonStyle.imageDisabled = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));



            //final SpellBarImageButton imageButton = new SpellBarImageButton(imageButtonStyle, button.getCooldown());

            //final SpellBarImageButton imageButton = new SpellBarImageButton(imageButtonStyle, button.getCooldown()); // old testing something !!

            final QuickSpellBarImageButton imageButton_Two = new QuickSpellBarImageButton(imageButtonStyle, button.getCooldown());
            // TODO button lay out and position Jump Position is Correct :)
//System.out.println("ControllerStickButtonStyle Class: Test Position Extra button : " + count);

            //imageButton.setPosition(7 + count * 57, 8);


            switch (count){
                case 0:
                    imageButton_Two.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 0 * 90, 54); // jump
                    break;
                case 1:
                    imageButton_Two.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 167, 54); // Shoot
                    break;
                case 2:
                    imageButton_Two.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 167, 170); // dash 230
                    break;
                //case 3:
                //    //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 0 * 90, 170); // 230 should be shiled is now dash
                //    break;
                case 3:
                    imageButton_Two.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 97, 140); // meny button
                    break;
                case 4:
                    imageButton_Two.setPosition( (GameUtility.V_WIDTH / 2) - 150 - 1 * 240, 190); // power_chose button
                    break;

            }

            if(count == 2){
                //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 90, 190); // Power Button - Far Right Location Over Main action buttons
            }else{
                // org. imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + count * 90, 70); // Main action buttons - Far Right Location
                //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + count * 90, 54); // Main action buttons - Far Right Location
            }

            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 0 * 65, 70);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 1 * 90, 120);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 2 * 90, 8);

            stage.addActor(imageButton_Two);

            imageButton_Two.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {

                    if (!((QuickSpellBarImageButton)actor).isOnCooldown()) {

                        if( button.getName().equals("attack_one")) {

                            player.setPlayerIsShooting(true); /** Floating text with inn player class "die monster die" etc */
                            player.fire(  Integer.parseInt(player.getPlayerActvieShootingPower() ) ); //1 or 2
                            shotButtonIsPressed = true;

                            // message = "fire weapon 1";
                            //float x;
                            //float y;
                            //x = 50 * 0.05f;
                            //y = 50 * 0.05f;

                            //initCreateFloatingText("controllerJoyStick 50f*MPP 50f*MPP", x,y, 50);
                        }

                        if( button.getName().equals("attack_two")) {
                            player.setPlayerIsShooting(true); /** Floating text with inn player class "die monster die" etc */
                            player.fire(  Integer.parseInt(player.getPlayerActvieShootingPower() ) ); //1 or 2
                            shotButtonIsPressed = true;
                            //message = "fire weapon 2";
                        }

                        if( button.getName().equals("jump")) {

                            player.jump();
                            jumpButtonIsPressed = true;


                        }

                        if( button.getName().equals("power_one")) {

                            player.dash(1);
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("power_two")) {

                            //player.dash(1);
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("meny_power")) {

                            //player.dash(1);
                            gameMapShow = true;
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("power_chblue")) { // power_change
                            System.out.println("power is blue");

                            power_Change_from_blue_pressed = true;

                        }

                        if( button.getName().equals("power_chgreen")) { // power_change

  System.out.println("ControllJoyStickButtonStyle: power_chose pressed"); // ????

                            System.out.println("ControllJoyStickButtonStyle initButton change fire Blue amo count is: " +
                                    player.getBallooneBulletBlue() + " is blu weapon known " +
                                    player.getPlayerKnownWeaponPowerBlue() );

                            if(player.getPlayerKnownWeaponPowerBlue()){

                                System.out.println("ControllJoyStickButtonStyle initButton: Power weapon blue is known to player!!");
                                if(player.getBallooneBulletBlue() > 0 ) {
                                    power_Change_from_green_pressed = true;

                                }
                            }
                        }


                        button.getAction().execute( button.getName() );
                        ((QuickSpellBarImageButton)actor).setCooldownTriggerTime(GameTime.getCurrentTime());
                    }
                }
            });
            // HashMap ( button, imageButton )
            quickBarButtons.put(button, imageButton_Two); //spellBarButtons
//System.out.println("ControllJoyStickButtonStyle Class: spellBarButtons " + spellBarButtons. );
        }

    }

    public void initQuickButtonBar(){

        //Button button;
        for (int count = 0; count < Math.min(quick_Button_list.size(), MAX_SLOTS); count ++) {

            //final Button button = ....
            final Button button = quick_Button_list.get(count);
            final String buttonName = button.getName().toLowerCase();

            //final Button button = new Button("attack", 2, new Action());
            //final Button b = button.get; // = spells.get(count);

            //String buttonName = "attack";

            ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
            imageButtonStyle.imageUp = new TextureRegionDrawable( textureAtlas.findRegion( buttonName +  GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_UP_SUFFIX ));
            imageButtonStyle.imageDown = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));
            imageButtonStyle.imageDisabled = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));



            //final SpellBarImageButton imageButton = new SpellBarImageButton(imageButtonStyle, button.getCooldown());
            final QuickSpellBarImageButton quick_imageButton = new QuickSpellBarImageButton(imageButtonStyle, button.getCooldown());

            // TODO button lay out and position Jump Position is Correct :)
//System.out.println("ControllerStickButtonStyle Class: Test Position Extra button : " + count);

            //imageButton.setPosition(7 + count * 57, 8);


            switch (count){
                case 0:
                    quick_imageButton.setPosition( (GameUtility.V_WIDTH / 2) - 150 - 1 * 150, 190); // power_chose button
                    //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 0 * 220, 54); // jump
                    break;
                case 1:
                    //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 167, 54); // Shoot
                    break;
                case 2:
                    //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 167, 170); // dash 230
                    break;
                case 3:
                    //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 0 * 90, 170); // 230 should be shiled is now dash
                    break;
                case 4:
                    //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 97, 140); // meny button
                    break;
                case 5:
                    //imageButton.setPosition( (GameUtility.V_WIDTH / 2) - 150 - 1 * 240, 190); // power_chose button
                    break;

            }

            if(count == 2){
                //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 90, 190); // Power Button - Far Right Location Over Main action buttons
            }else{
                // org. imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + count * 90, 70); // Main action buttons - Far Right Location
                //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + count * 90, 54); // Main action buttons - Far Right Location
            }

            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 0 * 65, 70);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 1 * 90, 120);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 2 * 90, 8);

            stage.addActor(quick_imageButton);

            quick_imageButton.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {

                    if (!((QuickSpellBarImageButton)actor).isOnCooldown()) {

                        if( button.getName().equals("attack_one")) {

                            player.fire(  Integer.parseInt(player.getPlayerActvieShootingPower() ) ); //1 or 2
                            shotButtonIsPressed = true;
                            // message = "fire weapon 1";
                        }

                        if( button.getName().equals("attack_two")) {

                            player.fire(  Integer.parseInt(player.getPlayerActvieShootingPower() ) ); //1 or 2
                            shotButtonIsPressed = true;
                            //message = "fire weapon 2";
                        }

                        if( button.getName().equals("jump")) {

                            player.jump();
                            jumpButtonIsPressed = true;


                        }

                        if( button.getName().equals("power_one")) {

                            player.dash(1);
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("power_two")) {

                            //player.dash(1);
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("meny_power")) {



                            //player.dash(1);
                           // gameMapShow = true;
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("power_chgreen")) { // power_change

System.out.println("ControllJoyStickButtonStyle: Quick bar -power_chose pressed!!"); // ????

                            //System.out.println("ControllJoyStickButtonStyle initButton change fire Blue amo count is " +
                            //        player.getBallooneBulletBlue() + " is blu weapon known " +
                            //        player.getPlayerKnownWeaponPowerBlue() );

                            //if(player.getPlayerKnownWeaponPowerBlue()){

                              //  System.out.println("ControllJoyStickButtonStyle initButton: Power weapon blue is known to player!!");
                                //if(player.getBallooneBulletBlue() > 0 ) {
                                //    power_Change_from_green_pressed = true;
                                //}
                            //}
                        }


                        button.getAction().execute( button.getName() );
                        ((QuickSpellBarImageButton)actor).setCooldownTriggerTime(GameTime.getCurrentTime());
                    }
                }
            });
            // HashMap ( button, imageButton )
            quickBarButtons.put(button, quick_imageButton);
        }

    }


    // TODO
    //  * to much this way if we have more then 2 attacks !!???? change
    //  * The power Cool down can be interrupted
    public void newButtonBarDownFling() {


        this.spellBarButtons.clear();
        this.button_list.clear();

        button_list.add(new Button("jump", 0, new Action()));
        button_list.add(new Button("attack_one", 0.5f, new Action()));
        button_list.add(new Button("power_one", 1f, new Action()) );

        for (int count = 0; count < Math.min(button_list.size(), MAX_SLOTS); count ++) {

            //final Button button = ....
            final Button button = button_list.get(count);
            final String buttonName = button.getName().toLowerCase();

            //final Button button = new Button("attack", 2, new Action());
            //final Button b = button.get; // = spells.get(count);

            //String buttonName = "attack";

            ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
            imageButtonStyle.imageUp = new TextureRegionDrawable( textureAtlas.findRegion( buttonName +  GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_UP_SUFFIX ));
            imageButtonStyle.imageDown = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));
            imageButtonStyle.imageDisabled = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));



            //final SpellBarImageButton imageButton = new SpellBarImageButton(imageButtonStyle, button.getCooldown());
            final SpellBarImageButton imageButton = new SpellBarImageButton(imageButtonStyle, button.getCooldown());

            if(count == 2){
                imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 90, 190); // Power Button - Far Right Location Over Main action buttons
            }else{
                imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + count * 90, 70); // Main action buttons - Far Right Location
            }

            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 0 * 65, 70);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 1 * 90, 120);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 2 * 90, 8);

            stage.addActor(imageButton);

            imageButton.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {

                    if (!((SpellBarImageButton)actor).isOnCooldown()) {

                        if( button.getName().equals("attack_one")) {

                            player.fire(1);
                            message = "fire weapon 1";
                        }

                        if( button.getName().equals("jump")) {

                            player.jump();
                        }

                        if( button.getName().equals("power_one")) {

                            player.dash(1);
                        }

                        button.getAction().execute( button.getName() );
                        ((SpellBarImageButton)actor).setCooldownTriggerTime(GameTime.getCurrentTime());
                    }
                }
            });
            // HashMap ( button, imageButton )
            spellBarButtons.put(button, imageButton);
//System.out.println("ControllJoyStickButtonStyle Class: spellBarButtons " + spellBarButtons. );
        }

    }

    public void newButtonBarUpFling(){

        // TODO Change image of attack buttons and be able to do this multiple time
        //this.boolPowerChangeButtonPressedTrue = false; //can't remember this one

        this.spellBarButtons.clear();
        this.button_list.clear();

        button_list.add(new Button("jump", 0, new Action()));
        button_list.add(new Button("attack_two", 1, new Action()));
        button_list.add(new Button("power_two", 0, new Action()) );

        for (int count = 0; count < Math.min(button_list.size(), MAX_SLOTS); count ++) {

            //final Button button = ....
            final Button button = button_list.get(count);
            final String buttonName = button.getName().toLowerCase();

            //final Button button = new Button("attack", 2, new Action());
            //final Button b = button.get; // = spells.get(count);

            //String buttonName = "attack";

            ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
            imageButtonStyle.imageUp = new TextureRegionDrawable( textureAtlas.findRegion( buttonName +  GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_UP_SUFFIX ));
            imageButtonStyle.imageDown = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));
            imageButtonStyle.imageDisabled = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));



            //final SpellBarImageButton imageButton = new SpellBarImageButton(imageButtonStyle, button.getCooldown());
            final SpellBarImageButton imageButton = new SpellBarImageButton(imageButtonStyle, button.getCooldown());

            if(count == 2){
                imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 90, 190); // Power Button - Far Right Location Over Main action buttons
            }else{
                imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + count * 90, 70); // Main action buttons - Far Right Location
            }

            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 0 * 65, 70);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 1 * 90, 120);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 2 * 90, 8);

            stage.addActor(imageButton);

            imageButton.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {

                    if (!((SpellBarImageButton)actor).isOnCooldown()) {

                        if( button.getName().equals("attack_two")) {

                            player.fire(2);
                            message = "fire weapon 2";
                        }

                        if( button.getName().equals("jump")) {

                            player.jump();

                        }

                        if( button.getName().equals("power_two")) {

                            System.out.println("power_two");


                        }

                        button.getAction().execute( button.getName() );
                        ((SpellBarImageButton)actor).setCooldownTriggerTime(GameTime.getCurrentTime());
                    }
                }

            });
            // HashMap ( button, imageButton )
            spellBarButtons.put(button, imageButton);
//System.out.println("ControllJoyStickButtonStyle Class: spellBarButtons " + spellBarButtons. );
        }

    }

/*
    public GestureDetector getGestureDect(){
        return  this.gestureDetector;
    }
 */
    public String getMessage(){
        return this.message;
    }

    public boolean getGameMapShow(){
        return gameMapShow;
    }

    public void hideGameMapShow(){this.gameMapShow = false;}

    public void update() {

        //this.floatingText = player.getFloatingText();

        if(floatingText != null) {
            if (!floatingText.isAnimated()) {
                floatingText.animate();
            }
        }
/*
        for (SpellBarImageButton spellBarButton : spellBarButtons.values()) {
            spellBarButton.update();
        }
*/
        for (QuickSpellBarImageButton quickSpellBarButton : quickBarButtons.values()) {
            quickSpellBarButton.update();
        }
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        //message = "touchDown";
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        message = "tap";
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        //message = "longPress";
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

         if(Math.abs(velocityX) > Math.abs(velocityY)) {
            if(velocityX > 0) {
                //System.out.println("ControllJoyStickButtonStyle Class fling Right");
                message = "fling Right: " + Float.toString(velocityX) + "," + Float.toString(velocityY);
                //newButtonBarRightFling();
            }else{
                //System.out.println("ControllJoyStickButtonStyle Class fling Left");
                message = "fling Left: " + Float.toString(velocityX) + "," + Float.toString(velocityY);
                //newButtonBarLeftFling();
            }
        }else{
            if(velocityY > 0) {
                //System.out.println("ControllJoyStickButtonStyle Class fling Down");
                message = "fling Down: " + Float.toString(velocityX) + "," + Float.toString(velocityY);
                //newButtonBarDownFling();
            }else{
                //System.out.println("ControllJoyStickButtonStyle Class fling Up");
                message = "fling Up: " + Float.toString(velocityX) + "," + Float.toString(velocityY);
                //newButtonBarUpFling();
            }
        }


//System.out.println("ControllJoyStickButtonStyle Class fling start new buttonTool bar ");
        //boolPowerChangeButtonPressedTrue = true;
        //newButtonBar();


       return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }


    /**
     * Represents a button of the spell bar component.
     *
     * @author serhiy
     */

    /** cool down timer but not visual */
    class SpellBarImageButton extends ImageButton {

        private final CooldownTimer cooldownTimer;
        private final float cooldown;

        private float cooldownTriggerTime = -Float.MAX_VALUE;

        public SpellBarImageButton(ImageButtonStyle imageButtonStyle, float cooldown) {
            super(imageButtonStyle);
            this.cooldown = cooldown;

            this.cooldownTimer = new CooldownTimer(true, 78, 84); //50, 54);
            cooldownTimer.setPosition(0, 0);
            cooldownTimer.setColor(Color.BLACK);// WHITE);

            /** implement this later on Higher Difficulty for now we sett zero time*/
            //System.out.println("ColdownTimer Spellbare : " + this.cooldownTimer);
            addActor(this.cooldownTimer);
        }

        public boolean isOnCooldown() {
            return getRemainingCooldownTime() - GameManagerAssets.EPSILON > 0;
        }

        public float getCooldownTriggerTime() {
            return cooldownTriggerTime;
        }

        public void setCooldownTriggerTime(float cooldownTriggerTime) {
            this.cooldownTriggerTime = cooldownTriggerTime;
        }

        public float getRemainingCooldownTime() {
            return Math.max(0, cooldown - (GameTime.getCurrentTime() - cooldownTriggerTime));
        }

        public float getRemainingCooldownPercentage() {
            if ((cooldown - (GameTime.getCurrentTime() - cooldownTriggerTime)) <= 0) {
                return 0.0f;
            }
            return (cooldown - (GameTime.getCurrentTime() - cooldownTriggerTime)) / cooldown;
        }

        public void update() {
            if (getRemainingCooldownPercentage() - GameManagerAssets.EPSILON >= 0.0f) {
                cooldownTimer.setVisible(true);
                cooldownTimer.update(getRemainingCooldownPercentage());
                System.out.println("coolTime: " + getRemainingCooldownPercentage() );
            } else {
                cooldownTimer.setVisible(false);
            }
        }
    }

    /** visual cool down timer */
    class QuickSpellBarImageButton extends ImageButton {

        private final CooldownTimer cooldownTimer;
        private final float cooldown;

        private float cooldownTriggerTime = -Float.MAX_VALUE;

        public QuickSpellBarImageButton(ImageButtonStyle imageButtonStyle, float cooldown) {
            super(imageButtonStyle);
            this.cooldown = cooldown;

            this.cooldownTimer = new CooldownTimer(true, 52, 56); //50, 54);
            cooldownTimer.setPosition(0, 0);
            cooldownTimer.setColor(Color.WHITE);

            System.out.println("ColdownTimer QuickSpellbar : " + this.cooldownTimer);
            addActor(this.cooldownTimer);
        }

        public boolean isOnCooldown() {
            return getRemainingCooldownTime() - GameManagerAssets.EPSILON > 0;
        }

        public float getCooldownTriggerTime() {
            return cooldownTriggerTime;
        }

        public void setCooldownTriggerTime(float cooldownTriggerTime) {
            this.cooldownTriggerTime = cooldownTriggerTime;
        }

        public float getRemainingCooldownTime() {
            return Math.max(0, cooldown - (GameTime.getCurrentTime() - cooldownTriggerTime));
        }

        public float getRemainingCooldownPercentage() {
            if ((cooldown - (GameTime.getCurrentTime() - cooldownTriggerTime)) <= 0) {
                return 0.0f;
            }
            return (cooldown - (GameTime.getCurrentTime() - cooldownTriggerTime)) / cooldown;
        }

        public void update() {
            if (getRemainingCooldownPercentage() - GameManagerAssets.EPSILON >= 0.0f) {
                cooldownTimer.setVisible(true);
                cooldownTimer.update(getRemainingCooldownPercentage());
            } else {
                cooldownTimer.setVisible(false);
            }
        }
    }


    public boolean getJumpButtonHudPressed(){ return this.jumpButtonIsPressed; }
    public void setJumpButtonIsPressedFalse(){this.jumpButtonIsPressed = false;}

    public boolean getShotButtonHudPressed(){ return this.shotButtonIsPressed; }
    public void setShotButtonHudPressedFalse(){this.shotButtonIsPressed = false; }

    public void remove_PowerButton_Green_Add_PowerButton_Blue() {

        //System.out.println("ControllerJoyStickButtonStyle Class: Void: - Remove_Green add Blue weaponPower!! ");

        this.spellBarButtons.clear();
        this.button_list.clear();

// need input of order we chose power button - and check if there are amo to fire the power !!! etc
        button_list.add(new Button("jump", 0, new Action()));
        button_list.add(new Button("attack_two", 0.8f, new Action())); // 0.5f attack_two
        button_list.add(new Button("power_one", 1.5f, new Action()) );
        //button_list.add(new Button("power_two", 1.5f, new Action()) );
        button_list.add(new Button("meny_power", 0, new Action()) );
        button_list.add(new Button("power_chblue", 0, new Action()) );

        for (int count = 0; count < Math.min(button_list.size(), MAX_SLOTS); count ++) {

            //final Button button = ....
            final Button button = button_list.get(count);
            final String buttonName = button.getName().toLowerCase();

            //final Button button = new Button("attack", 2, new Action());
            //final Button b = button.get; // = spells.get(count);

            //String buttonName = "attack";

            ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
            imageButtonStyle.imageUp = new TextureRegionDrawable( textureAtlas.findRegion( buttonName +  GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_UP_SUFFIX ));
            imageButtonStyle.imageDown = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));
            imageButtonStyle.imageDisabled = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));



            //final SpellBarImageButton imageButton = new SpellBarImageButton(imageButtonStyle, button.getCooldown());
            final SpellBarImageButton imageButton = new SpellBarImageButton(imageButtonStyle, button.getCooldown());

            // TODO button lay out and position Jump Position is Correct :)
//System.out.println("ControllerStickButtonStyle Class: Test Position Extra button : " + count);

            //imageButton.setPosition(7 + count * 57, 8);


            switch (count){
                case 0:
                    imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 0 * 90, 54); // jump
                    break;
                case 1:
                    imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 167, 54); // Shoot
                    break;
                case 2:
                    imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 167, 170); // dash 230
                    break;
                //case 3:
                //    imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 0 * 90, 170); // 230 should be shiled is now dash
                //    break;
                case 3:
                    imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 97, 140); // meny button
                    break;
                case 4:
                    imageButton.setPosition( (GameUtility.V_WIDTH / 2) - 150 - 1 * 240, 190); // power_chose button
                    break;

            }

            if(count == 2){
                //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 90, 190); // Power Button - Far Right Location Over Main action buttons
            }else{
                // org. imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + count * 90, 70); // Main action buttons - Far Right Location
                //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + count * 90, 54); // Main action buttons - Far Right Location
            }

            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 0 * 65, 70);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 1 * 90, 120);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 2 * 90, 8);

            stage.addActor(imageButton);

            imageButton.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {

                    if (!((SpellBarImageButton)actor).isOnCooldown()) {

                        if( button.getName().equals("attack_one")) {

                            player.fire(  Integer.parseInt(player.getPlayerActvieShootingPower() ) ); //1 or 2
                            shotButtonIsPressed = true;
                            // message = "fire weapon 1";
                        }

                        if( button.getName().equals("attack_two")) {

                            player.fire(  Integer.parseInt(player.getPlayerActvieShootingPower() ) ); //1 or 2
                            shotButtonIsPressed = true;
                            //message = "fire weapon 2";
                        }

                        if( button.getName().equals("jump")) {

                            player.jump();
                            jumpButtonIsPressed = true;


                        }

                        if( button.getName().equals("power_one")) {

                            player.dash(1);
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("power_two")) {

                            //player.dash(1);
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("meny_power")) {

                            //player.dash(1);
                            gameMapShow = true;
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("power_chblue")) { // power_change
//System.out.println("test -power chblue, with inn remove_PowerButton_Green_Add_PowerButton_Blue() !! ");
                            power_Change_from_blue_pressed = true;

                        }


                        button.getAction().execute( button.getName() );
                        ((SpellBarImageButton)actor).setCooldownTriggerTime(GameTime.getCurrentTime());
                    }
                }
            });
            // HashMap ( button, imageButton )
            spellBarButtons.put(button, imageButton);
//System.out.println("ControllJoyStickButtonStyle Class: spellBarButtons " + spellBarButtons. );
        }
    }


    public void remove_PowerButton_Blue_Add_PowerButton_Green() {

        //System.out.println("ControllerJoyStickButtonStyle Class: Void: remove_PowerButton... - Remove_Blue add Green weaponPower!! ");

        this.spellBarButtons.clear();
        this.button_list.clear();

// need input of order we chose power button - and check if there are amo to fire the power !!! etc
        button_list.add(new Button("jump", 0, new Action()));
        button_list.add(new Button("attack_one", 0.8f, new Action())); // 0.5f attack_two
        button_list.add(new Button("power_one", 1.5f, new Action()) );
        //button_list.add(new Button("power_two", 1.5f, new Action()) );
        button_list.add(new Button("meny_power", 0, new Action()) );
        button_list.add(new Button("power_chgreen", 0, new Action()) );

        for (int count = 0; count < Math.min(button_list.size(), MAX_SLOTS); count ++) {

            //final Button button = ....
            final Button button = button_list.get(count);
            final String buttonName = button.getName().toLowerCase();

            //final Button button = new Button("attack", 2, new Action());
            //final Button b = button.get; // = spells.get(count);

            //String buttonName = "attack";

            ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
            imageButtonStyle.imageUp = new TextureRegionDrawable( textureAtlas.findRegion( buttonName +  GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_UP_SUFFIX ));
            imageButtonStyle.imageDown = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));
            imageButtonStyle.imageDisabled = new TextureRegionDrawable( textureAtlas.findRegion( buttonName + GameManagerAssets.TEXTURE_ATLAS_HUD_BUTTON_DOWN_SUFFIX));



            //final SpellBarImageButton imageButton = new SpellBarImageButton(imageButtonStyle, button.getCooldown());
            final SpellBarImageButton imageButton = new SpellBarImageButton(imageButtonStyle, button.getCooldown());

            // TODO button lay out and position Jump Position is Correct :)
//System.out.println("ControllerStickButtonStyle Class: Test Position Extra button : " + count);

            //imageButton.setPosition(7 + count * 57, 8);


            switch (count){
                case 0:
                    imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 0 * 90, 54); // jump
                    break;
                case 1:
                    imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 167, 54); // Shoot
                    break;
                case 2:
                    imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 167, 170); // dash 230
                    break;
                //case 3:
                //    imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 0 * 90, 170); // 230 should be shiled is now dash
                //    break;
                case 3:
                    imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 97, 140); // meny button
                    break;
                case 4:
                    imageButton.setPosition( (GameUtility.V_WIDTH / 2) - 150 - 1 * 240, 190); // power_chose button
                    break;

            }

            if(count == 2){
                //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + 1 * 90, 190); // Power Button - Far Right Location Over Main action buttons
            }else{
                // org. imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + count * 90, 70); // Main action buttons - Far Right Location
                //imageButton.setPosition( (GameUtility.V_WIDTH / 2) + 150 + count * 90, 54); // Main action buttons - Far Right Location
            }

            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 0 * 65, 70);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 1 * 90, 120);
            //imageButton.setPosition( GameUtility.V_WIDTH / 2 + 150 + 2 * 90, 8);

            stage.addActor(imageButton);

            imageButton.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {

                    if (!((SpellBarImageButton)actor).isOnCooldown()) {

                        if( button.getName().equals("attack_one")) {

                            player.fire(  Integer.parseInt(player.getPlayerActvieShootingPower() ) ); //1 or 2
                            shotButtonIsPressed = true;
                            // message = "fire weapon 1";
                        }

                        if( button.getName().equals("attack_two")) {

                            player.fire(  Integer.parseInt(player.getPlayerActvieShootingPower() ) ); //1 or 2
                            shotButtonIsPressed = true;
                            //message = "fire weapon 2";
                        }

                        if( button.getName().equals("jump")) {

                            player.jump();
                            jumpButtonIsPressed = true;


                        }

                        if( button.getName().equals("power_one")) {

                            player.dash(1);
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("power_two")) {

                            //player.dash(1);
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("meny_power")) {

                            //player.dash(1);
                            gameMapShow = true;
                            //boolPowerChangeButtonPressedTrue = true; // can't remember this one !!!???
                        }

                        if( button.getName().equals("power_chgreen")) { // power_change
//System.out.println("test -power chgreen, with inn remove_PowerButton_Blue_Add_PowerButton_Green()!! ");
                            System.out.println("ControllJoyStickButtonStyle change fire Blue amo count is " + player.getBallooneBulletBlue());
                            if(player.getBallooneBulletBlue() > 0) {
                                power_Change_from_green_pressed = true;
                            }
                        }


                        button.getAction().execute( button.getName() );
                        ((SpellBarImageButton)actor).setCooldownTriggerTime(GameTime.getCurrentTime());
                    }
                }
            });
            // HashMap ( button, imageButton )
            spellBarButtons.put(button, imageButton);
//System.out.println("ControllJoyStickButtonStyle Class: spellBarButtons " + spellBarButtons. );
        }
    }

    public void render(float delta) {
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//ToDo: change when we move form world or level last power in use, if that power has bullets left.
//ToDo: Done BluePower, next after etc !!

        //System.out.println("ControllJoyStickButtonStyle change fire Blue amo count is " + player.getBallooneBulletBlue());

        /**
         * playerActiveShootingPower - change from active pushing button in game or from moving player world or level = saveGame!!
         */

/*
        if(player.getPlayerActvieShootingPower() == "2" && power_IsGreenActive){

            remove_PowerButton_Green_Add_PowerButton_Blue();
            power_IsGreenActive = false;
            power_IsBlueActive = true;

        }else if(player.getPlayerActvieShootingPower() == "1" && power_IsBlueActive){

            remove_PowerButton_Blue_Add_PowerButton_Green();
            power_IsBlueActive = false;
            power_IsGreenActive = true;
        }
*/
  //      if(player.getPlayerActvieShootingPower() == "2" && player.getBallooneBulletBlue() == 0) {

            if(player.getBallooneBulletBlue() == 0 ) {

                //remove_PowerButton_Blue_Add_PowerButton_Green();
                player.setPlayerActiveShootingPower("1");
                power_Change_from_blue_pressed = false;
            }
    //    }


        if(power_Change_from_green_pressed) { // || player.getPlayerActvieShootingPower() == "2" ){
System.out.println("ControllerJoyStickButton -render: power_Change_from_green_pressed = true!! ");

            power_IsBlueActive = true;
            power_IsGreenActive = false;

            //remove_PowerButton_Green_Add_PowerButton_Blue();
            player.setPlayerActiveShootingPower("2");
            power_Change_from_green_pressed = false;
        }

        if(power_Change_from_blue_pressed){
System.out.println("ControllerJoyStickButton -render: power_Change_from_blue_pressed = true!! ");

            power_IsBlueActive = false;
            power_IsGreenActive = true;

            //remove_PowerButton_Blue_Add_PowerButton_Green();
            player.setPlayerActiveShootingPower("1");
            power_Change_from_blue_pressed = false;

        }
        stage.act(delta);
        stage.draw();
    }


    public void show() {
        /* So we can handle the back key on phone */
        Gdx.input.setInputProcessor(this.stage);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        //Gdx.input.setCatchBackKey(true); // deprecated

    }

    public void hide() {
        //Gdx.input.setInputProcessor(null);
        dispose();
    }
    /*
        public void draw(){

            stage.draw();


        }
    */
    public Stage getStage() {
        return stage;
    }



    public void resize(int width, int height){
        //viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }


    public void dispose() {
        stage.dispose();

    }

    public float getKnobPXLeft() {
        return touchpad.getKnobPercentX();
    }

    public float getKnobPYLeft() {
        return touchpad.getKnobPercentY();
    }

}

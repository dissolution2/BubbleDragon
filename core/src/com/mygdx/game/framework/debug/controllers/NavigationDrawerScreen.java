package com.mygdx.game.framework.debug.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.managers.GameManagerAssets;
import com.mygdx.game.framework.debug.screens.GameMapScreen;
import com.mygdx.game.framework.debug.screens.MainMenuScreen;
import com.mygdx.game.framework.debug.util.DrawerPauseScreenUtils;
import com.mygdx.game.framework.debug.util.GameUtility;

//TODO Bug Pause Screen get active with drag - should be false ???!!!
public class NavigationDrawerScreen {private Viewport viewport;
    private Stage stage;
    private NameGame gameName;
    private GameMapScreen gameMapScreen;

    private TextButton buttonPlay, buttonExit, buttonSettings, buttonGameMap;
    private Skin skin;

    final NavigationDrawer drawer;

    private boolean gameMapShow = false;

    private GameManagerAssets gameManagerAssetsInstance;

    public NavigationDrawerScreen(NameGame game, float nav_width, float nav_height, GameManagerAssets instance) {

        this.gameName = game;
        this.gameManagerAssetsInstance = instance;

        viewport = new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, gameName.batch);

        final Image image_background = new Image( DrawerPauseScreenUtils.getTintedDrawable( GameUtility.getTextureAtlas("drawerUi/menu_ui.atlas").findRegion("image_background"), Color.BLACK )); //  atlas.findRegion("image_background"), Color.BLUE));
        //final Image button_menu = new Image(Utility.getTextureAtlas("drawerUi/menu_ui.atlas").findRegion("button_menu"));
        final Image button_menu = new Image(GameUtility.getTextureAtlas("drawerUi/ButtonPauseUI.atlas").findRegion("button_pause"));

        initMenuButtons();

        //final NavigationDrawer drawer = new NavigationDrawer( nav_width, nav_height );//NAV_WIDTH, NAV_HEIGHT);
        drawer = new NavigationDrawer( nav_width, nav_height );//NAV_WIDTH, NAV_HEIGHT);
        /**
         * Remember to chose witch Listener we use ; Image's or Skin / Texture button's
         */
        // add items into drawer panel.

        buttonExit.setVisible(false);
        buttonGameMap.setVisible(false);
        buttonSettings.setVisible(false);


        drawer.add(buttonGameMap).size(150,40).expandX().row();
        drawer.add().size(20,20).expandX().row();
        drawer.add(buttonSettings).size(150,40).expandX().row();
        drawer.add().size(20,20).expandX().row();
        drawer.add(buttonExit).size(150,40).expandX().row();
        drawer.add().row();
        drawer.add().row();
        drawer.add().row();
        drawer.add().row();
        drawer.add().size(20,20).expandX().row();
        drawer.add().size(10,10).expandX().row();
        drawer.add().size(10,10).expandX().row();
        drawer.add().size(10,10).expandX().row();
        drawer.add().size(10,10).expandX().row();
        drawer.add().size(10,10).expandX().row();
        drawer.add().size(10,10).expandX().row();
        drawer.add().size(10,10).expandX().row();


        drawer.setBackground(image_background.getDrawable());
        drawer.bottom().left();

        /* z-index = 1 */
        // add image_background as a separating actor into stage to make smooth shadow with dragging value.
        image_background.setFillParent(true);

        stage.addActor(image_background);

        drawer.setFadeBackground(image_background, 0.5f);

        /* z-index = 2 */
        stage.addActor(drawer);


        /* z-index = 3 */
        // add button_menu as a separating actor into stage to rotates with dragging value.
        button_menu.setSize(48, 48);
        //button_menu.setOrigin(Align.center);
        //button_menu.setOrigin(Align.topRight);
        // turned of button rotation!!

        //System.out.println("V: " + Utility.V_WIDTH + " H: " + Utility.V_HEIGHT);
        //System.out.println("V: " + button_menu.getWidth() + " H: " + button_menu.getHeight() );

        button_menu.setPosition(GameUtility.V_WIDTH / 2 + 215 + 50 , GameUtility.V_HEIGHT / 2 + 125 + 50, Align.center); //.setOrigin(Align.center);

        stage.addActor(button_menu);

        drawer.setRotateMenuButton(button_menu, 90f);

        /** Optional **/
        Image image_shadow = new Image(GameUtility.getTextureAtlas("drawerUi/menu_ui.atlas").findRegion("image_shadow"));
        image_shadow.setHeight(nav_height);
        image_shadow.setX(nav_width);
        drawer.setAreaWidth(nav_width + image_shadow.getWidth());

        // was not included testing to add it ???
        drawer.setAreaHeight(nav_height + image_shadow.getImageHeight());

        drawer.addActor(image_shadow);

        // show the panel
        drawer.showManually(false);

//drawer.debug();

        /************ add item listener ***********/
        //icon_rate.setName("RATE");
        //icon_share.setName("SHARE");
        //icon_music.setName("MUSIC_ON");
        //icon_off_music.setName("MUSIC_OFF");
        button_menu.setName("BUTTON_MENU");

        image_background.setName("IMAGE_BACKGROUND"); // can remove it prob.

        ClickListener listener = new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                boolean closed = drawer.isCompletelyClosed();
                Actor actor = event.getTarget();

                if (actor.getName().equals("RATE")) {
                    System.out.println("Rate button clicked.");

                } else if (actor.getName().equals("SHARE")) {
                    System.out.println("Share button clicked.");

                } else if (actor.getName().equals("BUTTON_MENU") || actor.getName().equals("IMAGE_BACKGROUND")) {
                    System.out.println("Menu button clicked.");

                    /**  set all the other buttons visible    */
                    buttonExit.setVisible(true);
                    buttonGameMap.setVisible(true);
                    buttonSettings.setVisible(true);

                    if(gameManagerAssetsInstance.getGameState() != GameManagerAssets.GameState.GAME_PAUSED) {
                        gameManagerAssetsInstance.setGameState(GameManagerAssets.GameState.GAME_PAUSED);
                    }else {
                        gameManagerAssetsInstance.setGameState(GameManagerAssets.GameState.GAME_RESUMED);
                    }

                    System.out.println("Menu Button State: " + gameManagerAssetsInstance.getGameState());
                    System.out.println("Menu Button preState: " + gameManagerAssetsInstance.getPreGameState());


                    image_background.setTouchable(closed ? Touchable.enabled : Touchable.disabled);
                    drawer.showManually(closed);

                } else if (actor.getName().contains("MUSIC")) {
                    System.out.println("Music button clicked.");

                    //icon_music.setVisible(!icon_music.isVisible());
                    //icon_off_music.setVisible(!icon_off_music.isVisible());
                }
            }
        };

        /**
         * We gone use this for Main Pause Menu Button, and if we want to use Image as buttons with in the menu we use this listener
         */
        DrawerPauseScreenUtils.addListeners(listener, button_menu ); //, image_background);//icon_rate, icon_share, icon_music, icon_off_music, button_menu, image_background);

    }

    /**
     * We gone use this if we like to use skin and buttons for menu!!!
     */
    public void initMenuButtons() {

        // Listener fault because utils.addList etc is from Image ???

        this.skin = new Skin();
        this.skin.addRegions(GameUtility.assetManager.get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.add("default-font", gameName.font24);//app.font24);
        this.skin.load(Gdx.files.internal("ui/uiskin.json"));

        buttonGameMap  = new TextButton("Game Map", skin, "default");

        buttonGameMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //MainMenuScreen.this.notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_TITLE);
                System.out.println("GameMap button clicked.");

                gameMapShow = true;
                //gameMapScreen = new GameMapScreen((MyGdxGame)gameName);


                //gameName.setScreen(new MainMenuScreen((MyGdxGame)gameName,
                //		GameManagerAssets.instance.getCurrentWorld() ));
            }
        });

        buttonSettings = new TextButton("Game Setting", skin, "default");

        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //MainMenuScreen.this.notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_TITLE);
                System.out.println("Settings button clicked.");
                //gameName.setScreen(new MainMenuScreen((MyGdxGame)gameName,
                //		GameManagerAssets.instance.getCurrentWorld() ));
            }
        });

        buttonExit = new TextButton("Quit No Save", skin, "default");

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //MainMenuScreen.this.notify(AudioObserver.AudioCommand.MUSIC_STOP, AudioObserver.AudioTypeEvent.MUSIC_TITLE);
                System.out.println("Exit button clicked.");

                // happens if we have a boss fight but not save position only player
                // life etc... only in boss
                //GameManagerAssets.instance.setSaveGamePlayerStatsWithoutPosition();


                gameManagerAssetsInstance.gameManagerPlayerDataReadWriteSave();
                System.out.println("NavigationDrawerScreen Class Exit We Write saveHolderClass to file!!");

                gameName.setScreen(new MainMenuScreen((NameGame)gameName,
                        gameManagerAssetsInstance.getCurrentWorld(), gameManagerAssetsInstance ));
                //gameName.dispose();
            }
        });
    }

    public boolean getGameMapShow(){
        return gameMapShow;
    }

    public void update() {
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public NavigationDrawer setPauseMenuActive(){

        buttonExit.setVisible(true);
        buttonGameMap.setVisible(true);
        buttonSettings.setVisible(true);

        return drawer;
    }

    public void resize(int width, int height){
        //viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        System.out.println("NavigationDrawerScreen is disposed!!");

    }

}


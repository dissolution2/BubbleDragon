package com.mygdx.game.framework.debug.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.framework.debug.NameGame;
import com.mygdx.game.framework.debug.sprites.BubblePlayer;
import com.mygdx.game.framework.debug.util.GameUtility;


import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;

public class GameMapScreen {

    private Stage stage;
    private NameGame game;
    private Viewport viewport;

    private boolean
            exitPressed,
            pauseMenuIsHidden;
    private Window gameMap;
    private Window gameMap_two;

    private TextureRegion imgPower_Chosen_one;



    /**change*/
    private Image imgPowerChosenOne, imgPowerChosenTwo, imgWeaponChosen, imgPowerUnlocked_One, imgPowerUnlocked_Two, imgPowerUnlocked_Three, imgPowerLocked_One, imgPowerLocked_Two, imgPowerLocked_Three, imgPowerWeaponDefault;
    private Image imgWeaponChosenGreen, imgWeaponChosenBlue;
    private Image imgLifePuzzleEmpty, imgLifePuzzleOne, imgLifePuzzleTwo, imgLifePuzzleThree, imgLifePuzzleFour;


    private Skin skin;
    private TextureAtlas textureAtlas_Skin;


    private ImageButtonStyle closeButtonStyle_3;

    private ImageButtonStyle closeButtonStyle,  mainButtonCharacterStyle, mainButtonWeaponStyle, mainButtonSkillsStyle,  clearAllButtonStyle;
    private ImageButtonStyle weaponButtonStyle_1, weaponButtonStyle_PowerBlue,weaponButtonStyle_3,weaponButtonStyle_4;
    private ImageButtonStyle choseButtonStyle;

    private ImageButtonStyle skillButton_Style_1, skillButton_Style_2, skillButton_Style_3, skillButton_Style_4;

    private Button powerButtonPlayerHaveChosenToUseSetActive;

    private Label label_power_one, label_power_two, label_skills_one, power_discription_message_Label, power_one_Label,power_two_Label, power_three_Label, power_four_Label, power_five_Label, power_six_Label;

    private Label label_power_And_Skill_Name, label_power_And_Skill_Discription;

    /** Label over Menu player's -Player,Weapon and Skills */
    private Label label_menu_player_text, label_menu_weapon_text, label_menu_skills_text;

    private Label label_player_descriptian_headline, label_weapon_descriptian_headline, label_skills_descriptian_headline;


    private Label player_info_label;

    private String powerStringOne, discriptionOfPowerText;
    private Window.WindowStyle windowStyle, windowStyle_two_test;






    private boolean menu_PlayerButtonIsPressed = false;
    private boolean menu_WeaponButtonIsPressed = false;
    private boolean menu_SkillsButtonIsPressed = false;



    private Table tableMainFirstWindow, tableMainSecondWindow, tableMainButton, tablePowerTextOfPowerInUse, tablePowerImageOfPowerInUse, tablePowerTextOfPowerLabel, tablePowerButtonsToChoseFrom;

    private Table table_Player_Button_Menu, table_Weapon_Button_Menu ,table_Skill_Button_Menu;

    private Table table_To_Chose_Life_Weapon_Skills, table_Discription_Add_From_Life_Weapon_Skills;
    private Table tablePowerTextOfPowerDiscription;

    private Table table_Content_for_SecondWindow_First_Table_View, table_Content_for_SecondWindow_Secondary_Table_View;


    private Button mainButtonCharacter, mainButtonWeapons, mainButtonSkills, clearAllButton; //, powerBlueImgButton;

    private Button weaponButtonPowerGreen, weaponButtonPowerBlue,weaponButtonImage_3,weaponButtonImage_4;
    private Button skillButtonImage_1, skillButtonImage_2, skillButtonImage_3, skillButtonImage_4;


    // working here!!! new
    private Label labelHeadLineOne, labelHeadLineTwo, labelHeadLineThree;
    private Label label_Amunition_text ,label_Damage_text, label_Amunition_AmountCount, label_Damage_DamageAmount;
    private Label label_lifeForce_text;

    Table tableTextHeadLineOne, tableTextHeadLineTwo, tableTextHeadLineThree;

    private boolean weaponButton_oneIsPressed, weaponButton_twoIsPressed, weaponButton_threeIsPressed, weaponButton_fourIsPressed;
    private Button testButtonImage_11, testButtonImage_1, testButtonImage_2, testButtonImage_3, testButtonImage_4;

    private Image imgPowerBlueDiscription;

    private boolean buttonPlayerPowerIsActive, buttonWeaponPowerIsActive, buttonSkillPowerIsActive;


    private String weaponButtonPressed, skilleButtonPressed;

    private BubblePlayer player;
    public GameMapScreen(NameGame g, BubblePlayer p) { // public GameMapScreen(NameGame g, final BubblePlayer player) {


        this.game = g;
        this.player = p;
        //stage = new Stage(new FitViewport(Utility.V_WIDTH, Utility.V_HEIGHT ));
        viewport = new FitViewport(GameUtility.V_WIDTH, GameUtility.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch); //new StretchViewport(200f,200f, new OrthographicCamera()), game.batch); //viewport, game.batch);


        //imgPower_Chosen_one = new TextureRegion( GameUtility.getTextureAtlas("controllerUi/BubbleGameUI.atlas").findRegion("attack_one_up"), 0, 0, 52, 56);
        skin = new Skin();
        textureAtlas_Skin = GameUtility.getTextureAtlas("gameMapScreen/GameMapScreenAssets.atlas"); //"controllerUi/BubbleGameUI.atlas");
        skin.addRegions( textureAtlas_Skin );

        weaponButton_oneIsPressed = false;
        weaponButton_twoIsPressed = false;
        weaponButton_threeIsPressed = false;
        weaponButton_fourIsPressed = false;

        buttonPlayerPowerIsActive = false;
        buttonWeaponPowerIsActive = false;
        buttonSkillPowerIsActive = false;

        weaponButtonPressed = "1";// 1 as default


        //tableContainer = new Container<Table>();

        /**Main Table*/
        tableMainFirstWindow = new Table();
        tableMainSecondWindow = new Table();

        tableMainButton = new Table();

        tableTextHeadLineOne = new Table();
        tableTextHeadLineTwo = new Table();
        tableTextHeadLineThree = new Table();

        tablePowerImageOfPowerInUse = new Table();
        tablePowerButtonsToChoseFrom = new Table();





        /**image to table's*/
        imgPowerChosenOne = new Image(skin.getDrawable("powerImgSword60"));//"power_one_up"));
        imgPowerChosenOne.setWidth(22);
        imgPowerChosenOne.setHeight(26);
        imgPowerChosenOne.setX(0);
        imgPowerChosenOne.setY(0);

        imgPowerChosenTwo = new Image(skin.getDrawable("powerImgShild60"));//("power_two_up"));
        imgPowerChosenOne.setWidth(22);
        imgPowerChosenOne.setHeight(26);
        imgPowerChosenOne.setX(0);
        imgPowerChosenOne.setY(0);

        imgWeaponChosen = new Image(skin.getDrawable("powerImgBlue60"));//("attack_one_up"));
        imgPowerChosenOne.setWidth(22);
        imgPowerChosenOne.setHeight(26);
        imgPowerChosenOne.setX(0);
        imgPowerChosenOne.setY(0);


        imgWeaponChosenGreen = new Image(skin.getDrawable("powerImgGreen60"));//("attack_one_up"));
        imgWeaponChosenGreen.setWidth(22);
        imgWeaponChosenGreen.setHeight(26);
        imgWeaponChosenGreen.setX(0);
        imgWeaponChosenGreen.setY(0);

        imgWeaponChosenBlue = new Image(skin.getDrawable("powerImgBlue60"));//("attack_one_up"));
        imgWeaponChosenBlue.setWidth(22);
        imgWeaponChosenBlue.setHeight(26);
        imgWeaponChosenBlue.setX(0);
        imgWeaponChosenBlue.setY(0);




/** don't know need button if there are two img/buttons to get info */
        imgLifePuzzleEmpty = new Image(skin.getDrawable("heartPuzzleEmpty"));
        imgLifePuzzleEmpty.setWidth(22);
        imgLifePuzzleEmpty.setHeight(26);
        imgLifePuzzleEmpty.setX(0);
        imgLifePuzzleEmpty.setY(0);

        imgLifePuzzleOne = new Image(skin.getDrawable("heartPuzzleOne"));
        imgLifePuzzleOne.setWidth(22);
        imgLifePuzzleOne.setHeight(26);
        imgLifePuzzleOne.setX(0);
        imgLifePuzzleOne.setY(0);

        imgLifePuzzleTwo = new Image(skin.getDrawable("heartPuzzleTwo"));
        imgLifePuzzleTwo.setWidth(22);
        imgLifePuzzleTwo.setHeight(26);
        imgLifePuzzleTwo.setX(0);
        imgLifePuzzleTwo.setY(0);

        imgLifePuzzleThree = new Image(skin.getDrawable("heartPuzzleThree"));
        imgLifePuzzleThree.setWidth(22);
        imgLifePuzzleThree.setHeight(26);
        imgLifePuzzleThree.setX(0);
        imgLifePuzzleThree.setY(0);

        imgLifePuzzleFour = new Image(skin.getDrawable("heartPuzzleFour"));
        imgLifePuzzleFour.setWidth(22);
        imgLifePuzzleFour.setHeight(26);
        imgLifePuzzleFour.setX(0);
        imgLifePuzzleFour.setY(0);




        imgPowerUnlocked_One = new Image(skin.getDrawable("powerImgEmptyPowerUnlocked60"));//("attack_one_up"));
        imgPowerUnlocked_One.setWidth(22);
        imgPowerUnlocked_One.setHeight(26);
        imgPowerUnlocked_One.setX(0);
        imgPowerUnlocked_One.setY(0);

        imgPowerUnlocked_Two = new Image(skin.getDrawable("powerImgEmptyPowerUnlocked60"));//("attack_one_up"));
        imgPowerUnlocked_Two.setWidth(22);
        imgPowerUnlocked_Two.setHeight(26);
        imgPowerUnlocked_Two.setX(0);
        imgPowerUnlocked_Two.setY(0);

        imgPowerUnlocked_Three = new Image(skin.getDrawable("powerImgEmptyPowerUnlocked60"));//("attack_one_up"));
        imgPowerUnlocked_Three.setWidth(22);
        imgPowerUnlocked_Three.setHeight(26);
        imgPowerUnlocked_Three.setX(0);
        imgPowerUnlocked_Three.setY(0);

        imgPowerLocked_One = new Image(skin.getDrawable("powerImgEmptyPowerLocked60"));//("attack_one_up"));
        imgPowerLocked_One.setWidth(22);
        imgPowerLocked_One.setHeight(26);
        imgPowerLocked_One.setX(0);
        imgPowerLocked_One.setY(0);

        imgPowerWeaponDefault = new Image(skin.getDrawable("powerImgGreen60"));//("attack_one_up"));
        imgPowerWeaponDefault.setWidth(22);
        imgPowerWeaponDefault.setHeight(26);
        imgPowerWeaponDefault.setX(0);
        imgPowerWeaponDefault.setY(0);

        imgPowerBlueDiscription = new Image(skin.getDrawable("powerImgBlue60"));//("attack_one_up"));
        imgPowerBlueDiscription.setWidth(22);
        imgPowerBlueDiscription.setHeight(26);
        imgPowerBlueDiscription.setX(0);
        imgPowerBlueDiscription.setY(0);




        // Layout with Table
/*
		Table table = new Table();
		table.setFillParent(true);

		Image title = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("bludbourne_title"));
		TextButton newGameButton = new TextButton("New Game", Utility.STATUSUI_SKIN);

		//Layout Table organized
		table.add(title).spaceBottom(75).row();
		table.add(newGameButton).spaceBottom(10).row();

*/
        // ToDo: move all into one atlas a must!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("windows.pack"));
        TextureAtlas textureAtlasButtons = new TextureAtlas(Gdx.files.internal("gameMapScreen/GameMapScreenAssets.atlas")); //"controllerUi/BubbleGameUI.atlas"));

        //windowStyle = new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(textureAtlas.findRegion("window-1-background")));


        gameMap = new Window("", GameUtility.STATUSUI_SKIN); // OR USE // windowStyle);

        gameMap_two = new Window("", GameUtility.STATUSUI_SKIN); // OR USE // windowStyle);

//gameMap.setDebug(true);
        //gameMap.setFillParent(true);
        powerStringOne = "40";
        //discriptionOfPowerText = "This power comes at a dark price";
        label_power_one = new Label("-Power's In Use-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        label_power_two = new Label("-Chose your Power's-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));

        player_info_label = new Label("-   Player Info   -", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));


        label_skills_one = new Label("-Chose your Skill's-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));


        label_menu_player_text = new Label("-PLAYER-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        label_menu_weapon_text = new Label("-WEAPON-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        label_menu_skills_text = new Label("-SKILLS-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));


        //label_player_descriptian_headline = new Label("-Power in Use-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        //label_weapon_descriptian_headline = new Label("-Life-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        //label_skills_descriptian_headline = new Label("-Skills-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));

        label_power_And_Skill_Discription = new Label("-Power's or skill Description\n " +
                "this power is so dark you need a flash light to see it-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        label_power_And_Skill_Name = new Label("-Power's Name", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));

        power_one_Label = new Label("label 1 " + powerStringOne, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        power_two_Label = new Label("label 2", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        power_three_Label = new Label("label 3", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        power_four_Label = new Label("label 4", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        power_five_Label = new Label("label 5", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        power_six_Label = new Label("label 6", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        power_discription_message_Label = new Label("" + discriptionOfPowerText, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        // or see through
        //gameMap = new Window("", GameUtility.STATUSUI_SKIN);


        label_Amunition_text = new Label("AMO", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        label_Damage_text = new Label("DMG", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        label_Amunition_AmountCount = new Label("-", new Label.LabelStyle(new BitmapFont(), Color.MAROON));
        label_Damage_DamageAmount = new Label("1", new Label.LabelStyle(new BitmapFont(), Color.MAROON));


        label_lifeForce_text = new Label("Life Force Crystal picked up", new Label.LabelStyle(new BitmapFont(), Color.MAROON));


        /** First View of players GameMapScreen */
        labelHeadLineOne = new Label("-Power in Use-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        labelHeadLineTwo = new Label("-Life-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));
        labelHeadLineThree = new Label("-Life Force Crystal-", new Label.LabelStyle(new BitmapFont(), Color.GOLDENROD));


        closeButtonStyle = new ImageButtonStyle();
        closeButtonStyle.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("windowCloseButton"));
        Button closeButton = new ImageButton(closeButtonStyle);
        closeButton.addListener(new ClickListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        exitPressed = true;
                                        System.out.println("Button Close");
                                        hide();
                                        return true;
                                    }

                                    @Override
                                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                                        //System.out.println("Button pressed in Menu Window down");
                                        exitPressed = false;

                                    }
                                }
        );

        clearAllButtonStyle = new ImageButtonStyle();
        clearAllButtonStyle.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("up"));
        clearAllButtonStyle.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("down"));
        //clearAllButtonStyle.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("disabled"));
        clearAllButton = new ImageButton(clearAllButtonStyle);
        clearAllButton.addListener(new ClickListener() {
                                       @Override
                                       public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                           System.out.println("Button ClearAll pressed!! ");
                                           return true;
                                       }

                                       @Override
                                       public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                                       }


                                   }
        );

        choseButtonStyle = new ImageButtonStyle();
        choseButtonStyle.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("addButton60_Up"));
        choseButtonStyle.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("addButton60_Down"));
        choseButtonStyle.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("addButton60_Disabled"));
        powerButtonPlayerHaveChosenToUseSetActive = new ImageButton(choseButtonStyle);
        powerButtonPlayerHaveChosenToUseSetActive.addListener(new ClickListener() {
                                           @Override
                                           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                               if(powerButtonPlayerHaveChosenToUseSetActive.isDisabled()){
                                                   System.out.println("choseButton pressed, Is disabled - but weapon number is: " + weaponButtonPressed);
                                               }else {
                                                   System.out.println("choseButton pressed, weapon number is: " + weaponButtonPressed);
                                                   player.setPlayerActiveShootingPower(weaponButtonPressed);
                                               }
                                               return true;
                                           }

                                           @Override
                                           public void touchUp (InputEvent event, float x, float y, int pointer, int button) {



                                           }
                                       }
        );

        mainButtonCharacterStyle = new ImageButtonStyle();
        mainButtonCharacterStyle.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("mainButtonCharacter_Up"));

        mainButtonCharacterStyle.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("mainButtonCharacter_Down2"));

        //mainButtonCharacterStyle.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLocked60"));

        mainButtonCharacter = new ImageButton(mainButtonCharacterStyle);
        mainButtonCharacter.addListener(new ClickListener() {
                                           @Override
                                           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                               System.out.println("Button MainCharacter pressed!! ");
                                               menu_PlayerButtonIsPressed= true;
// test remove next line, this will change locked img to a power or skilled img // set's it active.
// clearChildren() of table and set img , then set set it active.

                                               return true;
                                           }

                                           @Override
                                           public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                                           }


                                       }
        );
        mainButtonWeaponStyle = new ImageButtonStyle();
        mainButtonWeaponStyle.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("mainButtonWeapons_Up"));

        mainButtonWeaponStyle.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("mainButtonWeapons_Down2"));

        //mainButtonCharacterStyle.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLocked60"));

        mainButtonWeapons = new ImageButton(mainButtonWeaponStyle);
        mainButtonWeapons.addListener(new ClickListener() {
                                            @Override
                                            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                System.out.println("Button MainWeapon pressed!! ");
                                                menu_WeaponButtonIsPressed= true;
                                                buttonWeaponPowerIsActive = true;
/*
                                               if(!powerBlueImgButton.isDisabled()) {
                                                   System.out.println("Button 1");
                                                   testButtonIsPressed = true;
                                               }
*/
                                                return true;
                                            }

                                            @Override
                                            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                                            }


                                        }
        );

        mainButtonSkillsStyle = new ImageButtonStyle();
        mainButtonSkillsStyle.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("mainButtonSkills_Up"));

        mainButtonSkillsStyle.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("mainButtonSkills_Down2"));

        //mainButtonCharacterStyle.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLocked60"));

        mainButtonSkills = new ImageButton(mainButtonSkillsStyle);
        mainButtonSkills.addListener(new ClickListener() {
                                          @Override
                                          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                              System.out.println("Button MainSkills pressed!! ");
                                              menu_SkillsButtonIsPressed = true;
/*
                                               if(!powerBlueImgButton.isDisabled()) {
                                                   System.out.println("Button 1");
                                                   testButtonIsPressed = true;
                                               }
*/
                                              return true;
                                          }

                                          @Override
                                          public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                                          }


                                      }
        );

        weaponButtonStyle_1 = new ImageButtonStyle();
        weaponButtonStyle_1.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgGreenPowerN70_Up"));
        weaponButtonStyle_1.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgGreenPowerN70_Down"));
        weaponButtonStyle_1.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));

        weaponButtonPowerGreen = new ImageButton(weaponButtonStyle_1);
        weaponButtonPowerGreen.addListener(new ClickListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        if(buttonWeaponPowerIsActive) {
                                            weaponButton_oneIsPressed = true;
                                            System.out.println("Button 1");
                                            weaponButtonPressed = "1";
                                        }
/*
                                        if(!powerBlueImgButton.isDisabled()) {
                                            System.out.println("Button 1");
                                            //testButtonIsPressed = true;
                                        }
*/
                                        return true;
                                    }

                                    @Override
                                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                                    }


                                }
        );



        weaponButtonStyle_PowerBlue = new ImageButtonStyle();
        weaponButtonStyle_PowerBlue.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgBluePowerN70_Up"));
        weaponButtonStyle_PowerBlue.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgBluePowerN70_Down"));
        weaponButtonStyle_PowerBlue.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));

        weaponButtonPowerBlue = new ImageButton(weaponButtonStyle_PowerBlue);
        weaponButtonPowerBlue.addListener(new ClickListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        if(buttonWeaponPowerIsActive) {
                                            weaponButton_twoIsPressed = true;
                                            System.out.println("Button 2");
                            System.out.println("Player Blue ammunition is: " + player.getBallooneBulletBlue());
                                            System.out.println("isChosenButton " + powerButtonPlayerHaveChosenToUseSetActive.isDisabled());


                                            /** Get Player's Ammunition  !!*/
                                            if(player.getBallooneBulletBlue() < 1) {
                                                powerButtonPlayerHaveChosenToUseSetActive.setDisabled(true);
                                            }

                                            if(!powerButtonPlayerHaveChosenToUseSetActive.isDisabled()){
                                                weaponButtonPressed = "2";
                                            }
                                        }
                                        return true;
                                    }

                                    @Override
                                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {



                                    }
                                }
        );




        weaponButtonStyle_3 = new ImageButtonStyle();
        weaponButtonStyle_3.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        weaponButtonStyle_3.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        weaponButtonStyle_3.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));

        weaponButtonImage_3 = new ImageButton(weaponButtonStyle_3);
        weaponButtonImage_3.addListener(new ClickListener() {
                                          @Override
                                          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                              if(buttonWeaponPowerIsActive) {
                                                  weaponButton_threeIsPressed = true;
                                                  System.out.println("Button 3");
                                                  weaponButtonPressed = "3";
                                              }
                                              return true;
                                          }

                                          @Override
                                          public void touchUp (InputEvent event, float x, float y, int pointer, int button) {



                                          }
                                      }
        );

        weaponButtonStyle_4 = new ImageButtonStyle();
        weaponButtonStyle_4.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        weaponButtonStyle_4.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        weaponButtonStyle_4.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));

        weaponButtonImage_4 = new ImageButton(weaponButtonStyle_4);
        weaponButtonImage_4.addListener(new ClickListener() {
                                          @Override
                                          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                              if(buttonWeaponPowerIsActive) {
                                                  weaponButton_fourIsPressed = true;
                                                  System.out.println("Button 4");
                                                  weaponButtonPressed = "4";
                                              }
                                              return true;
                                          }

                                          @Override
                                          public void touchUp (InputEvent event, float x, float y, int pointer, int button) {



                                          }
                                      }
        );

        skillButton_Style_1 = new ImageButtonStyle();
        skillButton_Style_1.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        skillButton_Style_1.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        skillButton_Style_1.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));

        skillButtonImage_1 = new ImageButton(skillButton_Style_1);
        skillButtonImage_1.addListener(new ClickListener() {
                                          @Override
                                          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                              System.out.println("skill button 1");
                                              return true;
                                          }

                                          @Override
                                          public void touchUp (InputEvent event, float x, float y, int pointer, int button) {



                                          }
                                      }
        );

        skillButton_Style_2 = new ImageButtonStyle();
        skillButton_Style_2.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        skillButton_Style_2.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        skillButton_Style_2.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));

        skillButtonImage_2 = new ImageButton(skillButton_Style_2);
        skillButtonImage_2.addListener(new ClickListener() {
                                          @Override
                                          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                              System.out.println("skill button 2");
                                              return true;
                                          }

                                          @Override
                                          public void touchUp (InputEvent event, float x, float y, int pointer, int button) {



                                          }
                                      }
        );

        skillButton_Style_3 = new ImageButtonStyle();
        skillButton_Style_3.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        skillButton_Style_3.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        skillButton_Style_3.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));

        skillButtonImage_3 = new ImageButton(skillButton_Style_3);
        skillButtonImage_3.addListener(new ClickListener() {
                                          @Override
                                          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                              System.out.println("skill button 3");
                                              return true;
                                          }

                                          @Override
                                          public void touchUp (InputEvent event, float x, float y, int pointer, int button) {



                                          }
                                      }
        );

        skillButton_Style_4 = new ImageButtonStyle();
        skillButton_Style_4.imageUp = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        skillButton_Style_4.imageDown = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));
        skillButton_Style_4.imageDisabled = new TextureRegionDrawable(textureAtlasButtons.findRegion("powerImgEmptyPowerLockedN70"));

        skillButtonImage_4 = new ImageButton(skillButton_Style_4);
        skillButtonImage_4.addListener(new ClickListener() {
                                          @Override
                                          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                              System.out.println("skill button 4");
                                              return true;
                                          }

                                          @Override
                                          public void touchUp (InputEvent event, float x, float y, int pointer, int button) {



                                          }
                                      }
        );





        gameMap.getTitleTable().add(closeButton).size(38, 38).padLeft(-295).padTop(-5);

        table_Player_Button_Menu = new Table();
        table_Weapon_Button_Menu = new Table();
        table_Skill_Button_Menu = new Table();


        /**Begin -First init of table menu */
        table_Player_Button_Menu.add(mainButtonCharacter);
        table_Weapon_Button_Menu.add(mainButtonWeapons);
        table_Skill_Button_Menu.add(mainButtonSkills);


        tableMainFirstWindow.add(label_menu_player_text).colspan(28).row();
        tableMainFirstWindow.add().size(10).colspan(28).row();
        tableMainFirstWindow.add(table_Player_Button_Menu).colspan(28).row();

        tableMainFirstWindow.add().size(40).colspan(28).row();

        tableMainFirstWindow.add(label_menu_weapon_text).colspan(28).row();
        tableMainFirstWindow.add().size(10).colspan(28).row();
        tableMainFirstWindow.add(table_Weapon_Button_Menu).colspan(28).row();

        tableMainFirstWindow.add().size(40).colspan(28).row();

        tableMainFirstWindow.add(label_menu_skills_text).colspan(28).row();
        tableMainFirstWindow.add().size(10).colspan(28).row();
        tableMainFirstWindow.add(table_Skill_Button_Menu).colspan(28).row();
        /**End -First init of table menu */

        table_Content_for_SecondWindow_First_Table_View = new Table();
        table_Content_for_SecondWindow_Secondary_Table_View = new Table();


        /**Begin -First init of SecondWindow : Player life info */
        table_Content_for_SecondWindow_First_Table_View.add(imgLifePuzzleTwo);
        /**End -First init of SecondWindow : Player life info */



        /**Begin -Second init of SecondWindow : Player life info */
        table_Content_for_SecondWindow_Secondary_Table_View.add(imgLifePuzzleEmpty);
        /**End -Second init of SecondWindow : Player life info */

        //First top table added to the second window main table
        tableMainSecondWindow.add(table_Content_for_SecondWindow_First_Table_View).row();
        //Next table added to the second window main table
        tableMainSecondWindow.add(table_Content_for_SecondWindow_Secondary_Table_View);

        /** Player Table */
        /** Weapon Table */
        /** Skills Table */



        gameMap.add(tableMainFirstWindow).padLeft(-30).padTop(-10); //-140 -10
        gameMap_two.add(tableMainSecondWindow);

        gameMap.setMovable(false);
        gameMap.setResizable(true);

        gameMap_two.setMovable(false);
        gameMap_two.setResizable(true);


        //gameMap.padTop(50);
        //gameMap.pack();

/**Debug*/

        //tableMainFirstWindow.setDebug(true);
        //table_Discription_Add_From_Life_Weapon_Skills.setDebug(true);


        TextButton continueGameButton = new TextButton("Continue", GameUtility.STATUSUI_SKIN);
        continueGameButton.addListener(new ClickListener() {
                                           @Override
                                           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                               exitPressed = true;
                                               System.out.println("Button pressed in Menu Window up");
                                              hide();
                                               //Gdx.app.exit();
                                               return true;
                                           }

                                           @Override
                                           public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                                               //devilGame.setScreen(new MainGameScreen((Devil) devilGame));
                                               System.out.println("Button pressed in Menu Window down");
                                               exitPressed = false;

                                           }
                                       }
        );

        TextButton backToMainMenuGameButton = new TextButton("Main Menu", GameUtility.STATUSUI_SKIN);
        backToMainMenuGameButton.addListener(new ClickListener() {
                                                 @Override
                                                 public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                                     // Here we should make a end splase screen or a transition screen etc.
                                                     return true;
                                                 }

                                                 @Override
                                                 public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                                                     //devilGame.setScreen(new MainGameScreen((Devil) devilGame));
                                                     //System.out.println("Button pressed in Menu Window");

                                                     System.out.println("Back to menu Pressed!!");

                                                     //devilGame.setScreen(new MainMenuScreen((Devil) devilGame ));
                                                     //dispose();
                                                 }
                                             }
        );


        gameMap.setSize(GameUtility.V_WIDTH / 4.4f, GameUtility.V_HEIGHT / 1.2f); // org 1.5f
        gameMap.setPosition(GameUtility.V_WIDTH / 2 - gameMap.getWidth() / 2 -250, GameUtility.V_HEIGHT / 2 - gameMap.getHeight() / 2 );

        gameMap_two.setSize(GameUtility.V_WIDTH / 2.4f, GameUtility.V_HEIGHT / 1.2f); // org 1.5f
        gameMap_two.setPosition(GameUtility.V_WIDTH / 2 - gameMap.getWidth() / 2 , GameUtility.V_HEIGHT / 2 - gameMap.getHeight() / 2 );

        stage.addActor(gameMap); //gameMap);
        stage.addActor(gameMap_two);

    }

    public void show() {

        gameMap.setVisible(true);

        if(menu_WeaponButtonIsPressed){

            tableMainSecondWindow.clearChildren();

            table_Content_for_SecondWindow_First_Table_View.clearChildren();
            table_Content_for_SecondWindow_First_Table_View.add(imgPowerUnlocked_One);
            table_Content_for_SecondWindow_First_Table_View.add(imgPowerUnlocked_Two);
            table_Content_for_SecondWindow_First_Table_View.add(imgPowerUnlocked_Three);

            table_Content_for_SecondWindow_Secondary_Table_View.clearChildren();
            table_Content_for_SecondWindow_Secondary_Table_View.add(weaponButtonPowerGreen);
            table_Content_for_SecondWindow_Secondary_Table_View.add().size(10);
            table_Content_for_SecondWindow_Secondary_Table_View.add(weaponButtonPowerBlue);
            table_Content_for_SecondWindow_Secondary_Table_View.add().size(10);
            table_Content_for_SecondWindow_Secondary_Table_View.add(powerButtonPlayerHaveChosenToUseSetActive);
            //table_Content_for_SecondWindow_Secondary_Table_View.add();

            tableMainSecondWindow.add(table_Content_for_SecondWindow_First_Table_View);
            tableMainSecondWindow.add().row();
            tableMainSecondWindow.add().size(15);
            tableMainSecondWindow.add().row();
            tableMainSecondWindow.add(table_Content_for_SecondWindow_Secondary_Table_View);

        }


    }

    public void hide() {
        gameMap.setVisible(false);
    }


    public void setActorWindowVisible(boolean value){this.gameMap.setVisible(value); }

    public void draw(){
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public boolean getExitWindow(){return this.exitPressed; }

    public void resize(int width, int height){
        viewport.update(width, height);
    }


    public boolean isExitPressed() {
        return exitPressed;
    }


    public void dispose() {
        stage.dispose();
    }
}

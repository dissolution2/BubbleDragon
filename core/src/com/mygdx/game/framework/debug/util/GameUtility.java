package com.mygdx.game.framework.debug.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class GameUtility {

    private static final String TAG = GameUtility.class.getSimpleName();

    public static final AssetManager assetManager = new AssetManager();

    private static InternalFileHandleResolver filePathResolver =  new InternalFileHandleResolver();

    /*
    // org size
    public static final int V_WIDTH = 640;
    public static final int V_HEIGHT = 420;

    // looks good, longer away from camera , need to solve pause menu larger size !!
    public static final int V_WIDTH = 800;
    public static final int V_HEIGHT = 525;

    // Org Size of Ps painting - looks god, but ratio between player , assets and layer Painting is wrong
    public static final int V_WIDTH = 2560;
    public static final int V_HEIGHT = 1280;


    // Way to large
    public static final int V_WIDTH = 480;
    public static final int V_HEIGHT = 320;
    */

    //Virtual Screen size and Box2D Scale(Pixels Per Meter)
    public static final int V_WIDTH = 800; //1280; //2560; 1/2 av org 20 * 64
    public static final int V_HEIGHT = 525; //1280; 10 * 64
    public static final float PPM = 100; // - Pixel per meter
    public static float MPP = 0.05f;	// - Meter per Pixel

    public static int MAX_PLAYER_MAIN_LIFE = 4;

    /** Box2D Collision Bits */
/*
    public static final short = 0 || 0x0000
    public static final short = 1 || 0x0001
    public static final short = 2 || 0x0002
    public static final short = 4 || 0x0004
    public static final short = 8 || 0x0008
    public static final short = 16 || 0x0010
    public static final short = 32 || 0x0020
    public static final short = 64 || 0x0040
    public static final short = 128 || 0x0080
    public static final short = 256 || 0x0100
    public static final short = 512 || 0x0200
    public static final short = 1024 || 0x0400
    public static final short = 2048 || 0x0800
    public static final short = 4096 || 0x1000
    public static final short = 8192 || 0x2000
    public static final short = 16382 || 0x4000


*/

    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short WALL_JUMPING_BIT = 2; /**wall jumping*/
    public static final short PLAYER_BIT = 4;
    public static final short ENEMY_BIT = 8;
    public static final short PLAYER_POWER_BIT = 16;
    public static final short ENEMY_POWER_BIT = 32;
    public static final short ENEMY_CLOSE_ATTACK_BIT = 64;
    public static final short ENEMY_RANGE_ATTACK_BIT = 128;
    public static final short GAME_ITEM_BIT = 256; /** ITEM PICK UP EX_LIFE, POWER_UPS ETC*/

    /** we will free up this one with true wall & ground hit/left */
    public static final short ENEMY_LEGS_BIT = 512; // enemy more then to legs etc !!??
    //public static final short GAME_PFX_OBJECT_BIT = 512; // enemy more then to legs etc !!??

    public static final short GAME_OBJECT_BIT = 1024; /** SAVEPOINT PORTALS ETC */



    public static final short WALL_AND_SEALING_BIT = 2048;

    /** have to look into freeing up this one two ???!! */
    public static final short ENEMY_BOTTOM_BIT = 4096;

    public static final short GAME_OBSTACLE_BIT = 8192;

    public static final short GAME_AI_OBJECT_BIT = 16384;


    /*
    public static final short = 16384 // THIS GAME ME PROBLEM LAST!! because wrong number changed
     */





    private final static String STATUSUI_TEXTURE_ATLAS_PATH = "statusUI/statusui.atlas";
    private final static String STATUSUI_SKIN_PATH = "statusUI/statusui.json";


/*
	private final static String PLAYER_TEXTURE_ATLAS_PATH = "BubblePlayerAtlas.atlas";
	public static TextureAtlas PLAYER_TEXTUREATLAS = new TextureAtlas(PLAYER_TEXTURE_ATLAS_PATH);
*/


    //private final static String TMX_MAP_PATH = "NewBubbleTestMap.tmx";

    //private final static String ITEMS_TEXTURE_ATLAS_PATH = "items.atlas";
    //private final static String ITEMS_SKIN_PATH = "items.json";

    public static TextureAtlas STATUSUI_TEXTUREATLAS = new TextureAtlas(STATUSUI_TEXTURE_ATLAS_PATH);
    //public static TextureAtlas ITEMS_TEXTUREATLAS = new TextureAtlas(ITEMS_TEXTURE_ATLAS_PATH);

    //
    //public static TiledMap TMX_MAP = new TiledMap(TMX_MAP_PATH);

    public static Skin STATUSUI_SKIN = new Skin(Gdx.files.internal(STATUSUI_SKIN_PATH), STATUSUI_TEXTUREATLAS);
    //public static TiledMap LEVEL_MAP = new TiledMap(Gdx.files.internal(TMX_MAP_PATH), ) ???

    //public static TextureAtlas PLAYER_SKIN = new TextureAtlas(Gdx.files.internal(PLAYER_TEXTURE_ATLAS_PATH) );

// check get both loaded and unloaded !!!!
    public static void unloadAsset(String assetFilenamePath){
        // once the asset manager is done loading
        if( assetManager.isLoaded(assetFilenamePath) ){
            assetManager.unload(assetFilenamePath);
            Gdx.app.debug(TAG, "Asset unloaded: " + assetFilenamePath );
        } else {
            Gdx.app.debug(TAG, "Asset is not loaded; Nothing to unload: " + assetFilenamePath );
        }
    }

    //public static int getMaxPlayerLife(){ return MAX_PLAYER_MAIN_LIFE; }

    public static float loadCompleted(){
        return assetManager.getProgress();
    }

    public static int numberAssetsQueued(){
        return assetManager.getQueuedAssets();
    }

    public static Array<String> getAssetNames() { return assetManager.getAssetNames(); }

    public static boolean updateAssetLoading(){
        return assetManager.update();
    }

    public static boolean isAssetLoaded(String fileName){
        return assetManager.isLoaded(fileName);

    }

    public static void loadMapAsset(String mapFilenamePath){
        if( mapFilenamePath == null || mapFilenamePath.isEmpty() ){
            return;
        }

        if( assetManager.isLoaded(mapFilenamePath) ){
            return;
        }

        //load asset
        if( filePathResolver.resolve(mapFilenamePath).exists() ){
            assetManager.setLoader(TiledMap.class, new TmxMapLoader(filePathResolver));
            assetManager.load(mapFilenamePath, TiledMap.class);
            System.out.println("[GameUtility] loadMapAsset");
            //Until we add loading screen, just block until we load the map
            assetManager.finishLoadingAsset(mapFilenamePath);
            Gdx.app.debug(TAG, "Map loaded!: " + mapFilenamePath);
        }
        else{
            Gdx.app.debug(TAG, "Map doesn't exist!: " + mapFilenamePath );
        }
    }


    public static TiledMap getMapAsset(String mapFilenamePath){
        TiledMap map = null;

        // once the asset manager is done loading
        if( assetManager.isLoaded(mapFilenamePath) ){
            map = assetManager.get(mapFilenamePath,TiledMap.class);
        } else {
            Gdx.app.debug(TAG, "Map is not loaded: " + mapFilenamePath );
        }

        return map;
    }

    public static void loadSoundAsset(String soundFilenamePath){
        if( soundFilenamePath == null || soundFilenamePath.isEmpty() ){
            return;
        }

        if( assetManager.isLoaded(soundFilenamePath) ){
            return;
        }

        //load asset
        if( filePathResolver.resolve(soundFilenamePath).exists() ){
            assetManager.setLoader(Sound.class, new SoundLoader(filePathResolver));
            assetManager.load(soundFilenamePath, Sound.class);

            //Until we add loading screen, just block until we load the map
            assetManager.finishLoadingAsset(soundFilenamePath);
            Gdx.app.debug(TAG, "Sound loaded!: " + soundFilenamePath);
        }
        else{
            Gdx.app.debug(TAG, "Sound doesn't exist!: " + soundFilenamePath );
        }
    }


    public static Sound getSoundAsset(String soundFilenamePath){
        Sound sound = null;

        // once the asset manager is done loading
        if( assetManager.isLoaded(soundFilenamePath) ){
            sound = assetManager.get(soundFilenamePath,Sound.class);
        } else {
            Gdx.app.debug(TAG, "Sound is not loaded: " + soundFilenamePath );
        }

        return sound;
    }

    public static void loadMusicAsset(String musicFilenamePath){

        if( musicFilenamePath == null || musicFilenamePath.isEmpty() ){
            return;
        }

        if( assetManager.isLoaded(musicFilenamePath) ){
            return;
        }

        //load asset
        if( filePathResolver.resolve(musicFilenamePath).exists() ){
            assetManager.setLoader(Music.class, new MusicLoader(filePathResolver));
            assetManager.load(musicFilenamePath, Music.class);

            //Until we add loading screen, just block until we load the map
            assetManager.finishLoadingAsset(musicFilenamePath);
            Gdx.app.debug(TAG, "Music loaded!: " + musicFilenamePath);
        }
        else{
            Gdx.app.debug(TAG, "Music doesn't exist!: " + musicFilenamePath );
        }
    }


    public static Music getMusicAsset(String musicFilenamePath){
        Music music = null;

        // once the asset manager is done loading
        if( assetManager.isLoaded(musicFilenamePath) ){
            music = assetManager.get(musicFilenamePath,Music.class);
        } else {
            Gdx.app.debug(TAG, "Music is not loaded: " + musicFilenamePath );
        }

        return music;
    }


    public static void loadTextureAtlas(String textureAtlasFilenamePath){
        if( textureAtlasFilenamePath == null || textureAtlasFilenamePath.isEmpty() ){
            return;
        }

        if( assetManager.isLoaded(textureAtlasFilenamePath) ){
            return;
        }

        //load asset
        if( filePathResolver.resolve(textureAtlasFilenamePath).exists() ){
            //assetManager.setLoader(Texture.class, new TextureLoader(filePathResolver));
            assetManager.setLoader(TextureAtlas.class, new TextureAtlasLoader(filePathResolver));
            assetManager.load(textureAtlasFilenamePath, TextureAtlas.class);

            //Until we add loading screen, just block until we load the map
            assetManager.finishLoadingAsset(textureAtlasFilenamePath);
            //Gdx.app.debug(TAG, "Texture loaded: " + textureAtlasFilenamePath );
        }
        else{
            Gdx.app.debug(TAG, "Texture doesn't exist!: " + textureAtlasFilenamePath );
        }
    }

    public static TextureAtlas getTextureAtlas(String textureAtlasFilenamePath){
        TextureAtlas atlas = null;

        // once the asset manager is done loading
        if( assetManager.isLoaded(textureAtlasFilenamePath) ){
            atlas = assetManager.get(textureAtlasFilenamePath,TextureAtlas.class);
        } else {
            Gdx.app.debug(TAG, "Texture is not loaded: " + textureAtlasFilenamePath );
        }

        return atlas;
    }



    public static void loadTextureAsset(String textureFilenamePath){
        if( textureFilenamePath == null || textureFilenamePath.isEmpty() ){
            return;
        }

        if( assetManager.isLoaded(textureFilenamePath) ){
            return;
        }

        //load asset
        if( filePathResolver.resolve(textureFilenamePath).exists() ){
            assetManager.setLoader(Texture.class, new TextureLoader(filePathResolver));
            //assetManager.setLoader(TextureAtlas.class, new TextureAtlasLoader(filePathResolver));
            assetManager.load(textureFilenamePath, Texture.class);

            //Until we add loading screen, just block until we load the map
            assetManager.finishLoadingAsset(textureFilenamePath);
        }
        else{
            Gdx.app.debug(TAG, "Texture doesn't exist!: " + textureFilenamePath );
        }
    }


    public static Texture getTextureAsset(String textureFilenamePath){
        Texture texture = null;

        // once the asset manager is done loading
        if( assetManager.isLoaded(textureFilenamePath) ){
            texture = assetManager.get(textureFilenamePath,Texture.class);
        } else {
            Gdx.app.debug(TAG, "Texture is not loaded: " + textureFilenamePath );
        }

        return texture;
    }
}

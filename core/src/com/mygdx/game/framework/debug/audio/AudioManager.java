package com.mygdx.game.framework.debug.audio;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
//import com.mygdx.game.Utility;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.framework.debug.util.GameUtility;

import java.util.Hashtable;


public class AudioManager implements AudioObserver{

	
	private static final String TAG = AudioManager.class.getSimpleName();

    private static AudioManager instance = null;

    private Hashtable<String, Music> queuedMusic;
    private Hashtable<String, Sound> queuedSounds;

    private float MUSIC_FADE_STEP = 0.001f;
    private float fadeRate = 0.01f;

    private AudioManager(){
        queuedMusic = new Hashtable<String, Music>();
        queuedSounds = new Hashtable<String, Sound>();

    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }

        return instance;
    }

//ToDo: Make fade SOUND - like we did Music / usage: echo sounds fade start fade start / !!!
    @Override
    public void onNotify(AudioCommand command, AudioTypeEvent event) {
        switch(command){
            case MUSIC_LOAD:
                GameUtility.loadMusicAsset(event.getValue());
                break;
            case MUSIC_PLAY_ONCE:
                playMusic(false, event.getValue());
                break;
            case MUSIC_PLAY_LOOP:
                playMusic(true, event.getValue());
                Music musicV = queuedMusic.get(event.getValue());

                if(musicV.getVolume() < 0.2f){
                    musicV.setVolume(2f);
                }

                break;
            case MUSIC_FADE:
                Music music_to_fade = queuedMusic.get(event.getValue());
                if( music_to_fade != null ){
                    // lower the wolume, then stop the music. after the a time frame !!
                    fadeSound(event);
                    //music_to_fade.getVolume()
                }
                break;
            case MUSIC_STOP:
                Music music = queuedMusic.get(event.getValue());
                if( music != null ){
                    music.stop();
                }
                break;
            case MUSIC_STOP_ALL:
                for( Music musicStop: queuedMusic.values() ){
                    musicStop.stop();
                }
                break;
            case SOUND_LOAD:
                GameUtility.loadSoundAsset(event.getValue());
                break;
            case SOUND_PLAY_LOOP:
                playSound(true, event.getValue());
                break;
            case SOUND_PLAY_ONCE:
                playSound(false, event.getValue());
                break;
            case SOUND_FADE:
                //playSound(false, event.getValue());
                break;
            case SOUND_STOP:
                Sound sound = queuedSounds.get(event.getValue());
                if( sound != null ){
                    sound.stop();
                }
                break;
            default:
                break;
        }
    }


    private void fadeSound(AudioTypeEvent event){

        final Music music_to_fade = queuedMusic.get(event.getValue());

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (music_to_fade.getVolume() >= MUSIC_FADE_STEP)
                    music_to_fade.setVolume(music_to_fade.getVolume() - MUSIC_FADE_STEP);
                else {
                    music_to_fade.stop();
                    this.cancel();
                }
            }
        }, 0f, fadeRate);

        //music_to_fade.setVolume(2f); // did not exit right away, felt like it faded to times... moved to when we load Music, check volume.
    }

    private Music playMusic(boolean isLooping, String fullFilePath){
        Music music = queuedMusic.get(fullFilePath);
        if( music != null ){
            music.setLooping(isLooping);
            music.play();
        }else if(GameUtility.isAssetLoaded(fullFilePath)){
            music = GameUtility.getMusicAsset(fullFilePath);
            music.setLooping(isLooping);
            music.play();
            queuedMusic.put(fullFilePath, music);
        }else{
            Gdx.app.debug(TAG, "Music not loaded");
            return null;
        }
        return music;
    }

    private Sound playSound(boolean isLooping, String fullFilePath){
        Sound sound = queuedSounds.get(fullFilePath);
        if( sound != null ){
            long soundId = sound.play();
            sound.setLooping(soundId, isLooping);
        }else if( GameUtility.isAssetLoaded(fullFilePath) ) {
            sound = GameUtility.getSoundAsset(fullFilePath);
            long soundId = sound.play();
            sound.setLooping(soundId, isLooping);
            queuedSounds.put(fullFilePath, sound);
        }else{
            Gdx.app.debug(TAG, "Sound not loaded");
            return null;
        }
        return sound;
    }

    public void dispose(){
        for(Music music: queuedMusic.values()){
            music.dispose();
        }

        for(Sound sound: queuedSounds.values()){
            sound.dispose();
        }
    }

}

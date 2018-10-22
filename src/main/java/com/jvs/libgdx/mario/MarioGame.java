package com.jvs.libgdx.mario;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.jvs.libgdx.mario.screen.loading.LoadingScreen;

public class MarioGame extends Game {

    private AssetManager assetManager;
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        assetManager = new AssetManager();
        assetManager.getLogger().setLevel(Logger.DEBUG);

        spriteBatch = new SpriteBatch();

        this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void dispose() {
        // dispose the actual com.mygdx.obstacleavoid.screen object
        getScreen().dispose(); // Important!

        assetManager.dispose();
        spriteBatch.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SpriteBatch getSpriteBatch(){
        return spriteBatch;
    }

}

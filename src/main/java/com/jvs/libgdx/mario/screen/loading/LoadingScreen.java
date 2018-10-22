package com.jvs.libgdx.mario.screen.loading;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jvs.libgdx.mario.MarioGame;
import com.jvs.libgdx.mario.assets.AssetDescriptors;
import com.jvs.libgdx.mario.config.GameConfig;
import com.jvs.libgdx.mario.screen.game.GameScreen;
import com.jvs.libgdx.mario.utils.GdxUtils;


public class LoadingScreen extends ScreenAdapter {

    private final MarioGame marioGame;
    private final AssetManager assetManager;

    private static final float PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2f; // world units
    private static final float PROGRESS_BAR_HEIGHT = 60; // world units

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;

    private float progress;
    private float waitTime = 0.75f;
    private boolean changeScreen = false;


    public LoadingScreen(MarioGame marioGame) {
        this.marioGame = marioGame;
        assetManager = marioGame.getAssetManager();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FillViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();

        assetManager.load(AssetDescriptors.MARIO_ATLAS);
        assetManager.load(AssetDescriptors.WORLD_11_ATLAS);
        assetManager.load(AssetDescriptors.HIT_SOUND);
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();
        update(delta);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        draw();
        shapeRenderer.end();

        if(changeScreen){
            marioGame.setScreen(new GameScreen(marioGame));
        }
    }

    private void update(float delta){
        // progress is between 0 and 1
        progress = assetManager.getProgress();

        // update() returns true when all the assets are loaded
        if(assetManager.update()){
            waitTime -= delta;

            if(waitTime <= 0){
                changeScreen = true;
            }
        }
    }

    private void draw(){
        float progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2;
        float progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2;

        shapeRenderer.rect(progressBarX, progressBarY, PROGRESS_BAR_WIDTH * progress, PROGRESS_BAR_HEIGHT);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        // NOTE: screens don't dispose automatically
        dispose();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        shapeRenderer = null;
    }
}

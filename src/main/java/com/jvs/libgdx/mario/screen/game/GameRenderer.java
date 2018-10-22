package com.jvs.libgdx.mario.screen.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jvs.libgdx.mario.assets.AssetDescriptors;
import com.jvs.libgdx.mario.assets.RegionNames;
import com.jvs.libgdx.mario.config.GameConfig;
import com.jvs.libgdx.mario.entity.Background;
import com.jvs.libgdx.mario.entity.Player;
import com.jvs.libgdx.mario.utils.GdxUtils;
import com.jvs.libgdx.mario.utils.ViewportUtils;
import com.jvs.libgdx.mario.utils.debug.DebugCameraController;


public class GameRenderer implements Disposable {
    private static final Logger LOGGER = new Logger(GameRenderer.class.getName(), Logger.DEBUG);

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;


    private final GameController gameController;
    private final AssetManager assetManager;
    private final SpriteBatch spriteBatch;

    private TextureRegion playerRegion;
    private TextureRegion backgroundRegion;

    private DebugCameraController debugCameraController;

    public GameRenderer(GameController gameController, AssetManager assetManager, SpriteBatch spriteBatch) {
        this.gameController = gameController;
        this.assetManager = assetManager;
        this.spriteBatch = spriteBatch;
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();

        TextureAtlas playerAtlas = assetManager.get(AssetDescriptors.MARIO_ATLAS);
        playerRegion = playerAtlas.findRegion(RegionNames.MARIO);

        TextureAtlas backgroundAtlas = assetManager.get(AssetDescriptors.WORLD_11_ATLAS);
        backgroundRegion = backgroundAtlas.findRegion(RegionNames.BACKGROUND);


        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);
    }

    public void render(float deltaTime) {
        // update camera (is not wrapped inside the alive conditions, because we
        // want to be able to control the camera even when the game is over)
        debugCameraController.handleDebugInput(deltaTime);
        debugCameraController.applyTo(camera);

        // clear screen
        GdxUtils.clearScreen();

        // render game characters
        renderGamePlay();

        // render debug graphics
        //renderDebug();
    }

    public void resize(int width, int height) {
        // NOTE:  Not centering camera for this sample
        viewport.update(width, height, true);

        ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void renderGamePlay() {
        viewport.apply(); // important! Apply the viewport before rendering
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        drawCharacters();
        spriteBatch.end();
    }

    private void drawCharacters() {
        // draw background
        Background background = gameController.getBackground();
        spriteBatch.draw(backgroundRegion, background.getX(), background.getY(), background.getWidth(), background.getHeight());

        // draw player
        Player player = gameController.getPlayer();
        spriteBatch.draw(playerRegion, player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }

     private void renderDebug() {
        viewport.apply(); // important! Apply the viewport before rendering
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        shapeRenderer.end();

        ViewportUtils.drawGrid(viewport, shapeRenderer);
    }

    private void drawDebug() {
        gameController.getPlayer().drawDebug(shapeRenderer);
    }
}

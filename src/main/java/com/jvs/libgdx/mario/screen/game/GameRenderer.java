package com.jvs.libgdx.mario.screen.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jvs.libgdx.mario.assets.AssetPaths;
import com.jvs.libgdx.mario.config.GameConfig;
import com.jvs.libgdx.mario.entity.Background;
import com.jvs.libgdx.mario.entity.Player;
import com.jvs.libgdx.mario.utils.GdxUtils;
import com.jvs.libgdx.mario.utils.ViewportUtils;
import com.jvs.libgdx.mario.utils.debug.DebugCameraController;


public class GameRenderer implements Disposable {
    private static final Logger LOGGER = new Logger(GameRenderer.class.getName(), Logger.DEBUG);

    private final GameController gameController;
    private final AssetManager assetManager;
    private final SpriteBatch spriteBatch;

    private Viewport viewport;
    private ShapeRenderer shapeRenderer;

    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

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
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        shapeRenderer = new ShapeRenderer();

        //TextureAtlas playerAtlas = assetManager.get(AssetDescriptors.MARIO_ATLAS);
        //playerRegion = playerAtlas.findRegion(RegionNames.MARIO);

        //TextureAtlas backgroundAtlas = assetManager.get(AssetDescriptors.WORLD_11_ATLAS);
        //backgroundRegion = backgroundAtlas.findRegion(RegionNames.BACKGROUND);


        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load(AssetPaths.MarioTileMap);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        //initially set the camera to be centered correctly at startup
        //viewport.getCamera().position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);
        //viewport.apply(true);
    }

    public void resize(int width, int height) {
        // NOTE:  Not centering camera for this sample
        viewport.update(width, height);

        // prints xPPU and yPPU after resizing
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    public void render(float deltaTime) {
        // clear screen
        GdxUtils.clearScreen();

        // render game characters
        renderGamePlay2();

        // update camera (is not wrapped inside the alive conditions, because we
        // want to be able to control the camera even when the game is over)
        debugCameraController.handleDebugInput(deltaTime);
        debugCameraController.applyTo((OrthographicCamera) viewport.getCamera());

        // render debug graphics
        //renderDebug();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void renderGamePlay() {
        viewport.apply(); // important! Apply the viewport before rendering
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        drawCharacters();
        spriteBatch.end();
    }

    private void renderGamePlay2() {
        viewport.apply(); // important! Apply the viewport before rendering
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        orthogonalTiledMapRenderer.setView((OrthographicCamera)viewport.getCamera());
        orthogonalTiledMapRenderer.render();

        spriteBatch.begin();
        // render something...
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
        //viewport.apply(); // important! Apply the viewport before rendering
        //shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        shapeRenderer.end();

        ViewportUtils.drawGrid(viewport, shapeRenderer);
    }

    private void drawDebug() {
        gameController.getPlayer().drawDebug(shapeRenderer);
    }
}

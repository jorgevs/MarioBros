package com.jvs.libgdx.mario.screen.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jvs.libgdx.mario.assets.AssetDescriptors;
import com.jvs.libgdx.mario.assets.RegionNames;
import com.jvs.libgdx.mario.config.GameConfig;
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

    // Tiled map variables
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    // Box2D variables
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private TextureAtlas playerAtlas;
    private TextureRegion playerRegion;

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

        playerAtlas = assetManager.get(AssetDescriptors.MARIO_ATLAS_DESC);
        playerRegion = playerAtlas.findRegion(RegionNames.MARIO);

        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

        tiledMap = assetManager.get(AssetDescriptors.MARIO_TILE_MAP_DESC);

        float unitScale = 1 / 1f;
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);

        // Box2D world creation
        world = new World(new Vector2(0, -10), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        createStaticBodies(tiledMap);
    }

    private void createStaticBodies(TiledMap tiledMap) {
        String[] layerList = {"Background", "Coins", "Ground", "Pipes", "Powers", "Bricks", "SecretBox"};
        for (String layer: layerList) {
            for (MapObject mapObject : tiledMap.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
                createStaticBody(mapObject);
            }
        }
    }

    /**
     * Static bodies are objects which do not move and are not affected by forces.
     * Static bodies are perfect for ground, walls, and any object which does not need to move.
     * Static bodies require less computing power.
     */
    private void createStaticBody(MapObject mapObject) {
        // Define a static body (type and position)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // Set our body's starting position in the world
        Rectangle rect = ((RectangleMapObject) mapObject).getRectangle();
        bodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

        // Create our body in the world using the body definition
        Body body = world.createBody(bodyDef);

        // Define the shape for the fixture
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);

        // Create a fixture definition to apply our shape to.
        // A fixture has a shape, density, friction and restitution attached to it.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        //fixtureDef.density = 0.5f;
        //fixtureDef.friction = 0.4f;
        //fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        polygonShape.dispose();
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

        // render our game tiled map
        orthogonalTiledMapRenderer.setView((OrthographicCamera) viewport.getCamera());
        orthogonalTiledMapRenderer.render();

        // render game characters
        renderGamePlay();

        // render the object debugging lines in our Box2D world (Box2DDebugLines)
        box2DDebugRenderer.render(world, viewport.getCamera().combined);

        // take 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);


        // update camera (is not wrapped inside the alive conditions, because we
        // want to be able to control the camera even when the game is over)
        debugCameraController.handleDebugInput(deltaTime);
        debugCameraController.applyTo((OrthographicCamera) viewport.getCamera());

        // render debug graphics
        renderDebug();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        tiledMap.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
        playerAtlas.dispose();
    }

    private void renderGamePlay() {
        //viewport.apply(); // important! Apply the viewport before rendering
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        drawCharacters();
        spriteBatch.end();

        // render the Hud
        gameController.getHud().getStage().draw();
    }

    private void drawCharacters() {
        // draw player
        Player player = gameController.getPlayer();
        spriteBatch.draw(playerRegion, player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }

    private void renderDebug() {
        viewport.apply(); // important! Apply the viewport before rendering
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        shapeRenderer.end();

        //ViewportUtils.drawGrid(viewport, shapeRenderer);
    }

    private void drawDebug() {
        gameController.getPlayer().drawDebug(shapeRenderer);
    }
}

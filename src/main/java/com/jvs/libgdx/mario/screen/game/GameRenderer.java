package com.jvs.libgdx.mario.screen.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jvs.libgdx.mario.MarioGame;
import com.jvs.libgdx.mario.assets.AssetDescriptors;
import com.jvs.libgdx.mario.assets.RegionNames;
import com.jvs.libgdx.mario.config.GameConfig;
import com.jvs.libgdx.mario.entity.Player;
import com.jvs.libgdx.mario.utils.GdxUtils;
import com.jvs.libgdx.mario.utils.ViewportUtils;
import com.jvs.libgdx.mario.utils.debug.DebugCameraController;

public class GameRenderer implements Disposable {

    private final MarioGame marioGame;

    private final GameController gameController;

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


    public GameRenderer(MarioGame marioGame, GameController gameController) {
        this.marioGame = marioGame;
        this.gameController = gameController;
        init();
    }

    private void init() {
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        // Shape renderer used to draw the debug grid
        shapeRenderer = new ShapeRenderer();

        // Player region
        playerAtlas = marioGame.getAssetManager().get(AssetDescriptors.MARIO_ATLAS_DESC);
        playerRegion = playerAtlas.findRegion(RegionNames.MARIO);

        // Tiled map
        tiledMap = marioGame.getAssetManager().get(AssetDescriptors.MARIO_TILE_MAP_DESC);
        float unitScale = 1 / 1f;
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);

        // Debug camera controller to monitor our game
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

        // Box2D world creation
        world = new World(new Vector2(0, -10), true);
        // Box2D debug renderer (used to display the body shapes in our Box2D world)
        box2DDebugRenderer = new Box2DDebugRenderer();

        // Create static bodies in our world that are defined in the tiled map layers
        createStaticBodies(tiledMap);

        // Define Box2D player's body
        gameController.getPlayer().defineBodyPlayer(world);
        //Player player = gameController.getPlayer();
        //player.setB2body(createDynamicBody(player));
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

        // prints in the LOGGER the xPPU and yPPU values after resizing
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

        // take 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        //attach our gamecam to our players.x coordinate
        viewport.getCamera().position.x = gameController.getPlayer().getB2body().getPosition().x;
        viewport.getCamera().position.y = GameConfig.WORLD_HEIGHT/2;

        // render debug graphics
        renderDebug(deltaTime);
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
        marioGame.getSpriteBatch().setProjectionMatrix(viewport.getCamera().combined);

        marioGame.getSpriteBatch().begin();
        drawCharacters();
        marioGame.getSpriteBatch().end();

        // render the Hud
        gameController.getHud().getStage().draw();
    }

    private void drawCharacters() {
        // draw player
        Player player = gameController.getPlayer();
        marioGame.getSpriteBatch().draw(playerRegion, player.getX(), player.getY(), player.getWidth(), player.getHeight());

        player.setPosition(player.getB2body().getPosition().x - player.getWidth() / 2, player.getB2body().getPosition().y - player.getHeight() / 2);
    }


    // ================================== DEBUGGING ==================================

    private void renderDebug(float deltaTime) {
        //------------------------------ 2DBox world ---------------------------------
        // render the object debugging lines in our Box2D world (Box2DDebugLines)
        box2DDebugRenderer.render(world, viewport.getCamera().combined);

        //------------------------------ Debug camera --------------------------------
        // update camera (is not wrapped inside the alive conditions, because we
        // want to be able to control the camera even when the game is over)
        //debugCameraController.handleDebugInput(deltaTime);
        //debugCameraController.applyTo((OrthographicCamera) viewport.getCamera());

        //----------------------- Debug entities shape and grid -----------------------
        viewport.apply(); // important! Apply the viewport before rendering
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        // Draws any entity's shape
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        shapeRenderer.end();

        // Uses the same shapeRenderer to draw the grid
        //ViewportUtils.drawGrid(viewport, shapeRenderer);
        //----------------------------------------------------------------------------
    }

    private void drawDebug() {
        gameController.getPlayer().drawDebug(shapeRenderer);
    }
}

package com.jvs.libgdx.mario.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.jvs.libgdx.mario.config.GameConfig;

public class Player extends GameObjectBase {

    public Player() {
        super(GameConfig.PLAYER_WIDTH / GameConfig.GAME_SCALE, GameConfig.PLAYER_HEIGHT / GameConfig.GAME_SCALE);
    }

    @Override
    public void update(float deltaTime) {
        setPosition(b2body.getPosition().x - (getWidth() / 2), b2body.getPosition().y - (getHeight() / 2));
    }

    /**
     * Static bodies are objects which do not move and are not affected by forces.
     * Static bodies are perfect for ground, walls, and any object which does not need to move.
     * Static bodies require less computing power.
     */
    public void defineBodyPlayer(World world) {
        // Define a static body (type and position)
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Set our body's starting position in the world
        Rectangle rect = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        bodyDef.position.set((this.getX() + this.getWidth() / 2), (this.getY() + this.getHeight() / 2));

        // Create our body in the world using the body definition
        this.b2body = world.createBody(bodyDef);

        // Define the shape for the fixture
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox((rect.getWidth() / 2), (rect.getHeight() / 2));

        // Create a fixture definition to apply our shape to.
        // A fixture has a shape, density, friction and restitution attached to it.
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 12.0f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = this.b2body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        polygonShape.dispose();
    }
}

package com.jvs.libgdx.mario.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameObjectBase {
    private float x = 0;
    private float y = 0;
    private float width = 0;
    private float height = 0;
    private Rectangle bounds;
    protected Body b2body;

    public GameObjectBase(float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        setSize(width, height);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Body getB2body() {
        return b2body;
    }

    public void drawDebug(ShapeRenderer renderer) {
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds();
    }

    private void updateBounds() {
        bounds.setPosition(x, y);
    }

    public abstract void update(float deltaTime);

}

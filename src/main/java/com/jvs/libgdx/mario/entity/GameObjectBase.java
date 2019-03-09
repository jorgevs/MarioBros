package com.jvs.libgdx.mario.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameObjectBase {
    private float x;
    private float y;
    private float width;
    private float height;
    private Rectangle bounds;
    protected Body b2body;

    public GameObjectBase(float width, float height) {
        bounds = new Rectangle(x, y, width, height);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        updateBounds();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        updateBounds();
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

    public void setB2body(Body b2body) {
        this.b2body = b2body;
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
        float halfWidth = getWidth() / 2f;
        float halfHeight = getHeight() / 2f;
        // draw the object according to sprite position (left-bottom corner)
        //bounds.setPosition(x + halfWidth, y + halfHeight);
        bounds.setPosition(x, y);
    }

    public abstract void update(float deltaTime);

}

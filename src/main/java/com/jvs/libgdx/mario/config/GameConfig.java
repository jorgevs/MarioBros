package com.jvs.libgdx.mario.config;

public class GameConfig {

    public static final String GAME_NAME = "MarioGame";

    public static final float WIDTH = 3392f; // pixels
    public static final float HEIGHT = 224f; // pixels

    public static final float HUD_WIDTH = 3392f; // world units
    public static final float HUD_HEIGHT = 224f; // world units

    public static final float WORLD_WIDTH = WIDTH;// / 40f; // world units (20.0f)
    public static final float WORLD_HEIGHT = HEIGHT;// / 30f; // world units (20.0f)

    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2; // world units (10.0f)
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2; // world units (10.0f)

    public static final float PLAYER_BOUNDS_RADIUS = 20f; // world units
    public static final float PLAYER_SIZE = PLAYER_BOUNDS_RADIUS * 2; // world units

    private GameConfig() {}
}

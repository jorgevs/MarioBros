package com.jvs.libgdx.mario.config;

public class GameConfig {

    public static final String GAME_NAME = "MarioGame";

    public static final float WIDTH = 1200f; // pixels
    public static final float HEIGHT = 624f; // pixels

    public static final float PLAYER_WIDTH = 19f; // pixels
    public static final float PLAYER_HEIGHT = 27f; // pixels

    public static final float GAME_SCALE = 16f; // pixels

    public static final float WORLD_WIDTH = 30f;//WIDTH / 3f; // world units (400.0f)
    public static final float WORLD_HEIGHT = 13f;//HEIGHT / 3f; // world units (208.0f)

    public static final float HUD_WIDTH = WORLD_WIDTH; // world units
    public static final float HUD_HEIGHT = WORLD_HEIGHT; // world units

    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2; // world units (200.0f)
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2; // world units (104.0f)

    private GameConfig() {}
}

package com.jvs.libgdx.mario.config;

public class GameConfig {

    public static final String GAME_NAME = "MarioGame";

    public static final float WIDTH = 1200f; // pixels
    public static final float HEIGHT = 624f; // pixels

    public static final float HUD_WIDTH = WIDTH; // pixels
    public static final float HUD_HEIGHT = HEIGHT; // pixels


    public static final float WORLD_WIDTH = WIDTH / 3f; // world units (400.0f)
    public static final float WORLD_HEIGHT = HEIGHT / 3f; // world units (208.0f)

    //public static final float PPM = 100;

    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2; // world units (200.0f)
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2; // world units (104.0f)

    public static final float PLAYER_WIDTH = 20f; // world units
    public static final float PLAYER_HEIGHT = 26f; // world units


    private GameConfig() {}
}

package com.jvs.libgdx.mario.entity;

import com.jvs.libgdx.mario.config.GameConfig;

public class Player extends GameObjectBase {

    public Player() {
        super(GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
        setSize(GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
    }

}

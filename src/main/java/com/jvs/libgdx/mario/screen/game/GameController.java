package com.jvs.libgdx.mario.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Logger;
import com.jvs.libgdx.mario.MarioGame;
import com.jvs.libgdx.mario.config.GameConfig;
import com.jvs.libgdx.mario.entity.Background;
import com.jvs.libgdx.mario.entity.Player;
import com.jvs.libgdx.mario.screen.scenes.Hud;

public class GameController extends InputAdapter {
    private static final Logger LOGGER = new Logger(GameController.class.getName(), Logger.DEBUG);

    private Player player;
    private Background background;
    private Hud hud;

    public GameController(MarioGame marioGame) {
        background = new Background();
        background.setPosition(0, 0);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        player = new Player();
        player.setPosition(0, 0);

        hud = new Hud(marioGame.getSpriteBatch());

        init();
    }

    private void init(){
        Gdx.input.setInputProcessor(this);
    }

    public void update(float deltaTime) {

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //player.setX(player.getX()+10);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            //player.setX(player.getX()-10);
        }

        hud.update(deltaTime);
    }

    public Player getPlayer() {
        return player;
    }

    public Background getBackground() {
        return background;
    }

    public Hud getHud() {
        return hud;
    }
}

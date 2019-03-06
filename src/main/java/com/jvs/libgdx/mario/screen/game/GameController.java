package com.jvs.libgdx.mario.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;
import com.jvs.libgdx.mario.MarioGame;
import com.jvs.libgdx.mario.config.GameConfig;
import com.jvs.libgdx.mario.entity.Background;
import com.jvs.libgdx.mario.entity.Player;
import com.jvs.libgdx.mario.screen.scenes.Hud;

public class GameController extends InputAdapter implements Disposable {
    private static final Logger LOGGER = new Logger(GameController.class.getName(), Logger.DEBUG);

    private Player player;
    private Hud hud;

    public GameController(MarioGame marioGame) {
        player = new Player();
        player.setPosition(0, 16);

        hud = new Hud(marioGame.getSpriteBatch());

        init();
    }

    private void init(){
        Gdx.input.setInputProcessor(this);
    }

    public void update(float deltaTime) {

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setX(player.getX()+2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setX(player.getX()-2);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.setY(player.getY()+2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setY(player.getY()-2);
        }

        hud.update(deltaTime);
    }

    public Player getPlayer() {
        return player;
    }

    public Hud getHud() {
        return hud;
    }

    @Override
    public void dispose() {
        hud.dispose();
    }
}

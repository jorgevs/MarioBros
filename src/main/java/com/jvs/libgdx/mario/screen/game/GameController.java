package com.jvs.libgdx.mario.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.jvs.libgdx.mario.MarioGame;
import com.jvs.libgdx.mario.entity.Player;
import com.jvs.libgdx.mario.screen.scenes.Hud;

public class GameController extends InputAdapter implements Disposable {

    private final MarioGame marioGame;

    private Player player;
    private Hud hud;

    public GameController(MarioGame marioGame) {
        this.marioGame = marioGame;
        init();
    }

    private void init() {
        //Gdx.input.setInputProcessor(this);

        player = new Player();
        player.setPosition(0, 10);

        hud = new Hud(marioGame.getSpriteBatch());

    }

    private static final float IMPULSE_HRTL = 12.0f;
    private static final float IMPULSE_VRTL = 100.0f;
    public void update(float deltaTime) {

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.getB2body().applyLinearImpulse(new Vector2(IMPULSE_HRTL, 0), player.getB2body().getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.getB2body().applyLinearImpulse(new Vector2(-IMPULSE_HRTL, 0), player.getB2body().getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            //public void jump(){
                //if ( currentState != State.JUMPING ) {
            player.getB2body().applyLinearImpulse(new Vector2(0, IMPULSE_VRTL), player.getB2body().getWorldCenter(), true);
                    //currentState = State.JUMPING;
                //}
            //}
        }

        /*if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setY(player.getY()-2);
        }*/

        player.update(deltaTime);
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

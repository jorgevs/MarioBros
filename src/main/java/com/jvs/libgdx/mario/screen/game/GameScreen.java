package com.jvs.libgdx.mario.screen.game;

import com.badlogic.gdx.ScreenAdapter;
import com.jvs.libgdx.mario.MarioGame;

public class GameScreen extends ScreenAdapter {

    private final MarioGame marioGame;

    private GameController gameController;
    private GameRenderer gameRenderer;

    public GameScreen(MarioGame marioGame) {
        this.marioGame = marioGame;
    }

    @Override
    public void show() {
        gameController = new GameController(marioGame);
        gameRenderer = new GameRenderer(marioGame, gameController);
    }

    @Override
    public void render(float deltaTime) {
        gameController.update(deltaTime);
        gameRenderer.render(deltaTime);
    }

    @Override
    public void resize(int width, int height) {
        gameRenderer.resize(width, height);
    }

    @Override
    public void hide() {
        // NOTE: screens don't dispose automatically
        dispose();
    }

    @Override
    public void dispose() {
        gameController.dispose();
        gameRenderer.dispose();
    }

}

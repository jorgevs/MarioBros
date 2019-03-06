package com.jvs.libgdx.mario;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Logger;
import com.jvs.libgdx.mario.config.GameConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootDesktopLauncher implements CommandLineRunner {
    private static final Logger LOGGER = new Logger(SpringBootDesktopLauncher.class.getName(), Logger.DEBUG);

    @Override
    public void run(String... args) {
        System.out.println("Starting MarioGame...");

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = GameConfig.GAME_NAME;
        config.useGL30 = true;
        config.width = (int)GameConfig.WIDTH;
        config.height = (int)GameConfig.HEIGHT;
        new LwjglApplication(new MarioGame(), config);

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        LOGGER.info(">> LOGGER LEVEL: " + Gdx.app.getLogLevel());
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDesktopLauncher.class, args);
    }
}

package com.jvs.libgdx.mario.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetDescriptors {
    public static final AssetDescriptor<TextureAtlas> MARIO_ATLAS_DESC = new AssetDescriptor<>(AssetPaths.MARIO_ATLAS_PATH, TextureAtlas.class);
    public static final AssetDescriptor<TiledMap> MARIO_TILE_MAP_DESC = new AssetDescriptor<>(AssetPaths.MARIO_TILE_MAP_PATH, TiledMap.class);
    public static final AssetDescriptor<Sound> HIT_SOUND_DESC = new AssetDescriptor<>(AssetPaths.HIT_SOUND_PATH, Sound.class);

    private AssetDescriptors(){
    }
}

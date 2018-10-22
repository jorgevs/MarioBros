package com.jvs.libgdx.mario.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetDescriptors {
    public static final AssetDescriptor<TextureAtlas> MARIO_ATLAS = new AssetDescriptor<TextureAtlas>(AssetPaths.MARIO_ATLAS_PATH, TextureAtlas.class);
    public static final AssetDescriptor<TextureAtlas> WORLD_11_ATLAS = new AssetDescriptor<TextureAtlas>(AssetPaths.WORLD_11_ATLAS_PATH, TextureAtlas.class);
    public static final AssetDescriptor<Sound> HIT_SOUND = new AssetDescriptor<Sound>(AssetPaths.HIT, Sound.class);

    private AssetDescriptors(){
    }
}

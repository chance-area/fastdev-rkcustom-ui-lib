package ru.rodionkrainov.fastdevrkcustomuilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public abstract class GlobalImagesManager {
    private static final ArrayList<String>                   arrImgNames    = new ArrayList<>();
    private static final ArrayList<AssetDescriptor<Texture>> arrImgTextures = new ArrayList<>();

    public static int numImgTextures       = 0;
    public static int numLoadedImgTextures = 0;

    public static void init(String _pngFilesFolder, String[][] _imagesNamesPath, AssetManager _assetManager) {
        numImgTextures = _imagesNamesPath.length;

        // init...
        for (String[] imgNamePath : _imagesNamesPath) {
            AssetDescriptor<Texture> imgTexture = new AssetDescriptor<>(Gdx.files.internal((_pngFilesFolder.isEmpty() ? "" : (_pngFilesFolder + "/")) + imgNamePath[1]), Texture.class);

            arrImgNames.add(imgNamePath[0]);
            arrImgTextures.add(imgTexture);
        }

        TextureLoader.TextureParameter textureParam = new TextureLoader.TextureParameter();
        textureParam.genMipMaps = true;
        textureParam.minFilter = Texture.TextureFilter.MipMap;
        textureParam.magFilter = Texture.TextureFilter.Nearest;
        textureParam.wrapU     = Texture.TextureWrap.ClampToEdge;
        textureParam.wrapV     = Texture.TextureWrap.ClampToEdge;

        for (AssetDescriptor<Texture> texture : arrImgTextures) {
            if (!_assetManager.isLoaded(texture)) _assetManager.load(texture.fileName, Texture.class, textureParam);
        }
    }

    public static void loadNext(AssetManager _assetManager) {
        if (numImgTextures != numLoadedImgTextures) {
            AssetDescriptor<Texture> currentTexture = arrImgTextures.get(numLoadedImgTextures);

            if (!_assetManager.isLoaded(currentTexture)) {
                _assetManager.finishLoadingAsset(currentTexture);
                numLoadedImgTextures++;
            }
        }
    }

    public static Texture getImageTexture(String _name, AssetManager _assetManager) {
        int imageIndex = arrImgNames.indexOf(_name);

        return (imageIndex != -1 ? _assetManager.get(arrImgTextures.get(imageIndex)) : null);
    }
}

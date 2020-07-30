/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.gl;

import java.awt.Font;
import org.joml.Vector4f;
import ru.g905.engine.FontTexture;
import ru.g905.engine.GameItem;
import ru.g905.engine.IHud;
import ru.g905.engine.TextItem;
import ru.g905.engine.Window;
import ru.g905.engine.graph.Material;
import ru.g905.engine.graph.Mesh;
import ru.g905.engine.graph.ObjLoader;

/**
 *
 * @author zharnikov
 */
public class Hud implements IHud {

    private static final Font FONT = new Font("Monospace", Font.PLAIN, 20);
    private static final String CHARSET = "ISO-8859-1";
    private final GameItem[] gameItems;

    private final TextItem statusTextItem;

    private final GameItem compassItem;

    public Hud(String statusText) throws Exception {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET);
        this.statusTextItem = new TextItem(statusText, fontTexture);
        this.statusTextItem.getMesh().getMaterial().setAmbientColor(new Vector4f(1, 1, 1, 1));

        Mesh mesh = ObjLoader.loadMesh("models/compass.obj");
        Material material = new Material();
        material.setAmbientColor(new Vector4f(1, 0, 0, 1));
        mesh.setMaterial(material);
        compassItem = new GameItem(mesh);
        compassItem.setScale(40f);
        compassItem.setRotation(0f, 0f, 180f);

        gameItems = new GameItem[]{statusTextItem, compassItem};
    }

    public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }

    public void rotateCompass(float angle) {
        this.compassItem.setRotation(0, 0, 180 + angle);
    }

    @Override
    public GameItem[] getGameItems() {
        return gameItems;
    }

    public void updateSize(Window window) {
        this.statusTextItem.setPosition(10f, window.getHeight() - 50f, 0);
        this.compassItem.setPosition(window.getWidth() - 40f, 50f, 0);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine;

import java.util.ArrayList;
import java.util.List;
import ru.g905.engine.graph.Material;
import ru.g905.engine.graph.Mesh;
import ru.g905.engine.graph.Texture;

/**
 *
 * @author zharnikov
 */
public class TextItem extends GameItem {

    private static final float ZPOS = 0.0f;
    private static final int VERTICES_PER_QUAD = 4;
    private String text;
    private FontTexture fontTexture;

    public TextItem(String text, FontTexture fontTexture) throws Exception {
        super();
        this.text = text;
        this.fontTexture = fontTexture;
        this.setMesh(buildMesh());
    }

    private Mesh buildMesh() {
        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        float[] normals = new float[0];
        List<Integer> indices = new ArrayList<>();

        char[] characters = text.toCharArray();

        int numChars = characters.length;

        float startX = 0;
        for (int i = 0; i < numChars; i++) {
            FontTexture.CharInfo charInfo = fontTexture.getCharInfo(characters[i]);

            // left top
            positions.add(startX);
            positions.add(0.0f);
            positions.add(ZPOS);
            textCoords.add((float) charInfo.getStartX() / (float) fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i * VERTICES_PER_QUAD);

            // left bottom
            positions.add(startX);
            positions.add((float) fontTexture.getHeight());
            positions.add(ZPOS);
            textCoords.add((float) charInfo.getStartX() / (float) fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i * VERTICES_PER_QUAD + 1);

            positions.add(startX + charInfo.getWidth());
            positions.add((float) fontTexture.getHeight());
            positions.add(ZPOS);
            textCoords.add((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i * VERTICES_PER_QUAD + 2);

            positions.add(startX + charInfo.getWidth());
            positions.add(0.0f);
            positions.add(ZPOS);
            textCoords.add((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i * VERTICES_PER_QUAD + 3);

            indices.add(i * VERTICES_PER_QUAD);
            indices.add(i * VERTICES_PER_QUAD + 2);

            startX += charInfo.getWidth();
        }

        float[] posArr = Utils.listToArray(positions);
        float[] textCoordsArr = Utils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setMaterial(new Material(fontTexture.getTexture()));
        return mesh;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        Texture texture = this.getMesh().getMaterial().getTexture();
        this.getMesh().deleteBuffers();
        this.setMesh(buildMesh());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine;

import java.nio.charset.Charset;
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
    private final int numCols;
    private final int numRows;

    public TextItem(String text, String fontFileName, int numCols, int numRows) throws Exception {
        super();
        this.text = text;
        this.numCols = numCols;
        this.numRows = numRows;
        Texture texture = new Texture(fontFileName);
        this.setMesh(buildMesh(texture, numCols, numRows));
    }

    private Mesh buildMesh(Texture texture, int numCols, int numRows) {
        byte[] chars = text.getBytes(Charset.forName("ISO-8859-1"));
        int numChars = chars.length;

        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        float[] normals = new float[0];
        List<Integer> indices = new ArrayList<>();

        float tileWidth = (float) texture.getWidth() / (float) numCols;
        float tileHeight = (float) texture.getHeight() / (float) numRows;

        for (int i = 0; i < numChars; i++) {
            byte currChar = chars[i];
            int col = currChar % numCols;
            int row = currChar / numCols;

            // left top
            positions.add((float) i * tileWidth);
            positions.add(0.0f);
            positions.add(ZPOS);
            textCoords.add((float) col / (float) numCols);
            textCoords.add((float) row / (float) numRows);
            indices.add(i * VERTICES_PER_QUAD);

            // left bottom
            positions.add((float) i * tileWidth);
            positions.add(tileHeight);
            positions.add(ZPOS);
            textCoords.add((float) col / (float) numCols);
            textCoords.add((float) (row + 1) / (float) numRows);
            indices.add(i * VERTICES_PER_QUAD + 1);

            positions.add((float) i * tileWidth + tileWidth);
            positions.add(tileHeight);
            positions.add(ZPOS);
            textCoords.add((float) (col + 1) / (float) numCols);
            textCoords.add((float) (row + 1) / (float) numRows);
            indices.add(i * VERTICES_PER_QUAD + 2);

            positions.add((float) i * tileWidth + tileWidth);
            positions.add(0.0f);
            positions.add(ZPOS);
            textCoords.add((float) (col + 1) / (float) numCols);
            textCoords.add((float) row / (float) numRows);
            indices.add(i * VERTICES_PER_QUAD + 3);

            indices.add(i * VERTICES_PER_QUAD);
            indices.add(i * VERTICES_PER_QUAD + 2);
        }

        float[] posArr = Utils.listToArray(positions);
        float[] textCoordsArr = Utils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setMaterial(new Material(texture));
        return mesh;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        Texture texture = this.getMesh().getMaterial().getTexture();
        this.getMesh().deleteBuffers();
        this.setMesh(buildMesh(texture, numCols, numRows));
    }
}

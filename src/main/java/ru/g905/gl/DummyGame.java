/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.gl;

import java.util.ArrayList;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import ru.g905.engine.GameItem;
import ru.g905.engine.IGameLogic;
import ru.g905.engine.Window;
import ru.g905.engine.graph.Mesh;
import ru.g905.engine.graph.Texture;

/**
 *
 * @author g905
 */
public class DummyGame implements IGameLogic {

    private int displxInc = 0;
    private int displyInc = 0;
    private int displzInc = 0;
    private int scaleInc = 0;
    private final Renderer renderer;
    private Mesh mesh;
    private ArrayList<GameItem> gameItems;

    public DummyGame() {
        renderer = new Renderer();
        gameItems = new ArrayList<>();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        float[] positions = new float[]{
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,
            
            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,

            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,

            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,

            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f,};
        float[] texCoords = new float[] {
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,
            
            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            
            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,

            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,

            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,

            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,
        };
        int[] indices = new int[]{
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            14, 15, 6, 4, 14, 6,
            // Bottom face
            16, 18, 19, 17, 16, 19,
            // Back face
            4, 6, 7, 5, 4, 7,
        };
        Texture texture = new Texture("textures/grassblock.png");
        mesh = new Mesh(positions, texCoords, indices, texture);
        
        float z = 0;
        for(int i = 0; i < 200; ++i) {
            for (int j = 0; j < 200; ++j) {
                if (i < 50 || j < 50) {
                    z -= 0.001f;
                } else {
                    z += 0.001f;
                }
                GameItem gamei = new GameItem(mesh);
                gamei.setPosition(i, j, (z*z));
                gameItems.add(gamei);
            }
        }

        //gameItems = new GameItem[]{gameItem, gameItem2};
    }

    @Override
    public void input(Window window) {
        displxInc = 0;
        displyInc = 0;
        displzInc = 0;
        scaleInc = 0;
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displyInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displyInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displxInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displxInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            displzInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = -1;
        }
    }

    @Override
    public void update(float interval) {
        for (GameItem gameItem : gameItems) {
            Vector3f itemPos = gameItem.getPosition();
            float posx = itemPos.x + displxInc * 1f;
            float posy = itemPos.y + displyInc * 1f;
            float posz = itemPos.z + displzInc * 1f;
            gameItem.setPosition(posx, posy, posz);
            
            float scale = gameItem.getScale();
            scale += scaleInc * 0.05f;
            if (scale < 0) {
                scale = 0;
            }
            gameItem.setScale(scale);
            
            float zrotation = gameItem.getRotation().z + 1.5f;
            float yrotation = gameItem.getRotation().y + 1.5f;
            float xrotation = gameItem.getRotation().x + 1.5f;
            if (zrotation > 360) {
                zrotation = 0;
            }
            gameItem.setRotation(xrotation, yrotation, zrotation);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.gl;

import java.util.ArrayList;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import ru.g905.engine.GameItem;
import ru.g905.engine.IGameLogic;
import ru.g905.engine.MouseInput;
import ru.g905.engine.Window;
import ru.g905.engine.graph.Camera;
import ru.g905.engine.graph.Mesh;
import ru.g905.engine.graph.ObjLoader;
import ru.g905.engine.graph.Texture;

/**
 *
 * @author g905
 */
public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;
    private final Camera camera;
    private ArrayList<GameItem> gameItems;
    private static final float CAMERA_POS_STEP = 0.05f;

    public DummyGame() {
        renderer = new Renderer();
        gameItems = new ArrayList<>();
        camera = new Camera();
        cameraInc = new Vector3f();

    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        Texture texture = new Texture("src/main/resources/textures/grassblock.png");
        Mesh mesh = ObjLoader.loadMesh("models/screw.obj");
        //mesh.setTexture(texture);

        float z = 0;
        float inc = 0.05f;
        float inc2 = 0.05f;
        for (int i = 0; i < 50; ++i) {
            if (i < 25) {
                z += inc;
            } else {
                z -= inc;
            }
            for (int j = 0; j < 50; ++j) {
                if (j < 25) {
                    z += inc2;
                } else {
                    z -= inc2;
                }
                GameItem gamei = new GameItem(mesh);
                gamei.setPosition(i, z * z, j);
                gamei.setScale(0.01f);
                gameItems.add(gamei);
            }
        }
        /*GameItem gamei = new GameItem(mesh);
        gamei.setPosition(0, 0, -2);
        gamei.setScale(1.5f);
        gameItems.add(gamei);*/

        //gameItems = new GameItem[]{gameItem, gameItem2};
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSIVITY, rotVec.y * MOUSE_SENSIVITY, 0);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}

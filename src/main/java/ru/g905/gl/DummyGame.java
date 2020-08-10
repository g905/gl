/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.gl;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.*;
import ru.g905.engine.IGameLogic;
import ru.g905.engine.MouseInput;
import ru.g905.engine.Scene;
import ru.g905.engine.SceneLight;
import ru.g905.engine.Window;
import ru.g905.engine.graph.Camera;
import ru.g905.engine.graph.Material;
import ru.g905.engine.graph.Mesh;
import ru.g905.engine.graph.Renderer;
import ru.g905.engine.graph.Texture;
import ru.g905.engine.graph.anim.AnimGameItem;
import ru.g905.engine.graph.lights.DirectionalLight;
import ru.g905.engine.graph.particles.FlowParticleEmitter;
import ru.g905.engine.graph.particles.Particle;
import ru.g905.engine.items.GameItem;
import ru.g905.engine.items.Terrain;
import ru.g905.engine.loaders.md5.MD5AnimModel;
import ru.g905.engine.loaders.md5.MD5Loader;
import ru.g905.engine.loaders.md5.MD5Model;
import ru.g905.engine.loaders.obj.ObjLoader;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private Hud hud;

    private static final float CAMERA_POS_STEP = 0.05f;

    private Terrain terrain;

    private float angleInc;

    private float lightAngle;

    private AnimGameItem monster;

    private FlowParticleEmitter particleEmitter;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        angleInc = 0;
        lightAngle = 45;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();

        float reflectance = 1f;

        Mesh quadMesh = ObjLoader.loadMesh("models/plane.obj");
        Material quadMaterial = new Material(new Vector4f(0.0f, 0.0f, 1.0f, 1.f), reflectance);
        quadMesh.setMaterial(quadMaterial);
        GameItem quadGameItem = new GameItem(quadMesh);
        quadGameItem.setPosition(0, 0, 0);
        quadGameItem.setScale(2.5f);

        // Setup  GameItems
        MD5Model md5Meshodel = MD5Model.parse("models/monster.md5mesh");
        MD5AnimModel md5AnimModel = MD5AnimModel.parse("models/monster.md5anim");
        //MD5Model md5Meshodel = MD5Model.parse("models/boblamp.md5mesh");
        //MD5AnimModel md5AnimModel = MD5AnimModel.parse("models/boblamp.md5anim");

        monster = MD5Loader.process(md5Meshodel, md5AnimModel, new Vector4f(1, 1, 1, 1));
        monster.setScale(0.05f);
        monster.setRotation(90, 0, 0);
        monster.getMesh().getMaterial().setReflectance(1.0f);
        monster.getMesh().getMaterial().setNormalMap(new Texture("src/main/resources/textures/monster/hellknight_local.png"));

        scene.setGameItems(new GameItem[]{quadGameItem, monster});

        // Setup Lights
        setupLights();

        //Setup particles
        Vector3f particleSpeed = new Vector3f(0, 1, 0);
        particleSpeed.mul(2.5f);
        long ttl = 4000;
        int maxParticles = 200;
        long creationPeriodMillis = 300;
        float range = 0.2f;
        float scale = 1.0f;
        Mesh partMesh = ObjLoader.loadMesh("models/particle.obj");
        Texture texture = new Texture("src/main/resources/textures/particle_anim.png", 4, 4);

        Material partMaterial = new Material(texture, reflectance);
        partMesh.setMaterial(partMaterial);

        Particle particle = new Particle(partMesh, particleSpeed, ttl, 100);
        particle.setScale(scale);
        particleEmitter = new FlowParticleEmitter(particle, maxParticles, creationPeriodMillis);
        particleEmitter.setActive(true);
        particleEmitter.setPositionRndRange(range);
        particleEmitter.setSpeedRndRange(range);
        this.scene.setParticleEmitters(new FlowParticleEmitter[]{particleEmitter});

        camera.getPosition().x = 0.25f;
        camera.getPosition().y = 6.5f;
        camera.getPosition().z = 6.5f;
        camera.getRotation().x = 25;
        camera.getRotation().y = -1;
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightDirection = new Vector3f(0, 1, 1);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
        directionalLight.setShadowPosMult(5);
        directionalLight.setOrthoCoords(-10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 20.0f);
        sceneLight.setDirLight(directionalLight);
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
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            angleInc -= 0.05f;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            angleInc += 0.05f;
        } else {
            angleInc = 0;
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            monster.nextFrame();
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        // Update camera position
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        // Check if there has been a collision. If true, set the y position to
        // the maximum height
        float height = terrain != null ? terrain.getHeight(camera.getPosition()) : -Float.MAX_VALUE;
        if (camera.getPosition().y <= height) {
            camera.setPosition(prevPos.x, prevPos.y, prevPos.z);
        }

        lightAngle += angleInc;
        if (lightAngle < 0) {
            lightAngle = 0;
        } else if (lightAngle > 180) {
            lightAngle = 180;
        }
        float zValue = (float) Math.cos(Math.toRadians(lightAngle));
        float yValue = (float) Math.sin(Math.toRadians(lightAngle));
        Vector3f lightDirection = this.scene.getSceneLight().getDirLight().getDirection();
        lightDirection.x = 0;
        lightDirection.y = yValue;
        lightDirection.z = zValue;
        lightDirection.normalize();

        particleEmitter.update((long) (interval * 1000));
    }

    @Override
    public void render(Window window) {
        if (hud != null) {
            hud.updateSize(window);
        }
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        scene.cleanup();
        if (hud != null) {
            hud.cleanup();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine.graph.particles;

import org.joml.Vector3f;
import ru.g905.engine.graph.Mesh;
import ru.g905.engine.graph.Texture;
import ru.g905.engine.items.GameItem;

/**
 *
 * @author zharnikov
 */
public class Particle extends GameItem {

    private Vector3f speed;

    private long ttl;

    private long updateTextureMillis;

    private long currentAnimTimeMillis;

    private int animFrames;

    public Particle(Mesh mesh, Vector3f speed, long ttl, long updateTextureMillis) {
        super(mesh);
        this.speed = speed;
        this.ttl = ttl;
        this.updateTextureMillis = updateTextureMillis;
        this.currentAnimTimeMillis = 0;
        Texture texture = this.getMesh().getMaterial().getTexture();
        this.animFrames = texture.getNumCols() * texture.getNumRows();
    }

    public Particle(Particle baseParticle) {
        super(baseParticle.getMesh());
        Vector3f aux = baseParticle.getPosition();
        setPosition(aux.x, aux.y, aux.z);
        aux = baseParticle.getRotation();
        setRotation(aux.x, aux.y, aux.z);
        setScale(baseParticle.getScale());
        this.speed = new Vector3f(baseParticle.speed);
        this.ttl = baseParticle.ttl;
        this.updateTextureMillis = baseParticle.getUpdateTextureMillis();
        this.currentAnimTimeMillis = 0;
        this.animFrames = baseParticle.getAnimFrames();
    }

    public Vector3f getSpeed() {
        return speed;
    }

    public void setSpeed(Vector3f speed) {
        this.speed = speed;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public int getAnimFrames() {
        return animFrames;
    }

    public void setAnimFrames(int animFrames) {
        this.animFrames = animFrames;
    }

    public long updateTtl(long elapsedTime) {
        this.ttl -= elapsedTime;
        this.currentAnimTimeMillis += elapsedTime;
        if (this.currentAnimTimeMillis >= getUpdateTextureMillis() && this.animFrames > 0) {
            this.currentAnimTimeMillis = 0;
            int pos = this.getTextPos();
            pos++;
            if (pos < this.animFrames) {
                this.setTextPos(pos);
            } else {
                this.setTextPos(0);
            }
        }
        return this.ttl;
    }

    public long getUpdateTextureMillis() {
        return updateTextureMillis;
    }

    public void setUpdateTextureMillis(long updateTextureMillis) {
        this.updateTextureMillis = updateTextureMillis;
    }

    public long getCurrentAnimTimeMillis() {
        return currentAnimTimeMillis;
    }

    public void setCurrentAnimTimeMillis(long currentAnimTimeMillis) {
        this.currentAnimTimeMillis = currentAnimTimeMillis;
    }

}

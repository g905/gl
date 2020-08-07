/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine.loaders.md5;

import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author zharnikov
 */
class MD5Utils {

    public static final String FLOAT_REGEXP = "[+-]?\\d*\\.?\\d*";
    public static final String VECTOR3_REGEXP = "\\(\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*\\)";

    private MD5Utils() {

    }

    public static Quaternionf calculateQuaternion(Vector3f vec) {
        Quaternionf orientation = new Quaternionf(vec.x, vec.y, vec.z, 0);
        float temp = 1.0f - (orientation.x * orientation.x) - (orientation.y * orientation.y) - (orientation.z * orientation.z);
        if (temp < 0.0f) {
            orientation.w = 0.0f;
        } else {
            orientation.w = -(float) (Math.sqrt(temp));
        }
        return orientation;
    }
}
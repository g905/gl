/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author g905
 */
public class Utils {

    public static String loadResource(String filename) throws Exception {
        System.out.println("load filename: " + filename);
        String result;
        try (InputStream in = Utils.class.getResourceAsStream(filename);
                Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        System.out.println("trying to load model: " + fileName);
        Class cls = Class.forName(Utils.class.getName());
        ClassLoader cloader = cls.getClassLoader();
        InputStream i = cloader.getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(i));
        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        /*try (
                Class cls = Class.forName("test");
                BufferedReader br = new BufferedReader(new InputStreamReader(Class.forName(Utils.class.getName()).getResourceAsStream(fileName)))) {
            String line;
            while((line = br.readLine()) != null) {
                list.add(line);
            }
        }*/
        return list;
    }

    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    public static boolean existsResourceFile(String fileName) {
        boolean result;
        try (InputStream is = Utils.class.getResourceAsStream(fileName)) {
            result = is != null;
        } catch (Exception excp) {
            result = false;
        }
        return result;
    }
}

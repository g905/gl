/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.engine;

import java.io.InputStream;
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.g905.gl;

import ru.g905.engine.GameEngine;
import ru.g905.engine.IGameLogic;

/**
 *
 * @author g905
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEng = new GameEngine("Game", 600, 480, vSync, gameLogic);
            gameEng.run();
        } catch (Exception exp) {
            exp.printStackTrace();
            System.exit(-1);
        }
    }

}

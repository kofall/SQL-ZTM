/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package com.example.ztm;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author kofal
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) {
        try {
            new Swapper(false, stage, null, null, null, "startup/loginFXML", null);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class sRegisterFXController implements Initializable {

    @FXML
    private Label lb_Error;
    @FXML
    private TextField tf_Username;
    @FXML
    private TextField tf_Password;
    @FXML
    private TextField tf_Email;
    @FXML
    private TextField tf_Name;
    @FXML
    private TextField tf_Surname;

    private Stage stage = null;
    private User user = null;

    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void register(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            /*
            ADD THE RECORD IF POSSIBLE
             */
            user = new User(tf_Username.getText(), tf_Password.getText());
            if(true/*RECORD_FOUND*/) {
                if (tf_Username.getText().equalsIgnoreCase("admin") &&
                        tf_Password.getText().equalsIgnoreCase("admin")) {
                    swapScenes_admin();
                } else {
                    swapScenes_user();
                }
            }
            else {
                close(event);
            }
        }
    }

    @FXML
    private void close(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, null, null, null, "startup/loginFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void swapScenes_admin() {
        try {
            new Swapper(false, stage, user, null, null, "admin/adminFXML", null);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void swapScenes_user() {
        try {
            new Swapper(false, stage, user, null, null, "user/userFXML", null);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}

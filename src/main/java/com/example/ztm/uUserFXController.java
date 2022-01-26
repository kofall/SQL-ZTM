/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class uUserFXController implements Initializable {

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

    public void initTables() {
        /*
        HERE INITIALIZE TABLE VALUES
         */
    }

    @FXML
    private void swapKoszyk(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                Swapper swapper = new Swapper(false, stage, user, null, null, "user/buyTicketFXML", null);
                ((uBuyTicketFXController) swapper.getController()).myInitialize(true);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void swapHistoria(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/userHistoriaFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void swapBilety(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/userBiletyFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void swapUlgi(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/userUlgiFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void swapLinie(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/userLinieFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void swapLogout(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, null, null, null, "startup/loginFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}

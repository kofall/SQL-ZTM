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
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class uLinieFXController implements Initializable {

    @FXML
    private TextField tf_Pattern;
    @FXML
    private TableView<?> tv_Table;

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
    private void find(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            /*
            FIND RECORDS (ObservableList)
             */
        }
    }

    @FXML
    private void swapKoszyk(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/buyTicketFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void swapUlgi(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/ulgiFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void swapTrasa(MouseEvent event) {
        if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if(event.getClickCount() == 2) {
                try {
                    /*
                    CHECK IF THE RECORD IS SELECTED
                     */
                    Swapper swapper = new Swapper(false, stage, user, null, null, "user/trasaFXML", null);
                    ((uTrasaFXController) swapper.getController()).myInitialize(null/*RECORD*/);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    @FXML
    private void close(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/userHistoriaFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}

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
public class uSelectReliefCountFXController implements Initializable {

    @FXML
    private Label lb_Name;
    @FXML
    private Label lb_Price;
    @FXML
    private Label lb_TotalCost;
    @FXML
    private TextField tf_Pattern;
    @FXML
    private TableView<?> tv_TableUlgi;
    @FXML
    private TableView<?> tv_TableWybraneUlgi;

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

    public void myInitialize(String name, String price, Object record) {
        lb_Name.setText(name);
        lb_Price.setText(price);
        /*
        SET THE RECORD IF MODIFY
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
    private void addUlga(MouseEvent event) {
        if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if(event.getClickCount() == 2) {
                /*
                ADD THE RECORD IF POSSIBLE
                 */
                /*
                UPDATE TotalCost LABEL
                 */
            }
        }
    }

    @FXML
    private void deleteRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                /*
                CHECK IF THE RECORD IS SELECTED
                 */
                Swapper swapper = new Swapper(true, null, user, null, null, "startup/sureFXML", null);
                ((sSureFXController) swapper.getController()).myInitialize(this, null/*RECORD*/, null, null, null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void addToCart(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            /*
            ADD THE RECORDS IF POSSIBLE
            */
            back(event);
        }
    }

    @FXML
    private void back(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/buyTicketFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}

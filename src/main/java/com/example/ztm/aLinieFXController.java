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
public class aLinieFXController implements Initializable {

    @FXML
    private TableView<?> tv_Table;
    @FXML
    private TextField tf_Pattern;

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
    private void add(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                Swapper swapper = new Swapper(true, null, user, null, null, "admin/insertUpdateLinieFXML", "Linia");
                ((aInsertUpdateLinieFXController) swapper.getController()).myInitialize(this, null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void modify(MouseEvent event) {
        if(event.getSource() == tv_Table) {
            if (!(event.getEventType() == MouseEvent.MOUSE_PRESSED &&
                    event.getClickCount() == 2)) {
                return;
            }
        }
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                /*
                CHECK IF THE RECORD IS SELECTED
                 */
                Swapper swapper = new Swapper(true, null, user, null, null, "admin/insertUpdateLinieFXML", "Linia");
                ((aInsertUpdateLinieFXController) swapper.getController()).myInitialize(this, null/*RECORD*/);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void delete(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                /*
                CHECK IF THE RECORD IS SELECTED
                 */
                Swapper swapper = new Swapper(true, null, user, null, null, "startup/sureFXML", null);
                ((sSureFXController) swapper.getController()).myInitialize(this, null/*RECORD*/);
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
                    Swapper swapper = new Swapper(false, stage, user, null, null, "admin/trasaFXML", null);
                    ((aTrasaFXController) swapper.getController()).myInitialize(null/*RECORD*/);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    @FXML
    private void back(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "admin/adminFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}

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
public class uBuyTicketFXController implements Initializable {

    @FXML
    private TextField tf_Pattern;
    @FXML
    private TableView<?> tv_TableBilety;
    @FXML
    private TableView<?> tv_TableWybraneBilety;
    @FXML
    private Label lb_TotalCost;

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

    public void myInitialize(String totalPrice) {
        /*
        SET VALUE OF TotalPrice LABEL TO current + totalPrice
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
    private void deleteRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
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
    private void buyANDvalidate(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            /*
            ADD THE RECORDS IF POSSIBLE
             */
            close(event);
        }
    }

    @FXML
    private void buy(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            /*
            ADD THE RECORDS IF POSSIBLE
             */
            close(event);
        }
    }

    @FXML
    private void swapWybierzUlgi(MouseEvent event) {
        if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if(event.getClickCount() == 2) {
                try {
                    Swapper swapper = new Swapper(false, stage, user, null, null, "user/selectReliefCountFXML", null);
                    /*
                    CHECK IF THE RECORD FROM TableWybraneBilety IS SELECTED
                     */
                    ((uSelectReliefCountFXController) swapper.getController()).myInitialize(null/*TICKET_NAME*/, null/*TICKET_PRICE*/, null/*RECORD*/);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
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
    private void swapLinie(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/linieFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
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

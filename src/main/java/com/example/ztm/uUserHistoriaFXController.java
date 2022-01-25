/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.ztm.User;
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
public class uUserHistoriaFXController implements Initializable {

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
    private void validate(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            /*
            VALIDATE THE RECORD IF POSSIBLE
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
    private void swapForMoreInfo(MouseEvent event) {
        if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if(event.getClickCount() == 2) {
                try {
                    /*
                    CHECK IF THE RECORD IS SELECTED
                     */
                    Swapper swapper = new Swapper(false, stage, user, null, null, "user/userHistoriaBiletInfoFXML", null);
                    /*
                    do dokończenia
                     */
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
                new Swapper(false, stage, user, null, null, "user/userFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}

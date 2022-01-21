/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class aPrzejazdyFXController implements Initializable {

    @FXML
    private TableView<Map<String,Object>> tv_Table;

    @FXML
    private TextField tf_Pattern;

    private Stage stage = null;
    private User user = null;

    private ObservableList<Map<String, Object>> table_items = FXCollections.<Map<String, Object>>observableArrayList();
    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tv_Table.getSelectionModel().setCellSelectionEnabled(true);

        final ObservableList<TablePosition> selectedCells = tv_Table.getSelectionModel().getSelectedCells();
        selectedCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change change) {
                for (TablePosition pos : selectedCells) {
                    EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            swapForMoreInfo(event, pos.getRow(), pos.getTableColumn().getText());
                        }
                    };
                }
            };
        });
    }

    public void initTables() {
        /*
        YOU KNOW BRO, HERE SHOULD BE SOME INITS, COFFEE BREAK YEEEEEEAH EASTEREGG
         */
    }

    @FXML
    private void find(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {

        }
    }

    @FXML
    private void add(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                Swapper swapper = new Swapper(true, null, user, null, null, "admin/insertUpdatePrzejazdyFXML", "Przejazd");
                ((aInsertUpdatePrzejazdyFXController) swapper.getController()).myInitialize(this, null);
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
                Swapper swapper = new Swapper(true, null, user, null, null, "admin/insertUpdatePrzejazdyFXML", "Przejazd");
                ((aInsertUpdatePrzejazdyFXController) swapper.getController()).myInitialize(this, null/*RECORD*/);
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
                Swapper swapper = new Swapper(true, null, user, null, null, "startup/sureFXML", null);
                ((sSureFXController) swapper.getController()).myInitialize(this, null/*RECORD*/, null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void swapForMoreInfo(MouseEvent event, Object record, String column) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if(event.getClickCount() == 2) {
                try {
                    Swapper swapper = null;
                    switch (column) {
                        case "Linia nr":
                            swapper = new Swapper(true, null, user, null, null, "admin/liniaInfoFXML", column);
                            ((aLiniaInfoFXController) swapper.getController()).myInitialize(null/*RECORD*/);
                        case "Kierowca":
                            swapper = new Swapper(true, null, user, null, null, "admin/kierowcaInfoFXML", column);
                            ((aKierowcaInfoFXController) swapper.getController()).myInitialize(null/*RECORD*/);
                        case "Pojazd":
                            swapper = new Swapper(true, null, user, null, null, "admin/pojazdInfoFXML", column);
                            ((aPojazdInfoFXController) swapper.getController()).myInitialize(null/*RECORD*/);
                    }
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

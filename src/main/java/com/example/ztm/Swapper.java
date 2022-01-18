package com.example.ztm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Swapper {
    private Object controller = null;

    public Swapper(boolean newStage, Stage stage, User user, String backFxmlFile, String backTitle, String newFxmlFile, String newTitle) throws IOException {
        stage = newStage ? new Stage() : stage;

        if (backFxmlFile != null) {
            Stage finalStage = stage;
            stage.setOnCloseRequest(event -> {
                event.consume();
                try {
                    new Swapper(false, finalStage, user, null, null, backFxmlFile, backTitle);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

            });
        }

        swapScenes(stage, user, newFxmlFile, newTitle);
    }

    private void swapScenes(Stage stage, User user, String fxmlFile, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/" + fxmlFile + ".fxml"));
        Parent root = (Parent) fxmlLoader.load();

        controller = fxmlLoader.getController();
        try {
            controller.getClass().getMethod("setStage", Stage.class).invoke(controller, stage);
            controller.getClass().getMethod("setUser", User.class).invoke(controller, user);
            controller.getClass().getMethod("initTables").invoke(controller);
        } catch (IllegalAccessException e) {
            "".isEmpty();
        } catch (InvocationTargetException e) {
            "".isEmpty();
        } catch (NoSuchMethodException e) {
            "".isEmpty();
        }

        stage.setTitle(title == null ? "ZTM" : title);
        stage.getIcons().add(new Image(controller.getClass().getResourceAsStream("images/bus.png")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public Object getController() {
        return this.controller;
    }
}

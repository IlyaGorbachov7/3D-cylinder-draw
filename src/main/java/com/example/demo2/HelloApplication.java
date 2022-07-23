package com.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Класс запускает приложение
 */
public class HelloApplication extends Application {
    private Stage stageApp;

    @Override
    public void start(Stage stage) throws IOException {
        stageApp = stage;
        initStage();
        stageApp.show();
    }

    private void initStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene sceneApp = new Scene(fxmlLoader.load());
        // устанавливаем иконку
        InputStream iconStream = getClass().getResourceAsStream("/image/Cylinder.jpg");
        Image icon = new Image(iconStream);
        stageApp.getIcons().add(icon);
        // устанавливаем сцену окну
        stageApp.setScene(sceneApp);

    }



    public static void main(String[] args) {
        launch();
    }
}
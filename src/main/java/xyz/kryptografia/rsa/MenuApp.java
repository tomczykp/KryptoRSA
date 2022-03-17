package xyz.kryptografia.rsa;

import javafx.application.Application;
import javafx.stage.Stage;

public class MenuApp extends Application {

    @Override
    public void start(Stage stage) {

        MenuController menu = new MenuController();
        menu.showStage();
    }

    public static void main(String[] args) {
        launch();
    }

}
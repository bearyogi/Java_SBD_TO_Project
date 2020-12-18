package client.java.controllers;

import java.io.IOException;

import client.resources.tools.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class SceneCreator {

    public static void launchScene (String sceneName, User activeUser) throws IOException {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource(sceneName));
        Main.setRoot(loader.load());
        Scene scene = new Scene(Main.getRoot());
        Main.getStage().setScene(scene);
        Main.getStage().show();
        Main.setUser(activeUser);
    }

}

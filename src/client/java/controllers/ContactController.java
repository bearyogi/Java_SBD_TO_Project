package client.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import client.resources.tools.Clock;

import java.io.IOException;

public class ContactController {
    @FXML
    Label clockLabel;

    Clock clk;
    Thread th;
    @FXML
    public void initialize(){
         clk = new Clock(clockLabel);
         th = new Thread(clk);
        th.start();
    }
    @FXML
    public void logOutButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("../../resources/fxml-files/LogInScene.fxml",Main.getUser());
        shutdown();
    }
    @FXML
    public void goBackButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("../../resources/fxml-files/UserScene.fxml",Main.getUser());
        shutdown();
    }
    public void shutdown(){
        clk.terminate();
    }
}

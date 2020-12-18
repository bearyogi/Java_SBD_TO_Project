package client.java.controllers;

import client.resources.tools.Tour;
import client.resources.tools.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.*;
import java.io.*;

public class Main extends Application {

    static Parent root;
    static Stage primaryStage;
    static Main main;
    static User activeUser;
    static Tour activeTour;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("../../resources/fxml-files/LogInScene.fxml"));
        activeUser = new User();
        activeTour = new Tour();
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("Booking Application");
        Scene welcomeScene = new Scene(root, 1024, 768);
        primaryStage.setResizable(false);
        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        main = new Main();
        Socket s = new Socket("localhost", 4999);

        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("Hi, its me, Server!");
        pr.flush();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        System.out.println("server : " + str);
        launch(args);
    }

    public static void setRoot(Parent root) {
        Main.root = root;
    }

    public static Parent getRoot() {
        return root;
    }

    public static Stage getStage() {

        return primaryStage;
    }

    public static void setStage(Stage stage) {

        Main.primaryStage = stage;
    }

    public static void setUser(User user) {
        Main.activeUser = user;
    }

    public static User getUser() {
        return Main.activeUser;
    }

    public static Tour getTour() {
        return Main.activeTour;
    }
}

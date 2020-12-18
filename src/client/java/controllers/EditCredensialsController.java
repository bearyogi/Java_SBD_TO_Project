package client.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import client.resources.tools.Clock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EditCredensialsController {

    @FXML
    Label clockLabel;
    @FXML
    Label nameLabel;
    @FXML
    Label surnameLabel;
    @FXML
    Label emailLabel;
    @FXML
    Label nickLabel;
    @FXML
    Label passwordLabel;
    @FXML
    TextField nameInput;
    @FXML
    TextField surnameInput;
    @FXML
    TextField emailInput;
    @FXML
    TextField passwordInput;
    @FXML
    Label errorLabel;

    Clock clk;
    Thread th;
    @FXML
    public void initialize(){
        clk = new Clock(clockLabel);
        th = new Thread(clk);
        th.start();
        nameLabel.setText(Main.getUser().getName());
        surnameLabel.setText(Main.getUser().getSurname());
        nickLabel.setText(Main.getUser().getNick());
        emailLabel.setText(Main.getUser().getEmail());
        passwordLabel.setText(Main.getUser().getPassword());
    }
    public void logOutButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("../../resources/fxml-files/LogInScene.fxml",Main.getUser());
        shutdown();
    }
    public void confirmButton(MouseEvent event) throws IOException {
        if (nameInput.getText().isEmpty() || surnameInput.getText().isEmpty() || emailInput.getText().isEmpty() || passwordInput.getText().isEmpty()) {
            errorLabel.setText("Należy wypełnić wszystkie tabele z danymi!");
        } else {
            communicateWithServer();

        }

    }
    public void goBackButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("../../resources/fxml-files/UserScene.fxml",Main.getUser());
        shutdown();
    }
    public void communicateWithServer() throws IOException {
        String result = "changeUserData " + nameInput.getText() + " " + surnameInput.getText() + " " + emailInput.getText() + " " + passwordInput.getText() + " " + Main.getUser().getNick();
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(result);
        pr.flush();
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        System.out.println("server : " + str);
        if (str.equals("Accepted")) {
            Main.getUser().setEmail(emailInput.getText());
            Main.getUser().setName(nameInput.getText());
            Main.getUser().setSurname(surnameInput.getText());
            Main.getUser().setPassword(passwordInput.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Potwierdzenie");
            alert.setHeaderText(null);
            alert.setContentText("Dane użytkownika zostały pomyślnie zmienione!");
            alert.setX(750);
            alert.setY(384);
            alert.showAndWait();
            SceneCreator.launchScene("../../resources/fxml-files/UserScene.fxml",Main.getUser());
            shutdown();
            }else{
                errorLabel.setText("Wpisany adres e-mail jest już używany!");
            }
    }
    public void shutdown(){
        clk.terminate();
    }
}

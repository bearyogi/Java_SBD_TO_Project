package client.java.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class RegisterController {

  @FXML
  Label errorLabel;
  @FXML
  TextField usernameBox;
  @FXML
  TextField passwordBox;
  @FXML
  TextField nameBox;
  @FXML
  TextField surnameBox;
  @FXML
  TextField emailBox;
  @FXML
  AnchorPane mainPane;

    @FXML
    public void registerButton(MouseEvent event) throws IOException{
      if(!usernameBox.getText().isEmpty() && !passwordBox.getText().isEmpty() && !nameBox.getText().isEmpty() && !surnameBox.getText().isEmpty() && !emailBox.getText().isEmpty()) {
        if(emailBox.getText().indexOf('@') == -1){
          errorLabel.setText("Podaj poprawny adres E-mail!");
        }else{
          communicateWithServer();
        }
      }else{
        errorLabel.setText("Wszystkie pola muszą zostać uzupełnione!");
      }
    }
    public void goBackButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("../../resources/fxml-files/LogInScene.fxml",Main.getUser());
    }

  public void communicateWithServer() throws IOException {
    String result = "register " + usernameBox.getText() + " " + passwordBox.getText() + " " + nameBox.getText() + " " + surnameBox.getText() + " " + emailBox.getText();
    Socket s = new Socket("localhost", 4999);

    PrintWriter pr = new PrintWriter(s.getOutputStream());
    pr.println(result);
    pr.flush();

    InputStreamReader in = new InputStreamReader(s.getInputStream());
    BufferedReader bf = new BufferedReader(in);

    String str = bf.readLine();
    System.out.println("server : " + str);
    if (str.equals("Accepted")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Potwierdzenie");
      alert.setHeaderText(null);
      alert.setContentText("Nowe konto użytkownika zostało utworzone!");
      alert.setX(750);
      alert.setY(384);
      alert.showAndWait();


      SceneCreator.launchScene("../../resources/fxml-files/LogInScene.fxml",Main.getUser());
    } else {
      errorLabel.setText("Użytkownik o podanych danych (email,nick) już istnieje!");
    }
  }

}
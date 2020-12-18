package client.java.controllers;
import client.resources.tools.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainController {
    @FXML
    TextField usernameBox;
    @FXML
    PasswordField passwordBox;
    @FXML
     Label errorLabel;
    @FXML
    public void loginButton(MouseEvent event) throws IOException {
        if(!usernameBox.getText().isEmpty() && !passwordBox.getText().isEmpty()) {
            communicateWithServer();
        }else{
            errorLabel.setText("Pole nazwa użytkownika i hasło nie mogą być puste!");
        }
    }


    public void registerButton(MouseEvent event) throws IOException {
        SceneCreator.launchScene("../../resources/fxml-files/RegisterScene.fxml", Main.getUser());
    }

    public void communicateWithServer() throws IOException {
        String result = "users " + usernameBox.getText() + " " + passwordBox.getText();
        Socket s = new Socket("localhost", 4999);

        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(result);
        pr.flush();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String str = bf.readLine();
        System.out.println("server : " + str);
        if (str.equals("Accepted")) {
            if(usernameBox.getText().equals("admin")){
                SceneCreator.launchScene("../../resources/fxml-files/AdminScene.fxml",Main.getUser());
            }else{
                String[] userData = askServerForUserData().split("\\s+");
                Main.getUser().setId(Integer.parseInt(userData[0]));
                Main.getUser().setEmail(userData[5]);
                Main.getUser().setName(userData[3]);
                Main.getUser().setNick(userData[1]);
                Main.getUser().setSurname(userData[4]);
                Main.getUser().setPassword(userData[2]);
                SceneCreator.launchScene("../../resources/fxml-files/UserScene.fxml",Main.getUser());
            }

        } else {
            errorLabel.setText("Wprowadzone dane są niepoprawne, lub użytkownik o podanych danych nie istnieje.");
        }
    }
    private String askServerForUserData() throws IOException{
        String result = "getUserData " + usernameBox.getText();
        Socket s = new Socket("localhost", 4999);

        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(result);
        pr.flush();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String str = bf.readLine();
        return str;
    }

}

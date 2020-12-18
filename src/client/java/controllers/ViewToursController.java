package client.java.controllers;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import client.resources.tools.Clock;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ViewToursController {

  @FXML
  Label clockLabel;
  @FXML
  ScrollPane scrollPane;
  @FXML
  GridPane gridPane;
  @FXML
  AnchorPane anchorPane2;
  @FXML
  Image image;
  @FXML
  ImageView picture;

  HBox hBox = new HBox();
  Clock clk;
  Thread th;

  int toursNumber;
  int gridPaneRows;
  int gridPaneColumns = 4;
  int iterator = 1;

    @FXML
    public void initialize() throws IOException {
        clk = new Clock(clockLabel);
        th = new Thread(clk);
        th.start();

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gridPane.setPadding(new Insets(7,7,7,7));
        gridPane.setHgap(15);
        gridPane.setVgap(10);
        toursNumber = getToursNumber();
        gridPaneRows = toursNumber/4 + 1;

        for (int i = 0 ; i < gridPaneRows; i++) {
            for (int j = 0; j < gridPaneColumns; j++) {
                if(iterator <= toursNumber){
                    addToGridPane(getTourFromServer(iterator),j, i);
                    iterator++;
                }
                }
            }
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

    public String getTourFromServer(int id) throws IOException {
        String query = "getTour " + id;
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(query);
        pr.flush();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        return bf.readLine();
    }

    public int getToursNumber() throws IOException {
        String query = "getToursNumber";
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(query);
        pr.flush();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();

        return Integer.parseInt(str);
    }

    public void addToGridPane(String tour, int columnNumber, int rowNumber){
        String[] s = tour.split("\\s+");
        if(s[7].equals("null"))
            image = new Image("/client/resources/images/noImageIcon.jpg");
        else
            image = new Image("/client/resources/images/" + s[7] + ".jpg");

        picture = new ImageView();
        picture.setFitWidth(230);
        picture.setFitHeight(250);
        picture.setImage(image);
        picture.setId(s[0]);
        hBox.getChildren().add(picture);
        GridPane.setConstraints(picture, columnNumber, rowNumber, 1,1, HPos.CENTER, VPos.CENTER);
        gridPane.getChildren().addAll(picture);

        picture.setOnMouseClicked(e -> {
            Main.getTour().setId(Integer.parseInt(s[0]));
            Main.getTour().setTitle(s[1]);
            Main.getTour().setText(s[2]);
            Main.getTour().setDistance(Integer.parseInt(s[3]));
            Main.getTour().setDays(Integer.parseInt(s[4]));
            Main.getTour().setPrice(Integer.parseInt(s[5]));
            Main.getTour().setAvailableTickets(Integer.parseInt(s[6]));
            Main.getTour().setImage(s[7]);
            try {
                SceneCreator.launchScene("../../resources/fxml-files/ViewOneTourScene.fxml", Main.getUser());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}
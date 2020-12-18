package client.java.controllers;

import client.resources.tools.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import client.resources.tools.Clock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

import static java.util.Objects.isNull;


public class RemoveReservationController {
    @FXML
    Label clockLabel;
    @FXML
    ListView<Reservation> removeList;
    ObservableList<Reservation> list = FXCollections.observableArrayList();
    Clock clk;
    Thread th;
    int toBeDeletedId;

    @FXML
    public void initialize() throws IOException {
        clk = new Clock(clockLabel);
        th = new Thread(clk);
        th.start();
        getAllReservations();
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

    @FXML
    public void confirmPopup() throws IOException {

        if(!isNull(this.removeList.getSelectionModel().getSelectedItem())) {
            Reservation res = this.removeList.getSelectionModel().getSelectedItem();
            toBeDeletedId = res.getReservationId();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie");
            alert.setHeaderText("Usuwanie rezerwacji");
            alert.setContentText("Czy jesteś pewny/a że chcesz anulować rezerwację o id: " + toBeDeletedId + "?");
            alert.setX(750);
            alert.setY(384);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                giveBackToTour();
                deleteReservation();
            }
        }
    }

    public void shutdown(){
        clk.terminate();
    }

    public void getAllReservations() throws IOException{
        String result = "getAllReservations " + Main.getUser().getId();
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(result);
        pr.flush();
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        String[] all = str.split("#");
        for(String reservation: all){
            String[] one = reservation.split("@");
            Reservation listReservation = new Reservation(Integer.parseInt(one[0]),one[1],Integer.parseInt(one[2]),one[3],one[4]);
            list.add(listReservation);
        }
        this.removeList.setItems(list);
    }

    public void deleteReservation() throws IOException{

        String result = "deleteReservation " + toBeDeletedId;
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(result);
        pr.flush();
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
            for(Reservation el: list){
                if(el.getReservationId() == toBeDeletedId) {
                    list.remove(el);
                    break;
                }
            }
    }

    public void giveBackToTour() throws IOException{
        String result = "giveBack " + toBeDeletedId;
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(result);
        pr.flush();
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
    }

}

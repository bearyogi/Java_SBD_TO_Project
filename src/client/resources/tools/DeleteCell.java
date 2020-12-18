package client.resources.tools;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DeleteCell extends ListCell<Reservation> {
    HBox hbox = new HBox();
    Label label = new Label("");
    Pane pane = new Pane();
    Button button = new Button("Usuń");
    int tourId;
    public DeleteCell() {
        super();
        button.setMinSize(100,35);
        button.setMaxSize(100,35);
        button.setStyle("-fx-background-color: #a30a0a; -fx-opacity: 80%; -fx-text-fill: white;");


        hbox.setSpacing(8);
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);

        button.setOnAction(event -> {
            try {
                getTourId(getItem().getReservationId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                giveBackToTour();
                deleteReservation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    protected void updateItem(Reservation item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);
        if (item != null && !empty) {
            label.setTextFill(Color.WHITE);
            label.setText(item.toString());
            setGraphic(hbox);
        }
    }

    public void deleteReservation() throws IOException {

        String result = "deleteReservation " + getItem().getReservationId();
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(result);
        pr.flush();
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        for(Reservation el: getListView().getItems()){
            if(el.getReservationId() == tourId) {
                getListView().getItems().remove(el);
                break;
            }
        }
        getListView().setItems(getListView().getItems());
    }

    public void giveBackToTour() throws IOException{
        String result = "giveBack " + getItem().getReservationId();
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(result);
        pr.flush();
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
    }

    public void getTourId(int id) throws IOException {
        String query = "getTourId " + id;
        Socket s = new Socket("localhost", 4999);
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(query);
        pr.flush();

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        String str = bf.readLine();
        tourId = Integer.parseInt(str);
    }
}

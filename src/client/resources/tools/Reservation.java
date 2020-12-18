package client.resources.tools;


public class Reservation {

    @Override
    public String toString() {
        return "| " + title + " |   " + "ID: " + reservationId + ", Koszt: " + totalPrice + "zł, Data wyjazdu: " + date + ", Status: " + status;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private int reservationId;
    private String title;
    private int totalPrice;
    private String date;
    private String status;

    public Reservation(int reservationId, String title, int totalPrice, String date, String status){
        this.reservationId = reservationId;
        this.title = title;
        this.totalPrice = totalPrice;
        this.date = date;
        this.status = status;
    }

}

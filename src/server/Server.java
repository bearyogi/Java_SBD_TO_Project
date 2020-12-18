package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.Semaphore;

public class Server {

    static final int PORT = 4999;

    public static void main(String[] args) throws IOException, SQLException {

        String host = "jdbc:mysql://localhost:3306/filmdb?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8";
        String userName = "root";
        String userPassword = "Minotaur21#";
        Connection connection = DriverManager.getConnection(host, userName, userPassword);

        Socket socket;
        ServerSocket serverSocket = new ServerSocket(PORT);
        Semaphore semaphore = new Semaphore(1);
        while (true) {
            socket = serverSocket.accept();
            new MyThread(socket, connection, semaphore).start();
        }

    }
}

class MyThread extends Thread {
    protected Socket socket;
    protected Connection connection;
    protected Semaphore semaphore;

    public MyThread(Socket clientSocket, Connection connection, Semaphore semaphore) {
        this.socket = clientSocket;
        this.connection = connection;
        this.semaphore = semaphore;
    }

    public void run() {
        InputStream inputStream;
        BufferedReader bufferedReader;
        DataOutputStream dataOutputStream;

        try {
            inputStream = socket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            semaphore.acquire();
        } catch (IOException | InterruptedException e) {
            return;
        }

        String line;
        while (true) {
            try {
                line = bufferedReader.readLine();

                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                } else {
                    String[] s = line.split("\\s+");
                    switch (s[0]) {
                        case "users":
                            makeLoginQuery(dataOutputStream, s);
                            break;
                        case "register":
                            makeRegisterQuery(dataOutputStream, s);
                            break;
                        case "getUserData":
                            getUserDataQuery(dataOutputStream, s);
                            break;
                        case "changeUserData":
                            changeUserDataQuery(dataOutputStream, s);
                            break;
                        case "getToursNumber":
                            getToursNumber(dataOutputStream, s);
                            break;
                        case "getTour":
                            getTour(dataOutputStream, s);
                            break;
                        case "changeAvailableTickets":
                            changeAvailableTickets(dataOutputStream, s);
                            break;
                        case "makeReservation":
                            makeReservationQuery(dataOutputStream, s);
                            break;
                        case "deleteReservation":
                            deleteReservation(dataOutputStream, s);
                            break;
                        case "getAllReservations":
                            getAllReservations(dataOutputStream, s);
                            break;
                        case "giveBack":
                            giveBackToTour(dataOutputStream, s);
                            break;
                        case "getTourId":
                            getTourId(dataOutputStream, s);
                            break;
                        case "changeToPayed":
                            changeToPayed(dataOutputStream, s);
                            break;
                        default:
                            dataOutputStream.writeBytes(line + "\n\r");
                            dataOutputStream.flush();
                            break;
                    }
                    socket.close();
                    semaphore.release();
                }
                return;
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                return;
            }
        }

    }

    private void getTour(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();
        String result = "";
        String sql = "select * from filmdb.tours where tourId = " + s[1];
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);
        if (rs.next()) {
            result = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " " + rs.getString(8);
        }
        dataOutputStream.writeBytes(result);
        dataOutputStream.flush();
    }

    private void getToursNumber(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();
        String result = "";
        String sql = "select count(*) as number from filmdb.tours";
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);
        if (rs.next()) {
            result = rs.getString(1);
        }
        dataOutputStream.writeBytes(result);
        dataOutputStream.flush();
    }

    public void makeLoginQuery(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();
        String sql = "select * from filmdb." + s[0] + " where userLogin = '" + s[1] + "' and userPassword = '" + s[2] + "'";
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);

        if (rs.next()) {
            dataOutputStream.writeBytes("Accepted" + "\n\r");
        } else {
            dataOutputStream.writeBytes("Rejected" + "\n\r");
        }
        dataOutputStream.flush();
    }

    public void makeRegisterQuery(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();
        String sql = "select * from filmdb." + "users" + " where userLogin = '" + s[1] + "' or userEmail = '" + s[5] + "'";
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);

        if (rs.next()) {
            dataOutputStream.writeBytes("Rejected" + "\n\r");
        } else {
            sql = "select * from filmdb." + "users" + " where idUser = (select max(idUser) from users);";
            System.out.println(sql);
            rs = stat.executeQuery(sql);
            rs.next();
            int biggestId = rs.getInt("idUser") + 1;
            sql = "insert into filmdb." + "users" + " (idUser, userLogin, userPassword, userName, userSurname, userEmail) values (\"" + biggestId + "\",\"" + s[1] + "\",\"" + s[2] + "\",\"" + s[3] + "\",\"" + s[4] + "\",\"" + s[5] + "\");";
            System.out.println(sql);
            stat.executeUpdate(sql);
            dataOutputStream.writeBytes("Accepted" + "\n\r");
        }
        dataOutputStream.flush();
    }

    public void getUserDataQuery(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();
        String sql = "select * from filmdb." + "users" + " where userLogin = '" + s[1] + "';";
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);

        if (rs.next()) {
            dataOutputStream.writeBytes(rs.getInt("idUser") + " " + rs.getString("userLogin") + " " + rs.getString("userPassword") + " " + rs.getString("userName") + " " + rs.getString("userSurname") + " " + rs.getString("userEmail") + "\n\r");
            dataOutputStream.flush();
        }
    }

    public void changeUserDataQuery(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();

        String sql = "select * from filmdb." + "users" + " where userEmail = '" + s[3] + "';";
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);
        if (rs.next()) {
            dataOutputStream.writeBytes("Rejected" + "\n\r");
        } else {
            sql = "update filmdb.users set userName = " + "\"" + s[1] + "\", userSurname = \"" + s[2] + "\", userEmail = \"" + s[3] + "\", userPassword = \"" + s[4] + "\"" + " where (userLogin = \"" + s[5] + "\");";
            System.out.println(sql);
            stat.executeUpdate(sql);
            dataOutputStream.writeBytes("Accepted" + "\n\r");
        }
        dataOutputStream.flush();
    }

    public void changeAvailableTickets(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();

        String sql = "select * from filmdb." + "tours" + " where tourId = " + s[1] + ";";
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);
        if (!rs.next()) {
            dataOutputStream.writeBytes("Rejected" + "\n\r");
        } else {
            sql = "update filmdb.tours set availableTickets = " + "\"" + s[2] + "\"" + " where (tourId = " + s[1] + ");";
            System.out.println(sql);
            stat.executeUpdate(sql);
            dataOutputStream.writeBytes("Accepted" + "\n\r");
        }
        dataOutputStream.flush();
    }

    public void makeReservationQuery(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();
        String sql = "select * from filmdb." + "reservations" + " where reservationsId = (select max(reservationsId) from reservations);";
        ResultSet rs;
        System.out.println(sql);
        rs = stat.executeQuery(sql);
        int biggestId;
        if (rs.next()) {
            biggestId = rs.getInt("reservationsId") + 1;
        } else {
            biggestId = 1;
        }
        sql = "insert into filmdb." + "reservations" + " (reservationsId, idUser, tourId, totalPrice, date, status) values (\"" + biggestId + "\",\"" + s[1] + "\",\"" + s[2] + "\",\"" + s[3] + "\",\"" + s[4] + "\",\"" + s[5] + "\");";
        System.out.println(sql);
        stat.executeUpdate(sql);
        dataOutputStream.writeBytes("Accepted" + "\n\r");
        dataOutputStream.flush();
    }

    public void deleteReservation(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();
        String sql = "delete from filmdb." + "reservations" + " where reservationsId = " + s[1] + ";";
        System.out.println(sql);
        stat.executeUpdate(sql);
        dataOutputStream.writeBytes("Accepted" + "\n\r");
        dataOutputStream.flush();
    }

    public void getAllReservations(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();
        String sql = "select reservationsId, tours.title, totalPrice, date, status from filmdb.reservations join tours on tours.tourId = reservations.tourId where reservations.idUser = " + s[1] + ";";
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);
        String result = "";
        while (rs.next()) {
            result += String.join("@", rs.getInt(1) + "", rs.getString(2), rs.getInt(3) + "", rs.getDate(4) + "", rs.getString(5));
            result += "#";
        }
        dataOutputStream.writeBytes(result);
        dataOutputStream.flush();
    }

    public void giveBackToTour(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        int tourId;
        int availableTickets;
        int howMany;
        Statement stat = connection.createStatement();

        String sql = "select tourId from filmdb." + "reservations" + " where reservationsId = " + s[1] + ";";
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);
        rs.next();
        tourId = rs.getInt(1);

        sql = "select availableTickets from filmdb.tours where tourId = " + tourId + ";";
        System.out.println(sql);
        rs = stat.executeQuery(sql);
        rs.next();
        availableTickets = rs.getInt(1);

        sql = "select totalPrice, tours.price from reservations join tours on reservations.tourId = tours.tourId where reservations.reservationsId = " + s[1] + ";";
        System.out.println(sql);
        rs = stat.executeQuery(sql);
        rs.next();
        howMany = rs.getInt(1) / rs.getInt(2);
        System.out.println(rs.getInt(1) + " " + rs.getInt(2) + " " + howMany);
        sql = "update filmdb.tours set availableTickets = " + (availableTickets + howMany) + " where (tourId = " + tourId + ");";
        System.out.println(sql);
        stat.executeUpdate(sql);
        dataOutputStream.writeBytes("Accepted" + "\n\r");
        dataOutputStream.flush();
    }

    public void getTourId(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();
        String sql = "select tours.tourId from filmdb.tours join reservations on reservations.tourId = tours.tourId where reservations.reservationsId = " + s[1] + ";";
        System.out.println(sql);
        ResultSet rs = stat.executeQuery(sql);
        rs.next();
        int result = rs.getInt(1);
        dataOutputStream.writeBytes(result + "");
        dataOutputStream.flush();
    }

    public void changeToPayed(DataOutputStream dataOutputStream, String[] s) throws SQLException, IOException {
        Statement stat = connection.createStatement();
        String sql = "update filmdb.reservations set status = 'oplacone' where reservationsId = " + s[1] + ";";
        System.out.println(sql);
        stat.executeUpdate(sql);
        dataOutputStream.writeBytes("Accepted" + "\n\r");
        dataOutputStream.flush();
    }
}


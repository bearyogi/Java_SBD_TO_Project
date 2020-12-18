package client.resources.tools;

public class User {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String nick;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }

    public String getSurname() {
        return surname;
    }
    public int getId(){
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setId(int id) {
        this.id = id;
    }
}

package frisbee.puf.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table (name= "players")
@Setter
@Getter
public class Player {

    @Id
    private int id;

    @Column (name = "name")
    private String name;

    @Column (name = "email")
    private String email;

    @Column (name = "password")
    private String password;

    @Column (name = "isLoggedIn")
    private boolean isLoggedIn;

    public Player() {
    }

    public Player(String name, String email, String password, boolean isLoggedIn) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

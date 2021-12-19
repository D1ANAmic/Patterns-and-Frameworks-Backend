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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column (name = "name")
    private String name;

    @Column (name = "email")
    private String email;

    @Column (name = "password")
    private String password;

    public Player() {
    }

    public Player(String name, String email, String password) {

        this.name = name;
        this.email = email;
        this.password = password;
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

}

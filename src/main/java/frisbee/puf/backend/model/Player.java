package frisbee.puf.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "players")
@Setter
@Getter
public class Player {

    /**
     * The player's id as stored in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * The player's name.
     */
    @Column(name = "name")
    private String name;

    /**
     * The player'semail.
     */
    @Column(name = "email")
    private String email;

    /**
     * The player's password.
     */
    @Column(name = "password")
    private String password;

    /**
     * Default constructor, creates a Player instance.
     */
    public Player() {
    }

    /**
     * Constructs a Player object with the provided parameters name, email
     * and password.
     *
     * @param name     the player's name
     * @param email    the player's email
     * @param password the player's password
     */
    public Player(String name, String email, String password) {

        this.name = name;
        this.email = email;
        this.password = password;
    }

}

package frisbee.puf.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "teams")
@Setter
@Getter
public class Team {
    /**
     * The team's id as stored in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * The team's name.
     */
    @Column(name = "name")
    private String name;

    /**
     * The left player.
     */
    @OneToOne
    @JoinColumn(name = "player_left_id", nullable = true)
    private Player playerLeft;

    /**
     * The right player.
     */
    @OneToOne
    @JoinColumn(name = "player_right_id", nullable = true)
    private Player playerRight;

    /**
     * The team's level.
     */
    @Column(name = "level")
    private int level;

    /**
     * The team's score.
     */
    @Column(name = "score")
    private int score;

    /**
     * The team's lives.
     */
    @Column(name = "lives")
    private int lives;

    /**
     * The team's status.
     */
    @Column(name = "active")
    private boolean active;
}

package frisbee.puf.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name= "teams")
@Setter
@Getter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column (name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "player_left_id", nullable = true)
    private Player playerLeft;

    @OneToOne
    @JoinColumn(name = "player_right_id", nullable = true)
    private Player playerRight;

    @Column (name="level")
    private int level;

    @Column (name="score")
    private int score;

    @Column (name="lives")
    private int lives;

    @Column (name="active")
    private boolean active;
}

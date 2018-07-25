package model;

import javax.persistence.*;

@Entity
@Table(name = "AUTHORITIES")
public class Authorities {
    @Id
    @Column(name = "AUTHORITY")
    private String authority;

    @ManyToOne
    @JoinColumn(name = "USERNAME")
    private User user;

    //TODO: Getter and Setter methods
}

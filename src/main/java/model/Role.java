package model;

import javax.persistence.*;

@Entity
@Table(name="roles")
public class Role {

    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq2", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    @Column(name="role_id")
    private int id;

    @Column(name="role")
    private String role;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}

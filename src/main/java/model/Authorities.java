package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

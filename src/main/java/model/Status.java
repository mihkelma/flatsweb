package model;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Entity
@Table(name = "STATUS")
public class Status {

    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq4", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    private Integer id;
    private String name;
    //DRAFT, READY, SENT, FAILED, PAID, CLOSED

    @OneToMany(mappedBy = "contractStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

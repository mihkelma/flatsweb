package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "UNITTYPES")
public class UnitType {

    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq5", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "unitType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Unit> units;

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

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }
}

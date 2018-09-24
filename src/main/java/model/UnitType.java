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
    private String type;

    @OneToMany(mappedBy = "unitType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Unit> units;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }
}

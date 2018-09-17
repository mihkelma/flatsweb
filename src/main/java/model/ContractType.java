package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CONTRACTTYPES")
public class ContractType {

    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq3", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    private Long id;
    private String type;
    //private Integer DefaultExtensionNotification;
    //private Float DefaultPenaltyPerDay;
    //private Integer TerminationDays;
    //private Integer objectCheckingNotification;
    //private Integer DefaultTerminationNotification;

    @OneToMany(mappedBy = "contractType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

}

package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CONTRACTTYPES")
public class ContractType {

    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq3", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    private Integer id;
    private String contracttype;
    //private Integer DefaultExtensionNotification;
    //private Float DefaultPenaltyPerDay;
    //private Integer TerminationDays;
    //private Integer objectCheckingNotification;
    //private Integer DefaultTerminationNotification;

    @OneToMany(mappedBy = "contractType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractType() {
        return contracttype;
    }

    public void setContractType(String contracttype) {
        this.contracttype = contracttype;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

}

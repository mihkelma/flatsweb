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
//    private String contractDefExtension;
//    private Integer contractSendInvoiceDate;
//    private Integer contractInvoiceTerm;
    private Float contractPenalty;
//    private Integer contractTerminationDays;

    @OneToMany(mappedBy = "contractType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getContractDefExtension() {
//        return contractDefExtension;
//    }
//
//    public void setContractDefExtension(String contractDefExtension) {
//        this.contractDefExtension = contractDefExtension;
//    }
//
//    public Integer getContractSendInvoiceDate() {
//        return contractSendInvoiceDate;
//    }
//
//    public void setContractSendInvoiceDate(Integer contractSendInvoiceDate) {
//        this.contractSendInvoiceDate = contractSendInvoiceDate;
//    }
//
//    public Integer getContractInvoiceTerm() {
//        return contractInvoiceTerm;
//    }
//
//    public void setContractInvoiceTerm(Integer contractInvoiceTerm) {
//        this.contractInvoiceTerm = contractInvoiceTerm;
//    }

    public Float getContractPenalty() {
        return contractPenalty;
    }

    public void setContractPenalty(Float contractPenalty) {
        this.contractPenalty = contractPenalty;
    }

//    public Integer getContractTerminationDays() {
//        return contractTerminationDays;
//    }
//
//    public void setContractTerminationDays(Integer contractTerminationDays) {
//        this.contractTerminationDays = contractTerminationDays;
//    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

}

package model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CONTRACTS")
public class Contract {

    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq1", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    private Long id;
    private String ownerName;
    private String customerName;
    private Date created;
    private Integer invoiceSendDate;
    private BigDecimal price;
    private String contractNumber;
    private Date modified;
    private String ownerCode;
    private String ownerAddress;
    private String ownerPhone;
    private String ownerEmail;
    private String ownerBankAccount;
    private String ownerBankName;
    private String customerCode;
    private String customerAddress;
    private String customerPhone;
    private String customerEmail;
    private String customerRefNumber;
    private Integer contractTerm;
    private BigDecimal vatValue;
    private Date contractSigned;
    private Date contractTerminated;
    private String contractObjectAddress;
    private String contractObjectRoom;


    //TODO: add, merge, remove Invoices
    //@OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Invoice> invoices = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="contracttype")
    private ContractType contractType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="contractstatus")
    private Status contractStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getInvoiceSendDate() {
        return invoiceSendDate;
    }

    public void setInvoiceSendDate(Integer invoiceSendDate) {
        this.invoiceSendDate = invoiceSendDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerBankAccount() {
        return ownerBankAccount;
    }

    public void setOwnerBankAccount(String ownerBankAccount) {
        this.ownerBankAccount = ownerBankAccount;
    }

    public String getOwnerBankName() {
        return ownerBankName;
    }

    public void setOwnerBankName(String ownerBankName) {
        this.ownerBankName = ownerBankName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerRefNumber() {
        return customerRefNumber;
    }

    public void setCustomerRefNumber(String customerRefNumber) {
        this.customerRefNumber = customerRefNumber;
    }

    public Integer getContractTerm() {
        return contractTerm;
    }

    public void setContractTerm(Integer contractTerm) {
        this.contractTerm = contractTerm;
    }

    public Status getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Status contractStatus) {
        this.contractStatus = contractStatus;
    }

    public BigDecimal getVatValue() {
        return vatValue;
    }

    public void setVatValue(BigDecimal vatValue) {
        this.vatValue = vatValue;
    }

    public Date getContractSigned() {
        return contractSigned;
    }

    public void setContractSigned(Date contractSigned) {
        this.contractSigned = contractSigned;
    }

    public Date getContractTerminated() {
        return contractTerminated;
    }

    public void setContractTerminated(Date contractTerminated) {
        this.contractTerminated = contractTerminated;
    }

    public String getContractObjectAddress() {
        return contractObjectAddress;
    }

    public void setContractObjectAddress(String contractObjectAddress) {
        this.contractObjectAddress = contractObjectAddress;
    }

    public String getContractObjectRoom() {
        return contractObjectRoom;
    }

    public void setContractObjectRoom(String contractObjectRoom) {
        this.contractObjectRoom = contractObjectRoom;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }
}

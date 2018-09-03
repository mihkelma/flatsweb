package model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq1", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    private Long id;                    //TODO
    private String ownerName;
    private String customerName;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date created;               //TODO
    private Integer invoiceSendDate;
    private BigDecimal price;
    private String contractNumber;
    @Temporal(TemporalType.DATE)
    private Date modified;              //TODO
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
    @Temporal(TemporalType.DATE)
    private Date contractTerm;
    private Boolean VATRequired;
    @Temporal(TemporalType.DATE)
    private Date contractSigned;        //TODO
    private String contractObjectAddress;
    private String contractObjectRoom;
    @Transient
    private static Integer baseId = 1;

    //TODO: add, merge, remove Invoices
    //@OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Invoice> invoices = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit")
    private Unit unit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="contracttype")
    private ContractType contractType;

    public Contract() {
        // L0818XXXX1
        this.contractNumber = genContractNumber();
    }

    public String genContractNumber() {
        //Contract starts with "L"
        StringBuilder tmp = new StringBuilder("L");
        //Get month and year "MMyy"
        long millis=System.currentTimeMillis();
        Date now = new Date(millis);
        SimpleDateFormat sDate = new SimpleDateFormat("MMyy");
        tmp.append(sDate.format(now));
        //Add zeros
        if (baseId < 10) tmp.append("00000" + baseId);
        else if (baseId < 100) tmp.append("0000" + baseId);
        else if (baseId < 1000) tmp.append("000" + baseId);
        else if (baseId < 10000) tmp.append("00" + baseId);
        else if (baseId < 100000) tmp.append("0" +baseId);
        else if (baseId < 1000000) tmp.append(baseId);
        //increase the baseId for next constructor
        baseId++;
        String cNum = tmp.toString();
        return cNum;
    }

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

    public Date getContractTerm() {
        return contractTerm;
    }

    public void setContractTerm(Date contractTerm) {
        this.contractTerm = contractTerm;
    }

    public Boolean getVATRequired() {
        return VATRequired;
    }

    public void setVATRequired(Boolean VATRequired) {
        this.VATRequired = VATRequired;
    }

    public Date getContractSigned() {
        return contractSigned;
    }

    public void setContractSigned(Date contractSigned) {
        this.contractSigned = contractSigned;
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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contract )) return false;
        return id != null && id.equals(((Contract) o).id);
    }
    @Override
    public int hashCode() {
        return 31;
    }
}

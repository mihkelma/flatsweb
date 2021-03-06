package model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq7", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    private Long id;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date created;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date sendDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date invoiceTerm;

    private String status; //draft - 0, ready -1, queue -2, sent -3, paid -4, deleted - 5, failed - 6
    private String ownerName;
    private String ownerAddress;
    private String ownerPhone;
    private String ownerEmail;
    private String ownerIBAN;
    private String ownerBank;
    private String ownerNotes;
    private BigDecimal vat;
    private String ownerSalesName;
    private String customerEmail;
    private String customerName;
    private String customerAddress;
    private String customerReference;
    private String title;
    private String iNumber;
    private BigDecimal sum;
    @Transient
    private static Integer baseId = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract")
    private Contract contract;

    //TODO: add, merge, remove invoiceRow
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceRow> invoiceRows = new ArrayList<InvoiceRow>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private User user;

    public Invoice() {
        this.iNumber = getInvNumber();
    }

    public Invoice(Date created, Date sendDate, Date invoiceTerm, String status, String ownerName, String ownerAddress, String ownerPhone, String ownerEmail, String ownerIBAN, String ownerBank, String ownerNotes, BigDecimal vat, String ownerSalesName, String customerEmail, String customerName, String customerAddress, String customerReference, BigDecimal sum, List<InvoiceRow> invoiceRows) {
        this.created = created;
        this.sendDate = sendDate;
        this.invoiceTerm = invoiceTerm;
        this.status = status;
        this.ownerName = ownerName;
        this.ownerAddress = ownerAddress;
        this.ownerPhone = ownerPhone;
        this.ownerEmail = ownerEmail;
        this.ownerIBAN = ownerIBAN;
        this.ownerBank = ownerBank;
        this.ownerNotes = ownerNotes;
        this.vat = vat;
        this.ownerSalesName = ownerSalesName;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerReference = customerReference;
        this.iNumber = getInvNumber();
        this.sum = sum;
        this.invoiceRows = invoiceRows;
    }

    public String getInvNumber() {
        //Invoice starts with "AMMyy"
        StringBuilder tmp = new StringBuilder("A");
        //Get month and year "MMyy"
        long millis=System.currentTimeMillis();
        java.util.Date now = new java.util.Date(millis);
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
        String iNum = tmp.toString();
        return iNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getInvoiceTerm() {
        return invoiceTerm;
    }

    public void setInvoiceTerm(Date invoiceTerm) {
        this.invoiceTerm = invoiceTerm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public String getOwnerIBAN() {
        return ownerIBAN;
    }

    public void setOwnerIBAN(String ownerIBAN) {
        this.ownerIBAN = ownerIBAN;
    }

    public String getOwnerBank() {
        return ownerBank;
    }

    public void setOwnerBank(String ownerBank) {
        this.ownerBank = ownerBank;
    }

    public String getOwnerNotes() {
        return ownerNotes;
    }

    public void setOwnerNotes(String ownerNotes) {
        this.ownerNotes = ownerNotes;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public String getOwnerSalesName() {
        return ownerSalesName;
    }

    public void setOwnerSalesName(String ownerSalesName) {
        this.ownerSalesName = ownerSalesName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getiNumber() {
        return iNumber;
    }

    public void setiNumber(String iNumber) {
        this.iNumber = iNumber;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<InvoiceRow> getInvoiceRows() {
        return invoiceRows;
    }

    public void setInvoiceRows(List<InvoiceRow> invoiceRows) {
        this.invoiceRows = invoiceRows;
    }

    public void addInvoiceRow(InvoiceRow invoiceRow) {
        invoiceRows.add(invoiceRow);
        invoiceRow.setInvoice(this);
    }

    public void removeInvoiceRow(InvoiceRow invoiceRow) {
        invoiceRows.remove(invoiceRow);
        invoiceRow.setInvoice(null);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", created=" + created +
                ", sendDate=" + sendDate +
                ", invoiceTerm=" + invoiceTerm +
                ", status='" + status + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", ownerAddress='" + ownerAddress + '\'' +
                ", ownerPhone='" + ownerPhone + '\'' +
                ", ownerEmail='" + ownerEmail + '\'' +
                ", ownerIBAN='" + ownerIBAN + '\'' +
                ", ownerBank='" + ownerBank + '\'' +
                ", ownerNotes='" + ownerNotes + '\'' +
                ", vat=" + vat +
                ", ownerSalesName='" + ownerSalesName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerReference='" + customerReference + '\'' +
                ", iNumber='" + iNumber + '\'' +
                ", sum=" + sum +
                //", contract=" + contract.getId() +
                ", invoiceRows=" + invoiceRows.size() +
                //", user=" + user.getUsername() +
                '}';
    }
}

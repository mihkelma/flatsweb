package model;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "invoicerows")
public class InvoiceRow {

    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq8", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    private Long id;
    private String title;
    private String descr;
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private BigDecimal quantity;
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private BigDecimal unitPrice;
    @Transient
    private BigDecimal rowPrice;
    private BigDecimal vat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice")
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private User user;

    public InvoiceRow() {};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getRowPrice() {

        if (unitPrice == null || quantity == null) {
            //return new BigDecimal(0);
            return null;
        }
        return quantity.multiply(unitPrice);
    }

    public void setRowPrice() {
        if (unitPrice == null || quantity == null) {
            this.rowPrice = new BigDecimal(0);
        }
        this.rowPrice = unitPrice.multiply(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceRow )) return false;
        return id != null && id.equals(((InvoiceRow) o).id);
    }
    @Override
    public int hashCode() {
        return 31;
    }


    @Override
    public String toString() {
        return "InvoiceRow{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", descr='" + descr + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", rowPrice=" + rowPrice +
                ", vat=" + vat +
                ", user=" + user.getUsername() +
                '}';
    }
}

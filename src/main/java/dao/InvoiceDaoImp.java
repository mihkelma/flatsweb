package dao;

import model.Contract;
import model.Invoice;
import model.InvoiceRow;
import model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public class InvoiceDaoImp implements InvoiceDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Invoice> getUserInvoices(String username) {
        List<Invoice> invoices;
        try {
            invoices = em.createQuery("SELECT i FROM Invoice i " +
                    "LEFT JOIN FETCH i.user WHERE lower(i.user.username) = lower(:username) ", Invoice.class)
                    .setParameter("username", username)
                    .getResultList();
        } catch (Exception e) {
            invoices = null;
        }
        return invoices;
    }

    @Override
    public List<Invoice> getInvoicesByContractId(Long id, String username) {
        List<Invoice> invoices;
        try {
            invoices = em.createQuery("SELECT i FROM Invoice i " +
                    "LEFT JOIN FETCH i.contract " +
                    "WHERE i.contract.id = :id AND lower(i.user.username) = lower(:username) ", Invoice.class)
                    .setParameter("username", username)
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            invoices = null;
        }
        return invoices;
    }

    @Override
    public Invoice getInvoiceById(Long id, String username) {
        Invoice invoice;
        try {
            invoice = em.createQuery("SELECT i FROM Invoice i " +
                    "LEFT JOIN FETCH i.invoiceRows " +
                    "WHERE i.id = :id AND lower(i.user.username) = lower(:username) ", Invoice.class)
                    .setParameter("username", username)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            invoice = null;
        }
        return invoice;
    }

    @Override
    @Transactional
    public void saveInvoice(Invoice invoice, Long cid, String username) {
        //TODO
        if (invoice.getId() != null) {
//            existing invoice
            System.out.println("InvoiceDao: merge");
            User user = em.find(User.class, username);
            Contract contract = em.find(Contract.class, cid);
            //TODO: make method public, for other DAO's to use
            Invoice old = getInvoiceById(invoice.getId(),username);
            if (old.getUser().getUsername().equals(username)) {
                invoice.setUser(user);
            }
            invoice.setContract(contract);
            //TODO
            for (int i =0; i < invoice.getInvoiceRows().size(); i++) {
                invoice.getInvoiceRows().get(i).setUser(user);
                invoice.getInvoiceRows().get(i).setInvoice(invoice);
            }
            em.merge(invoice);
        } else {                        //new invoice
            System.out.println("InvoiceDao: create, and sum" + invoice.getSum());
            Contract contract = em.find(Contract.class, cid);
            User user = em.find(User.class, username);
            invoice.setUser(user);
            invoice.setContract(contract);

            Date today = new Date(Calendar.getInstance().getTime().getTime());
            invoice.setCreated(today);

            //Set the status always to 0 - draft, when invoice created at first
            invoice.setStatus("DRAFT");
            for (int i =0; i < invoice.getInvoiceRows().size(); i++) {
                invoice.getInvoiceRows().get(i).setUser(user);
                invoice.getInvoiceRows().get(i).setInvoice(invoice);
            }
            em.persist(invoice);
        }
    }

    @Transactional
    @Override
    public void deleteInvoice(Long id, String username) {
        //TODO
    }

    @Override
    public void sendInvoice(Invoice invoice, String username) {

    }
}

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
import java.sql.Date;
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
                    "WHERE i.invoiceRows.id = :id AND lower(i.user.username) = lower(:username) ", Invoice.class)
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
//            User user = em.find(User.class, username);
//            invoice.setUser(user);
            //TODO

            em.merge(invoice);
        } else {                        //new invoice
            Contract contract = em.find(Contract.class, cid);
            User user = em.find(User.class, username);
            invoice.setUser(user);
            invoice.setContract(contract);
            //invoice.setInvoiceRows(new ArrayList<>()); //working solution

            if (invoice.getDateCreated() == null) {
                Date today = new Date(Calendar.getInstance().getTime().getTime());
                invoice.setDateCreated(today);
            }
            //Set the status always to 0 - draft, when invoice created at first
            invoice.setStatus("DRAFT");
            //System.out.println(invoice.toString());
            for (int i =0; i < invoice.getInvoiceRows().size(); i++) {
                invoice.getInvoiceRows().get(i).setUser(user);
                invoice.getInvoiceRows().get(i).setInvoice(invoice);

            }


            try {
                em.persist(invoice);
            } catch (Exception e) {
                System.out.println("SQL error happened: " + e);
            }

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

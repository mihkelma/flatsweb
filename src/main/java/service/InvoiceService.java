package service;

import dao.InvoiceDao;
import dao.InvoiceDaoImp;
import model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    public List<Invoice> getInvoicesByContractId(Long id, String username) {
        return invoiceDao.getInvoicesByContractId(id, username);
    }

    public void saveInvoice(Invoice invoice, Long cid, String username) {
        invoiceDao.saveInvoice(invoice, cid, username);
    }

    public Invoice getInvoiceById(Long id, String username) {
        return invoiceDao.getInvoiceById(id, username);
    }
}

package dao;

import model.Invoice;

import java.util.List;

public interface InvoiceDao {

    List<Invoice> getUserInvoices(String username);
    List<Invoice> getInvoicesByContractId(Long id, String username);
    Invoice getInvoiceById(Long id, String username);
    void saveInvoice(Invoice invoice, Long cid, String username);
    void deleteInvoice(Long id, String username);
    void sendInvoice(Invoice invoice, String username);
}

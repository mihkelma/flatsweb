package service;

import dao.ContractDao;
import dao.InvoiceDao;
import model.Contract;
import model.Invoice;
import model.InvoiceRow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private ContractService contractService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private InvoiceService invoiceService;

    private static final Logger logger = LogManager.getLogger(ScheduleService.class);

    //Scheduled generation of invoices 1 day before send date
    @Scheduled(cron = "0 07 1 * * *")
    public void generateInvoicesByDate() {
        Integer dateToday = LocalDate.now().getDayOfMonth();
        String status = "created";

        List<Contract> contractList = contractService.getContractByDateByStatus(dateToday+1, status);
        for (Contract ct : contractList) {          //generating invoices
            Invoice inv = new Invoice();
            Calendar today = Calendar.getInstance();
            inv.setCreated(today.getTime());        //sets today as created day
            today.add(Calendar.DATE,1);
            inv.setSendDate(today.getTime());       //sets today+1 as invoice send date TODO: get from contract
            today.add(Calendar.DATE,30);
            inv.setInvoiceTerm(today.getTime());    //sets invoice due date (+30days)
            inv.setStatus("created");
            if (ct.getOwnerName() != null) inv.setOwnerName(ct.getOwnerName());
            if (ct.getOwnerAddress() != null) inv.setOwnerAddress(ct.getOwnerAddress());
            if (ct.getOwnerPhone() != null) inv.setOwnerPhone(ct.getOwnerPhone());
            if (ct.getOwnerEmail() != null) inv.setOwnerEmail(ct.getOwnerEmail());
            if (ct.getOwnerBankAccount() != null) inv.setOwnerIBAN(ct.getOwnerBankAccount());
            if (ct.getOwnerBankName() != null) inv.setOwnerBank(ct.getOwnerBankName());
            if (ct.getCustomerName() != null) inv.setCustomerName(ct.getCustomerName());
            if (ct.getCustomerEmail() != null) inv.setCustomerEmail(ct.getCustomerEmail());
            if (ct.getCustomerAddress() != null) inv.setCustomerAddress(ct.getCustomerAddress());
            if (ct.getCustomerRefNumber() != null) inv.setCustomerReference(ct.getCustomerRefNumber());
            if (ct.getObjectAddress() != null) inv.setTitle(ct.getObjectAddress());
            if (ct.getVat() != null) {
                inv.setVat(ct.getVat());
            }
            inv.setContract(ct);
            InvoiceRow ir = new InvoiceRow();
            LocalDate now = LocalDate.now();
            LocalDate lastMonth = now.minusMonths(1);
            ir.setTitle(lastMonth.getMonth() + " rent");
            ir.setQuantity(new BigDecimal(1));
            if (ct.getPrice() != null) ir.setUnitPrice(ct.getPrice());
            if (ct.getVat() != null) { //if VAT is not zero
                ir.setVat(ct.getVat());
            }
            inv.addInvoiceRow(ir);
            inv.setSum(ir.getRowPrice());

            //saving invoice
            invoiceDao.saveInvoice(inv, ct.getId(), ct.getUser().getUsername());
        }
    }

    //Scheduled job to create invoice pdf-s and send to the customer
    @Scheduled(cron = "0 07 1 * * *")
    public void createPdfAndEmailInvoice() {
        Integer invoiceType = 1;                // Type defines the email content

        logger.info("createPdfAndEmailInvoice started");
        Calendar cal = Calendar.getInstance();
        List<Invoice> invoices = invoiceDao.getInvoicesByDate(cal.getTime());
        for (Invoice invoice : invoices) {
            try {
                pdfService.createInvoicePdf(invoice);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("Email sending started");
            String subject = "Arve number " + invoice.getiNumber() + ", (" + invoice.getTitle() + ")";
            String text = "Tere " + invoice.getCustomerName() + "\n\nSaadame Teile eelmise perioodi arve.\n" +
                    "Täname õigeaegselt tasutud arve eest!\n\n" + "Soovides head,\n\n" + invoice.getOwnerName();
            String filePath = "/tmp/" + invoice.getiNumber() + ".pdf";

            emailService.sendEmail(invoice.getCustomerEmail(), subject, text,"noreply@flats.ee", filePath);

            invoice.setStatus("sent");
            invoiceService.saveInvoice(invoice, invoice.getContract().getId(), invoice.getUser().getUsername());
        }

    }

}

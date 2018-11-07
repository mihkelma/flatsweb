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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Component
public class ScheduleService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private ContractDao contractDao;

    private static final Logger logger = LogManager.getLogger(ScheduleService.class);

    @Scheduled(cron = "0 41 23 * * *")
    public void generateInvoicesByDate() {
        Integer dateToday = LocalDate.now().getDayOfMonth();
        String status = "aktiivne";
        List<Contract> contractList = contractDao.getContractByDateByStatus(dateToday, status);
        for (Contract ct : contractList) {          //generating invoices
            Invoice inv = new Invoice();
            Calendar today = Calendar.getInstance();
            inv.setCreated(today.getTime());        //sets today as created day
            inv.setSendDate(today.getTime());       //sets today as invoice send date TODO: get from contract
            today.add(Calendar.DATE,30);
            inv.setInvoiceTerm(today.getTime());    //sets invoice due date (+30days)
            inv.setStatus("uus");
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

}

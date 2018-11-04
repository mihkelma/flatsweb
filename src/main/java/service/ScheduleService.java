package service;

import dao.ContractDao;
import dao.InvoiceDao;
import model.Contract;
import model.Invoice;
import model.InvoiceRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private ContractDao contractDao;


    public void generateInvoicesByDate() {
        Integer dateToday = LocalDate.now().getDayOfMonth();
        String status = "aktiivne";
        List<Contract> contractList = contractDao.getContractByDateByStatus(dateToday, status);
        System.out.println("Arvete genereerimine: " +contractList.size());
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

    public void generateInvoicesByDate2(Integer dayOfMonth) {
        Integer dateToday = dayOfMonth;
        String status = "aktiivne";
        List<Contract> contractList = contractDao.getContractByDateByStatus(dateToday, status);
        System.out.println("Arvete genereerimine: " +contractList.size());
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

    public void generateContractInvoice() {
        Invoice inv = new Invoice();
        Contract contract = contractDao.getContractById(new Long(1), "user");
        Calendar today = Calendar.getInstance();
        inv.setCreated(today.getTime());        //sets today as created day
        inv.setSendDate(today.getTime());       //sets today as invoice send date TODO: get from contract
        today.add(Calendar.DATE,30);
        inv.setInvoiceTerm(today.getTime());    //sets invoice due date (+30days)
        inv.setStatus("VALMIS");
        if (contract.getOwnerName() != null) inv.setOwnerName(contract.getOwnerName());
        if (contract.getOwnerAddress() != null) inv.setOwnerAddress(contract.getOwnerAddress());
        if (contract.getOwnerPhone() != null) inv.setOwnerPhone(contract.getOwnerPhone());
        if (contract.getOwnerEmail() != null) inv.setOwnerEmail(contract.getOwnerEmail());
        if (contract.getOwnerBankAccount() != null) inv.setOwnerIBAN(contract.getOwnerBankAccount());
        if (contract.getOwnerBankName() != null) inv.setOwnerBank(contract.getOwnerBankName());
        if (contract.getCustomerName() != null) inv.setCustomerName(contract.getCustomerName());
        if (contract.getCustomerEmail() != null) inv.setCustomerEmail(contract.getCustomerEmail());
        if (contract.getCustomerAddress() != null) inv.setCustomerAddress(contract.getCustomerAddress());
        if (contract.getCustomerRefNumber() != null) inv.setCustomerReference(contract.getCustomerRefNumber());
        if (contract.getObjectAddress() != null) inv.setTitle(contract.getObjectAddress());
        if (contract.getVat() != null) {
            inv.setVat(contract.getVat());
        }
        inv.setContract(contract);
        InvoiceRow ir = new InvoiceRow();
        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1);
        ir.setTitle(lastMonth.getMonth() + " rent");
        ir.setQuantity(new BigDecimal(1));
        if (contract.getPrice() != null) ir.setUnitPrice(contract.getPrice());
        if (contract.getVat() != null) { //if VAT is not zero
            ir.setVat(contract.getVat());
        }
        inv.addInvoiceRow(ir);
        inv.setSum(ir.getRowPrice());

        //saving invoice
        invoiceDao.saveInvoice(inv, new Long(1), "user");
    }
}

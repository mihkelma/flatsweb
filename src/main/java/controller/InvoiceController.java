package controller;

import model.Contract;
import model.Invoice;
import model.InvoiceRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    ContractService contractService;

    @Autowired
    PdfService pdfService;

    @Autowired
    private EmailService emailService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null,  new CustomDateEditor(dateFormat, true));
    }

    //TODO: error management: http://blog.codeleak.pl/2014/06/better-error-messages-with-bean.html

    @GetMapping("/contracts/{id}/invoices/add")
    public String addInvoiceForm(@PathVariable Long id, ModelMap model, Authentication auth) {
        Invoice inv = new Invoice();
        Contract ctr = contractService.getContractById(id, auth.getName());
        inv.setCustomerName(ctr.getCustomerName());
        inv.setCustomerAddress(ctr.getCustomerAddress());
        Calendar c = Calendar.getInstance();
        inv.setCreated(c.getTime());
        inv.setSendDate(c.getTime());
        c.add(Calendar.DATE, 30);

        inv.setInvoiceTerm(c.getTime());
        List<InvoiceRow> irl = new ArrayList<>();
        InvoiceRow ir = new InvoiceRow();
        irl.add(ir);
        inv.setInvoiceRows(irl);

        model.addAttribute("invoice", inv);
        model.addAttribute("contractId", id);
        return "invoices/add";
    }

    //Save invoice
    @PostMapping("/contracts/{cid}/invoices")
    public String addInvoice(@Valid Invoice invoice, @PathVariable Long cid, Authentication auth) {
        if (invoice != null) {
            invoiceService.saveInvoice(invoice, cid, auth.getName());
            return "redirect:/contracts/" +cid;
        }
        return "/invoices/add";
    }

    //Get view invoice page
    @GetMapping("/contracts/{cid}/invoices/{iid}")
    public String editInvoiceForm(@PathVariable Long cid, @PathVariable Long iid, Model model, Authentication auth) {
        Invoice invoice = invoiceService.getInvoiceById(iid, auth.getName());
        model.addAttribute("invoice", invoice);
        model.addAttribute("contractId", cid);
        return "invoices/view";
    }

    @GetMapping("/contracts/{cid}/generateinvoice")
    public String generateInvoiceFromContract(@PathVariable Long cid, Authentication auth) {

        Invoice inv = new Invoice();
        Contract contract = contractService.getContractById(cid, auth.getName());
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
        invoiceService.saveInvoice(inv, cid, auth.getName());
        return "redirect:/contracts/" + cid;
    }

    @GetMapping("/contracts/{cid}/invoices/{iid}/openpdf")
    public ResponseEntity<String> generatePdf(@PathVariable Long cid, @PathVariable Long iid, Authentication auth) {
        Invoice invoice = invoiceService.getInvoiceById(iid, auth.getName());
        HttpHeaders headers = new HttpHeaders();

        //https://stackoverflow.com/questions/20333394/return-a-stream-with-spring-mvcs-responseentity
        //https://stackoverflow.com/questions/8913259/how-to-generate-a-downloadable-pdf-with-pdfbox-corrupted-pdf
        ByteArrayResource output = null;
        try {
            output = new ByteArrayResource(pdfService.createInvoice(invoice));

            headers.add("Content-Type", "application/force-download");
            headers.add("Content-Disposition", "attachment; filename=\"" + invoice.getiNumber() + ".pdf\"");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(output, headers, HttpStatus.OK);
    }

    @GetMapping("/contracts/{cid}/invoices/{iid}/sendviaemail")
    public String generatePdfAndEmail(@PathVariable Long cid, @PathVariable Long iid, Authentication auth) {
        Integer invoiceType = 1;                // Type defines the email content
        Invoice invoice = invoiceService.getInvoiceById(iid, auth.getName());
        try {
            pdfService.createInvoicePdf(invoice, auth.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String subject = "Arve number " + invoice.getiNumber() + ", (" + invoice.getTitle() + ")";
        String text = "Tere " + invoice.getCustomerName() + "\n\nSaadame Teile eelmise perioodi arve.\n" +
                "Täname õigeaegselt tasutud arve eest!\n\n" + "Soovides head,\n\n" + invoice.getOwnerName();
        String filePath = "/tmp/" + invoice.getiNumber() + ".pdf";

        emailService.sendEmail(invoice.getCustomerEmail(), subject, text,"noreply@flats.ee", filePath);

        invoice.setStatus("VÄLJASTATUD");
        invoiceService.saveInvoice(invoice, cid, auth.getName());

        return "redirect:/contracts/" + cid;
    }

    //Delete invoice
    @GetMapping("/contracts/{cid}/invoices/{iid}/delete")
    public String deleteInvoice(@PathVariable Long cid, @PathVariable Long iid, Authentication auth) {
        invoiceService.deleteInvoice(iid, auth.getName());
        return "redirect:/contracts/" +cid;
    }

}

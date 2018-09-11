package controller;

import model.Contract;
import model.Invoice;
import model.InvoiceRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import service.ContractService;
import service.InvoiceService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    ContractService contractService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null,  new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/contracts/{id}/invoices/add")
    public String addInvoiceForm(@PathVariable Long id, ModelMap model, Authentication auth) {
        Invoice inv = new Invoice();
        Contract ctr = contractService.getContractById(id, auth.getName());
        inv.setCustomerName(ctr.getCustomerName());
        inv.setCustomerAddress(ctr.getCustomerAddress());

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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(Exception e) {
        System.out.println("Returning HTTP 400 Bad Request: " + e + ", cause: " + e.getStackTrace());
    }
}

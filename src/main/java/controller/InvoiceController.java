package controller;

import model.Invoice;
import model.InvoiceRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
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

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null,  new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/contracts/{id}/invoices/add")
    public String addInvoiceForm(@PathVariable Long id, ModelMap model, Authentication auth) {
        System.out.println("Add form start");
        Invoice inv = new Invoice();
        List<InvoiceRow> irl = new ArrayList<>();
        InvoiceRow ir = new InvoiceRow();
        irl.add(ir);
        System.out.println("Add form create row");
        if (inv.getDateCreated() == null) {
            Date today = new Date(Calendar.getInstance().getTime().getTime());
            inv.setDateCreated(today);
        }
        System.out.println("Add form setting status");
        //Set the status always to 0 - draft, when invoice created at first
        inv.setStatus("DRAFT");
        inv.setInvoiceRows(irl);
        System.out.println("Add form add rows");
        System.out.println("Arve seis: " +inv.toString());
        model.addAttribute("invoice", inv);
        model.addAttribute("contractId", id);
        return "invoices/add";
    }

    //Save invoice
    @PostMapping("/contracts/{cid}/invoices")
    public String addInvoice(@Valid Invoice invoice, @PathVariable Long cid, Authentication auth) {
        if (invoice != null) {
            //System.out.println("Invoircerow_item1: " + invoice.getInvoiceRows().get(0).getQuantity());
            //System.out.println("Invoircerow_item2: " + invoice.getInvoiceRows().get(0).getTitle());
            //System.out.println("Invoircerow_item3: " + invoice.getInvoiceRows().get(0).getUnitPrice());
            invoiceService.saveInvoice(invoice, cid, auth.getName());
            return "redirect:/contracts/" +cid;
        }
        return "/invoices/add";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(Exception e) {
        System.out.println("Returning HTTP 400 Bad Request: " + e + ", cause: " + e.getStackTrace());
    }
}

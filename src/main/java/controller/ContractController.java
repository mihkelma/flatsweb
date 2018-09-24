package controller;

import model.Contract;
import model.ContractType;
import model.Invoice;
import model.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import service.ContractService;
import service.InvoiceService;
import service.UnitService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Controller
public class ContractController {

    @Autowired
    private ContractService contractService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private InvoiceService invoiceService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null,  new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/contracts")
    public String contracts(Authentication auth, Model model) {
        model.addAttribute("contracts", contractService.getAllUserContracts(auth.getName()));
        return "contracts/index";
    }

    //Get new contract form page
    @GetMapping("/units/{id}/contracts/add")
    public String addContractForm(@PathVariable Long id, Model model, Authentication auth) {
        Contract tmp = new Contract();
        Unit unit = unitService.getUnitById(id, auth.getName());
        tmp.setObjectAddress(unit.getAddress() + ", "+ unit.getCity());
        tmp.setOwnerEmail(auth.getName());
        Calendar c = Calendar.getInstance();
        tmp.setCreated(c.getTime());
        model.addAttribute("contract", tmp);
        model.addAttribute("unitId", id);
        return "contracts/add";
    }

    //Save contract
    @PostMapping("/units/{cid}/contracts")
    public String addContract(Contract contract, @PathVariable Long cid, Authentication auth) {
        if (contract != null) {
            contractService.saveContract(contract, cid, auth.getName());
            return "redirect:/units/" +cid;
        }
        return "/contracts/new";
    }

    //Get view contract page
    @GetMapping("/contracts/{id}")
    public String editContractForm(@PathVariable Long id, Model model, Authentication auth) {
        Contract tmp = contractService.getContractById(id, auth.getName());
        List<Invoice> invoiceList = invoiceService.getInvoicesByContractId(id, auth.getName());
        List<ContractType> contractTypes = contractService.getAllContractTypes(auth.getName());
        model.addAttribute("allContractTypes", contractTypes);
        model.addAttribute("invoices", invoiceList);
        model.addAttribute("contract", tmp);
        return "contracts/view";
    }

    //Delete unit
    @GetMapping("/contracts/delete/{id}")
    public String deleteContract(@PathVariable Long id, Authentication auth) {
        contractService.deleteContract(id, auth.getName());
        return "redirect:/contracts";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(Exception e) {
        System.out.println("Returning HTTP 400 Bad Request: " + e);
    }
}

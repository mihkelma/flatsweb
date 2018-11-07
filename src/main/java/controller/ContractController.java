package controller;

import model.Contract;
import model.ContractType;
import model.Invoice;
import model.Unit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import service.ContractService;
import service.InvoiceService;
import service.ScheduleService;
import service.UnitService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    @Autowired
    private ScheduleService scheduleService;

    private static final Logger logger = LogManager.getLogger(ContractController.class);

    //TODO: error management: http://blog.codeleak.pl/2014/06/better-error-messages-with-bean.html
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
        System.out.println("Unit: " + unit.getAddress()+ ", " + unit.getCity());
        tmp.setObjectAddress(unit.getAddress() + ", "+ unit.getCity());
        tmp.setOwnerEmail(auth.getName());
        List<ContractType> contractTypes = contractService.getAllContractTypes();
        Calendar c = Calendar.getInstance();
        tmp.setCreated(c.getTime());
        c.add(Calendar.YEAR, 1);
        tmp.setContractTerm(c.getTime());
        model.addAttribute("allContractTypes", contractTypes);
        model.addAttribute("contract", tmp);
        model.addAttribute("unitId", id);
        return "contracts/add";
    }

    //Save contract
    @PostMapping("/units/{uid}/contracts")
    public String addContract(@Valid Contract contract, BindingResult bindingResult, @PathVariable Long uid, Authentication auth, Model model) {
        if (bindingResult.hasErrors()) {
            List<ContractType> contractTypes = contractService.getAllContractTypes();
            model.addAttribute("allContractTypes", contractTypes);
            model.addAttribute("error", "Viga andmete sisestamisel!");
            model.addAttribute("unitId", uid);
            return "contracts/add";
        }
        contractService.saveContract(contract, uid, auth.getName());
        return "redirect:/units/" +uid;

    }

    @PostMapping("/units/{uid}/contracts/{cid}/sign")
    public String signContract(@PathVariable Long uid, @PathVariable Long cid, Authentication auth) {
        contractService.signContract(cid, auth.getName());
        return "redirect:/units/" +uid;
    }

    //Get view contract page
    @GetMapping("/contracts/{id}")
    public String editContractForm(@PathVariable Long id, Model model, Authentication auth) {
        Contract tmp = contractService.getContractById(id, auth.getName());
        List<Invoice> invoiceList = invoiceService.getInvoicesByContractId(id, auth.getName());
        List<ContractType> contractTypes = contractService.getAllContractTypes();
        model.addAttribute("allContractTypes", contractTypes);
        model.addAttribute("invoices", invoiceList);
        model.addAttribute("contract", tmp);
        return "contracts/view";
    }

    //Delete unit
    @GetMapping("/contracts/{id}/delete")
    public String deleteContract(@PathVariable Long id, Authentication auth) {
        contractService.deleteContract(id, auth.getName());
        return "redirect:/contracts";
    }

    //Generate invoices
    @GetMapping("/contracts/gis")
    public String gis(Authentication auth, Model model) {
        System.out.println("IKontroller, genereeri arveid link");
        scheduleService.generateInvoicesByDate();
        model.addAttribute("contracts", contractService.getAllUserContracts(auth.getName()));
        return "contracts/index";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(Exception e) {
        System.out.println("Returning HTTP 400 Bad Request: " + e);
    }
}

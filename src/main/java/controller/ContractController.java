package controller;

import model.Contract;
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
import service.UnitService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Controller
public class ContractController {

    @Autowired
    private ContractService contractService;
    @Autowired
    private UnitService unitService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        System.out.println("Initbinder is started.\n");
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
        tmp.setContractObjectAddress(unit.getAddress() + ", "+ unit.getCity());
        tmp.setOwnerEmail(auth.getName());
        System.out.println("Leping: " +tmp.toString());
        model.addAttribute("contract", tmp);
        model.addAttribute("unitId", id);
        return "contracts/add";
    }

    //Save contract
    @PostMapping("/units/{cid}/contracts")
    public String addContract(Contract contract, @PathVariable Long cid, Authentication auth) {
        System.out.println("ContController save:" + contract.getId());
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
        model.addAttribute("contract", tmp);
        return "contracts/viewcontract";
    }

    //Delete unit
    @GetMapping("/contracts/delete/{id}")
    public String deleteContract(@PathVariable Long id, Authentication auth) {
        System.out.println("Deleting contract");
        contractService.deleteContract(id, auth.getName());
        return "redirect:/contracts";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(Exception e) {
        System.out.println("Returning HTTP 400 Bad Request: " + e);
    }
}

package controller;

import model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import service.ContractService;

import javax.validation.Valid;
import java.sql.Date;

@Controller
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping("/contracts")
    public String contracts(Authentication auth, Model model) {
        model.addAttribute("contracts", contractService.getAllUserContracts(auth.getName()));
        return "contracts/index";
    }

    //Get new contract form page
    @GetMapping("/contracts/new")
    public String addContractForm(Model model) {
        Contract tmp = new Contract();
        System.out.println("Leping: " +tmp.toString());
        model.addAttribute("contract", tmp);
        return "contracts/addcontract";
    }

    //Get view contract page
    @GetMapping("/contracts/{id}")
    public String editContractForm(@PathVariable Long id, Model model, Authentication auth) {
        Contract tmp = contractService.getContractById(id, auth.getName());
        model.addAttribute("contract", tmp);
        return "contracts/viewcontract";
    }

    //Save contract
    @PostMapping("/contracts")
    public String addContract(@Valid Contract contract, Authentication auth) {
        System.out.println("ContController save:" + contract.getId());
        if (contract != null) {
            contract.setCreated(new Date(0));
            contractService.saveContract(contract, auth.getName());
            return "redirect:/contracts";
        }
        return "/contracts/new";
    }

    //Delete unit
    @GetMapping("/contracts/delete/{id}")
    public String deleteContract(@PathVariable Long id, Authentication auth) {
        System.out.println("Deleting contract");
        contractService.deleteContract(id, auth.getName());
        return "redirect:/contracts";
    }
}

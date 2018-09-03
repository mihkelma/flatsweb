package controller;

import model.Contract;
import model.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import service.ContractService;
import service.UnitService;

import javax.sound.midi.Soundbank;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.sql.Date;
import java.util.List;

@Controller
public class UnitController {
    @Autowired
    private UnitService unitService;
    @Autowired
    private ContractService contractService;

    @GetMapping("/units")
    public String units(Authentication auth, Model model) {
        List<Unit> tmp = unitService.getAllUserUnits(auth.getName());
        model.addAttribute("units", tmp);
        return "units/index";
    }

    @GetMapping("/units/{id}")
    public String getUnitById(@PathVariable Long id, Authentication auth, Model model) {
        Unit tmp = unitService.getUnitById(id ,auth.getName());
        List<Contract> clist = contractService.getContractsByUnitId(id, auth.getName());
        model.addAttribute("unit", tmp);
        model.addAttribute("contracts", clist);
        return "units/view";
    }

    //Get new unit form page
    @GetMapping("/units/add")
    public String addUnitForm(Model model) {
        model.addAttribute("unit", new Unit());
        return "units/add";
    }

    //Save contract
    @PostMapping("/units")
    public String addUnit(@Valid Unit unit, Authentication auth) {
        //TODO: 1) Update will give error (A collection with cascade=”all-delete-orphan” was no longer referenced by the owning entity instance)
        //TODO: 2) add validation and error handling
        if (unit != null) {
            unitService.saveUnit(unit, auth.getName());
            return "redirect:/units";
        }
        return "/units/add";
    }

    //Delete unit
    @GetMapping("/units/delete/{id}")
    public String deleteContract(@PathVariable Long id, Authentication auth) {
        System.out.println("Deleting unit");
        unitService.deleteUnit(id, auth.getName());
        return "redirect:/units";
    }
}

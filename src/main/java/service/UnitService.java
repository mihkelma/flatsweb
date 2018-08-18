package service;

import dao.UnitDao;
import model.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UnitService {
    @Autowired
    private UnitDao unitDao;

    public List<Unit> getAllUserUnits(String username) {
        return unitDao.getAllUserUnits(username);
    }

    public Unit getUnitById(Long id, String username) {
        return unitDao.getUnitById(id, username);
    }

    @Transactional
    public void saveUnit(Unit unit, String username) {
        System.out.println("Unit: " + unit.getUnitType());
        if (unit.getUnitType().equals("1")) unit.setUnitType("Korter");
        else if (unit.getUnitType().equals("2")) unit.setUnitType("Tuba");
        else if (unit.getUnitType().equals("3")) unit.setUnitType("Garaaz");
        unitDao.saveUnit(unit, username);
    }

    @Transactional
    public void deleteUnit(Long id, String username) {
        unitDao.deleteUnit(id, username);
    }


}

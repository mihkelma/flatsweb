package service;

import dao.UnitDao;
import model.Unit;
import model.UnitType;
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
        unitDao.saveUnit(unit, username);
    }

    @Transactional
    public void deleteUnit(Long id, String username) {
        unitDao.deleteUnit(id, username);
    }


    public List<UnitType> getAllUnitTypes() {
        return unitDao.getAllUnitTypes();
    }

    public UnitType getUnitTypeById(Integer id) {
        return unitDao.getUnitTypeById(id);
    }
}

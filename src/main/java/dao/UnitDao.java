package dao;

import model.Unit;

import java.util.List;

public interface UnitDao {
    List<Unit> getAllUserUnits(String username);
    Unit getUnitById(Long id, String username);
    void saveUnit(Unit unit, String username);
    void deleteUnit(Long id, String username);
}

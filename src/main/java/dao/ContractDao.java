package dao;

import model.Contract;
import model.ContractType;

import java.sql.Date;
import java.util.List;

public interface ContractDao {
    List<Contract> getUserContracts(String username);

    List<Contract> getContractsByUnitId(Long cid, String username);

    Contract getContractById(Long id, String username);

    void saveContract(Contract contract, Long cid, String username);

    void deleteContract(Long id, String username);

    List<ContractType> getAllContractTypes();

    void signContract(Long cid, String username);

    List<Contract> getContractByDateByStatus(String date, String status);
}

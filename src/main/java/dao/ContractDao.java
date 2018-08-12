package dao;

import model.Contract;

import java.util.List;

public interface ContractDao {
    List<Contract> getUserContracts(String username);
    Contract getContractById(Long id, String username);
    void saveContract(Contract contract, String username);
    void deleteContract(Long id, String username);
}

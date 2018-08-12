package service;

import dao.ContractDao;
import model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ContractService {

    @Autowired
    private ContractDao contractDao;

    public List<Contract> getAllUserContracts(String username) {
        return contractDao.getUserContracts(username);
    }

    @Transactional
    public void saveContract(Contract contract, String username) {
        System.out.println("Contractservice save:" +contract.getId());
        contractDao.saveContract(contract, username);
    }

    @Transactional
    public void deleteContract(Long id, String username) {
        contractDao.deleteContract(id, username);
    }

    public Contract getContractById(Long id, String username) {
        return contractDao.getContractById(id, username);
    }
}

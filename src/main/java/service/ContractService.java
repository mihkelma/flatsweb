package service;

import dao.ContractDao;
import model.Contract;
import model.ContractType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Service
public class ContractService {

    @Autowired
    private ContractDao contractDao;

    public List<Contract> getAllUserContracts(String username) {
        return contractDao.getUserContracts(username);
    }

    public List<Contract> getContractsByUnitId(Long cid, String username) {
        return contractDao.getContractsByUnitId(cid, username);
    }

    @Transactional
    public void saveContract(Contract contract, Long cid, String username) {
        long millis=System.currentTimeMillis();
        Date now = new Date(millis);
        if (contract.getId() == null) {        //if this is a new contract
            contract.setCreated(now);
        }
        contract.setModified(now);
        contractDao.saveContract(contract, cid, username);
    }

    @Transactional
    public void deleteContract(Long id, String username) {
        contractDao.deleteContract(id, username);
    }

    public Contract getContractById(Long id, String username) {
        return contractDao.getContractById(id, username);
    }

    public List<ContractType> getAllContractTypes() {
        return contractDao.getAllContractTypes();
    }

    public void signContract(Long cid, String username) {
        contractDao.signContract(cid, username);
    }
}

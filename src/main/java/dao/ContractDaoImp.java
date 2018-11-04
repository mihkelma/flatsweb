package dao;

import model.Contract;
import model.ContractType;
import model.Unit;
import model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Repository
public class ContractDaoImp implements ContractDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Contract> getUserContracts(String username) {
        List<Contract> contracts;
        contracts = em.createQuery("SELECT c FROM Contract c " +
                "LEFT JOIN FETCH c.user WHERE lower(c.user.username) = lower(:username) ", Contract.class)
                .setParameter("username", username)
                .getResultList();
        if (contracts.isEmpty()) return null;
        return contracts;
    }

    @Override
    public List<Contract> getContractsByUnitId(Long cid, String username) {
        try {
            List<Contract> tmp;
            tmp = em.createQuery("SELECT c FROM Contract c " +
                    "LEFT JOIN FETCH c.unit " +
                    "WHERE c.unit.id = :cid AND lower(c.user.username) = lower(:username)", Contract.class)
                    .setParameter("cid", cid)
                    .setParameter("username", username)
                    .getResultList();
            return tmp;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Contract getContractById(Long id, String username) {
        Contract tmp;
        try {
            tmp = em.createQuery("SELECT c FROM Contract c " +
                    "WHERE c.id = :id AND lower(c.user.username) = lower(:username)", Contract.class)
                    .setParameter("id", id)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            tmp = null;
        }
        return tmp;
    }

    @Override
    @Transactional
    public void saveContract(Contract contract, Long cid, String username) {

        User user = em.find(User.class, username);
        Unit unit = em.find(Unit.class, cid);

        if (contract.getId() != null) { //existing contract
            contract.setUser(user);
            contract.setUnit(unit);
            em.merge(contract);
        }
        else {                        //new contract
            contract.setUser(user);
            contract.setUnit(unit);
            em.persist(contract);
        }
    }

    @Override
    @Transactional
    public void deleteContract(Long id, String username) {
        Contract contract = em.find(Contract.class, id);
//        User user = em.find(User.class, username);
//        contract.setUser(user);
//        em.merge(contract);
        em.remove(contract);
    }

    @Override
    public List<ContractType> getAllContractTypes() {
        List<ContractType> contractTypes;
        try {
            contractTypes = em.createQuery("SELECT ct FROM ContractType ct", ContractType.class)
                    .getResultList();
        } catch (Exception e) {
            contractTypes = null;
        }
        return contractTypes;
    }

    @Override
    @Transactional
    public void signContract(Long cid, String username) {
        Contract contract = em.find(Contract.class, cid);
        Calendar c = Calendar.getInstance();
        contract.setModified(c.getTime());
        contract.setContractSigned(c.getTime());
        em.merge(contract);
    }

    @Override
    public List<Contract> getContractByDateByStatus(Integer dayOfMonth, String status) {
        List<Contract> contracts;

        System.out.println("Lepinguotsing kuupäevaga: " +dayOfMonth + " ja staatus: " + status);
        try {
            contracts = em.createQuery("SELECT c FROM Contract c" +
                    " WHERE lower(c.status) = lower(:status) AND c.invoiceSendDate = :dayOfMonth", Contract.class)
                    .setParameter("status", status)
                    .setParameter("dayOfMonth", dayOfMonth)
                    .getResultList();
        }
        catch (Exception e) {
            contracts = null;
        }
        return contracts;
    }
}

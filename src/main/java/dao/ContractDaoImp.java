package dao;

import model.Contract;
import model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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
    public Contract getContractById(Long id, String username) {
        try {
            Contract tmp;
            tmp = em.createQuery("SELECT c FROM Contract c " +
                    "LEFT JOIN FETCH c.invoices ci " +
                    "WHERE c.id = :id AND lower(c.user.username) = lower(:username)", Contract.class)
                    .setParameter("id", id)
                    .setParameter("username", username)
                    .getSingleResult();
            return tmp;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void saveContract(Contract contract, String username) {
        System.out.println("Contract saving with id:" +contract.getId());
        if (contract.getId() != null) { //existing contract
            User user = em.find(User.class, username);
            //TODO: merging without setting Invoices does not work (needs fix)
            //contract.setInvoices(new ArrayList<Invoice>());
            em.merge(contract);
        } else {                        //new contract
            User user = em.find(User.class, username);
            contract.setUser(user);
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
}

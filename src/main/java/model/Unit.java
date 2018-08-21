package model;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "UNITS")
public class Unit {
    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq6", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    private Long id;
    private String address;
    private String city;
    private Integer rooms;
    private Float size;
    private Float price;
    private String status;
    private String unitType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private User user;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public void addContract(Contract contract) {
        contracts.add(contract);
        contract.setUnit(this);
    }

    public void removeContract(Contract contract) {
        contracts.remove(contract);
        contract.setUnit(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return Objects.equals(id, unit.id) &&
                Objects.equals(address, unit.address) &&
                Objects.equals(city, unit.city) &&
                Objects.equals(rooms, unit.rooms) &&
                Objects.equals(size, unit.size) &&
                Objects.equals(price, unit.price) &&
                Objects.equals(status, unit.status) &&
                Objects.equals(unitType, unit.unitType) &&
                Objects.equals(user, unit.user) &&
                Objects.equals(contracts, unit.contracts);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, address, city, rooms, size, price, status, unitType, user, contracts);
    }
}

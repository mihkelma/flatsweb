package model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User {
    //Initially based on https://www.boraji.com/spring-mvc-5-spring-security-5-hibernate-5-example

    @Id
    @Column(name = "USERNAME")
    @NotEmpty(message="{register.error.emailRequired}")
    private String username;

    @Column(name = "FULLNAME")
    @NotEmpty(message="{register.error.fullNameRequired}")
    private String fullName;

    @Column(name = "PASSWORD")
    @Size(min=5, message="{register.error.passwordRequired}")
    private String password;

    @Column(name = "ENABLED")
    private boolean enabled;

    @Column(name = "CONFIRMATIONTOKEN")
    private String confirmationToken;

    //Merge - issue: https://stackoverflow.com/questions/13370221/jpa-hibernate-detached-entity-passed-to-persist
    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(name="users_roles", joinColumns=@JoinColumn(name="username"), inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public void addContract(Contract contract) {
        contracts.add(contract);
        contract.setUser(this);
    }

    public void removeContract(Contract contract) {
        contracts.remove(contract);
        contract.setUser(null);
    }
}

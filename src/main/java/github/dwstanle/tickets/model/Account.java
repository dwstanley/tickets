package github.dwstanle.tickets.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    @OneToMany(mappedBy = "account")
    private Set<Reservation> reservations = new HashSet<>();

    //JPA construction only
    private Account() {
    }

    public Account(String email) {
        this.email = Objects.requireNonNull(email);
    }

}
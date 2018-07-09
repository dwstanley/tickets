package github.dwstanle.tickets.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @Column(unique=true)
    private String email;

//    @OneToMany(mappedBy = "account")
//    private Set<Reservation> reservations = new HashSet<>();

    //JPA construction only
    private Account() {
    }

}
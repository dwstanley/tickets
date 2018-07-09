package github.dwstanle.tickets.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

    //JPA construction only
    private Account() {
    }

}
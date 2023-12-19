package com.ovd.gestionstock.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ovd.gestionstock.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;

import java.util.List;


@Data
@Builder
@Entity
@Table(name = "UTILISATEURS")
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String email;
    private String nom;
    private String prenom;



    @Column(name = "datedenaissance")
    private Instant dateDeNaissance;

    @Embedded
    private Adresse adresse;

    @ManyToOne
    @JoinColumn(name = "idEntreprise")
    private Entreprise entreprise;

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "utilisateur")
    private List<Token> tokens;

    @OneToMany(mappedBy = "utilisateur")
    @JsonManagedReference
    private List<Livraison> livraisons;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

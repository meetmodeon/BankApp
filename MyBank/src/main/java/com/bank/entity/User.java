package com.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    private String firstName;
    private String lastName;
    @Column(nullable = false,unique = true)
    private String username;
    private Date dob;
    private Long tel;
    private String tag;
    @JsonIgnore
    private String password;
    private String gender;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private List<String> roles;

    @JsonIgnore
    @OneToOne(cascade= CascadeType.ALL,mappedBy = "owner")
    private Card card;
    @JsonIgnore
    @OneToMany(mappedBy = "owner",cascade= CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @JsonIgnore
    @OneToMany(mappedBy = "owner",cascade= CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Account>  accounts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
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

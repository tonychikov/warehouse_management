package com.warehousemanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "warehouse", uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
@Data
@NoArgsConstructor
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 3, unique = true, nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "warehouse", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Bay> bays;

    public Warehouse(String code, String name, String address) {
        setName(name);
        setAddress(address);
        setCode(code);
    }

    public void setCode(String code) {
        if (!code.matches("\\d{3}")) {
            throw new IllegalArgumentException("Warehouse code must contain only 3 digits.");
        }
        this.code = code;
    }
}

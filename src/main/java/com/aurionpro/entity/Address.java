package com.aurionpro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String state;
    private String pincode;
}

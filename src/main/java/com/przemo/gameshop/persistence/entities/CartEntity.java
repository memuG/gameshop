package com.przemo.gameshop.persistence.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public final class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}

package com.przemo.gameshop.persistence.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Builder
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Cacheable(value = false)
public final class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 2, max = 250)
    @Column(unique = true)
    // TODO: check the unique constraint on h2 table schema
    private String title;

    @Min(1)
    @Max(999999999)
    private BigDecimal price;
}

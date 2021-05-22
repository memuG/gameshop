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

    public static final int MIN_TITLE_CHARS = 2;
    public static final int MAX_TITLE_CHARS = 250;

    public static final int MIN_PRICE = 1;
    public static final int MAX_PRICE = 999999999;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = MIN_TITLE_CHARS, max = MAX_TITLE_CHARS)
    @Column(unique = true)
    // TODO: check the unique constraint on h2 table schema
    private String title;

    @Min(MIN_PRICE)
    @Max(MAX_PRICE)
    private BigDecimal price;
}

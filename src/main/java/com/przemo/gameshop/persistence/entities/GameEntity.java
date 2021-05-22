package com.przemo.gameshop.persistence.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import static com.przemo.gameshop.persistence.entities.GameEntityConstraints.*;

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
    @Size(min = MIN_TITLE_CHARS, max = MAX_TITLE_CHARS)
    @Column(unique = true)
    // TODO: check the unique constraint on h2 table schema
    private String title;

    @Min(MIN_PRICE)
    @Max(MAX_PRICE)
    @NotNull
    private BigDecimal price;
}

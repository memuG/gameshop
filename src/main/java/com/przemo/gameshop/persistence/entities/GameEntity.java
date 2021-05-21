package com.przemo.gameshop.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
public final class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 2)
    private String title;

    @Min(1)
    private BigDecimal price;
}

package com.przemo.gameshop.persistence.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.przemo.gameshop.persistence.entities.CartsGamesEntityConstraints.*;

@Entity
@Builder
@Table(name = "carts_games")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CartsGamesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "game_id")
    GameEntity game;

    @Min(MIN_GAME_COUNT)
    @Max(MAX_GAME_COUNT)
    int game_count;
}
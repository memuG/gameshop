package com.przemo.gameshop.persistence;

import com.przemo.gameshop.persistence.entities.CartsGamesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartsGamesRepository extends JpaRepository<CartsGamesEntity, Integer> {
    List<CartsGamesEntity> findAllByCartId(final int cartId);

    Optional<CartsGamesEntity> findFirstByCartIdAndGameId(final int cartId, final int gameId);
}

package com.przemo.gameshop.persistence;

import com.przemo.gameshop.persistence.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameInventoryRepository extends JpaRepository<GameEntity, Integer> {
}

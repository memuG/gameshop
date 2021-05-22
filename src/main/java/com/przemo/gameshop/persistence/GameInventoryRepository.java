package com.przemo.gameshop.persistence;

import com.przemo.gameshop.persistence.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameInventoryRepository extends CrudRepository<GameEntity, Integer>, JpaRepository<GameEntity, Integer> {

    Optional<GameEntity> findByTitle(String title);

}

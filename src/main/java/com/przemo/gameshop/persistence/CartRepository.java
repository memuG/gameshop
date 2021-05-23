package com.przemo.gameshop.persistence;

import com.przemo.gameshop.persistence.entities.CartEntity;
import com.przemo.gameshop.persistence.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {
}

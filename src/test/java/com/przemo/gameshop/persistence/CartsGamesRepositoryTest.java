package com.przemo.gameshop.persistence;

import com.przemo.gameshop.persistence.entities.CartEntity;
import com.przemo.gameshop.persistence.entities.CartsGamesEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CartsGamesRepositoryTest {

    @Autowired
    private CartsGamesRepository cartsGamesRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testInitialDbCart() {
        Optional<CartsGamesEntity> cart = cartsGamesRepository.findById(1);

        Assertions.assertTrue(cart.isPresent());
        assertEquals(1, cart.get().getId());
    }

    @Test
    void testInitialDbCartGames() {
        Optional<CartsGamesEntity> cart = cartsGamesRepository.findById(1);

        Assertions.assertTrue(cart.isPresent());
        assertEquals(1, cart.get().getId());
    }

}
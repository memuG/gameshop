package com.przemo.gameshop.persistence;

import com.przemo.gameshop.persistence.entities.CartEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testInitialDbCart() {
        Optional<CartEntity> cart = cartRepository.findById(1);

        Assertions.assertTrue(cart.isPresent());
        assertEquals(1, cart.get().getId());
    }

    @Test
    void testInitialDbCartGames() {
        Optional<CartEntity> cart = cartRepository.findById(1);

        Assertions.assertTrue(cart.isPresent());
        assertEquals(1, cart.get().getId());
    }
}
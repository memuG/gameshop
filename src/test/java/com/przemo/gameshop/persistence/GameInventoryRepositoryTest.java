package com.przemo.gameshop.persistence;

import com.przemo.gameshop.persistence.entities.GameEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class GameInventoryRepositoryTest {

    @Autowired
    private GameInventoryRepository gameInventoryRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Sql("classpath:test-data.sql")
    void testDbEntriesCount() {
        List<GameEntity> allGames =gameInventoryRepository.findAll();
        System.out.println(allGames);
        assertEquals(9, allGames.size());
    }
}
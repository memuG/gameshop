package com.przemo.gameshop.persistence;

import com.przemo.gameshop.persistence.entities.GameEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import testutils.TestUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.przemo.gameshop.persistence.entities.GameEntityConstraints.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class GameInventoryRepositoryTest {

    @Autowired
    private GameInventoryRepository gameInventoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testInitialDbEntriesCount() {
        List<GameEntity> allGames = gameInventoryRepository.findAll();
        assertEquals(5, allGames.size());
    }

    @Test
    void testPricePrecisionAndRounding() {
        final Map<GameEntity, BigDecimal> inputResults = new LinkedHashMap<>();
        inputResults.put(GameEntity.builder().title("Fallout2").price(new BigDecimal("8.9954345")).build(), new BigDecimal("9.00"));
        inputResults.put(GameEntity.builder().title("The Witcher").price(new BigDecimal("9")).build(), new BigDecimal("9.00"));
        inputResults.put(GameEntity.builder().title("The Witcher 2 Assassins of Kings").price(new BigDecimal("14.3242")).build(), new BigDecimal("14.32"));
        inputResults.put(GameEntity.builder().title("The Witcher 3").price(new BigDecimal("50.000000001")).build(), new BigDecimal("50.00"));

        inputResults.keySet()
                .forEach(k -> {gameInventoryRepository.saveAndFlush(k); entityManager.detach(k);});

        inputResults.forEach(
                (k, v) -> assertEquals(v, gameInventoryRepository.findByTitle(k.getTitle()).orElseThrow().getPrice(), k.toString())
        );
    }

    @Test
    void testValidPrices() {
        assertDoesNotThrow(
                () -> gameInventoryRepository.save(
                        GameEntity.builder().title("Fable").price(new BigDecimal(MIN_PRICE)).build()));
        assertDoesNotThrow(
                () -> gameInventoryRepository.save(
                        GameEntity.builder().title("Fable2").price(new BigDecimal(MAX_PRICE)).build()));
    }

    @Test
    void testNegativePrice() {
        GameEntity secondFallout = GameEntity.builder().title("Fable").price(new BigDecimal("-45")).build();
        assertThrows(ConstraintViolationException.class, () -> gameInventoryRepository.saveAndFlush(secondFallout));
    }

    @Test
    void testTooLargePrice() {
        GameEntity secondFallout = GameEntity.builder().title("Fable").price(new BigDecimal("999999999.001")).build();
        assertThrows(ConstraintViolationException.class, () -> gameInventoryRepository.saveAndFlush(secondFallout));
    }

    @Test
    void testValidTitles() {
        assertDoesNotThrow(
                () -> gameInventoryRepository.saveAndFlush(
                        GameEntity.builder().title(TestUtils.generateGivenLengthXString(MIN_TITLE_CHARS)).price(new BigDecimal("1")).build()));
        assertDoesNotThrow(
                () -> gameInventoryRepository.saveAndFlush(
                        GameEntity.builder().title(TestUtils.generateGivenLengthXString(MAX_TITLE_CHARS)).price(new BigDecimal("999999999")).build()));
    }

    @Test
    void testNonUniqueTitle() {
        GameEntity secondFallout = GameEntity.builder().title("Fallout").price(new BigDecimal("1.99")).build();
        assertThrows(DataIntegrityViolationException.class, () -> gameInventoryRepository.saveAndFlush(secondFallout));
    }
}
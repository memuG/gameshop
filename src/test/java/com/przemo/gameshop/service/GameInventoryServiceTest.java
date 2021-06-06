package com.przemo.gameshop.service;

import com.przemo.gameshop.dto.GameEntityDto;
import com.przemo.gameshop.persistence.entities.GameEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class GameInventoryServiceTest {

    @Autowired
    GameInventoryService gameInventoryService;

    @Test
    void testGameModificationSequential() {
        final int gameId = 1;
        GameEntityDto gameModifications = GameEntityDto.builder().price(new BigDecimal("2.85")).build();
        GameEntityDto gameModifications2= GameEntityDto.builder().price(new BigDecimal("4.88")).build();

        GameEntity modifiedGame = gameInventoryService.updateGameById(gameId, gameModifications);
        modifiedGame = gameInventoryService.updateGameById(gameId, gameModifications2);
        assertEquals("4.88", modifiedGame.getPrice().toPlainString());
    }

    @Test
    void testGameModificationParallel() throws InterruptedException {
        final int gameId = 1;
        GameEntityDto gameModifications = GameEntityDto.builder().build();

        concurrentGameModification(gameId, gameModifications);

        assertTrue("Invalid value for game price after concurrent modification",
                "2.85".equals(gameInventoryService.getGameById(gameId).getPrice().toPlainString())
                || "4.55".equals(gameInventoryService.getGameById(gameId).getPrice().toPlainString())
        );
    }

    private void concurrentGameModification(int gameId, GameEntityDto gameModifications) throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            gameModifications.setPrice(new BigDecimal("2.85"));
            gameInventoryService.updateGameById(gameId, gameModifications);
        });
        executorService.execute(() -> {
            gameModifications.setPrice(new BigDecimal("4.55"));
            gameInventoryService.updateGameById(gameId, gameModifications);
        });
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }
}
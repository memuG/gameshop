package com.przemo.gameshop.service;

import com.przemo.gameshop.persistence.GameInventoryRepository;
import com.przemo.gameshop.persistence.entities.GameEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameInventoryService {

    private final GameInventoryRepository gameInventoryRepository;

    public GameInventoryService(GameInventoryRepository gameInventoryRepository) {
        this.gameInventoryRepository = gameInventoryRepository;
    }

    public void addGame(GameEntity gameEntity) {
        gameInventoryRepository.save(gameEntity);
    }

    public List<GameEntity> getAllGames() {
        return gameInventoryRepository.findAll();
    }

    public GameEntity getGameByTitle(String title) {
        return gameInventoryRepository.findByTitle(title).orElseThrow();
    }
}

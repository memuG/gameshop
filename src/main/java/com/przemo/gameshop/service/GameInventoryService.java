package com.przemo.gameshop.service;

import com.przemo.gameshop.persistence.GameInventoryRepository;
import com.przemo.gameshop.persistence.entities.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameInventoryService {

    private final GameInventoryRepository gameInventoryRepository;

    public GameInventoryService(GameInventoryRepository gameInventoryRepository) {
        this.gameInventoryRepository = gameInventoryRepository;
    }

    public GameEntity addGame(final GameEntity gameEntity) {
        return gameInventoryRepository.save(gameEntity);
    }

    public List<GameEntity> getAllGames(final int pageNo, final int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<GameEntity> pagedResult = gameInventoryRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public GameEntity getGameByTitle(final String title) {
        return gameInventoryRepository.findByTitle(title).orElseThrow();
    }
}

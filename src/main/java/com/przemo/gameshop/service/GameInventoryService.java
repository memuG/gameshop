package com.przemo.gameshop.service;

import com.przemo.gameshop.dto.GameEntityDto;
import com.przemo.gameshop.persistence.GameInventoryRepository;
import com.przemo.gameshop.persistence.entities.GameEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class GameInventoryService {

    private final GameInventoryRepository gameInventoryRepository;

    @Autowired
    public GameInventoryService(GameInventoryRepository gameInventoryRepository) {
        this.gameInventoryRepository = gameInventoryRepository;
    }

    public GameEntity addGame(final GameEntityDto gameEntityDto) {
        return gameInventoryRepository.save(GameEntity.builder()
                .title(gameEntityDto.getTitle())
                .price(gameEntityDto.getPrice())
                .build());
    }

    public Page<GameEntity> getAllGames(final int pageNo, final int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return gameInventoryRepository.findAll(paging);
    }

    public GameEntity getGameByTitle(final String title) {
        return gameInventoryRepository.findByTitle(title).orElseThrow();
    }

    public GameEntity getGameById(final int id) {
        return gameInventoryRepository.findById(id).orElseThrow();
    }

    public void deleteGameById(final int id) {
        gameInventoryRepository.deleteById(id);
    }

    @Transactional
    public GameEntity updateGameById(final int id, final GameEntityDto newGameProperties) {
        Optional<GameEntity> gameToModify= gameInventoryRepository.findById(id);
        if (gameToModify.isEmpty())
            throw new EntityNotFoundException("Game with id " + id + " could not be found");
        GameEntity modifiedGame = gameToModify.get();
        if (newGameProperties.getTitle()!= null && !modifiedGame.getTitle().equals(newGameProperties.getTitle()))
            modifiedGame.setTitle(newGameProperties.getTitle());
        if (newGameProperties.getPrice()!= null && !modifiedGame.getPrice().equals(newGameProperties.getPrice()))
            modifiedGame.setPrice(newGameProperties.getPrice());
        return gameInventoryRepository.saveAndFlush(modifiedGame);
    }
}

package com.przemo.gameshop.web;

import com.przemo.gameshop.persistence.entities.GameEntity;
import com.przemo.gameshop.service.GameInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@Slf4j
@Validated
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(path = "inventory/v1")
public class InventoryController {

    private final GameInventoryService gameInventoryService;

    @Autowired
    public InventoryController(GameInventoryService gameInventoryService){
        this.gameInventoryService = gameInventoryService;
    }

    @GetMapping(path = "list-all-games", produces = {"application/json"})
    public ResponseEntity<?> getAllGames() {
        //TODO: paginate the list 3 items per page
        return ResponseEntity.ok(gameInventoryService.getAllGames());
    }

    @PostMapping(path = "add-game")
    public ResponseEntity<?> addNewGame(@Valid @RequestBody final GameEntity game) {
        final GameEntity secureGame =
                GameEntity.builder()
                .title(game.getTitle()) // String is immutable itself
                .price(new BigDecimal(String.valueOf(game.getPrice())))
                .build();
        gameInventoryService.addGame(secureGame);

        return ResponseEntity.ok("Game '" + secureGame.getTitle() + "' for " + secureGame.getPrice() + " USD added");
    }

    @PutMapping(path = "update-game")
    public ResponseEntity<?> editGame() {
        return ResponseEntity.ok("Game modified");
    }

    @DeleteMapping(path = "delete-game")
    public ResponseEntity<?> deleteGame() {
        return ResponseEntity.ok("Game deleted");
    }
}

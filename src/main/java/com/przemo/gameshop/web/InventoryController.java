package com.przemo.gameshop.web;

import com.przemo.gameshop.persistence.entities.GameEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(path = "inventory/v1")
public class InventoryController {

    @GetMapping(path = "list-all-games", produces = {"application/json"})
    public ResponseEntity<?> getAllGames() {
        return ResponseEntity.ok("All games are listed below...");
    }

    @PostMapping(path = "add-game")
    public ResponseEntity<?> addNewGame(@Valid @RequestBody GameEntity game) {
        return ResponseEntity.ok("Game " + game.getTitle() + " for " + game.getPrice() + "USD added");
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

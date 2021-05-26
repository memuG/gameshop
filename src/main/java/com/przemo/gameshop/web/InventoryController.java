package com.przemo.gameshop.web;

import com.przemo.gameshop.dto.GameEntityDto;
import com.przemo.gameshop.persistence.entities.GameEntity;
import com.przemo.gameshop.service.GameInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Validated
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(path = "inventory/v1")
public class InventoryController {

    @Value("${gameshop.gamesPerPage}")
    private int gamesPerPage;

    private final GameInventoryService gameInventoryService;

    @Autowired
    public InventoryController(GameInventoryService gameInventoryService){
        this.gameInventoryService = gameInventoryService;
    }

    @GetMapping(path = "games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<GameEntity>> getAllGames(
            @RequestParam(defaultValue = "0", name = "page") @Min(0) final int page) {
        return ResponseEntity.ok(gameInventoryService.getAllGames(page, gamesPerPage));
    }

    @PostMapping(path = "game", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewGame(@Valid @RequestBody final GameEntityDto game) throws URISyntaxException {
        try {
            GameEntity result = gameInventoryService.addGame(game);
            return ResponseEntity.created(new URI("/inventory/v1/game/" + result.getId())).body(result);
        } catch (DataIntegrityViolationException exc) {
            return ResponseEntity.badRequest().body("\"Game '" + game.getTitle() + "' for " + game.getPrice() + " could not be added " + exc.getMessage() + "\"");
        }
    }

    @PutMapping(path = "game/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editGame(@PathVariable("id") final int id,
                                      @Valid @RequestBody GameEntityDto gameEntityDto) {
        return ResponseEntity.accepted().body(gameInventoryService.updateGameById(id, gameEntityDto));
    }

    @DeleteMapping(path = "game/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteGame(@PathVariable("id") @Min(1) final int id) {
        gameInventoryService.deleteGameById(id);
        return ResponseEntity.ok("\"Game with id " + id + " successfully deleted.\"");
    }
}

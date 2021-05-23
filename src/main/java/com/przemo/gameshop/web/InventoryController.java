package com.przemo.gameshop.web;

import com.przemo.gameshop.dto.GameEntityDto;
import com.przemo.gameshop.persistence.entities.GameEntity;
import com.przemo.gameshop.service.GameInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Validated
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(path = "inventory/v1")
public class InventoryController {

    private static final int GAMES_PER_PAGE = 3;

    private final GameInventoryService gameInventoryService;

    @Autowired
    public InventoryController(GameInventoryService gameInventoryService){
        this.gameInventoryService = gameInventoryService;
    }

    @GetMapping(path = "games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllGames(
            @RequestParam(defaultValue = "0", name = "page") @Min(0) @Max(9999) String page) {
        int pageNo = Integer.parseInt(page);
        return ResponseEntity.ok(gameInventoryService.getAllGames(pageNo, GAMES_PER_PAGE));
    }

    @PostMapping(path = "game", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewGame(@Valid @RequestBody final GameEntity game) throws URISyntaxException {
        final GameEntity secureGame =
                GameEntity.builder()
                .title(game.getTitle()) // String is immutable itself
                .price(new BigDecimal(String.valueOf(game.getPrice())))
                .build();
        GameEntity result;
        try {
            result = gameInventoryService.addGame(secureGame);
        } catch (DataIntegrityViolationException exc) {
            return ResponseEntity.badRequest().body("\"Game '" + secureGame.getTitle() + "' for " + secureGame.getPrice() + " could not be added\"");
        }
        return ResponseEntity.created(new URI("/inventory/v1/game/" + result.getId())).body(result);
    }

    @PutMapping(path = "game/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editGame(@PathVariable("id") final int id,
                                      @Valid @RequestBody GameEntityDto gameEntityDto) {
        GameEntityDto defensiveDto = GameEntityDto.builder()
                .title(gameEntityDto.getTitle())
                .price(new BigDecimal(String.valueOf(gameEntityDto.getPrice())))
                .build();
        return ResponseEntity.accepted().body(gameInventoryService.updateGameById(id, defensiveDto));
    }

    @DeleteMapping(path = "game/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteGame(@PathVariable("id") @Min(1) final int id) {
        gameInventoryService.deleteGameById(id);
        return ResponseEntity.ok("\"Game with id " + id + " successfully deleted.\"");
    }
}

package com.przemo.gameshop.web;

import com.przemo.gameshop.dto.GameEntityDto;
import com.przemo.gameshop.persistence.entities.CartEntity;
import com.przemo.gameshop.persistence.entities.GameEntity;
import com.przemo.gameshop.service.CartService;
import com.przemo.gameshop.service.GameInventoryService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
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
@RequestMapping(path = "shopping/v1")
public class ShoppingController {

    private final CartService cartService;

    private final GameInventoryService gameInventoryService;

    @Autowired
    public ShoppingController(CartService cartService, GameInventoryService gameInventoryService){
        this.cartService = cartService;
        this.gameInventoryService = gameInventoryService;
    }

    @PostMapping(path = "cart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewGame() throws URISyntaxException {
        CartEntity createdCart = cartService.addEmptyCart();
        return ResponseEntity.created(new URI("/inventory/v1/cart/" + createdCart.getId())).body(createdCart);
    }

    @PutMapping(path = "cart/{id}/{game_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProductToCart(@PathVariable("id") final int cartId,
                                              @PathVariable("game_id") final int gameId,
                                              @RequestParam (name = "count") @Min(1) @Max(10)final  int gameCount) {
        GameEntity gameToAdd = gameInventoryService.getGameById(gameId);
        return ResponseEntity.accepted().body(cartService.addGameToCart(cartId, gameToAdd, gameCount));
    }

    @DeleteMapping(path = "cart/{id}/{game_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProductFromTheCart(@PathVariable("id") final int cartId,
                                                      @PathVariable("game_id") final int gameId) {
        cartService.deleteProductById(cartId, gameId);
        return ResponseEntity.ok(
                "\"Game with id " + gameId + " successfully deleted from the cart " + cartId +".\"");
    }

    @GetMapping(path = "cart/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listProductsFromTheCart(@PathVariable("id") final int cartId) {
        return ResponseEntity.ok(cartService.listProducts(cartId));
    }
}

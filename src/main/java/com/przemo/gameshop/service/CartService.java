package com.przemo.gameshop.service;

import com.przemo.gameshop.persistence.CartsGamesRepository;
import com.przemo.gameshop.persistence.entities.CartEntity;
import com.przemo.gameshop.persistence.CartRepository;
import com.przemo.gameshop.persistence.entities.CartsGamesEntity;
import com.przemo.gameshop.persistence.entities.GameEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.przemo.gameshop.persistence.entities.CartsGamesEntityConstraints.*;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;

    private final CartsGamesRepository cartsGamesRepository;

    @Autowired
    public CartService(final CartRepository cartRepository, final CartsGamesRepository cartsGamesRepository) {
        this.cartRepository = cartRepository;
        this.cartsGamesRepository = cartsGamesRepository;
    }

    public CartEntity addEmptyCart() {
        return cartRepository.save(new CartEntity());
    }

    public CartsGamesEntity addGameToCart(final int cartId, final GameEntity gameToAdd, final int gameCount) {
        CartEntity cartToModify = cartRepository.findById(cartId).orElseThrow();
        CartsGamesEntity cartsGames = CartsGamesEntity.builder()
                .cart(cartToModify)
                .game(gameToAdd)
                .game_count(gameCount)
                .build();
        List<CartsGamesEntity> cartContents = cartsGamesRepository.findAllByCartId(cartId);
        if (!isGameCountSumValid(cartContents, gameToAdd.getId(), gameCount))
            throw new InvalidParameterException("Could not add more than allowed " + MAX_GAME_COUNT + " game instances to the cart.");
        if (cartContents.size() == MAX_CART_GAMES)
            if (!isGamePresentInCart(cartContents, gameToAdd.getId()))
                throw new InvalidParameterException("Could not add more than allowed " + MAX_CART_GAMES + " games to the cart.");

        for (CartsGamesEntity e: cartContents) {
            if (e.getGame().getId().equals(gameToAdd.getId())){
                int newCount = e.getGame_count() + gameCount;
                e.setGame_count(newCount);
                return cartsGamesRepository.save(e);
            }
        }
        return cartsGamesRepository.saveAndFlush(cartsGames);
    }

    public void deleteProductById(final int cartId,final int gameId) {
        cartsGamesRepository.delete(
                cartsGamesRepository.findFirstByCartIdAndGameId(cartId, gameId).orElseThrow());
    }

    private boolean isGameCountSumValid(final List<CartsGamesEntity> cartContents, final int gameId, final int gameCount) {
        int cartGameCount =
                cartContents.stream()
                        .filter(x -> x.getGame().getId().equals(gameId))
                        .mapToInt(CartsGamesEntity::getGame_count)
                        .sum();
        return cartGameCount + gameCount <= MAX_GAME_COUNT;
    }

    private boolean isGamePresentInCart(final List<CartsGamesEntity> cartContents, final int gameId) {
        boolean present = false;
        for (CartsGamesEntity e : cartContents)
            if (e.getGame().getId() == gameId){
                present = true;
                break;
            }
        return present;
    }

    public List<CartsGamesEntity> listProducts(final int cartId) {
        return cartsGamesRepository.findAllByCartId(cartId);
    }
}
package com.przemo.gameshop.web;

import com.przemo.gameshop.GameshopApplication;
import com.przemo.gameshop.dto.GameEntityDto;
import com.przemo.gameshop.persistence.entities.GameEntity;
import com.przemo.gameshop.service.GameInventoryService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import testutils.TestUtils;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import static com.przemo.gameshop.persistence.entities.GameEntityConstraints.*;

@SpringBootTest(classes = GameshopApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class InventoryControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    GameInventoryService gameInventoryService;

    private List<GameEntity> emptyGameEntitiesFixture(final int count) {
        List<GameEntity> games = new ArrayList<>();
        for (int i = 0; i < count; ++i)
            games.add(GameEntity.builder().build());
        return games;
    }

    @Test
    public void testGamesPagination() {
        Mockito.when(gameInventoryService.getAllGames(anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(emptyGameEntitiesFixture(3)));

        // TODO: find solution for deserializing Page

        //        Page s = this.restTemplate
        //                                .getForObject("http://localhost:" + port + "/inventory/v1/games", Page.class);
        String pageJsonString = this.restTemplate
                .getForObject("http://localhost:" + port + "/inventory/v1/games", String.class);
        System.out.println("Przemko: " + pageJsonString);
        assertThat(pageJsonString, CoreMatchers.containsString("\"numberOfElements\":3"));
        assertThat(pageJsonString, CoreMatchers.containsString("\"totalElements\":3"));
        assertThat(pageJsonString, CoreMatchers.containsString("\"totalPages\":1"));
    }

    @Test
    public void testGamesNegativePageInt_500_InternalServerError() {
        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/inventory/v1/games?page=-2", String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testGamesNegativePageFloat_400_BadRequest() {
        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/inventory/v1/games?page=-2.3", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGamesPageNotaNumber_400_BadRequest() {
        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/inventory/v1/games?page=bds", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGamesPageOverTotalNumber_200_Ok() {
        Mockito.when(gameInventoryService.getAllGames(anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/inventory/v1/games?page=9999", String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testPostValidGame_200_Ok() {
        GameEntityDto game = GameEntityDto.builder().title("test").price(new BigDecimal("3.23")).build();
        Mockito.when(gameInventoryService.addGame(any()))
                .thenReturn(GameEntity.builder().id(8).title(game.getTitle()).price(game.getPrice()).build());

        GameEntity responseGame = this.restTemplate
                .postForObject("http://localhost:" + port + "/inventory/v1/game", new HttpEntity<>(game), GameEntity.class);

        assertEquals(8, responseGame.getId());
        assertEquals("test", responseGame.getTitle());
        assertEquals("3.23", responseGame.getPrice().toPlainString());
    }

    @Test
    public void testPost1CharGameTitle_400_BadRequest() {
        GameEntityDto gameDto = GameEntityDto.builder().title("t").price(new BigDecimal("3.23")).build();
        Mockito.when(gameInventoryService.addGame(any()))
                .thenReturn(GameEntity.builder().build());

        ResponseEntity<?> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/inventory/v1/game", new HttpEntity<>(gameDto), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testPost251CharGameTitle_400_BadRequest() {
        Mockito.when(gameInventoryService.addGame(any()))
                .thenReturn(GameEntity.builder().build());

        GameEntityDto gameDto = GameEntityDto.builder()
                .title(TestUtils.generateGivenLengthXString(MAX_TITLE_CHARS + 1))
                .price(new BigDecimal("3"))
                .build();

        ResponseEntity<?> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/inventory/v1/game", new HttpEntity<>(gameDto), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testPost250CharGameTitle_201_Created() {
        Mockito.when(gameInventoryService.addGame(any()))
                .thenReturn(GameEntity.builder().build());

        GameEntityDto gameDto = GameEntityDto.builder()
                .title(TestUtils.generateGivenLengthXString(MAX_TITLE_CHARS))
                .price(new BigDecimal("31"))
                .build();

        ResponseEntity<?> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/inventory/v1/game", new HttpEntity<>(gameDto), String.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testPutValidGameProps_200_Ok() {
        Mockito.when(gameInventoryService.updateGameById(anyInt(), any()))
                .thenReturn(GameEntity.builder().build());

        GameEntityDto game = GameEntityDto.builder()
                .title("Changed")
                .price(new BigDecimal("15"))
                .build();

        String gameId = "1";
        ResponseEntity<?> responseEntity = this.restTemplate
                .exchange("http://localhost:" + port + "/inventory/v1/game/" + gameId,
                            HttpMethod.PUT,
                        new HttpEntity<>(game), String.class);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void testPutValidGameProps_204_NotFound() {
        Mockito.when(gameInventoryService.updateGameById(anyInt(), any()))
                .thenThrow(EntityNotFoundException.class);

        GameEntityDto game = GameEntityDto.builder()
                .title("Changed")
                .price(new BigDecimal("15"))
                .build();

        String gameId = "999";
        ResponseEntity<?> responseEntity = this.restTemplate
                .exchange("http://localhost:" + port + "/inventory/v1/game/" + gameId,
                        HttpMethod.PUT,
                        new HttpEntity<>(game), String.class);

        assertEquals(HttpStatus.valueOf(204), responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteGame_200_Ok() {
        Mockito.doNothing()
                .when(gameInventoryService).deleteGameById(anyInt());

        String gameId = "1";
        ResponseEntity<?> responseEntity = this.restTemplate
                .exchange("http://localhost:" + port + "/inventory/v1/game/" + gameId,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null), String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteGame_204_NotFound() {
        Mockito.doThrow(EmptyResultDataAccessException.class)
                .when(gameInventoryService).deleteGameById(anyInt());

        String gameId = "999";
        ResponseEntity<?> responseEntity = this.restTemplate
                .exchange("http://localhost:" + port + "/inventory/v1/game/" + gameId,
                        HttpMethod.DELETE,
                        new HttpEntity<>(null), String.class);

        assertEquals(HttpStatus.valueOf(204), responseEntity.getStatusCode());
    }
}
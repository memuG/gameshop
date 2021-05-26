package com.przemo.gameshop.web;

import com.przemo.gameshop.GameshopApplication;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;

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
    public void testGamesNegativePageNo_400BadRequest() {
        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/inventory/v1/games?page=-23", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGamesPageNotaNumber_400BadRequest() {
        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/inventory/v1/games?page=bds", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGamesPageMaxNumber_200Ok() {
        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/inventory/v1/games?page=9999", String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
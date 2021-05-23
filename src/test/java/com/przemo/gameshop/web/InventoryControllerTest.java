package com.przemo.gameshop.web;

import com.przemo.gameshop.GameshopApplication;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GameshopApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class InventoryControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGamesPagination() {
        assertEquals(3, this.restTemplate
                .getForObject("http://localhost:" + port + "/inventory/v1/games", List.class)
                .size());
        assertEquals(2, this.restTemplate
                .getForObject("http://localhost:" + port + "/inventory/v1/games?page=1", List.class)
                .size());
        assertEquals(0, this.restTemplate
                .getForObject("http://localhost:" + port + "/inventory/v1/games?page=2", List.class)
                .size());
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

    // TODO: add TestCases for further CRUD actions
}
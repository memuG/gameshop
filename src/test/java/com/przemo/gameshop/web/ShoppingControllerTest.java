package com.przemo.gameshop.web;

import com.przemo.gameshop.GameshopApplication;
import com.przemo.gameshop.service.ShoppingService;
import com.przemo.gameshop.service.GameInventoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest(classes = GameshopApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ShoppingControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    GameInventoryService gameInventoryService;

    @MockBean
    ShoppingService shoppingService;

    @Test
    public void testCartView_204_ResourceNotFound() {
        Mockito.when(shoppingService.listProducts(anyInt()))
                .thenThrow(NoSuchElementException.class);

        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/shopping/v1/cart/1", String.class);
        assertEquals(HttpStatus.valueOf(204), responseEntity.getStatusCode());
    }

    @Test
    public void testCartView_200_Ok() {
        Mockito.when(shoppingService.listProducts(anyInt()))
                .thenReturn("Almost real cart summary String");

        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/shopping/v1/cart/1", String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testCartViewInvalidId_400_BadRequest() {
        Mockito.when(shoppingService.listProducts(anyInt()))
                .thenReturn("");

        ResponseEntity<?> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/shopping/v1/cart/invalid", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
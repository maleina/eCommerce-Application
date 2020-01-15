package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);

        // Create an Item object, as well as the appropriate Mokito
        // "when" clause.
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        when(itemRepo.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepo.findByName("Round Widget")).thenReturn(Collections.singletonList(item));

    }

    @Test
    public void get_all_items_happy_path() throws Exception {
        ResponseEntity<List<Item>> response = itemController.getItems();
        // Verify that the correct status code and user information is returned.
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void get_item_by_id_happy_path() throws Exception {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        // Verify that the correct status code and user information is returned.
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item i = response.getBody();
        assertNotNull(i);
    }

    @Test
    public void get_item_by_id_not_found() throws Exception {
        ResponseEntity<Item> response = itemController.getItemById(2L);
        // Verify that the correct status code and user information is returned.
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_items_by_name_happy_path() throws Exception {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");
        // Verify that the correct status code and user information is returned.
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void get_items_by_name_not_found() throws Exception {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Square Widget");
        // Verify that the correct status code and user information is returned.
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }


}

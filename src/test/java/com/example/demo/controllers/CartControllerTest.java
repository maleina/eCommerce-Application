package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        // Inject repositories and encoder in order to use mocking.
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);

        // Create a User object, as well as the appropriate Mokito
        // "when" clause.
        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(cart);
        when(userRepo.findByUsername("test")).thenReturn(user);

        // Create an Item object, as well as the appropriate Mokito
        // "when" clause.
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        BigDecimal price = BigDecimal.valueOf(2.99);
        item.setPrice(price);
        item.setDescription("A widget that is round");
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void add_to_cart_happy_path() throws Exception {
        // Create a valid modify cart request.
        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(1);
        r.setUsername("test");
        ResponseEntity<Cart> response = cartController.addTocart(r);

        // Verify that the item has been added to the cart correctly,
        // with the proper status code and response.
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart c = response.getBody();
        assertNotNull(c);
        // If only one item no. 1 was inserted, then the total should
        // be $2.99.
        assertEquals(BigDecimal.valueOf(2.99), c.getTotal());

    }

    @Test
    public void add_to_cart_invalid_user() throws Exception {
        // Create a modify cart request with an invalid user.
        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(1);
        r.setUsername("boo");
        ResponseEntity<Cart> response = cartController.addTocart(r);

        // Verify that the "not found" status code has been returned.
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_invalid_item() throws Exception {
        // Create a modify cart request with an invalid item.
        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(2L);
        r.setQuantity(1);
        r.setUsername("test");
        ResponseEntity<Cart> response = cartController.addTocart(r);

        // Verify that the "not found" status code has been returned.
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_happy_path() throws Exception {
        // Set up test by adding two items to cart.
        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(2);
        r.setUsername("test");
        ResponseEntity<Cart> response = cartController.addTocart(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        // Remove one of the two items.
        r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(1);
        r.setUsername("test");
        response = cartController.removeFromcart(r);

        // Verify that the item has been removed from the cart correctly,
        // with the proper status code and response.
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart c = response.getBody();
        assertNotNull(c);
        // If only one item no. 1 was removed, then the total should
        // be $2.99.
        assertEquals(BigDecimal.valueOf(2.99), c.getTotal());

    }

    @Test
    public void remove_from_cart_invalid_user() throws Exception {
        // Create a modify cart request with an invalid user.
        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(1L);
        r.setQuantity(1);
        r.setUsername("boo");
        ResponseEntity<Cart> response = cartController.removeFromcart(r);

        // Verify that the "not found" status code has been returned.
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_invalid_item() throws Exception {
        // Create a modify cart request with an invalid item.
        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(2L);
        r.setQuantity(1);
        r.setUsername("test");
        ResponseEntity<Cart> response = cartController.removeFromcart(r);

        // Verify that the "not found" status code has been returned.
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }



}

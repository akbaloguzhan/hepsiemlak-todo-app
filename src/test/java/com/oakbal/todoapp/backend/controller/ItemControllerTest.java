package com.oakbal.todoapp.backend.controller;

import com.oakbal.todoapp.backend.model.Item;
import com.oakbal.todoapp.backend.service.ItemService;
import com.oakbal.todoapp.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createItem_Success() {
        Item item = new Item();
        item.setTitle("Test Item");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        when(userService.getUserIdByUsername("testuser")).thenReturn("user123");
        when(itemService.createItem(any(Item.class), anyString())).thenReturn(item);

        ResponseEntity<Item> response = itemController.createItem(item, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Item", response.getBody().getTitle());
    }

    @Test
    void getUserItems_Success() {
        Item item1 = new Item();
        item1.setId("item1");
        Item item2 = new Item();
        item2.setId("item2");
        List<Item> items = Arrays.asList(item1, item2);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        when(userService.getUserIdByUsername("testuser")).thenReturn("user123");
        when(itemService.getItemsByUserId("user123")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getUserItems(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getItemById_Success() {
        Item item = new Item();
        item.setId("item123");
        item.setTitle("Test Item");

        when(itemService.getItemById("item123")).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById("item123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("item123", response.getBody().getId());
        assertEquals("Test Item", response.getBody().getTitle());
    }

    @Test
    void getItemById_NotFound() {
        when(itemService.getItemById("nonexistent")).thenReturn(Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById("nonexistent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getItemsByUserId_Success() {
        Item item1 = new Item();
        item1.setId("item1");
        Item item2 = new Item();
        item2.setId("item2");
        List<Item> items = Arrays.asList(item1, item2);

        when(itemService.getItemsByUserId("user123")).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByUserId("user123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void updateItem_Success() {
        Item item = new Item();
        item.setId("item123");
        item.setTitle("Updated Item");

        when(itemService.updateItem(any(Item.class))).thenReturn(item);

        ResponseEntity<Item> response = itemController.updateItem("item123", item);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("item123", response.getBody().getId());
        assertEquals("Updated Item", response.getBody().getTitle());
    }

    @Test
    void deleteItem_Success() {
        doNothing().when(itemService).deleteItem("item123");

        ResponseEntity<Void> response = itemController.deleteItem("item123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemService, times(1)).deleteItem("item123");
    }

    @Test
    void createItem_WithInvalidUser() {
        Item item = new Item();
        item.setTitle("Test Item");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        when(userService.getUserIdByUsername("testuser")).thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> itemController.createItem(item, auth));
    }

    @Test
    void updateItem_WithNonExistingItem() {
        Item item = new Item();
        item.setId("nonexistent");
        item.setTitle("Updated Item");

        when(itemService.updateItem(any(Item.class))).thenThrow(new RuntimeException("Item not found"));

        assertThrows(RuntimeException.class, () -> itemController.updateItem("nonexistent", item));
    }

    @Test
    void deleteItem_WithNonExistingItem() {
        doThrow(new RuntimeException("Item not found")).when(itemService).deleteItem("nonexistent");

        assertThrows(RuntimeException.class, () -> itemController.deleteItem("nonexistent"));
    }
}
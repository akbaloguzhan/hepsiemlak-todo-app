package com.oakbal.todoapp.backend.service;

import com.oakbal.todoapp.backend.model.Item;
import com.oakbal.todoapp.backend.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createItem_Success() {
        Item item = new Item();
        item.setTitle("Test Item");
        String userId = "user123";

        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            savedItem.setId("item123");
            return savedItem;
        });

        Item result = itemService.createItem(item, userId);

        assertNotNull(result);
        assertEquals("item123", result.getId());
        assertEquals("Test Item", result.getTitle());
        assertEquals(userId, result.getUserId());
        assertEquals("Item", result.getType());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void getItemById_Success() {
        Item item = new Item();
        item.setId("item123");
        item.setTitle("Test Item");

        when(itemRepository.findById("item123")).thenReturn(Optional.of(item));

        Optional<Item> result = itemService.getItemById("item123");

        assertTrue(result.isPresent());
        assertEquals("item123", result.get().getId());
        assertEquals("Test Item", result.get().getTitle());
    }

    @Test
    void getItemById_NotFound() {
        when(itemRepository.findById("nonexistent")).thenReturn(Optional.empty());

        Optional<Item> result = itemService.getItemById("nonexistent");

        assertFalse(result.isPresent());
    }

    @Test
    void getItemsByUserId_Success() {
        Item item1 = new Item();
        item1.setId("item1");
        item1.setTitle("Item 1");
        Item item2 = new Item();
        item2.setId("item2");
        item2.setTitle("Item 2");

        when(itemRepository.findItemByUserId("user123")).thenReturn(Arrays.asList(item1, item2));

        List<Item> result = itemService.getItemsByUserId("user123");

        assertEquals(2, result.size());
        assertEquals("item1", result.get(0).getId());
        assertEquals("item2", result.get(1).getId());
    }

    @Test
    void updateItem_Success() {
        Item item = new Item();
        item.setId("item123");
        item.setTitle("Updated Item");

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = itemService.updateItem(item);

        assertNotNull(result);
        assertEquals("item123", result.getId());
        assertEquals("Updated Item", result.getTitle());
        assertEquals("Item", result.getType());
        assertNotNull(result.getUpdatedAt());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void deleteItem_Success() {
        doNothing().when(itemRepository).deleteById("item123");

        itemService.deleteItem("item123");

        verify(itemRepository, times(1)).deleteById("item123");
    }

    @Test
    void deleteItem_ItemNotFound() {
        doThrow(new RuntimeException("Item not found")).when(itemRepository).deleteById("nonexistent");

        assertThrows(RuntimeException.class, () -> itemService.deleteItem("nonexistent"));
    }
}
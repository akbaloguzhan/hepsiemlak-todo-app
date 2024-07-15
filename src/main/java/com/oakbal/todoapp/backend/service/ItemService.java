package com.oakbal.todoapp.backend.service;

import com.oakbal.todoapp.backend.model.Item;
import com.oakbal.todoapp.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(Item item, String userId) {
        item.setUserId(userId);
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        item.setType("Item");
        return itemRepository.save(item);
    }

    public Optional<Item> getItemById(String id) {
        return itemRepository.findById(id);
    }

    public List<Item> getItemsByUserId(String userId) {
        return itemRepository.findItemByUserId(userId);
    }

    public Item updateItem(Item item) {
        item.setUpdatedAt(LocalDateTime.now());
        item.setType("Item");
        return itemRepository.save(item);
    }

    public void deleteItem(String id) {
        itemRepository.deleteById(id);
    }
}

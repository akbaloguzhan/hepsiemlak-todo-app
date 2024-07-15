package com.oakbal.todoapp.backend.controller;

import com.oakbal.todoapp.backend.model.Item;
import com.oakbal.todoapp.backend.model.User;
import com.oakbal.todoapp.backend.service.ItemService;
import com.oakbal.todoapp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/items")
@RestController
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item, Authentication authentication) {
        String username = authentication.getName();
        String userId = userService.getUserIdByUsername(username);
        return ResponseEntity.ok(itemService.createItem(item, userId));
    }

    @GetMapping
    public ResponseEntity<List<Item>> getUserItems(Authentication authentication) {
        String userId = userService.getUserIdByUsername(authentication.getName());
        return ResponseEntity.ok(itemService.getItemsByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable String id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Item>> getItemsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(itemService.getItemsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable String id, @RequestBody Item item) {
        item.setId(id);
        return ResponseEntity.ok(itemService.updateItem(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }
}

package com.oakbal.todoapp.backend.repository;

import com.oakbal.todoapp.backend.model.Item;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.List;

public interface ItemRepository extends CouchbaseRepository<Item, String> {
    List<Item> findItemByUserId(String userId);
}

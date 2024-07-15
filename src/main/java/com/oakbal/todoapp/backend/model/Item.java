package com.oakbal.todoapp.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import java.time.LocalDateTime;

@Data
@Document
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    private String userId;

    @Field
    private String title;

    @Field
    private String description;

    @Field
    private boolean completed;

    @Field
    private LocalDateTime createdAt;

    @Field
    private LocalDateTime updatedAt;

    private String type = "Item";
}

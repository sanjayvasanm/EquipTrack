package com.equiptrack.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Category document for equipment classification
 */
@Document(collection = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String code;

    private String description;

    private String iconUrl;

    private String parentCategoryId;

    private Boolean isActive = true;

    private Integer displayOrder = 0;

    @CreatedDate
    private java.time.LocalDateTime createdAt;
}

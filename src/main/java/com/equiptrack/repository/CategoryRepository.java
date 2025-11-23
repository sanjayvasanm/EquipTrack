package com.equiptrack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.equiptrack.model.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    
    Optional<Category> findByName(String name);
    
    Optional<Category> findByCode(String code);
    
    List<Category> findByIsActiveTrue();
    
    List<Category> findByParentCategoryIdIsNull();
    
    List<Category> findByParentCategoryId(String parentId);
    
    @Query(value = "{ 'isActive': true }", sort = "{ 'displayOrder': 1 }")
    List<Category> findAllActiveOrderedByDisplayOrder();
    
    @Query(value = "{ 'parentCategoryId': null, 'isActive': true }", sort = "{ 'displayOrder': 1 }")
    List<Category> findMainCategoriesOrdered();
}

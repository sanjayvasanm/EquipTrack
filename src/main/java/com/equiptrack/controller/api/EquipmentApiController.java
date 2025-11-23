package com.equiptrack.controller.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.equiptrack.model.Equipment;
import com.equiptrack.service.EquipmentService;

import lombok.RequiredArgsConstructor;

/**
 * REST API controller for equipment operations
 */
@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EquipmentApiController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipment());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Equipment>> getAvailableEquipment() {
        return ResponseEntity.ok(equipmentService.getAvailableEquipment());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Equipment>> getFeaturedEquipment() {
        return ResponseEntity.ok(equipmentService.getFeaturedEquipment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable String id) {
        return equipmentService.getEquipmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Equipment>> searchEquipment(@RequestParam String keyword) {
        return ResponseEntity.ok(equipmentService.searchEquipment(keyword));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Equipment>> getByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(equipmentService.getAvailableEquipmentByCategory(categoryId));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<Equipment>> getByLocation(@PathVariable String locationId) {
        return ResponseEntity.ok(equipmentService.getEquipmentByLocation(locationId));
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Equipment>> getByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(equipmentService.getEquipmentByPriceRange(minPrice, maxPrice));
    }

    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment) {
        return ResponseEntity.ok(equipmentService.createEquipment(equipment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipment> updateEquipment(
            @PathVariable String id,
            @RequestBody Equipment equipment) {
        return ResponseEntity.ok(equipmentService.updateEquipment(id, equipment));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable String id,
            @RequestParam Equipment.EquipmentStatus status) {
        equipmentService.updateEquipmentStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable String id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.ok().build();
    }
}

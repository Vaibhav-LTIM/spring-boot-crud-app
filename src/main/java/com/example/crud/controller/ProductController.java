package com.example.crud.controller;

import com.example.crud.model.Product;
import com.example.crud.service.ProductFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ProductController {

    private final ProductFileService productFileService;

    public ProductController() {
        this.productFileService = new ProductFileService();
    }

    // GET /api/items: Retrieve all items
    @GetMapping
    public ResponseEntity<List<Product>> getAllItems() {
        List<Product> products = productFileService.readAll();
        return ResponseEntity.ok(products);
    }

    // GET /api/items/{id}: Retrieve an item by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getItemById(@PathVariable String id) {
        Optional<Product> product = productFileService.read(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST /api/items: Create a new item
    @PostMapping
    public ResponseEntity<String> createItem(@RequestBody Product newProduct) {
        productFileService.create(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully.");
    }

    // PUT /api/items/{id}: Update an existing item
    @PutMapping("/{id}")
    public ResponseEntity<String> updateItem(@PathVariable String id, @RequestBody Product updatedProduct) {
        boolean isUpdated = productFileService.update(id, updatedProduct);
        if (isUpdated) {
            return ResponseEntity.ok("Product updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
    }

    // DELETE /api/items/{id}: Delete an item
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable String id) {
        boolean isDeleted = productFileService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok("Product deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
    }
}
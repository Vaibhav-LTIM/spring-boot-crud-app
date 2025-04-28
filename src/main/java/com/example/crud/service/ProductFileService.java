package com.example.crud.service;

import com.example.crud.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ProductFileService {

    private static final String FILE_PATH = "src/main/resources/sample_data.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Method to read all products
    public List<Product> readAll() {
        return readProducts();
    }

    // Method to read a product by ID
    public Optional<Product> read(String id) {
        return readProducts().stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    // Method to create a new product
    public void create(Product newProduct) {
        List<Product> products = readProducts();
        products.add(newProduct);
        writeProducts(products);
    }

    // Method to update an existing product by ID
    public boolean update(String id, Product updatedProduct) {
        List<Product> products = readProducts();
        AtomicBoolean isUpdated = new AtomicBoolean(false);

        List<Product> updatedProducts = products.stream()
                .map(product -> {
                    if (product.getId().equals(id)) {
                        isUpdated.set(true);
                        return updatedProduct;
                    }
                    return product;
                })
                .collect(Collectors.toList());

        if (isUpdated.get()) {
            writeProducts(updatedProducts);
        }
        return isUpdated.get();
    }
    // Method to delete a product by ID
    public boolean delete(String id) {
        List<Product> products = readProducts();
        List<Product> filteredProducts = products.stream()
                .filter(product -> !product.getId().equals(id))
                .collect(Collectors.toList());

        if (filteredProducts.size() < products.size()) {
            writeProducts(filteredProducts);
            return true;
        }
        return false;
    }

    // Helper method to read products from the JSON file
    private List<Product> readProducts() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<List<Product>>() {});
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Helper method to write products to the JSON file
    private void writeProducts(List<Product> products) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
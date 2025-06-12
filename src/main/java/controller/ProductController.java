package controller;

import model.dto.product.ProductCreateDto;
import model.dto.product.ProductResponseDto;
import model.dto.product.UpdateProductDto;
import model.service.ProductService;

import java.util.List;
import java.util.Optional;

public class ProductController {
    private final ProductService productService;

    public ProductController() {
        this.productService = new ProductService();
    }

    public ProductResponseDto createProduct(ProductCreateDto createDto) {
        try {
            return productService.createProduct(createDto);
        } catch (Exception e) {
            throw new RuntimeException("Product creation failed: " + e.getMessage());
        }
    }

    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    public List<ProductResponseDto> searchProducts(String name) {
        return productService.searchProductsByName(name);
    }

    public Optional<ProductResponseDto> getProductById(String uuid) {
        return productService.getProductById(uuid);
    }

    public ProductResponseDto updateProduct(UpdateProductDto updateDto) {
        try {
            return productService.updateProduct(updateDto);
        } catch (Exception e) {
            throw new RuntimeException("Product update failed: " + e.getMessage());
        }
    }

    public void deleteProduct(String uuid) {
        try {
            productService.deleteProduct(uuid);
        } catch (Exception e) {
            throw new RuntimeException("Product deletion failed: " + e.getMessage());
        }
    }

    public void insertMillionProducts() {
        try {
            productService.insertMillionProducts();
        } catch (Exception e) {
            throw new RuntimeException("Million products insertion failed: " + e.getMessage());
        }
    }

    public void readMillionProducts() {
        try {
            productService.readMillionProducts();
        } catch (Exception e) {
            throw new RuntimeException("Million products reading failed: " + e.getMessage());
        }
    }
}

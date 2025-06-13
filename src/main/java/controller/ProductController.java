package controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.dto.product.ProductCreateDto;
import model.dto.product.ProductResponseDto;
import model.dto.product.UpdateProductDto;
import model.service.ProductService;
import model.service.ProductServiceImpl;

import java.util.List;
import java.util.Optional;
public class ProductController {
    private final ProductService productService;

    public ProductController() {
        this.productService = new ProductServiceImpl(); // Use implementation
    }

    public ProductResponseDto createProduct(ProductCreateDto createDto) {
        try {
            return productService.createProduct(createDto);
        } catch (Exception e) {
            throw new RuntimeException("Product creation failed: " + e.getMessage());
        }
    }

    public List<ProductResponseDto> getAllProducts() {
        try {
            return productService.getAllProducts();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve products: " + e.getMessage());
        }
    }

    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {
        try {
            return productService.getProductsByCategory(categoryId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve products by category: " + e.getMessage());
        }
    }

    public List<ProductResponseDto> searchProducts(String name) {
        try {
            return productService.searchProductsByName(name);
        } catch (Exception e) {
            throw new RuntimeException("Product search failed: " + e.getMessage());
        }
    }

    public Optional<ProductResponseDto> getProductById(String uuid) {
        try {
            return productService.getProductById(uuid);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve product: " + e.getMessage());
        }
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

    public ProductResponseDto restoreProduct(String uuid) {
        try {
            return productService.restoreProduct(uuid);
        } catch (Exception e) {
            throw new RuntimeException("Product restoration failed: " + e.getMessage());
        }
    }

    public List<ProductResponseDto> getAllProductsIncludingDeleted() {
        try {
            return productService.getAllProductsIncludingDeleted();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all products: " + e.getMessage());
        }
    }

    public void permanentlyDeleteProduct(String uuid) {
        try {
            productService.permanentlyDeleteProduct(uuid);
        } catch (Exception e) {
            throw new RuntimeException("Permanent deletion failed: " + e.getMessage());
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
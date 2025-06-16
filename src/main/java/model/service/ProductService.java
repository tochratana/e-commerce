package model.service;

import model.dto.product.ProductCreateDto;
import model.dto.product.ProductResponseDto;
import model.dto.product.UpdateProductDto;
import model.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponseDto createProduct(ProductCreateDto productCreateDto);

    List<ProductResponseDto> getAllProducts();

    List<ProductResponseDto> getProductsByCategory(Long categoryId);

    List<ProductResponseDto> searchProductsByName(String name);

    Optional<ProductResponseDto> getProductById(String uuid);

    ProductResponseDto updateProduct(UpdateProductDto updateProductDto);

    void deleteProduct(String uuid);

    ProductResponseDto restoreProduct(String uuid);

    List<ProductResponseDto> getAllProductsIncludingDeleted();

    void permanentlyDeleteProduct(String uuid);

    void insertMillionProducts();

    void readMillionProducts();
    Product getProductById(int productId);
}
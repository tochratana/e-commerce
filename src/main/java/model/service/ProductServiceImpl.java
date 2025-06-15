package model.service;

import mapper.ProductMapper;
import model.dto.product.ProductCreateDto;
import model.dto.product.ProductResponseDto;
import model.dto.product.UpdateProductDto;
import model.entities.Product;
import model.repositories.ProductRepository;
import utils.DatabaseConfig;
//import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(){
        this.productRepository = new ProductRepository();
        this.productMapper = new ProductMapper();
    }
    @Override
    public Product getProductById(int productId) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DatabaseConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {
                return new Product(
                        UUID.fromString(rs.getString("uuid")),     // if uuid is stored as VARCHAR in DB
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getLong("category_id"),
                        rs.getBoolean("is_deleted")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ProductResponseDto createProduct(ProductCreateDto productCreateDto){
        validateProductCreateDto(productCreateDto);

        Product product = productMapper.toEntity(productCreateDto);
        product.setUuid(UUID.randomUUID());

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    @Override
    public List<ProductResponseDto> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategory(categoryId).stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> searchProductsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllProducts();
        }

        return productRepository.findByNameStartingWith(name.trim()).stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponseDto> getProductById(String uuid) {
        return productRepository.findById(uuid)
                .map(productMapper::toResponseDto);
    }

    @Override
    public ProductResponseDto updateProduct(UpdateProductDto updateProductDto) {
        validateUpdateProductDto(updateProductDto);

        Optional<Product> existingProductOpt = productRepository.findById(updateProductDto.getUuid());
        if (existingProductOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found or has been deleted");
        }

        Product existingProduct = existingProductOpt.get();
        productMapper.updateProductFromDto(existingProduct, updateProductDto);

        Product updatedProduct = productRepository.update(existingProduct);
        return productMapper.toResponseDto(updatedProduct);
    }

    @Override
    public void deleteProduct(String uuid) {
        Optional<Product> productOpt = productRepository.findById(uuid);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found or already deleted");
        }

        productRepository.deleteById(uuid);
    }

    @Override
    public ProductResponseDto restoreProduct(String uuid) {
        productRepository.restoreById(uuid);

        List<Product> allProducts = productRepository.findAllIncludingDeleted();
        Optional<Product> restoredProduct = allProducts.stream()
                .filter(p -> p.getUuid().toString().equals(uuid))
                .findFirst();

        if (restoredProduct.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        return productMapper.toResponseDto(restoredProduct.get());
    }

    @Override
    public List<ProductResponseDto> getAllProductsIncludingDeleted() {
        return productRepository.findAllIncludingDeleted()
                .stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void permanentlyDeleteProduct(String uuid) {
        productRepository.hardDeleteById(uuid);
    }

    @Override
    public void insertMillionProducts() {
        System.out.println("Starting to insert 10 million products...");

        long startTime = System.currentTimeMillis();
        int batchSize = 10000;
        int totalProducts = 10_000_000;

        for (int batch = 0; batch < totalProducts / batchSize; batch++) {
            List<Product> products = new ArrayList<>();

            for (int i = 0; i < batchSize; i++) {
                int productNum = batch * batchSize + i + 1;
                Product product = new Product();
                product.setUuid(UUID.randomUUID());
                product.setName("Product " + productNum);
                product.setPrice(Math.random() * 1000 + 10);
                product.setQuantity((int)(Math.random() * 100) + 1);
                product.setCategoryId((long)(Math.random() * 5) + 1);
                product.setIsDeleted(false);
                products.add(product);
            }

            productRepository.batchInsert(products);

            if ((batch + 1) % 100 == 0) {
                System.out.printf("Inserted %d products (%.1f%%)%n",
                        (batch + 1) * batchSize,
                        ((double)(batch + 1) * batchSize / totalProducts) * 100);
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.printf("Successfully inserted 10 million products in %.2f seconds%n",
                (endTime - startTime) / 1000.0);
    }

    @Override
    public void readMillionProducts() {
        System.out.println("Starting to read all active products...");

        long startTime = System.currentTimeMillis();
        List<Product> products = productRepository.findAll();
        long endTime = System.currentTimeMillis();

        System.out.printf("Successfully read %d active products in %.2f seconds%n",
                products.size(), (endTime - startTime) / 1000.0);
    }

    private void validateProductCreateDto(ProductCreateDto createDto) {
        if (createDto.getName() == null || createDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (createDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        if (createDto.getQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative");
        }
        if (createDto.getCategoryId() == null || createDto.getCategoryId() <= 0) {
            throw new IllegalArgumentException("Valid category ID is required");
        }
    }

    private void validateUpdateProductDto(UpdateProductDto updateDto) {
        if (updateDto.getUuid() == null || updateDto.getUuid().trim().isEmpty()) {
            throw new IllegalArgumentException("Product UUID is required for update");
        }
        if (updateDto.getName() == null || updateDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (updateDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        if (updateDto.getQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative");
        }
        if (updateDto.getCategoryId() == null || updateDto.getCategoryId() <= 0) {
            throw new IllegalArgumentException("Valid category ID is required");
        }
    }
}
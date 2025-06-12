import model.dto.product.ProductCreateDto;
import model.dto.product.ProductResponseDto;
import model.dto.product.UpdateProductDto;
import model.entities.Product;
import model.service.ProductService;

import java.util.List;
import java.util.Optional;

public class Application {
    public static void main(String[] args) {
        ProductService productService = new ProductService();

        try {
            // Example 1: Create a product
            System.out.println("=== Creating a Product ===");
            ProductCreateDto createDto = new ProductCreateDto("iPhone 15", 999.99, 50, 1L);
            ProductResponseDto createdProduct = productService.createProduct(createDto);
            System.out.println("Created: " + createdProduct);

            // Example 2: Get all products
//            System.out.println("\n=== Getting All Products ===");
//            List<ProductResponseDto> allProducts = productService.getAllProducts();
//            allProducts.forEach(System.out::println);

            // Example 3: Search products by name
//            System.out.println("\n=== Searching Products ===");
//            List<ProductResponseDto> searchResults = productService.searchProductsByName("i");
//            searchResults.forEach(System.out::println);

            //! Error Example 4: Update a product
            System.out.println("\n=== Updating Product ===");
            UpdateProductDto updateDto = new UpdateProductDto(
                    createdProduct.getUuid(), "iPhone 15 Pro", 1199.99, 30, 1L);
            ProductResponseDto updatedProduct = productService.updateProduct(updateDto);
            System.out.println("Updated: " + updatedProduct);

            // Example 5: Soft delete a product
//            System.out.println("\n=== Soft Deleting Product ===");
//            productService.deleteProduct(createdProduct.getUuid());
//            System.out.println("Product soft deleted");

            // Example 6: Try to find deleted product (should return empty)
//            System.out.println("\n=== Trying to Find Deleted Product ===");
//            Optional<ProductResponseDto> deletedProduct = productService.getProductById(createdProduct.getUuid());
//            System.out.println("Found deleted product: " + deletedProduct.isPresent());

            // Example 7: Get all products including deleted ones
//            System.out.println("\n=== Getting All Products Including Deleted ===");
//            List<ProductResponseDto> allIncludingDeleted = productService.getAllProductsIncludingDeleted();
//            allIncludingDeleted.forEach(System.out::println);

            // Example 8: Restore deleted product
//            System.out.println("\n=== Restoring Product ===");
//            ProductResponseDto restoredProduct = productService.restoreProduct(createdProduct.getUuid());
//            System.out.println("Restored: " + restoredProduct);

            // Example 9: Performance test (uncomment if you want to test)
            // System.out.println("\n=== Performance Test ===");
            // productService.insertMillionProducts();
            // productService.readMillionProducts();

        } catch (Exception e) {
            System.err.println("Errordsfdsd: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
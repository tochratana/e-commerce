package mapper;

import model.dto.product.ProductCreateDto;
import model.dto.product.ProductResponseDto;
import model.dto.product.UpdateProductDto;
import model.entities.Product;

import java.util.UUID;

public class ProductMapper {

    public Product toEntity(ProductCreateDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategoryId(dto.getCategoryId());
        product.setIsDeleted(false);
        return product;
    }

    public ProductResponseDto toResponseDto(Product product) {
        return new ProductResponseDto(
                product.getUuid().toString(), // Convert UUID to String
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                "Category " + product.getCategoryId(), // Simple category name mapping
                product.getIsDeleted()
        );
    }

    public void updateProductFromDto(Product existingProduct, UpdateProductDto dto) {
        existingProduct.setName(dto.getName());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setQuantity(dto.getQuantity());
        existingProduct.setCategoryId(dto.getCategoryId());
    }
}
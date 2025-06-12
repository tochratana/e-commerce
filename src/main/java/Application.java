import model.dto.product.ProductCreateDto;
import model.dto.product.ProductResponseDto;
import model.dto.product.UpdateProductDto;
import view.ProductServer;

public class Application {
    public static void main(String[] args) {
        ProductServer productServer = new ProductServer();
        productServer.start();
    }
}
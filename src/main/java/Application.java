
import controller.AddToCartController;
import model.repositories.CartItemRepository;
import model.repositories.ProductRepository;
import model.service.AddToCartService;
import model.service.AddToCartServiceImpl;
import view.ProductServer;

public class Application {
    public static void main(String[] args) {
//        ProductServer productServer = new ProductServer();
//        productServer.start();
        ProductRepository productRepository = new ProductRepository();
        CartItemRepository cartRepo = new CartItemRepository();
        AddToCartService cartService = new AddToCartServiceImpl(productRepository, cartRepo);
        AddToCartController controller = new AddToCartController(cartService);

        ProductServer productServer = new ProductServer(controller, productRepository);
        productServer.start();

    }
}

<<<<<<< HEAD
import controller.AddToCartController;
import model.repositories.CartItemRepository;
import model.repositories.ProductRepository;
import model.service.AddToCartService;
import model.service.AddToCartServiceImpl;
=======
>>>>>>> ea167f60145c06217b4caf77bda66c11e501a775
import view.ProductServer;
import view.UserUI;

public class Application {
    public static void main(String[] args) {
<<<<<<< HEAD
//        ProductServer productServer = new ProductServer();
//        productServer.start();
        ProductRepository productRepository = new ProductRepository();
        CartItemRepository cartRepo = new CartItemRepository();
        AddToCartService cartService = new AddToCartServiceImpl(productRepository, cartRepo);
        AddToCartController controller = new AddToCartController(cartService);

        ProductServer productServer = new ProductServer(controller, productRepository);
        productServer.start();

=======
        UserUI.home();
//        ProductServer productServer = new ProductServer();
//        productServer.start();
>>>>>>> ea167f60145c06217b4caf77bda66c11e501a775
    }
}
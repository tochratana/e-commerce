package model.service;

import lombok.AllArgsConstructor;
import model.entities.CardItem;
import model.entities.Order;
import model.entities.OrderItem;
import model.entities.Product;
import model.repositories.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.valueOf;


@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository  orderItemRepository;
    private final CardItemRepository  cardItemRepository;
    private final ProductRepository productRepository;


    @Override
    public void createOrder(Integer userId) {
        Optional<CardItem> cardItems = cardItemRepository.findById(userId);

        if(cardItems.isEmpty()){
            System.out.println("❌ No items in cart.");
            return;
        }

        //Create Order
        Order order = new Order();
        order.setOrderCode("ORD-" + UUID.randomUUID().toString().substring(0, 8));
        order.setUserId(userId);
        order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));


        //Get order to order item

        CardItem cardItem = cardItems.get();
        Optional<Product> product = productRepository.findById(valueOf(cardItem.getId()));

        // Calculate and SetOrderItem


        // Print Recept Order
        // 5. Print summary
        System.out.println("✅ Order placed successfully!");
        System.out.println("📦 Order Code: " + order.getOrderCode());
        System.out.println("📅 Date: " + order.getOrderDate());
        System.out.println("🧾 Total Price: $" );
        System.out.println("🛒 Number of Products: " );
    }
}

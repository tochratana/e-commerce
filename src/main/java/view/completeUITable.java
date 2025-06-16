package view;

import model.dto.order.OrderDTO;
import model.dto.order.OrderItemDto;
import model.dto.product.ProductResponseDto;
import model.entities.Cart;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import java.util.List;
import static view.UIComponents.*;

public class completeUITable<T> {

    private static final BorderStyle border = BorderStyle.UNICODE_BOX_DOUBLE_BORDER;
    private static final CellStyle center = new CellStyle(CellStyle.HorizontalAlign.CENTER);

    public static String LoginMenuUI() {
        Table table = new Table(1, border, ShownBorders.ALL);
        Table table1 = new Table(1, border, ShownBorders.SURROUND);
        Table table2 = new Table(1);
        table.setColumnWidth(0,50,100);
        table1.setColumnWidth(0,50,100);
        table2.setColumnWidth(0,50,100);
        table.addCell(GREEN + "PRODUCT INVENTORY SYSTEM" +RESET, center);
        table2.addCell(BLUE + "USER CREATION" + RESET, center);

        String[] menuItems = {
            "                    1. Register" ,
            "                    2. Login",
            "                    3. Quit",
        };

        for (String item : menuItems) {
            table1.addCell(item);
        }


        return table.render() + "\n" + table2.render() + "\n" + table1.render();
    }
    public static String showMainMenuUI(){
        Table table = new Table(1);
        Table menuTable = new Table(1, border, ShownBorders.SURROUND);


        table.setColumnWidth(0,50,100);
        menuTable.setColumnWidth(0,50,100);

        table.addCell(BLUE + "MAIN MENU" + RESET, center);

        String[] menuItems = {
                "               1. User Management" ,
                "               2. Product Management",
                "               3. Order Management" ,
                "               4. Logout",
                "               5. Exit"
        };

        for (String item : menuItems) {
            menuTable.addCell(item);
        }

        return table.render()  + "\n" + menuTable.render();
    }
    public static String showUserMenuUI(){
        Table table = new Table(1);
        Table menuTable = new Table(1, border, ShownBorders.SURROUND);

        table.setColumnWidth(0,50,100);
        menuTable.setColumnWidth(0,50,100);

        table.addCell(PURPLE+ "USER MANAGEMENT"+ RESET, center);

        String[] menuItems = {
                "               1. View All Users",
                "               2. Create User",
                "               3. Update User",
                "               4. Find User by UUID",
                "               5. Delete User",
                "               6. Back to Main Menu"
        };

        for (String item : menuItems) {
            menuTable.addCell(item);
        }

        return table.render() +"\n" + menuTable.render();
    }
    public static String showProductMenuUI(){
        Table table = new Table(1);
        Table menuTable = new Table(1, border, ShownBorders.SURROUND);

        table.setColumnWidth(0,50,100);
        menuTable.setColumnWidth(0,50,100);

        table.addCell( CYAN+"PRODUCT MANAGEMENT MENU"+RESET, center);

        String[] menuItems = {
                " 1. Create Product",
                " 2. View All Products",
                " 3. View Product by ID",
                " 4. Search Products by Name",
                " 5. View Products by Category",
                " 6. Update Product",
                " 7. Delete Product",
                " 8. Insert Million Products (Performance Test)",
                " 9. Read Million Products (Performance Test)",
                " 10. Back to Main Menu"
        };

        for (String item : menuItems) {
            menuTable.addCell(item);
        }

        return table.render() +"\n" + menuTable.render();
    }
    public static String showOrderMenuUI() {
        Table header = new Table(1);
        Table menu = new Table(1, border, ShownBorders.SURROUND);

        header.setColumnWidth(0, 50, 100);
        menu.setColumnWidth(0, 50, 100);

        header.addCell(YELLOW+"ORDER MENAGEMENT"+RESET, center);

        String[] options = {
                "                1. Place Order",
                "                2. View All Orders",
                "                3. View Order Detail",
                "                4. Cancel Order",
                "                5. Add Item to Cart",
                "                6. View Cart",
                "                7. Back to Main Menu"
        };

        for (String option : options) {
            menu.addCell(option);
        }

        return header.render() + "\n" + menu.render();
    }
    public static String displayProduct(ProductResponseDto product) {
        // Create a single-column table with borders
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

        // Set fixed width to match your original box size
        table.setColumnWidth(0, 55, 55); // Fixed width of 55 characters

        // Add product details with the exact same formatting
        table.addCell(String.format("UUID: %s", product.getUuid()));
        table.addCell(String.format("Name: %s", product.getName()));
        table.addCell(String.format("Price: $%.2f", product.getPrice()));
        table.addCell(String.format("Quantity: %d", product.getQuantity()));
        table.addCell(String.format("Category: %s", product.getCategoryName()));
        table.addCell(String.format("Status: %s", product.getIsDeleted() ? "Deleted" : "Active"));

        // Get the rendered table
        String tableString = table.render();

        // Color only the outer borders (top, bottom, left, right)
        String[] lines = tableString.split("\n");
        StringBuilder result = new StringBuilder();

        if (lines.length > 1) {
            // Color top border
            result.append(BLUE).append(lines[0]).append(RESET).append("\n");

            // Color side borders for content lines
            for (int i = 1; i < lines.length - 1; i++) {
                result.append(BLUE).append("│").append(RESET)
                        .append(lines[i].substring(1, lines[i].length() - 1))
                        .append(BLUE).append("│").append(RESET)
                        .append("\n");
            }

            // Color bottom border
            result.append(BLUE).append(lines[lines.length - 1]).append(RESET);
        }

        return result.toString();
    }
    public static String printReceiptUI(OrderDTO order) {

    Table  table = new Table(1, border, ShownBorders.SURROUND);
    table.setColumnWidth(0, 50, 100); // optional, for nice width

    table.addCell(YELLOW+"ORDER RECEIPT"+RESET, center);
    table.addCell("---------------------------------------------------");
    table.addCell("Order Code  : " + order.orderCode());
    table.addCell("Order Date  : " + order.orderDate());
    table.addCell("---------------------------------------------------");
    // Column headers
    table.addCell(String.format("%-20s %5s %15s", "Product", "Qty", "Price"),center);

    // Items
    for (OrderItemDto item : order.items()) {
        table.addCell(String.format("%-20s %5d %15.2f",
                item.productName(), item.quantity(), item.productPrice()), center);
    }

    table.addCell("---------------------------------------------------");
    table.addCell(String.format("Total Quantity: %d", order.totalQuantity()));
    table.addCell(String.format("Total Price   : $%.2f", order.totalPrice()));
    table.addCell("===================================================");
    return table.render();
    }
    public static String cartItemUI(List<Cart> cartItems) {

        Table table = new Table(2, border, ShownBorders.SURROUND);
        CellStyle center = new CellStyle(CellStyle.HorizontalAlign.CENTER);

        // Set column widths to fit full UUID
        table.setColumnWidth(0, 40, 80); // UUID can be long
        table.setColumnWidth(1, 10, 20); // Quantity is short

//        table.addCell(YELLOW+"CART ITEM"+RESET, center);
        table.addCell("\u001B[1;33mProduct UUID\u001B[0m");
        table.addCell("\u001B[1;33mQuantity\u001B[0m", center);

        for (Cart cart : cartItems) {
            table.addCell(cart.getProductId()); // UUID as String
            table.addCell(String.valueOf(cart.getQuantity()), center);
        }

        return table.render();
    }
    public static String orderDetailUI(OrderDTO order) {

        Table table = new Table(3, border, ShownBorders.SURROUND);
        CellStyle center = new CellStyle(CellStyle.HorizontalAlign.CENTER);

        // Set custom column widths for better UUID and price display
        table.setColumnWidth(0, 20, 30); // Product Name
        table.setColumnWidth(1, 10, 15); // Price
        table.setColumnWidth(2, 10, 10); // Quantity

        // Header
        table.addCell("\u001B[1;33mProduct\u001B[0m");
        table.addCell("\u001B[1;33mPrice\u001B[0m", center);
        table.addCell("\u001B[1;33mQuantity\u001B[0m", center);

        // Order Items
        for (OrderItemDto item : order.items()) {
            table.addCell(item.productName());
            table.addCell(String.format("%.2f", item.productPrice()), center);
            table.addCell(String.valueOf(item.quantity()), center);
        }

        String header = String.format(
                "\u001B[1;36mOrder Code\u001B[0m: %s\n\u001B[1;36mDate\u001B[0m      : %s\n\u001B[1;36mTotal\u001B[0m     : $%.2f\n",
                order.orderCode(), order.orderDate(), order.totalPrice()
        );

        return  table.render() + "\n" + header;
    }

}

package view;

import model.dto.UserResponseDto;
import model.dto.product.ProductResponseDto;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;


import java.util.List;

import static view.Color.*;

public class completeUITable<T> {
    private Table table;
    private String [] columnNames;
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
            "                    1. Login" ,
            "                    2. Register",
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
                "               4. Exit",
                "               5. Logout"
        };

        for (String item : menuItems) {
            menuTable.addCell(item);
        }

        return table.render()  + "\n" + menuTable.render();
    }

    //todo (this one back to menu not work)
    public static String showProductMenuUI(){
        Table table = new Table(1, border);
        Table menuTable = new Table(1, border, ShownBorders.SURROUND);

        table.setColumnWidth(0,70,100);
        menuTable.setColumnWidth(0,70,100);

        table.addCell(BLUE +"PRODUCT MANAGEMENT MENU"+RESET, center);

        String[] menuItems = {
                "             1. Create Product",
                "             2. View All Products",
                "             3. View Product by ID",
                "             4. Search Products by Name",
                "             5. View Products by Category",
                "             6. Update Product",
                "             7. Delete Product",
                "             8. Insert Million Products (Performance Test)",
                "             9. Read Million Products (Performance Test)",
                "             10. Exit ",
                "             11. Back to Main Menu"
        };

        for (String item : menuItems) {
            menuTable.addCell(item);
        }

        return table.render() +"\n" + menuTable.render();
    }
    //
    public String getUserDisplay(List<T> tList){
        if (tList == null || tList.isEmpty()) {
            return "No users found";
        }

        // Setup table for UserResponseDto
        if (tList.get(0) instanceof UserResponseDto) {
            table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
            columnNames = new String[]{"UUID", "Username", "Email"};

            // Add headers
            for (String column : columnNames) {
                table.addCell(column, center);
            }

            // Add user data
            for (T item : tList) {
                UserResponseDto user = (UserResponseDto) item;
                table.addCell(user.uuid(), center);
                table.addCell(user.username(), center);
                table.addCell(user.email(), center);
//                table.addCell(user.createdDate().toString(), center);
            }

            // Set column widths
            for (int i = 0; i < columnNames.length; i++) {
                table.setColumnWidth(i, 20, 30);
            }
        }

        return table.render();
    }
    //todo getOrderDisplay
    /*
    public String getOrderDisplay(List<OrderDto> orders) {
        if (orders.isEmpty()) return "No orders found";

        table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String[] headers = {"Order Code", "Date", "Total", "Status"};

        // Add headers
        for (String h : headers) {
            table.addCell(h, center);
        }

        // Add orders
        for (OrderDto order : orders) {
            table.addCell(order.orderCode(), center);
            table.addCell(order.orderDate().toString(), center);
            table.addCell(String.format("$%.2f", order.totalPrice()), center);
            table.addCell("Completed", center); // Assuming all orders are completed
        }

        return table.render();
    }



    public String getOrderItemDisplay(List<OrderItemDto> items) {
        if (items.isEmpty()) return "No items in this order";

        table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String[] headers = {"Product", "Price", "Quantity", "Subtotal"};

        // Add headers
        for (String h : headers) {
            table.addCell(h, center);
        }

        // Add items
        for (OrderItemDto item : items) {
            ProductResponseDto product = productController.getProductById(item.productId().toString());
            String productName = product != null ? product.name() : "Unknown Product";
            double subtotal = item.price() * item.quantity();

            table.addCell(productName, center);
            table.addCell(String.format("$%.2f", item.price()), center);
            table.addCell(String.valueOf(item.quantity()), center);
            table.addCell(String.format("$%.2f", subtotal), center);
        }

        return table.render();
    }

     */
    //todo getCartDisplay
    /*
    public String getCartDisplay(List<CartItem> cartItems, List<ProductResponseDto> products) {
        if (cartItems.isEmpty()) {
            return "Your cart is empty";
        }

        // Create table with 5 columns
        table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

        // Set column headers
        String[] headers = {"No.", "Product Name", "Price", "Quantity", "Subtotal"};
        for (String header : headers) {
            table.addCell(header, center);
        }

        double total = 0;

        // Add cart items
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            ProductResponseDto product = products.stream()
                    .filter(p -> p.uuid().equals(item.getProductId().toString()))
                    .findFirst()
                    .orElse(null);

            if (product != null) {
                double subtotal = product.price() * item.getQuantity();
                total += subtotal;

                table.addCell(String.valueOf(i + 1), center);
                table.addCell(product.name(), center);
                table.addCell(String.format("$%.2f", product.price()), center);
                table.addCell(String.valueOf(item.getQuantity()), center);
                table.addCell(String.format("$%.2f", subtotal), center);
            }
        }

        // Add total row
        table.addCell("TOTAL", new CellStyle(CellStyle.HorizontalAlign.RIGHT), 4);
        table.addCell(String.format("$%.2f", total), center);

        return table.render();
    }

     */


    public static String showOrderMenuUI() {
        Table header = new Table(1, border);
        Table menu = new Table(1, border, ShownBorders.SURROUND);

        header.setColumnWidth(0, 60, 80);
        menu.setColumnWidth(0, 60, 80);

        header.addCell("ORDER MENU", center);

        String[] options = {
                "    1. Place Order",
                "    2. View All Orders",
                "    3. View Order Detail",
                "    4. Cancel Order",
                "    5. Add Item to Cart",
                "    6. View Cart",
                "    7. Back to Main Menu"
        };

        for (String option : options) {
            menu.addCell(option);
        }

        return header.render() + "\n" + menu.render();
    }
    public static String showUserMenuUI(){
        Table table = new Table(1, border);
        Table menuTable = new Table(1, border, ShownBorders.SURROUND);

        table.setColumnWidth(0,70,100);
        menuTable.setColumnWidth(0,70,100);

        table.addCell("USER MANAGEMENT", center);

        String[] menuItems = {
                "                1. View All Users",
                "                2. Create User",
                "                3. Update User",
                "                4. Find User by UUID",
                "                5. Delete User",
                "                5. Back to Main Menu"
        };

        for (String item : menuItems) {
            menuTable.addCell(item);
        }

        return table.render() +"\n" + menuTable.render();
    }

}

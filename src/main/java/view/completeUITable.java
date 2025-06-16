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
        table2.addCell(GREEN + "PRODUCT INVENTORY SYSTEM" +RESET, center);
        table.addCell(BLUE + "USER CREATION" + RESET, center);

        String[] menuItems = {
                "                    1. Register" ,
                "                    2. Login",
                "                    3. Quit",
        };

        for (String item : menuItems) {
            table1.addCell(item);
        }


        return table2.render() + "\n" + table.render() + "\n" + table1.render();
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

        Table menuTable = new Table(1, border, ShownBorders.SURROUND);

        menuTable.setColumnWidth(0,50,100);
        menuTable.setColumnWidth(0,50,100);

        menuTable.addCell(PURPLE+ "USER MANAGEMENT"+ RESET, center);

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

        return  menuTable.render();
    }
    public static String showProductMenuUI(){

        Table menuTable = new Table(1, border, ShownBorders.SURROUND);

        menuTable.setColumnWidth(0,50,100);
        menuTable.setColumnWidth(0,50,100);

        menuTable.addCell( CYAN+"PRODUCT MANAGEMENT MENU"+RESET, center);

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

        return menuTable.render();
    }
    public static String showOrderMenuUI() {
        Table menu = new Table(1, border, ShownBorders.SURROUND);

        menu.setColumnWidth(0, 50, 100);
        menu.setColumnWidth(0, 50, 100);

        menu.addCell(YELLOW+"ORDER MENAGEMENT"+RESET, center);

        String[] options = {
                "                1. Add Item to Cart",
                "                2. View Cart  ",
                "                3. Place Order  ",
                "                4. View All Orders  ",
                "                5. View Order Detail ",
                "                6. Cancel Order     ",
                "                7. Back to Main Menu"
        };

        for (String option : options) {
            menu.addCell(option);
        }

        return menu.render();
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
        table.addCell(BLUE+"---------------------------------------------------"+RESET);
        table.addCell("Order Code  : " + order.orderCode());
        table.addCell("Order Date  : " + order.orderDate());
        table.addCell(BLUE+"---------------------------------------------------"+RESET);
        // Column headers
        table.addCell(String.format("%-20s %5s %15s", "Product", "Qty", "Price"),center);

        // Items
        for (OrderItemDto item : order.items()) {
            table.addCell(String.format("%-20s %5d %15.2f",
                    item.productName(), item.quantity(), item.productPrice()), center);
        }

        table.addCell(BLUE+"---------------------------------------------------"+RESET);
        table.addCell(String.format("Total Quantity: %d", order.totalQuantity()));
        table.addCell(String.format("Total Price   : $%.2f", order.totalPrice()));
        table.addCell(BLUE+"==================================================="+RESET);
        return table.render();
    }
    public static String orderDetailUI(OrderDTO order) {
        Table table = new Table(3, border, ShownBorders.SURROUND);

        String header = String.format(
                "\u001B[1;36mOrder Code\u001B[0m: %s\n\u001B[1;36mDate\u001B[0m      : %s\n\u001B[1;36mTotal\u001B[0m     : $%.2f\n",
                order.orderCode(), order.orderDate(), order.totalPrice()
        );
        return  table.render() + "\n" + header;
    }

}

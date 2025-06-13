package view;

import model.dto.UserResponseDto;
import model.dto.product.ProductResponseDto;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;

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
        table.addCell("PRODUCT INVENTORY SYSTEM", center);
        table2.addCell("USER CREATION", center);

        String[] menuItems = {
                "                    1. Login",
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

        table.addCell("MAIN MENU", center);

        String[] menuItems = {
                "               1. User Management",
                "               2. Product Management",
                "               3. Order Management",
                "               4. Logout",
                "               5. Exit"
        };

        for (String item : menuItems) {
            menuTable.addCell(item);
        }

        return table.render()  + "\n" + menuTable.render();
    }
    public static String showProductMenuUI(){
        Table table = new Table(1, border);
        Table menuTable = new Table(1, border, ShownBorders.SURROUND);

        table.setColumnWidth(0,70,100);
        menuTable.setColumnWidth(0,70,100);

        table.addCell("PRODUCT MANAGEMENT MENU", center);

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
                "             11. Back to Main Menu",

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

    public static String showOrderMenuUI(){
        Table table = new Table(1, border);
        Table menuTable = new Table(1, border, ShownBorders.SURROUND);

        table.setColumnWidth(0,70,100);
        menuTable.setColumnWidth(0,70,100);

        table.addCell("PRODUCT MANAGEMENT MENU", center);

        String[] menuItems = {
                "               1.Add Product to Cart ",
                "               2.View My Cart ",
                "               3.Place Order  ",
                "               4. ",
                "               5. Back to Main Menu",
        };

        for (String item : menuItems) {
            menuTable.addCell(item);
        }

        return table.render() +"\n" + menuTable.render();
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
    public static String showCartMenuUI(){
        Table table = new Table(1, border);
        Table menuTable = new Table(1, border, ShownBorders.SURROUND);

        table.setColumnWidth(0,70,100);
        menuTable.setColumnWidth(0,70,100);

        table.addCell("CART MANAGEMENT", center);

        String[] menuItems = {
                "                1. Add Product to Cart ",
                "                2. View My Cart",
                "                3. Back to Main Menu"
        };

        for (String item : menuItems) {
            menuTable.addCell(item);
        }

        return table.render() +"\n" + menuTable.render();
    }


}

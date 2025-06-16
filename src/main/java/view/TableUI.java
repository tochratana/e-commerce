package view;

import model.dto.CartItemCreateDto;
import model.dto.CartItemDisplayDto;
import model.dto.order.OrderDTO;
import model.dto.order.OrderItemDto;
import model.dto.product.ProductResponseDto;
import model.dto.user.UserResponseDto;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;

public class TableUI<T> {
    private Table table;
    private String[] columnNames;
    private final CellStyle center = new CellStyle(CellStyle.HorizontalAlign.CENTER);

    // ANSI color codes for styling headers (bold yellow)
    private static final String HEADER_COLOR = "\u001B[1;33m"; // Bold yellow
    private static final String RESET_COLOR = "\u001B[0m";

    public void getTableDisplay(List<T> tList) {
        if (tList == null || tList.isEmpty()) {
            System.out.println("No data to display.");
            return;
        }

        T firstItem = tList.get(0);

        // ✅ Use simple ASCII border for full compatibility
        BorderStyle borderStyle = BorderStyle.CLASSIC_WIDE;

        if (firstItem instanceof ProductResponseDto) {
            table = new Table(6, borderStyle, ShownBorders.ALL);
            columnNames = new String[]{"UUID", "Name", "Price", "Quantity", "Category", "Status"};
        } else if (firstItem instanceof UserResponseDto) {
            table = new Table(3, borderStyle, ShownBorders.ALL);
            columnNames = new String[]{"UUID", "User Name", "Email"};
        } else if (firstItem instanceof OrderItemDto) {
            table = new Table(4, borderStyle, ShownBorders.ALL);
            columnNames = new String[]{"ORDER ID", "Product Name", "Price", "Quantity"};
        } else if (firstItem instanceof CartItemDisplayDto) {
            table = new Table(3, borderStyle, ShownBorders.ALL);
            columnNames = new String[]{"Product UUID", "Product Name", "Quantity"};

        }else if (firstItem instanceof OrderDTO) {
            table = new Table(5, borderStyle, ShownBorders.ALL);
            columnNames = new String[]{"Order ID", "Order Code", "Order Date", "Total Items", "Total Price"};
        }

        else {
            System.out.println("Unsupported data type.");
            return;
        }

        // ✅ Add headers with color
        for (String column : columnNames) {
            table.addCell(HEADER_COLOR + column + RESET_COLOR, center);
        }

        // ✅ Add data rows
        for (T t : tList) {
            if (t instanceof ProductResponseDto dto) {
                table.addCell(dto.getUuid(), center);
                table.addCell(dto.getName(), center);
                table.addCell(String.valueOf(dto.getPrice()), center);
                table.addCell(String.valueOf(dto.getQuantity()), center);
                table.addCell(dto.getCategoryName(), center);
                table.addCell(String.valueOf(dto.getIsDeleted()), center);
            } else if (t instanceof UserResponseDto dto) {
                table.addCell(dto.uuid().toString(), center);
                table.addCell(dto.username(), center);
                table.addCell(dto.email(), center);
            } else if (t instanceof OrderItemDto dto) {
                table.addCell(String.valueOf(dto.orderId()), center);
                table.addCell(dto.productName(), center);
                table.addCell(String.valueOf(dto.productPrice()), center);
                table.addCell(String.valueOf(dto.quantity()), center);
            } else if (t instanceof CartItemDisplayDto dto) {
                table.addCell(dto.productUuid(),center);
                table.addCell(dto.productName(), center);
                table.addCell(String.valueOf(dto.quantity()), center);

            }else if (t instanceof OrderDTO dto) {
                table.addCell(String.valueOf(dto.id()), center);
                table.addCell(dto.orderCode(), center);
                table.addCell(dto.orderDate().toString(), center); // format date if needed
                table.addCell(String.valueOf(dto.totalQuantity()), center);
                table.addCell(String.format("%.2f", dto.totalPrice()), center);
            }

        }

        System.out.println(table.render());
    }
}

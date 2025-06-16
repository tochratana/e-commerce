package view;

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

    public void getTableDisplay(List<T> tList) {
        // ✅ Handle null or empty list
        if (tList == null || tList.isEmpty()) {
            System.out.println("⚠️ No data to display.");
            return;
        }

        // ✅ Safe access using get(0), not getFirst()
        T firstItem = tList.get(0);

        // ✅ Identify type and set up columns
        if (firstItem instanceof ProductResponseDto) {
            table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
            columnNames = new String[]{"UUID", "Name", "Price", "Quantity", "Category", "Status"};
        } else if (firstItem instanceof UserResponseDto) {
            table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
            columnNames = new String[]{"UUID", "User Name", "Email"};
        } else if (firstItem instanceof OrderItemDto) {
            table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
            columnNames = new String[]{"ORDER ID", "Product Name", "Price", "Quantity"};
        } else {
            System.out.println("⚠️ Unsupported data type.");
            return;
        }

        // ✅ Add headers
        for (String column : columnNames) {
            table.addCell(column, center);
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
            }
        }

        // ✅ Print the table
        System.out.println(table.render());
    }
}

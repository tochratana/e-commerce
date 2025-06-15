package view;
import model.dto.product.ProductResponseDto;
import model.dto.user.UserResponseDto;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;

public class TableUI<T> {
    private Table table;
    private String [] columnNames;
    private final CellStyle center = new CellStyle(CellStyle.HorizontalAlign.CENTER);
    public void getTableDisplay(List<T> tList){
        if(tList.getFirst() instanceof ProductResponseDto){
            table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER,
                    ShownBorders.ALL);
            columnNames= new String[] {"UUID","Name","Price","Quantity","Category","Status"};

        }
        // TODO for users
        if(tList.getFirst() instanceof UserResponseDto){
            table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER,
                    ShownBorders.ALL);
            columnNames= new String[] {"UUID","User Name","Email"};
        }
        for(String column: columnNames){
            table.addCell(column,center);
        }
        for(T t: tList){
            if(t instanceof ProductResponseDto){
                table.addCell(((ProductResponseDto) t).getUuid(),center);
                table.addCell(((ProductResponseDto )t).getName(),center);
                table.addCell(String.valueOf(((ProductResponseDto)t).getPrice()),center);
                table.addCell(String.valueOf(((ProductResponseDto)t).getQuantity()),center);
                table.addCell(((ProductResponseDto )t).getCategoryName(),center);
                table.addCell(((ProductResponseDto )t).getIsDeleted().toString(),center);
            }
            // TODO for users
            if(t instanceof UserResponseDto){
                table.addCell(((UserResponseDto)t).uuid().toString(), center);
                table.addCell(((UserResponseDto)t).username(), center);
                table.addCell(((UserResponseDto)t).email(), center);

            }
        }
//        for(int i =0;i< columnNames.length;i++){
//            table.setColumnWidth(i, 40,45);
//        }
        System.out.println(table.render());
    }
}
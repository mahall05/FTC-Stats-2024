package Other;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Core.Utilities;

public class Data {
    private XSSFWorkbook workbook;
    public Data(){
        workbook = Utilities.getWorkbookFromFile("Data.xlsx");
    }
}

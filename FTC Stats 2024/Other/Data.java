package Other;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Core.Utilities;

public class Data {
    private XSSFWorkbook workbook;
    private XSSFSheet dataSheet;
    public Data(){
        workbook = Utilities.getWorkbookFromFile("Red Team Data.xlsx");
        dataSheet = workbook.getSheet("Data");

        printData();
    }

    private void printData(){
        for(int i = 0; i <= dataSheet.getLastRowNum(); i++){
            if(dataSheet.getRow(i)!=null){
                System.out.println(dataSheet.getRow(i).getCell(1).getStringCellValue());
            }
        }
    }
}

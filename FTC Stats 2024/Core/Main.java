package Core;


import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Other.Data;


public class Main{
    private XSSFWorkbook workbook;

    private Data data;

    public Main(){
        workbook = Utilities.getWorkbookFromFile(Settings.fileName);
        data = new Data(workbook);
    }

    public static void main(String[] args){
        new Main();
    }
}
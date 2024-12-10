package Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.awt.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Other.Data.Entry;
import Team.TeamMember;


public class Utilities {
    /**
     * Get an XSSFWorkbook file (a class that contains all the information from an Excel file) from a provided file name
     * @param fileName The name of the file to look for. Include the extension
     * @return a full XSSFWorkbook with all contained data
     */
    public static XSSFWorkbook getWorkbookFromFile(String fileName)
    {
        try{
            // Get the file input
            FileInputStream file = new FileInputStream(fileName);
            // Create a blank workbook from that file
            XSSFWorkbook wb = new XSSFWorkbook(file);
            file.close();
            return wb;
        }catch (FileNotFoundException e)
        {
            System.out.println("Error loading file: Could not find a file with name\""+fileName+"\" or was open with another program");
            return null;
        }catch(IOException e){
            System.out.println("Error loading file: An I/O error occurred");
            return null;
        }
    }

    /**
     * Write the provided workbook to the Excel file with name 'fileName'
     * @param fileName The name of the Excel file to look for. Include the extension
     * @param wb The workbook to be saved to the file
     */
    public static void writeWorkbookToSpreadsheet(String fileName, XSSFWorkbook wb){
        try{
            File file = new File(fileName);
            FileOutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.close();
            System.out.println("Successfully written");
        }catch(FileNotFoundException e){
            System.out.println("Error writing to file: Could not find a file with name \""+fileName+"\" or was open in another program");
        }catch(IOException e){
            System.out.println("Error writing to file: An I/O error eccorred");
        }
    }

    public static XSSFSheet getSheetFromWorkbook(XSSFWorkbook wb, String sheetName){
        XSSFSheet sheet = wb.getSheet(sheetName);
        if(sheet == null){
            sheet = wb.createSheet(sheetName);
        }
        return sheet;
    }

    public static Row getRowFromSheet(XSSFSheet sheet, int rowNum){
        Row row = sheet.getRow(rowNum);
        if(row == null){
            row = sheet.createRow(rowNum);
        }
        return row;
    }
    public static Cell getCellFromRow(Row row, int cellNum){
        Cell cell = row.getCell(cellNum);
        if(cell == null){
            cell = row.createCell(cellNum);
        }
        return cell;
    }

    public static void writeDatamapToSheet(int offset, XSSFSheet sheet, Map<Integer, ArrayList<Double>> sheetMap){
        for(int i = 0; i < sheetMap.size()+100; i++){
            if(sheetMap.get(i) == null){
                continue;
            }
            Row row = (sheet.getRow(i+offset)==null?sheet.createRow(i+offset):sheet.getRow(i+offset));
            writeDataToRow(row, sheetMap.get(i));
        }
    }
    
    public static void writeDataToRow(Row row, ArrayList<Double> d){
        for(int i = 0; i < d.size(); i++){
            Cell cell = (row.getCell(i+2) == null ? row.createCell(i+2) : row.getCell(i+2));
            cell.setCellValue(d.get(i));
        }
    }

    public static void launchSpreadsheet(String fileName){
        Desktop d = Desktop.getDesktop();
        try{
            d.open(new File(fileName));
        }catch(Exception e){
            System.out.println("Error opening file");
        }
    }

    public static ArrayList<Double> arrayToList(double[]... array){
        ArrayList<Double> list = new ArrayList<Double>();
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i].length; j++){
                list.add(array[i][j]);
            }
        }
        return list;
    }

    public static String dateToString(Date date){
        String dateString = date.toString();
        return dateString.substring(4,7) + '-' + dateString.substring(8,10) + '-' + dateString.substring(24, 28);
    }
}

package Core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

}

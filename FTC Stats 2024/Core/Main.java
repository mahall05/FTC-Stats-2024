package Core;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Other.Data;
import Team.Team;
import Team.TeamMember;


public class Main{
    private XSSFWorkbook workbook;

    private Team team;

    public Main(){
        workbook = Utilities.getWorkbookFromFile(Settings.fileName);
        team = new Team(workbook, new TeamMember[] {new TeamMember("Cas", workbook, 0),
                                        new TeamMember("Zoe", workbook, 1),
                                        new TeamMember("Ben", workbook, 0),
                                        new TeamMember("Lucas", workbook, 0),
                                        new TeamMember("Max", workbook, 1),
                                        new TeamMember("Jillian", workbook, 0),
                                        new TeamMember("Hailey", workbook, 1),
                                        new TeamMember("Keller", workbook, 0),
                                        new TeamMember("Maddie", workbook, 2),
                                        new TeamMember("Caleb", workbook, 2),
                                        new TeamMember("Matt", workbook, 2),
                                        new TeamMember("Alan", workbook, 2),
                                        new TeamMember("Emily", workbook, 3)});
    
        Utilities.writeWorkbookToSpreadsheet(Settings.fileName, workbook);
    }

    public static void main(String[] args){
        new Main();
    }
}
package Core;


import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Other.Data;
import Team.Team;
import Team.TeamMember;


public class Main{
    private XSSFWorkbook workbook;

    private Team team;

    public Main(){
        workbook = Utilities.getWorkbookFromFile(Settings.fileName);
        team = new Team(workbook, new TeamMember[] {new TeamMember("Cas", workbook),
                                        new TeamMember("Zoe", workbook),
                                        new TeamMember("Ben", workbook),
                                        new TeamMember("Lucas", workbook),
                                        new TeamMember("Max", workbook),
                                        new TeamMember("Jillian", workbook),
                                        new TeamMember("Hailey", workbook),
                                        new TeamMember("Keller", workbook),
                                        new TeamMember("Maddie", workbook),
                                        new TeamMember("Caleb", workbook),
                                        new TeamMember("Matt", workbook),
                                        new TeamMember("Alan", workbook)});
    
        Utilities.writeWorkbookToSpreadsheet(Settings.fileName, workbook);
    }

    public static void main(String[] args){
        new Main();
    }
}
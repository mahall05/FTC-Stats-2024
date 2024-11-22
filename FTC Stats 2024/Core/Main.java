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
        team = new Team(workbook, new TeamMember[] {new TeamMember("Cas"),
                                        new TeamMember("Zoe"),
                                        new TeamMember("Ben"),
                                        new TeamMember("Lucas"),
                                        new TeamMember("Max"),
                                        new TeamMember("Jillian"),
                                        new TeamMember("Hailey"),
                                        new TeamMember("Keller"),
                                        new TeamMember("Maddie"),
                                        new TeamMember("Caleb"),
                                        new TeamMember("Matt"),
                                        new TeamMember("Alan")});
    }

    public static void main(String[] args){
        new Main();
    }
}
package Team;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Core.Settings;
import Core.Utilities;
import Other.Data;
//21-22-23
import Other.Data.Entry;

public class Team extends Group{
    private Group drivers;
    private Group specialists;
    private Group coaches;
    private Group humans;
    public static Date firstDay = Utilities.getCellFromRow(Utilities.getRowFromSheet(Utilities.getSheetFromWorkbook(Utilities.getWorkbookFromFile(Settings.fileName), "Data"), 2), 0).getDateCellValue();

    public Team(XSSFWorkbook wb, TeamMember[] members){
        super(members, wb);

        ArrayList<TeamMember> drivers = new ArrayList<TeamMember>();
        ArrayList<TeamMember> specialists = new ArrayList<TeamMember>();
        ArrayList<TeamMember> coaches = new ArrayList<TeamMember>();
        ArrayList<TeamMember> humans = new ArrayList<TeamMember>();

        drivers.add(find(members, "Cas"));
        drivers.add(find(members, "Ben"));
        drivers.add(find(members, "Lucas"));
        drivers.add(find(members, "Jillian"));
        drivers.add(find(members, "Keller"));
        drivers.add(find(members, "Matt"));
        drivers.add(find(members, "Maddie"));

        specialists.add(find(members, "Zoe"));
        specialists.add(find(members, "Max"));
        specialists.add(find(members, "Hailey"));
        specialists.add(find(members, "Caleb"));

        coaches.add(find(members, "Maddie"));
        coaches.add(find(members, "Caleb"));
        coaches.add(find(members, "Matt"));
        coaches.add(find(members, "Alan"));

        humans.add(find(members, "Cas"));
        humans.add(find(members, "Ben"));
        humans.add(find(members, "Lucas"));
        humans.add(find(members, "Matt"));
        humans.add(find(members, "Maddie"));
        humans.add(find(members, "Zoe"));
        humans.add(find(members, "Caleb"));
        humans.add(find(members, "Emily"));

        this.drivers = new Group(drivers.toArray(), wb);
        this.specialists = new Group(specialists.toArray(), wb);
        this.coaches = new Group(coaches.toArray(), wb);
        this.humans = new Group(humans.toArray(), wb);

        for(Data.Entry e : entries){
            if(e.getDriver()!=null){
                if(find(this.drivers.getMembers(), e.getDriver())!=null){
                    find(this.drivers.getMembers(), e.getDriver()).addDriverMatch(e);
                }
            }
            if(e.getSpecialist()!=null){
                if(find(this.specialists.getMembers(), e.getSpecialist())!=null){
                    find(this.specialists.getMembers(), e.getSpecialist()).addSpecialistMatch(e);
                }
            }
            if(e.getCoach()!=null){
                if(find(this.coaches.getMembers(), e.getCoach())!=null){
                    find(this.coaches.getMembers(), e.getCoach()).addCoachMatch(e);
                }
            }
            if(e.getHuman()!=null){
                if(find(this.humans.getMembers(), e.getHuman())!=null){
                    find(this.humans.getMembers(), e.getHuman()).addHumanMatch(e);
                }
            }
        }
        
        Map<Integer, ArrayList<Double>> dataMap = new HashMap<Integer, ArrayList<Double>>();
        for(int i = 0; i < dataArray.length; i++){
            dataMap.put(i, Utilities.arrayToList(dataArray[i]));
        }
        
        Utilities.writeDatamapToSheet(3, Utilities.getSheetFromWorkbook(wb, "Team"), dataMap);
        for(TeamMember m : members){
            m.calcData();
        }

        for(int i = 0; i < entries.size(); i++){
            Row row = Utilities.getRowFromSheet(Utilities.getSheetFromWorkbook(wb, "Data"), i+2);
            row.createCell(21).setCellValue((int) ((entries.get(i).getDate().getTime()-Team.firstDay.getTime()) / (24.0 * 60.0 * 60.0 * 1000.0) + 1));
            if(entries.get(i).getTeleopStrategy()==null){

            }else if(entries.get(i).getTeleopStrategy().equals("Samples")){
                row.createCell(22).setCellValue(entries.get(i).getTeleopSamplesScored());
            }else if(entries.get(i).getTeleopStrategy().equals("Specimens")){
                row.createCell(23).setCellValue(entries.get(i).getTeleopSpecimensScored());
            }
        }

        for(int i = 0; i < 56; i++){
            Row row = Utilities.getRowFromSheet(Utilities.getSheetFromWorkbook(wb, "Data"), i+2);
            row.createCell(25).setCellValue(i);

            if(i< (int) (((new Date()).getTime()-Team.firstDay.getTime()) / (24.0 * 60.0 * 60.0 * 1000.0) + 1)){
                ArrayList<Entry> sampleList = new ArrayList<Entry>();
                ArrayList<Entry> specimenList = new ArrayList<Entry>();

                for(Entry e : entries){
                    int day = (int) ((e.getDate().getTime()-Team.firstDay.getTime()) / (24.0 * 60.0 * 60.0 * 1000.0)) + 1;

                    if(day < i){
                        if(e.getTeleopStrategy()!=null && e.getTeleopStrategy().equals("Samples")){
                            sampleList.add(e);
                        }else if(e.getTeleopStrategy()!=null && e.getTeleopStrategy().equals("Specimens")){
                            specimenList.add(e);
                        }
                    }
                }
                double sampleAvg=0, specAvg=0;

                try{
                    sampleAvg = Data.calcMean(sampleList, Entry::getTeleopSamplesScored, sampleList.get(sampleList.size()-1).getDate());
                }catch(IndexOutOfBoundsException e){
                }
                try{
                    specAvg = Data.calcMean(specimenList, Entry::getTeleopSpecimensScored, specimenList.get(specimenList.size()-1).getDate());
                }catch(IndexOutOfBoundsException e){

                }

                row.createCell(26).setCellValue(sampleAvg);
                row.createCell(27).setCellValue(specAvg);
            }
        }

        this.drivers.calcComparisonData("Drivers", 0);
        this.specialists.calcComparisonData("Specialists", 1);
        this.coaches.calcComparisonData("Coaches", 2);
        this.humans.calcComparisonData("Human Players", 3);
    }

    public TeamMember find(TeamMember[] list, String name){
        for(TeamMember m : list){
            if(m.getName().equals(name)){
                return m;
            }
        }
        return null;
    }
    public TeamMember find(ArrayList<TeamMember> list, String name){
        for(TeamMember m : list){
            if(m.getName().equals(name)){
                return m;
            }
        }
        return null;
    }

    
}

package Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Core.Settings;
import Core.Utilities;
import Other.Data;

public class Team extends Group{
    private Group drivers;
    private Group specialists;
    private Group coaches;
    private Group humans;


    public Team(XSSFWorkbook wb, TeamMember[] members){
        super(members, wb);

        ArrayList<TeamMember> drivers = new ArrayList<TeamMember>();
        ArrayList<TeamMember> specialists = new ArrayList<TeamMember>();
        ArrayList<TeamMember> coaches = new ArrayList<TeamMember>();
        ArrayList<TeamMember> humans = new ArrayList<TeamMember>();

        drivers.add(find(members, "Cas"));
        drivers.add(find(members, "Ben"));
        drivers.add(find(members, "Lucas"));
        drivers.add(find(members, "Matt"));
        drivers.add(find(members, "Jillian"));
        drivers.add(find(members, "Maddie"));
        drivers.add(find(members, "Keller"));

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
        dataMap.put(0, Utilities.arrayToList(net));
        dataMap.put(1, Utilities.arrayToList(lowBasket));
        dataMap.put(2, Utilities.arrayToList(highBasket));
        dataMap.put(3, Utilities.arrayToList(lowChamber));
        dataMap.put(4, Utilities.arrayToList(highChamber));
        dataMap.put(5, Utilities.arrayToList(endgamePoints));
        dataMap.put(6, Utilities.arrayToList(autoPoints));
        dataMap.put(7, Utilities.arrayToList(totalPoints));
        dataMap.put(8, Utilities.arrayToList(teleopPoints));
        dataMap.put(9, Utilities.arrayToList(piecesScored));
        dataMap.put(10, Utilities.arrayToList(autoSamplesScored));
        dataMap.put(11, Utilities.arrayToList(autoSpecimensScored));
        dataMap.put(12, Utilities.arrayToList(teleopSamplesScored));
        dataMap.put(13, Utilities.arrayToList(teleopSpecimensScored));
        
        Utilities.writeDatamapToSheet(3, Utilities.getSheetFromWorkbook(wb, "Team"), dataMap);
        for(TeamMember m : members){
            m.calcData();
        }
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

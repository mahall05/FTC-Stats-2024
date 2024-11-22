package Team;

import java.util.ArrayList;

import Other.Data;

public class TeamMember {
    private String name;

    private ArrayList<Data.Entry> driverMatches;
    private ArrayList<Data.Entry> specialistMatches;
    private ArrayList<Data.Entry> coachMatches;
    private ArrayList<Data.Entry> humanMatches;

    public TeamMember(String name){
        this.name=name;

        driverMatches = new ArrayList<Data.Entry>();
        specialistMatches = new ArrayList<Data.Entry>();
        coachMatches = new ArrayList<Data.Entry>();
        humanMatches = new ArrayList<Data.Entry>();
    }

    public String getName(){
        return name;
    }

    public void addDriverMatch(Data.Entry e){
        driverMatches.add(e);
    }
    public void addSpecialistMatch(Data.Entry e){
        specialistMatches.add(e);
    }
    public void addCoachMatch(Data.Entry e){
        coachMatches.add(e);
    }
    public void addHumanMatch(Data.Entry e){
        humanMatches.add(e);
    }
}

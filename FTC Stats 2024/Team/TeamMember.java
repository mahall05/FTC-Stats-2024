package Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Core.Settings;
import Core.Utilities;
import Other.Data.Entry;
import Other.Data;

public class TeamMember {
    private String name;

    private XSSFWorkbook wb;

    private ArrayList<Entry> driverMatches;
    private ArrayList<Entry> specialistMatches;
    private ArrayList<Entry> coachMatches;
    private ArrayList<Entry> humanMatches;

    private ArrayList<ArrayList<Entry>> matches;

    private int primaryType;

    // Net, Low Basket, High Basket, Low Chamber, High Chamber, Endgame, Auto Points, Total Points, Pieces Scored, Auto Samples, Auto Specimens, Teleop Samples, Teleop Specimens

    protected double[][][] data = new double[14][4][2];

    public TeamMember(String name, XSSFWorkbook wb, int primaryType){
        this.primaryType = primaryType;
        this.name=name;
        this.wb=wb;

        matches = new ArrayList<ArrayList<Entry>>();

        driverMatches = new ArrayList<Entry>();
        specialistMatches = new ArrayList<Entry>();
        coachMatches = new ArrayList<Entry>();
        humanMatches = new ArrayList<Entry>();

        matches.add(driverMatches);
        matches.add(specialistMatches);
        matches.add(coachMatches);
        matches.add(humanMatches);
    }

    public String getName(){
        return name;
    }

    public void addDriverMatch(Entry e){
        driverMatches.add(e);
    }
    public void addSpecialistMatch(Entry e){
        specialistMatches.add(e);
    }
    public void addCoachMatch(Entry e){
        coachMatches.add(e);
    }
    public void addHumanMatch(Entry e){
        humanMatches.add(e);
    }
    public double[][][] getData(){
        return data;
    }

    public void calcData(){
        for(int j = 0; j < 4; j++){
            for(int i = 0; i <= 11; i++){
                data[i][j] = new double[] {Data.calcMean(matches.get(j), Entry.getData(i)), Data.calcStdDev(matches.get(j), Entry.getData(i))};
            }
    
            ArrayList<Entry> sampleMatches = new ArrayList<Entry>();
            ArrayList<Entry> specimenMatches = new ArrayList<Entry>();
            for(Entry e : matches.get(j)){
                if(e.getTeleopStrategy()!=null){
                    if(e.getTeleopStrategy().equals("Samples")){
                        sampleMatches.add(e);
                    }else if(e.getTeleopStrategy().equals("Specimens")){
                        specimenMatches.add(e);
                    }
                }
            }
    
            data[12][j] = new double[] {Data.calcMean(sampleMatches, Data.Entry::getEntryTeleopSamplesScored), Data.calcStdDev(sampleMatches, Data.Entry::getEntryTeleopSamplesScored)};
            data[13][j] = new double[] {Data.calcMean(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored), Data.calcStdDev(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored)};
        }

        Map<Integer, ArrayList<Double>> dataMap = new HashMap<Integer, ArrayList<Double>>();
        for(int i = 0; i <=13 ; i++){
            dataMap.put(i, Utilities.arrayToList(data[i]));
        }

        dataMap.put(14, new ArrayList<Double>());
        dataMap.put(15, new ArrayList<Double>());

        for(int i = 0; i<=13; i++){
            dataMap.put(i+16, Utilities.arrayToList(Data.calc5NS(matches.get(primaryType), Data.Entry.getData(i))));
        }

        Utilities.writeDatamapToSheet(3, Utilities.getSheetFromWorkbook(wb, name), dataMap);
    }
}

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

    // Net, Low Basket, High Basket, Low Chamber, High Chamber, Endgame, Auto Points, Total Points, Pieces Scored, Auto Samples, Auto Specimens, Teleop Samples, Teleop Specimens

    protected double[][][] data = new double[14][4][2];

    /*
    protected double[][] net = new double[4][2];
    protected double[][] lowBasket = new double[4][2];
    protected double[][] highBasket = new double[4][2];
    protected double[][] lowChamber = new double[4][2];
    protected double[][] highChamber = new double[4][2];
    protected double[][] endgamePoints = new double[4][2];
    protected double[][] autoPoints = new double[4][2];
    protected double[][] totalPoints = new double[4][2];
    protected double[][] teleopPoints = new double[4][2];
    protected double[][] piecesScored = new double[4][2];
    protected double[][] autoSamplesScored = new double[4][2];
    protected double[][] autoSpecimensScored = new double[4][2];
    protected double[][] teleopSamplesScored = new double[4][2];
    protected double[][] teleopSpecimensScored = new double[4][2];
    */

    public TeamMember(String name, XSSFWorkbook wb){
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

    private void sort(ArrayList<Entry> parent, Consumer<Entry> sampleConsumer, Consumer<Entry> specimenConsumer){
        for(Entry e : parent){
            if(e.getTeleopStrategy()!=null){
                if(e.getTeleopStrategy().equals("Samples")){
                    sampleConsumer.accept(e);
                }else if(e.getTeleopStrategy().equals("Specimens")){
                    specimenConsumer.accept(e);
                }
            }
        }
    }

    public void singleTypeData(int type){
        for(int i = 0; i <= 11; i++){
            data[i][type] = new double[] {Data.calcMean(matches.get(type), Entry.getData(i)), Data.calcStdDev(matches.get(type), Entry.getData(i))};
        }

        ArrayList<Entry> sampleMatches = new ArrayList<Entry>();
        ArrayList<Entry> specimenMatches = new ArrayList<Entry>();
        for(Entry e : matches.get(type)){
            if(e.getTeleopStrategy()!=null){
                if(e.getTeleopStrategy().equals("Samples")){
                    sampleMatches.add(e);
                }else if(e.getTeleopStrategy().equals("Specimens")){
                    specimenMatches.add(e);
                }
            }
        }

        data[12][type] = new double[] {Data.calcMean(sampleMatches, Data.Entry::getEntryTeleopSamplesScored), Data.calcStdDev(sampleMatches, Data.Entry::getEntryTeleopSamplesScored)};
        data[13][type] = new double[] {Data.calcMean(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored), Data.calcStdDev(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored)};
    }

    public void calcData(){
        for(int i = 0; i < 4; i++){
            singleTypeData(i);
        }

        Map<Integer, ArrayList<Double>> dataMap = new HashMap<Integer, ArrayList<Double>>();
        for(int i = 0; i <=13 ; i++){
            dataMap.put(i, Utilities.arrayToList(data[i]));
        }

        Utilities.writeDatamapToSheet(3, Utilities.getSheetFromWorkbook(wb, name), dataMap);
    }
}

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
        net[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryNet), Data.calcStdDev(matches.get(type), Data.Entry::getEntryNet)};
        lowBasket[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryLowBasket), Data.calcStdDev(matches.get(type), Data.Entry::getEntryLowBasket)};
        highBasket[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryHighBasket), Data.calcStdDev(matches.get(type), Data.Entry::getEntryHighBasket)};
        lowChamber[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryLowChamber), Data.calcStdDev(matches.get(type), Data.Entry::getEntryLowChamber)};
        highChamber[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryHighChamber), Data.calcStdDev(matches.get(type), Data.Entry::getEntryHighChamber)};
        endgamePoints[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryEndgamePoints), Data.calcStdDev(matches.get(type), Data.Entry::getEntryEndgamePoints)};
        autoPoints[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryAutoPoints), Data.calcStdDev(matches.get(type), Data.Entry::getEntryAutoPoints)};
        totalPoints[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryTotalPoints), Data.calcStdDev(matches.get(type), Data.Entry::getEntryTotalPoints)};
        teleopPoints[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryTeleopPoints), Data.calcStdDev(matches.get(type), Data.Entry::getEntryTeleopPoints)};
        piecesScored[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryPiecesScored), Data.calcStdDev(matches.get(type), Data.Entry::getEntryPiecesScored)};
        autoSamplesScored[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryAutoSamplesScored), Data.calcStdDev(matches.get(type), Data.Entry::getEntryAutoSamplesScored)};
        autoSpecimensScored[type] = new double[] {Data.calcMean(matches.get(type), Data.Entry::getEntryAutoSpecimensScored), Data.calcStdDev(matches.get(type), Data.Entry::getEntryAutoSpecimensScored)};

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

        teleopSamplesScored[type] = new double[] {Data.calcMean(sampleMatches, Data.Entry::getEntryTeleopSamplesScored), Data.calcStdDev(sampleMatches, Data.Entry::getEntryTeleopSamplesScored)};
        teleopSpecimensScored[type] = new double[] {Data.calcMean(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored), Data.calcStdDev(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored)};
    }

    public void calcData(){
        for(int i = 0; i < 4; i++){
            singleTypeData(i);
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
        

        Utilities.writeDatamapToSheet(3, Utilities.getSheetFromWorkbook(wb, name), dataMap);
    }
}

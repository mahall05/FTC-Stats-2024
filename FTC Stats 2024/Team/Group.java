package Team;

import java.util.ArrayList;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Other.Data;

public class Group{
    protected TeamMember[] members;
    protected ArrayList<Data.Entry> entries;
    protected Data data;

    // Net, Low Basket, High Basket, Low Chamber, High Chamber, Endgame, Auto Points, Total Points, Teleop Points, Pieces Scored, Auto Samples, Auto Specimens, Teleop Samples, Teleop Specimens

    protected double[] net;
    protected double[] lowBasket;
    protected double[] highBasket;
    protected double[] lowChamber;
    protected double[] highChamber;
    protected double[] endgamePoints;
    protected double[] autoPoints;
    protected double[] totalPoints;
    protected double[] teleopPoints;
    protected double[] piecesScored;
    protected double[] autoSamplesScored;
    protected double[] autoSpecimensScored;
    protected double[] teleopSamplesScored;
    protected double[] teleopSpecimensScored;
    
    public Group(Object[] members, XSSFWorkbook wb){
        this.members = new TeamMember[members.length];
        data = new Data(wb);
        entries = data.getEntries();

        for(int i = 0; i < members.length; i++){
            this.members[i] = (TeamMember) members[i];
        }

        net = new double[] {Data.calcMean(entries, Data.Entry::getEntryNet), Data.calcStdDev(entries, Data.Entry::getEntryNet)};
        lowBasket = new double[] {Data.calcMean(entries, Data.Entry::getEntryLowBasket), Data.calcStdDev(entries, Data.Entry::getEntryLowBasket)};
        highBasket = new double[] {Data.calcMean(entries, Data.Entry::getEntryHighBasket), Data.calcStdDev(entries, Data.Entry::getEntryHighBasket)};
        lowChamber = new double[] {Data.calcMean(entries, Data.Entry::getEntryLowChamber), Data.calcStdDev(entries, Data.Entry::getEntryLowChamber)};
        highChamber = new double[] {Data.calcMean(entries, Data.Entry::getEntryHighChamber), Data.calcStdDev(entries, Data.Entry::getEntryHighChamber)};
        endgamePoints = new double[] {Data.calcMean(entries, Data.Entry::getEntryEndgamePoints), Data.calcStdDev(entries, Data.Entry::getEntryEndgamePoints)};
        autoPoints = new double[] {Data.calcMean(entries, Data.Entry::getEntryAutoPoints), Data.calcStdDev(entries, Data.Entry::getEntryAutoPoints)};
        totalPoints = new double[] {Data.calcMean(entries, Data.Entry::getEntryTotalPoints), Data.calcStdDev(entries, Data.Entry::getEntryTotalPoints)};
        teleopPoints = new double[] {Data.calcMean(entries, Data.Entry::getEntryTeleopPoints), Data.calcStdDev(entries, Data.Entry::getEntryTeleopPoints)};
        piecesScored = new double[] {Data.calcMean(entries, Data.Entry::getEntryPiecesScored), Data.calcStdDev(entries, Data.Entry::getEntryPiecesScored)};
        autoSamplesScored = new double[] {Data.calcMean(entries, Data.Entry::getEntryAutoSamplesScored), Data.calcStdDev(entries, Data.Entry::getEntryAutoSamplesScored)};
        autoSpecimensScored = new double[] {Data.calcMean(entries, Data.Entry::getEntryAutoSpecimensScored), Data.calcStdDev(entries, Data.Entry::getEntryAutoSpecimensScored)};

        ArrayList<Data.Entry> sampleMatches = new ArrayList<Data.Entry>();
        ArrayList<Data.Entry> specimenMatches = new ArrayList<Data.Entry>();

        for(Data.Entry e : entries){
            if(e.getTeleopStrategy()!=null){
                if(e.getTeleopStrategy().equals("Samples")){
                    sampleMatches.add(e);
                }else if(e.getTeleopStrategy().equals("Specimens")){
                    specimenMatches.add(e);
                }
            }
        }

        teleopSamplesScored = new double[] {Data.calcMean(sampleMatches, Data.Entry::getEntryTeleopSamplesScored), Data.calcStdDev(sampleMatches, Data.Entry::getEntryTeleopSamplesScored)};
        teleopSpecimensScored = new double[] {Data.calcMean(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored), Data.calcStdDev(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored)};
    }

    private void calcComparisonData(){
        ArrayList<Double> data = new ArrayList<Double>();

        /*for(int i = 0; i < members.length; i++){
            data.add(members[i].)
        }*/
    }

    public TeamMember[] getMembers(){
        return members;
    }
}

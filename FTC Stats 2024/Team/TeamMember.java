package Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Core.Settings;
import Core.Utilities;
import Other.Data;

public class TeamMember {
    private String name;

    private XSSFWorkbook wb;

    private ArrayList<Data.Entry> driverMatches;
    private ArrayList<Data.Entry> specialistMatches;
    private ArrayList<Data.Entry> coachMatches;
    private ArrayList<Data.Entry> humanMatches;

    protected double[] driverNet;
    protected double[] driverLowBasket;
    protected double[] driverHighBasket;
    protected double[] driverLowChamber;
    protected double[] driverHighChamber;
    protected double[] driverEndgamePoints;
    protected double[] driverAutoPoints;
    protected double[] driverTotalPoints;
    protected double[] driverTeleopPoints;
    protected double[] driverPiecesScored;
    protected double[] driverAutoSamplesScored;
    protected double[] driverAutoSpecimensScored;
    protected double[] driverTeleopSamplesScored;
    protected double[] driverTeleopSpecimensScored;

    protected double[] specialistNet;
    protected double[] specialistLowBasket;
    protected double[] specialistHighBasket;
    protected double[] specialistLowChamber;
    protected double[] specialistHighChamber;
    protected double[] specialistEndgamePoints;
    protected double[] specialistAutoPoints;
    protected double[] specialistTotalPoints;
    protected double[] specialistTeleopPoints;
    protected double[] specialistPiecesScored;
    protected double[] specialistAutoSamplesScored;
    protected double[] specialistAutoSpecimensScored;
    protected double[] specialistTeleopSamplesScored;
    protected double[] specialistTeleopSpecimensScored;

    protected double[] coachNet;
    protected double[] coachLowBasket;
    protected double[] coachHighBasket;
    protected double[] coachLowChamber;
    protected double[] coachHighChamber;
    protected double[] coachEndgamePoints;
    protected double[] coachAutoPoints;
    protected double[] coachTotalPoints;
    protected double[] coachTeleopPoints;
    protected double[] coachPiecesScored;
    protected double[] coachAutoSamplesScored;
    protected double[] coachAutoSpecimensScored;
    protected double[] coachTeleopSamplesScored;
    protected double[] coachTeleopSpecimensScored;

    protected double[] humanNet;
    protected double[] humanLowBasket;
    protected double[] humanHighBasket;
    protected double[] humanLowChamber;
    protected double[] humanHighChamber;
    protected double[] humanEndgamePoints;
    protected double[] humanAutoPoints;
    protected double[] humanTotalPoints;
    protected double[] humanTeleopPoints;
    protected double[] humanPiecesScored;
    protected double[] humanAutoSamplesScored;
    protected double[] humanAutoSpecimensScored;
    protected double[] humanTeleopSamplesScored;
    protected double[] humanTeleopSpecimensScored;

    public TeamMember(String name, XSSFWorkbook wb){
        this.name=name;
        this.wb=wb;

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

    public void calcData(){
        driverNet = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryNet), Data.calcStdDev(driverMatches, Data.Entry::getEntryNet)};
        driverLowBasket = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryLowBasket), Data.calcStdDev(driverMatches, Data.Entry::getEntryLowBasket)};
        driverHighBasket = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryHighBasket), Data.calcStdDev(driverMatches, Data.Entry::getEntryHighBasket)};
        driverLowChamber = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryLowChamber), Data.calcStdDev(driverMatches, Data.Entry::getEntryLowChamber)};
        driverHighChamber = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryHighChamber), Data.calcStdDev(driverMatches, Data.Entry::getEntryHighChamber)};
        driverEndgamePoints = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryEndgamePoints), Data.calcStdDev(driverMatches, Data.Entry::getEntryEndgamePoints)};
        driverAutoPoints = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryAutoPoints), Data.calcStdDev(driverMatches, Data.Entry::getEntryAutoPoints)};
        driverTotalPoints = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryTotalPoints), Data.calcStdDev(driverMatches, Data.Entry::getEntryTotalPoints)};
        driverTeleopPoints = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryTeleopPoints), Data.calcStdDev(driverMatches, Data.Entry::getEntryTeleopPoints)};
        driverPiecesScored = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryPiecesScored), Data.calcStdDev(driverMatches, Data.Entry::getEntryPiecesScored)};
        driverAutoSamplesScored = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryAutoSamplesScored), Data.calcStdDev(driverMatches, Data.Entry::getEntryAutoSamplesScored)};
        driverAutoSpecimensScored = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryAutoSpecimensScored), Data.calcStdDev(driverMatches, Data.Entry::getEntryAutoSpecimensScored)};
        driverTeleopSamplesScored = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryTeleopSamplesScored), Data.calcStdDev(driverMatches, Data.Entry::getEntryTeleopSamplesScored)};
        driverTeleopSpecimensScored = new double[] {Data.calcMean(driverMatches, Data.Entry::getEntryTeleopSpecimensScored), Data.calcStdDev(driverMatches, Data.Entry::getEntryTeleopSpecimensScored)};

        specialistNet = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryNet), Data.calcStdDev(specialistMatches, Data.Entry::getEntryNet)};
        specialistLowBasket = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryLowBasket), Data.calcStdDev(specialistMatches, Data.Entry::getEntryLowBasket)};
        specialistHighBasket = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryHighBasket), Data.calcStdDev(specialistMatches, Data.Entry::getEntryHighBasket)};
        specialistLowChamber = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryLowChamber), Data.calcStdDev(specialistMatches, Data.Entry::getEntryLowChamber)};
        specialistHighChamber = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryHighChamber), Data.calcStdDev(specialistMatches, Data.Entry::getEntryHighChamber)};
        specialistEndgamePoints = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryEndgamePoints), Data.calcStdDev(specialistMatches, Data.Entry::getEntryEndgamePoints)};
        specialistAutoPoints = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryAutoPoints), Data.calcStdDev(specialistMatches, Data.Entry::getEntryAutoPoints)};
        specialistTotalPoints = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryTotalPoints), Data.calcStdDev(specialistMatches, Data.Entry::getEntryTotalPoints)};
        specialistTeleopPoints = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryTeleopPoints), Data.calcStdDev(specialistMatches, Data.Entry::getEntryTeleopPoints)};
        specialistPiecesScored = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryPiecesScored), Data.calcStdDev(specialistMatches, Data.Entry::getEntryPiecesScored)};
        specialistAutoSamplesScored = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryAutoSamplesScored), Data.calcStdDev(specialistMatches, Data.Entry::getEntryAutoSamplesScored)};
        specialistAutoSpecimensScored = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryAutoSpecimensScored), Data.calcStdDev(specialistMatches, Data.Entry::getEntryAutoSpecimensScored)};
        specialistTeleopSamplesScored = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryTeleopSamplesScored), Data.calcStdDev(specialistMatches, Data.Entry::getEntryTeleopSamplesScored)};
        specialistTeleopSpecimensScored = new double[] {Data.calcMean(specialistMatches, Data.Entry::getEntryTeleopSpecimensScored), Data.calcStdDev(specialistMatches, Data.Entry::getEntryTeleopSpecimensScored)};

        coachNet = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryNet), Data.calcStdDev(coachMatches, Data.Entry::getEntryNet)};
        coachLowBasket = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryLowBasket), Data.calcStdDev(coachMatches, Data.Entry::getEntryLowBasket)};
        coachHighBasket = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryHighBasket), Data.calcStdDev(coachMatches, Data.Entry::getEntryHighBasket)};
        coachLowChamber = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryLowChamber), Data.calcStdDev(coachMatches, Data.Entry::getEntryLowChamber)};
        coachHighChamber = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryHighChamber), Data.calcStdDev(coachMatches, Data.Entry::getEntryHighChamber)};
        coachEndgamePoints = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryEndgamePoints), Data.calcStdDev(coachMatches, Data.Entry::getEntryEndgamePoints)};
        coachAutoPoints = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryAutoPoints), Data.calcStdDev(coachMatches, Data.Entry::getEntryAutoPoints)};
        coachTotalPoints = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryTotalPoints), Data.calcStdDev(coachMatches, Data.Entry::getEntryTotalPoints)};
        coachTeleopPoints = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryTeleopPoints), Data.calcStdDev(coachMatches, Data.Entry::getEntryTeleopPoints)};
        coachPiecesScored = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryPiecesScored), Data.calcStdDev(coachMatches, Data.Entry::getEntryPiecesScored)};
        coachAutoSamplesScored = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryAutoSamplesScored), Data.calcStdDev(coachMatches, Data.Entry::getEntryAutoSamplesScored)};
        coachAutoSpecimensScored = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryAutoSpecimensScored), Data.calcStdDev(coachMatches, Data.Entry::getEntryAutoSpecimensScored)};
        coachTeleopSamplesScored = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryTeleopSamplesScored), Data.calcStdDev(coachMatches, Data.Entry::getEntryTeleopSamplesScored)};
        coachTeleopSpecimensScored = new double[] {Data.calcMean(coachMatches, Data.Entry::getEntryTeleopSpecimensScored), Data.calcStdDev(coachMatches, Data.Entry::getEntryTeleopSpecimensScored)};

        humanNet = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryNet), Data.calcStdDev(humanMatches, Data.Entry::getEntryNet)};
        humanLowBasket = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryLowBasket), Data.calcStdDev(humanMatches, Data.Entry::getEntryLowBasket)};
        humanHighBasket = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryHighBasket), Data.calcStdDev(humanMatches, Data.Entry::getEntryHighBasket)};
        humanLowChamber = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryLowChamber), Data.calcStdDev(humanMatches, Data.Entry::getEntryLowChamber)};
        humanHighChamber = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryHighChamber), Data.calcStdDev(humanMatches, Data.Entry::getEntryHighChamber)};
        humanEndgamePoints = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryEndgamePoints), Data.calcStdDev(humanMatches, Data.Entry::getEntryEndgamePoints)};
        humanAutoPoints = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryAutoPoints), Data.calcStdDev(humanMatches, Data.Entry::getEntryAutoPoints)};
        humanTotalPoints = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryTotalPoints), Data.calcStdDev(humanMatches, Data.Entry::getEntryTotalPoints)};
        humanTeleopPoints = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryTeleopPoints), Data.calcStdDev(humanMatches, Data.Entry::getEntryTeleopPoints)};
        humanPiecesScored = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryPiecesScored), Data.calcStdDev(humanMatches, Data.Entry::getEntryPiecesScored)};
        humanAutoSamplesScored = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryAutoSamplesScored), Data.calcStdDev(humanMatches, Data.Entry::getEntryAutoSamplesScored)};
        humanAutoSpecimensScored = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryAutoSpecimensScored), Data.calcStdDev(humanMatches, Data.Entry::getEntryAutoSpecimensScored)};
        humanTeleopSamplesScored = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryTeleopSamplesScored), Data.calcStdDev(humanMatches, Data.Entry::getEntryTeleopSamplesScored)};
        humanTeleopSpecimensScored = new double[] {Data.calcMean(humanMatches, Data.Entry::getEntryTeleopSpecimensScored), Data.calcStdDev(humanMatches, Data.Entry::getEntryTeleopSpecimensScored)};

        Map<Integer, ArrayList<Double>> dataMap = new HashMap<Integer, ArrayList<Double>>();
        dataMap.put(0, Utilities.arrayToList(driverNet, specialistNet, coachNet, humanNet));
        dataMap.put(1, Utilities.arrayToList(driverLowBasket, specialistLowBasket, coachLowBasket, humanLowBasket));
        dataMap.put(2, Utilities.arrayToList(driverHighBasket, specialistHighBasket, coachHighBasket, humanHighBasket));
        dataMap.put(3, Utilities.arrayToList(driverLowChamber, specialistLowChamber, coachLowChamber, humanLowChamber));
        dataMap.put(4, Utilities.arrayToList(driverHighChamber, specialistHighChamber, coachHighChamber, humanHighChamber));
        dataMap.put(5, Utilities.arrayToList(driverEndgamePoints, specialistEndgamePoints, coachEndgamePoints, humanEndgamePoints));
        dataMap.put(6, Utilities.arrayToList(driverAutoPoints, specialistAutoPoints, coachAutoPoints, humanAutoPoints));
        dataMap.put(7, Utilities.arrayToList(driverTotalPoints, specialistTotalPoints, coachTotalPoints, humanTotalPoints));
        dataMap.put(8, Utilities.arrayToList(driverTeleopPoints, specialistTeleopPoints, coachTeleopPoints, humanTeleopPoints));
        dataMap.put(9, Utilities.arrayToList(driverPiecesScored, specialistPiecesScored, coachPiecesScored, humanPiecesScored));
        dataMap.put(10, Utilities.arrayToList(driverAutoSamplesScored, specialistAutoSamplesScored, coachAutoSamplesScored, humanAutoSamplesScored));
        dataMap.put(11, Utilities.arrayToList(driverAutoSpecimensScored, specialistAutoSpecimensScored, coachAutoSpecimensScored, humanAutoSpecimensScored));
        dataMap.put(12, Utilities.arrayToList(driverTeleopSamplesScored, specialistTeleopSamplesScored, coachTeleopSamplesScored, humanTeleopSamplesScored));
        dataMap.put(13, Utilities.arrayToList(driverTeleopSpecimensScored, specialistTeleopSpecimensScored, coachTeleopSpecimensScored, humanTeleopSpecimensScored));
        

        Utilities.writeDatamapToSheet(3, Utilities.getSheetFromWorkbook(wb, name), dataMap);
    }
}

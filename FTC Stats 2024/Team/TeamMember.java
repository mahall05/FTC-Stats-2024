package Team;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.microsoft.schemas.office.visio.x2012.main.CellType;

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

        /*
        dataMap.put(14, new ArrayList<Double>());
        dataMap.put(15, new ArrayList<Double>());

        for(int i = 0; i<=13; i++){
            dataMap.put(i+16, Utilities.arrayToList(Data.calc5NS(matches.get(primaryType), Data.Entry.getData(i))));
        }
        */

        XSSFSheet sheet = Utilities.getSheetFromWorkbook(wb, name);

        for(int i = 0; i < matches.get(primaryType).size(); i++){
            Row row = Utilities.getRowFromSheet(sheet, i+21);
            //Utilities.getCellFromRow(row, 0).setCellValue(Utilities.dateToString(matches.get(primaryType).get(i).getDate()).substring(0,6));
            Utilities.getCellFromRow(row, 0).setCellValue((int) ((matches.get(primaryType).get(i).getDate().getTime()-Team.firstDay.getTime()) / (24.0 * 60.0 * 60.0 * 1000.0) + 1));
            Utilities.getCellFromRow(row, 1).setCellValue(matches.get(primaryType).get(i).getDriver());
            Utilities.getCellFromRow(row, 2).setCellValue(matches.get(primaryType).get(i).getSpecialist());
            Utilities.getCellFromRow(row, 3).setCellValue(matches.get(primaryType).get(i).getHuman());
            Utilities.getCellFromRow(row, 4).setCellValue(matches.get(primaryType).get(i).getCoach());
            Utilities.getCellFromRow(row, 5).setCellValue(matches.get(primaryType).get(i).getNet());
            Utilities.getCellFromRow(row, 6).setCellValue(matches.get(primaryType).get(i).getLowBasket());
            Utilities.getCellFromRow(row, 7).setCellValue(matches.get(primaryType).get(i).getHighBasket());
            Utilities.getCellFromRow(row, 8).setCellValue(matches.get(primaryType).get(i).getLowChamber());
            Utilities.getCellFromRow(row, 9).setCellValue(matches.get(primaryType).get(i).getHighChamber());
            Utilities.getCellFromRow(row, 10).setCellValue(matches.get(primaryType).get(i).getEndgamePoints());
            Utilities.getCellFromRow(row, 11).setCellValue(matches.get(primaryType).get(i).getAutoPoints());
            Utilities.getCellFromRow(row, 12).setCellValue(matches.get(primaryType).get(i).getTotalPoints());
            Utilities.getCellFromRow(row, 13).setCellValue(matches.get(primaryType).get(i).getTeleopPoints());
            Utilities.getCellFromRow(row, 14).setCellValue(matches.get(primaryType).get(i).getPiecesScored());
            Utilities.getCellFromRow(row, 15).setCellValue(matches.get(primaryType).get(i).getAutoSamplesScored());
            Utilities.getCellFromRow(row, 16).setCellValue(matches.get(primaryType).get(i).getAutoSpecimensScored());
            Utilities.getCellFromRow(row, 17).setCellValue(matches.get(primaryType).get(i).getTeleopSamplesScored());
            Utilities.getCellFromRow(row, 18).setCellValue(matches.get(primaryType).get(i).getTeleopSpecimensScored());
            Utilities.getCellFromRow(row, 19).setCellValue(matches.get(primaryType).get(i).getAutoRan());
            Utilities.getCellFromRow(row, 20).setCellValue(matches.get(primaryType).get(i).getTeleopStrategy());
            Utilities.getCellFromRow(row, 21).setCellValue(matches.get(primaryType).get(i).getMatchType());
            Utilities.getCellFromRow(row, 22).setCellValue(matches.get(primaryType).get(i).wasCoachDriving());
        }

        for(int i = 0; i < matches.get(primaryType).size(); i++){
            Row row = Utilities.getRowFromSheet(sheet, i+21);
            row.createCell(25).setCellValue((int) ((matches.get(primaryType).get(i).getDate().getTime()-Team.firstDay.getTime()) / (24.0 * 60.0 * 60.0 * 1000.0) + 1));

            if(matches.get(primaryType).get(i).getTeleopStrategy()==null){

            }else if(matches.get(primaryType).get(i).getTeleopStrategy().equals("Samples")){
                row.createCell(26).setCellValue(matches.get(primaryType).get(i).getTeleopSamplesScored());
            }else if(matches.get(primaryType).get(i).getTeleopStrategy().equals("Specimens")){
                row.createCell(27).setCellValue(matches.get(primaryType).get(i).getTeleopSpecimensScored());
            }
        }

        Utilities.writeDatamapToSheet(3, Utilities.getSheetFromWorkbook(wb, name), dataMap);
    }
}

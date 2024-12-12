package Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Core.Utilities;
import Other.Data;

public class Group{
    protected TeamMember[] members;
    protected ArrayList<Data.Entry> entries;
    protected Data data;
    protected XSSFWorkbook wb;
    private int type;

    // Net, Low Basket, High Basket, Low Chamber, High Chamber, Endgame, Auto Points, Total Points, Teleop Points, Pieces Scored, Auto Samples, Auto Specimens, Teleop Samples, Teleop Specimens

    protected double[][] dataArray = new double[14][2];

    /*
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
    */
    
    public Group(Object[] members, XSSFWorkbook wb, int type){
        this.type=type;
        this.members = new TeamMember[members.length];
        this.wb=wb;
        data = new Data(wb);
        entries = data.getEntries();

        for(int i = 0; i < members.length; i++){
            this.members[i] = (TeamMember) members[i];
        }

        for(int i = 0; i < dataArray.length-2; i++){
            dataArray[i] = new double[] {Data.calcMean(entries, Data.Entry.getData(i)), Data.calcStdDev(entries, Data.Entry.getData(i))};
        }

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

        dataArray[dataArray.length-2] = new double[] {Data.calcMean(sampleMatches, Data.Entry::getEntryTeleopSamplesScored), Data.calcStdDev(sampleMatches, Data.Entry::getEntryTeleopSamplesScored)};
        dataArray[dataArray.length-1] = new double[] {Data.calcMean(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored), Data.calcStdDev(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored)};
    }

    public void calcComparisonData(String typeLabel){
        Map<Integer, ArrayList<Double>> dataMap = new HashMap<Integer, ArrayList<Double>>();

        for(int i = 0; i < dataArray.length; i++){
            ArrayList<Double> dataList = new ArrayList<Double>();

            for(int j = 0; j < members.length; j++){
                double memberAvg = members[j].getData()[i][type][0];
                double teamAvg = dataArray[i][0];
                double teamStdDev = dataArray[i][1];

                dataList.add((memberAvg-teamAvg)/teamStdDev);
                dataList.add(memberAvg-teamAvg);
            }

            dataMap.put(i, dataList);
        }

        Utilities.writeDatamapToSheet(3, Utilities.getSheetFromWorkbook(wb, typeLabel), dataMap);
    }

    public TeamMember[] getMembers(){
        return members;
    }
    public String[] getMemberNames(){
        String[] names = new String[members.length];
        for(int i = 0; i < members.length; i++){
            names[i]=members[i].getName();
        }
        return names;
    }

    public void runCombos(Group comboGroup, int column, TeamMember... exclusions){ // Start at 39, row 3
        for(int i = 0; i < members.length; i++){
            boolean breaker=false;
            for(int j = 0; j < exclusions.length; j++){
                if(members[i].equals(exclusions[j])){
                    breaker=true;
                }
            }
            if(breaker){
                continue;
            }

            for(int j = 0; j < dataArray.length; j++){
                ArrayList<Double> dataList = new ArrayList<Double>();

                for(int k = 0; k < comboGroup.size(); k++){
                    double memberAvg = members[i].getData()[j][type][0];
                    double memberStdDev = members[i].getData()[j][type][1];

                    double comboMemberAvg = members[i].selectiveAvg(comboGroup.getMembers()[k], comboGroup.getType(), j);
                    
                    dataList.add((comboMemberAvg-memberAvg)/memberStdDev);
                    dataList.add(comboMemberAvg-memberAvg);
                }
                Row row = Utilities.getRowFromSheet(Utilities.getSheetFromWorkbook(wb, members[i].getName()), 3+j);
                for(int k = 0; k < dataList.size(); k++){
                    Utilities.getCellFromRow(row, column+k).setCellValue(dataList.get(k));
                }
            }
        }
    }

    public int size(){
        return members.length;
    }
    public int getType(){
        return type;
    }
    public TeamMember find(String name){
        for(TeamMember m : members){
            if(m.getName().equals(name)){
                return m;
            }
        }
        return null;
    }

}

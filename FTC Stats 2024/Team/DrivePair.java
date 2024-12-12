package Team;

import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Core.Utilities;
import Other.Data;
import Other.Data.Entry;

public class DrivePair {
    private TeamMember driver;
    private TeamMember specialist;
    private TeamMember[] coaches;
    private ArrayList<Entry> matches;
    private double[][] data = new double[14][2];
    private XSSFWorkbook wb;

    public DrivePair(TeamMember driver, TeamMember specialist, TeamMember[] coaches, ArrayList<Entry> matches, XSSFWorkbook wb){
        this.driver=driver;
        this.specialist=specialist;
        this.wb=wb;
        this.coaches=coaches;
        this.matches = new ArrayList<Entry>();

        for(Entry m : matches){
            if(m.getDriver().equals(driver.getName())&&m.getSpecialist().equals(specialist.getName())){
                this.matches.add(m);
            }
        }

        for(int i = 0; i < data.length-2; i++){
            data[i][0] = Data.calcMean(this.matches, Entry.getData(i));
            data[i][1] = Data.calcStdDev(this.matches, Entry.getData(i));
        }

        ArrayList<Data.Entry> sampleMatches = new ArrayList<Data.Entry>();
        ArrayList<Data.Entry> specimenMatches = new ArrayList<Data.Entry>();

        for(Data.Entry e : this.matches){
            if(e.getTeleopStrategy()!=null){
                if(e.getTeleopStrategy().equals("Samples")){
                    sampleMatches.add(e);
                }else if(e.getTeleopStrategy().equals("Specimens")){
                    specimenMatches.add(e);
                }
            }
        }

        data[data.length-2] = new double[] {Data.calcMean(sampleMatches, Data.Entry::getEntryTeleopSamplesScored), Data.calcStdDev(sampleMatches, Data.Entry::getEntryTeleopSamplesScored)};
        data[data.length-1] = new double[] {Data.calcMean(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored), Data.calcStdDev(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored)};
    }

    public void calcData(int column, double[][] teamData){
        //System.out.println(driver.getName()+" "+specialist.getName());
        for(int i = 0; i < data.length; i++){
            Utilities.getCellFromRow(Utilities.getRowFromSheet(Utilities.getSheetFromWorkbook(wb, "Drive Teams"), 4+i), column).setCellValue(data[i][0]-teamData[i][0]);
        }
    }

    public void calcDataWithCoaches(int column, double[][] teamData){
        for(int i = 0; i < coaches.length; i++){
            double[][] tempData = new double[14][2];
            ArrayList<Entry> coachMatches = new ArrayList<Entry>();

            for(Entry e : this.matches){
                if(e.getCoach().equals(coaches[i].getName())){
                    coachMatches.add(e);
                }
            }

            for(int j = 0; j < data.length-2; j++){
                tempData[j][0] = Data.calcMean(coachMatches, Entry.getData(j));
                tempData[j][1] = Data.calcStdDev(coachMatches, Entry.getData(j));
            }
    
            ArrayList<Data.Entry> sampleMatches = new ArrayList<Data.Entry>();
            ArrayList<Data.Entry> specimenMatches = new ArrayList<Data.Entry>();
    
            for(Data.Entry e : coachMatches){
                if(e.getTeleopStrategy()!=null){
                    if(e.getTeleopStrategy().equals("Samples")){
                        sampleMatches.add(e);
                    }else if(e.getTeleopStrategy().equals("Specimens")){
                        specimenMatches.add(e);
                    }
                }
            }
    
            tempData[data.length-2] = new double[] {Data.calcMean(sampleMatches, Data.Entry::getEntryTeleopSamplesScored), Data.calcStdDev(sampleMatches, Data.Entry::getEntryTeleopSamplesScored)};
            tempData[data.length-1] = new double[] {Data.calcMean(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored), Data.calcStdDev(specimenMatches, Data.Entry::getEntryTeleopSpecimensScored)};

            for(int j = 0; j < data.length; j++){
                Utilities.getCellFromRow(Utilities.getRowFromSheet(Utilities.getSheetFromWorkbook(wb, "Drive Teams"), 4+j), column+i).setCellValue(tempData[j][0]-teamData[j][0]);
            }
        }
    }
    
}

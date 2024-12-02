package Other;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.poi.hwpf.usermodel.DateAndTime;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Core.Settings;
import Core.Utilities;

public class Data {
    private XSSFSheet sheet;
    private ArrayList<Entry> entries;
    private static Date date;

    public Data(XSSFWorkbook wb){
        sheet = wb.getSheet("Data");
        entries = new ArrayList<Entry>();
        date = new Date();

        retrieveEntries();
        //printEntries();
        //printData();
    }

    public ArrayList<Entry> getEntries(){
        return entries;
    }

    public static double calcWeight(Entry e){
        long millisecondsBetween = Math.abs(date.getTime() - e.getDate().getTime());
        double daysBetween = millisecondsBetween/ (24.0 * 60.0 * 60.0 * 1000.0) - 1;
        
        double dateWeight = Settings.dateWeightFunction(daysBetween);

        if(e.getMatchType().equals("Comp")){
            dateWeight*=1.0;
        }else{
            dateWeight*=Settings.relativePracticeWeight;
        }
        return dateWeight;
    }

    public static double calcMean(ArrayList<Entry> es, Function<Entry, Integer> f){
        double sum = 0;
        double numValues = 0;

        for(Entry e : es){
            double val = f.apply(e);
            if(val>=0){
                sum+=val * calcWeight(e);
                numValues += calcWeight(e);
            }
        }
        double mean = sum/numValues;
        return mean;
    }

    public static double calcStdDev(ArrayList<Entry> es, Function<Entry, Integer> f){
        double mean = calcMean(es, f);

        double sqrDevSum = 0;
        double numValues = 0;

        for(Entry e : es){
            double val = f.apply(e);
            if(val>=0){
                double deviation = Math.abs(val-mean);
                double sqrDev = deviation*deviation;
                sqrDevSum += sqrDev * calcWeight(e);
                numValues += calcWeight(e);
            }
        }

        double deviationSquared = sqrDevSum/numValues;
        double stdDev = Math.sqrt(deviationSquared);
        return stdDev;
    }

    public static double[] calc5NS(ArrayList<Entry> es, Function<Entry, Integer> f){
        if(es.isEmpty()) {return new double[] {-1, -1, -1, -1, -1};}

        double[] sortedArray = new double[es.size()];
        for(int i = 0; i < sortedArray.length; i++){
            sortedArray[i] = f.apply(es.get(i));
        }

        for(int i = (sortedArray.length-1); i>=0; i--){
            for(int j = 1; j <= i; j++){
                if(sortedArray[j-1] > sortedArray[j]){
                    double temp = sortedArray[j-1];
                    sortedArray[j-1] = sortedArray[j];
                    sortedArray[j] = temp;
                }
            }
        }
        int count = 0;
        for(int i = 0; i < sortedArray.length; i++){
            if(sortedArray[i]<0){
                count++;
            }else{
                i=sortedArray.length;
            }
        }

        double fixedArray[] = new double[sortedArray.length-count];
        for(int i = 0; i<fixedArray.length; i++){
            fixedArray[i] = sortedArray[i+count];
        }

        double q1 = fixedArray[(int) (25.0/100.0*(fixedArray.length+1))];
        double med = fixedArray[(int) (50.0/100.0*(fixedArray.length+1))];
        double q3 = fixedArray[(int) (75.0/100.0*(fixedArray.length+1))];

        double iqr = q3 - q1;
        double min = -1, max = -1;

        for(int i = 0; i < fixedArray.length-1; i++){
            if(fixedArray[i] >= q1-(1.5 * iqr)){
                min = fixedArray[i];
                i = fixedArray.length;
            }
        }
        for(int i = fixedArray.length-1; i>=0; i--){
            if(fixedArray[i] <= q3+(1.5*iqr)){
                max = fixedArray[i];
                i = -1;
            }
        }

        return new double[] {min, q1, med, q3, max};
    }

    // Format
    // Date, Drive, Specials, Human, Coach, Net, Low Basket, High Basket, Low Chamber, High Chamber, Endgame, Auto Points, Total Points, Pieces Scored, Auto Ran, Teleop Strategy, Match Type
    private void retrieveEntries(){
        for(int i = 1; i <= sheet.getLastRowNum(); i++){
            if(sheet.getRow(i)!=null){
                XSSFRow row = sheet.getRow(i);
                
                if(row.getCell(0)!=null && row.getCell(0).getDateCellValue()!=null && (row.getCell(17)==null || !row.getCell(17).getStringCellValue().equals("*"))){
                    entries.add(new Entry(row.getCell(0)==null?null: row.getCell(0).getDateCellValue(), 
                                            row.getCell(1)==null?null: row.getCell(1).getStringCellValue(), 
                                            row.getCell(2)==null?null: row.getCell(2).getStringCellValue(), 
                                            row.getCell(3)==null?null: row.getCell(3).getStringCellValue(), 
                                            row.getCell(4)==null?null: row.getCell(4).getStringCellValue(), 
                                            row.getCell(5)==null?-1: (int) row.getCell(5).getNumericCellValue(), 
                                            row.getCell(6)==null?-1: (int) row.getCell(6).getNumericCellValue(), 
                                            row.getCell(7)==null?-1: (int) row.getCell(7).getNumericCellValue(), 
                                            row.getCell(8)==null?-1: (int) row.getCell(8).getNumericCellValue(), 
                                            row.getCell(9)==null?-1: (int) row.getCell(9).getNumericCellValue(), 
                                            row.getCell(10)==null?-1: (int) row.getCell(10).getNumericCellValue(), 
                                            row.getCell(11)==null?-1: (int) row.getCell(11).getNumericCellValue(), 
                                            row.getCell(12)==null?-1: (int) row.getCell(12).getNumericCellValue(), 
                                            row.getCell(13)==null?-1: (int) row.getCell(13).getNumericCellValue(),
                                            row.getCell(14)==null?-1: (int) row.getCell(14).getNumericCellValue(),
                                            row.getCell(15)==null?null: row.getCell(15).getStringCellValue(),
                                            row.getCell(16)==null?null: row.getCell(16).getStringCellValue()
                                        ));
                }
            }
        }
    }

    private void printEntries(){
        for(Entry e : entries){
            System.out.println(e);
        }
    }

    /*
    private void printData(){
        System.out.println("\nNet:");
        System.out.println("Mean: " + calcMean(Entry::getEntryNet));
        System.out.println("Std Dev: " + calcStdDev(Entry::getEntryNet));
        
        System.out.println("\nLow Basket:");
        System.out.println("Mean: " + calcMean(Entry::getEntryLowBasket));
        System.out.println("Std Dev: " + calcStdDev(Entry::getEntryLowBasket));

        System.out.println("\nHigh Basket:");
        System.out.println("Mean: " + calcMean(Entry::getEntryHighBasket));
        System.out.println("Std Dev: " + calcStdDev(Entry::getEntryHighBasket));

        System.out.println("\nLow Chamber:");
        System.out.println("Mean: " + calcMean(Entry::getEntryLowChamber));
        System.out.println("Std Dev: " + calcStdDev(Entry::getEntryLowChamber));

        System.out.println("\nHigh Chamber:");
        System.out.println("Mean: " + calcMean(Entry::getEntryHighChamber));
        System.out.println("Std Dev: " + calcStdDev(Entry::getEntryHighChamber));

        System.out.println("\nEndgame Points:");
        System.out.println("Mean: " + calcMean(Entry::getEntryEndgamePoints));
        System.out.println("Std Dev: " + calcStdDev(Entry::getEntryEndgamePoints));

        System.out.println("\nAuto Points:");
        System.out.println("Mean: " + calcMean(Entry::getEntryAutoPoints));
        System.out.println("Std Dev: " + calcStdDev(Entry::getEntryAutoPoints));

        System.out.println("\nTotal Points:");
        System.out.println("Mean: " + calcMean(Entry::getEntryTotalPoints));
        System.out.println("Std Dev: " + calcStdDev(Entry::getEntryTotalPoints));

        System.out.println("\nPieces Scored:");
        System.out.println("Mean: " + calcMean(Entry::getEntryPiecesScored));
        System.out.println("Std Dev: " + calcStdDev(Entry::getEntryPiecesScored));
    }*/

    public class Entry{
        private Date date;
        private String driver;
        private String specialist;
        private String human;
        private String coach;
        private int net;
        private int lowBasket;
        private int highBasket;
        private int lowChamber;
        private int highChamber;
        private int endgamePoints;
        private int autoPoints;
        private int totalPoints;
        private int piecesScored;

        private int autoSamplesScored;
        private int autoSpecimensScored;
        private int teleopSamplesScored;
        private int teleopSpecimensScored;
        private int teleopPoints;

        private int autoRan;
        private String teleopStrategy;
        private String matchType;

        public static Function<Entry, Integer> getData(int data){
            switch(data){
                case (0):
                    return Entry::getEntryNet;
                case (1):
                    return Entry::getEntryLowBasket;
                case (2):
                    return Entry::getEntryHighBasket;
                case (3):
                    return Entry::getEntryLowChamber;
                case (4):
                    return Entry::getEntryHighChamber;
                case (5):
                    return Entry::getEntryEndgamePoints;
                case (6):
                    return Entry::getEntryAutoPoints;
                case (7):
                    return Entry::getEntryTotalPoints;
                case (8):
                    return Entry::getEntryTeleopPoints;
                case (9):
                    return Entry::getEntryPiecesScored;
                case (10):
                    return Entry::getEntryAutoSamplesScored;
                case (11):
                    return Entry::getEntryAutoSpecimensScored;
                case (12):
                    return Entry::getEntryTeleopSamplesScored;
                case (13):
                    return Entry::getEntryTeleopSpecimensScored;
                default:
                    return null;
            }
        }

        private void calcPieces(){
            double points = autoPoints;
            if(points % 2 != 0){
                points-=3;
            }

            autoSpecimensScored = (int) ((points - points%10)/10);
            autoSamplesScored = (int) ((points - autoSpecimensScored*10 - points%8)/8);
            autoSamplesScored += (int) ((points - autoSpecimensScored*10 - autoSamplesScored*8)/2);

            teleopSpecimensScored = lowChamber+highChamber - autoSpecimensScored;
            teleopSamplesScored = lowBasket+highBasket+net - autoSamplesScored;

            teleopPoints = totalPoints - autoPoints - endgamePoints;
        }


        public static int getEntryAutoRan(Entry e){
            return e.getAutoRan();
        }
        public static String getEntryTeleopStrategy(Entry e){
            return e.getTeleopStrategy();
        }
        public static String getEntryMatchType(Entry e){
            return e.getMatchType();
        }
        public static int getEntryAutoSamplesScored(Entry e){
            return e.getAutoSamplesScored();
        }
        public static int getEntryAutoSpecimensScored(Entry e){
            return e.getAutoSpecimensScored();
        }
        public static int getEntryTeleopSamplesScored(Entry e){
            return e.getTeleopSamplesScored();
        }
        public static int getEntryTeleopSpecimensScored(Entry e){
            return e.getTeleopSpecimensScored();
        }
        public static int getEntryTeleopPoints(Entry e){
            return e.getTeleopPoints();
        }
        public static int getEntryNet(Entry e){
            return e.getNet();
        }
        public static int getEntryLowBasket(Entry e){
            return e.getLowBasket();
        }
        public static int getEntryHighBasket(Entry e){
            return e.getHighBasket();
        }
        public static int getEntryLowChamber(Entry e){
            return e.getLowChamber();
        }
        public static int getEntryHighChamber(Entry e){
            return e.getHighChamber();
        }
        public static int getEntryEndgamePoints(Entry e){
            return e.getEndgamePoints();
        }
        public static int getEntryAutoPoints(Entry e){
            return e.getAutoPoints();
        }
        public static int getEntryTotalPoints(Entry e){
            return e.getTotalPoints();
        }
        public static int getEntryPiecesScored(Entry e){
            return e.getPiecesScored();
        }

        public Entry(Date date, String driver, String specialist, String human, String coach, int net, int lowBasket, int highBasket, int lowChamber, int highChamber, int endgamePoints, int autoPoints, int totalPoints, int piecesScored, int autoRan, String teleopStrategy, String matchType){
            this.date=date;
            this.driver=driver;
            this.specialist=specialist;
            this.human=human;
            this.coach=coach;
            this.net=net;
            this.lowBasket=lowBasket;
            this.highBasket=highBasket;
            this.lowChamber=lowChamber;
            this.highChamber=highChamber;
            this.endgamePoints=endgamePoints;
            this.autoPoints=autoPoints;
            this.totalPoints=totalPoints;
            this.piecesScored=piecesScored;
            this.autoRan=autoRan;
            this.teleopStrategy=teleopStrategy;
            this.matchType=matchType;

            calcPieces();
        }

        public Date getDate(){return date;}
        public String getDriver(){return driver;}
        public String getSpecialist(){return specialist;}
        public String getHuman(){return human;}
        public String getCoach(){return coach;}
        public int getNet(){return net;}
        public int getLowBasket(){return lowBasket;}
        public int getHighBasket(){return highBasket;}
        public int getLowChamber(){return lowChamber;}
        public int getHighChamber(){return highChamber;}
        public int getEndgamePoints(){return endgamePoints;}
        public int getAutoPoints(){return autoPoints;}
        public int getTotalPoints(){return totalPoints;}
        public int getPiecesScored(){return piecesScored;}
        public int getAutoSamplesScored(){return autoSamplesScored;}
        public int getAutoSpecimensScored(){return autoSpecimensScored;}
        public int getTeleopSamplesScored(){return teleopSamplesScored;}
        public int getTeleopSpecimensScored(){return teleopSpecimensScored;}
        public int getTeleopPoints(){return teleopPoints;}
        public int getAutoRan(){return autoRan;}
        public String getTeleopStrategy(){return teleopStrategy;}
        public String getMatchType(){return matchType;}

        public String toString(){
            return getDate() + " - " + getDriver() + " - " + getSpecialist() + " - " + getHuman() + " - " + getCoach() + " - " + getNet() + " - " + getLowBasket() + " - " + getHighBasket() + " - " + getLowChamber() + " - " + getHighChamber() + " - " + getEndgamePoints() + " - " + getAutoPoints() + " - " + getTotalPoints() + " - " + getPiecesScored();
        }
    }
}

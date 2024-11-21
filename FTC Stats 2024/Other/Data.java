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

import Core.Utilities;

public class Data {
    private XSSFSheet sheet;
    private ArrayList<Entry> entries;
    private Date date;

    public Data(XSSFWorkbook wb){
        sheet = wb.getSheet("Data");
        entries = new ArrayList<Entry>();
        date = new Date();

        getEntries();
        printEntries();
        printData();
        calcWeight(entries.get(0));
    }

    private double calcWeight(Entry e){
        long millisecondsBetween = Math.abs(date.getTime() - e.getDate().getTime());
        double daysBetween = millisecondsBetween/ (24.0 * 60.0 * 60.0 * 1000.0) - 1;
        
        double weight = Math.pow(1.07, -daysBetween);
        return weight;
    }

    private double calcMean(Function<Entry, Integer> f){
        double sum = 0;
        double numValues = 0;

        for(Entry e : entries){
            double val = f.apply(e);
            if(val>=0){
                sum+=val * calcWeight(e);
                numValues += calcWeight(e);
            }
        }
        double mean = sum/numValues;
        return mean;
    }

    private double calcStdDev(Function<Entry, Integer> f){
        double mean = calcMean(f);

        double sqrDevSum = 0;
        double numValues = 0;

        for(Entry e : entries){
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

    // Format
    // Date, Drive, Specials, Human, Coach, Net, Low Basket, High Basket, Low Chamber, High Chamber, Endgame, Auto Points, Total Points, Pieces Scored
    private void getEntries(){
        for(int i = 1; i <= sheet.getLastRowNum(); i++){
            if(sheet.getRow(i)!=null){
                XSSFRow row = sheet.getRow(i);

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
                                        row.getCell(13)==null?-1: (int) row.getCell(13).getNumericCellValue()));
            }
        }
    }

    private void printEntries(){
        for(Entry e : entries){
            System.out.println(e);
        }
    }

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
    }

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

        static int getEntryNet(Entry e){
            return e.getNet();
        }
        static int getEntryLowBasket(Entry e){
            return e.getLowBasket();
        }
        static int getEntryHighBasket(Entry e){
            return e.getHighBasket();
        }
        static int getEntryLowChamber(Entry e){
            return e.getLowChamber();
        }
        static int getEntryHighChamber(Entry e){
            return e.getHighChamber();
        }
        static int getEntryEndgamePoints(Entry e){
            return e.getEndgamePoints();
        }
        static int getEntryAutoPoints(Entry e){
            return e.getAutoPoints();
        }
        static int getEntryTotalPoints(Entry e){
            return e.getTotalPoints();
        }
        static int getEntryPiecesScored(Entry e){
            return e.getPiecesScored();
        }

        public Entry(Date date, String driver, String specialist, String human, String coach, int net, int lowBasket, int highBasket, int lowChamber, int highChamber, int endgamePoints, int autoPoints, int totalPoints, int piecesScored){
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

        public String toString(){
            return getDate() + " - " + getDriver() + " - " + getSpecialist() + " - " + getHuman() + " - " + getCoach() + " - " + getNet() + " - " + getLowBasket() + " - " + getHighBasket() + " - " + getLowChamber() + " - " + getHighChamber() + " - " + getEndgamePoints() + " - " + getAutoPoints() + " - " + getTotalPoints() + " - " + getPiecesScored();
        }
    }
}

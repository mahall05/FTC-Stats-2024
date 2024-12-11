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

        if(e.wasCoachDriving()){
            dateWeight*=Settings.relativeCoachMatchWeight;
        }else{
            dateWeight*=1.0;
        }

        return dateWeight;
    }
    public static double calcWeight(Entry e, Date refDate){
        long millisecondsBetween = Math.abs(refDate.getTime() - e.getDate().getTime());
        double daysBetween = millisecondsBetween/ (24.0 * 60.0 * 60.0 * 1000.0) - 1;
        
        double dateWeight = Settings.dateWeightFunction(daysBetween);

        if(e.getMatchType().equals("Comp")){
            dateWeight*=1.0;
        }else{
            dateWeight*=Settings.relativePracticeWeight;
        }

        if(e.wasCoachDriving()){
            dateWeight*=Settings.relativeCoachMatchWeight;
        }else{
            dateWeight*=1.0;
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
    public static double calcMean(ArrayList<Entry> es, Function<Entry, Integer> f, Date refDate){
        double sum = 0;
        double numValues = 0;

        for(Entry e : es){
            double val = f.apply(e);
            if(val>=0){
                sum+=val * calcWeight(e, refDate);
                numValues += calcWeight(e, refDate);
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
    public static double calcStdDev(ArrayList<Entry> es, Function<Entry, Integer> f, Date refDate){
        double mean = calcMean(es, f);

        double sqrDevSum = 0;
        double numValues = 0;

        for(Entry e : es){
            double val = f.apply(e);
            if(val>=0){
                double deviation = Math.abs(val-mean);
                double sqrDev = deviation*deviation;
                sqrDevSum += sqrDev * calcWeight(e, refDate);
                numValues += calcWeight(e, refDate);
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

        public boolean wasCoachDriving(){
            boolean coachDriving = false;
            if(getDriver().equals("Matt")||getDriver().equals("Caleb")||getDriver().equals("Maddie")
            ||getSpecialist().equals("Matt")||getSpecialist().equals("Caleb")||getSpecialist().equals("Maddie")){
            coachDriving=true;}

            return coachDriving;
        }

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

        public int getSingleData(int data){
            switch(data){
                case (0):
                    return getNet();
                case (1):
                    return getLowBasket();
                case (2):
                    return getHighBasket();
                case (3):
                    return getLowChamber();
                case (4):
                    return getHighChamber();
                case (5):
                    return getEndgamePoints();
                case (6):
                    return getAutoPoints();
                case (7):
                    return getTotalPoints();
                case (8):
                    return getTeleopPoints();
                case (9):
                    return getPiecesScored();
                case (10):
                    return getAutoSamplesScored();
                case (11):
                    return getAutoSpecimensScored();
                case (12):
                    return getTeleopSamplesScored();
                case (13):
                    return getTeleopSpecimensScored();
                default:
                    return -1;
            }
        }

        private void calcPieces(){
            if(autoPoints<0 || net<0 || lowBasket<0 || highBasket < 0 || lowChamber < 0 || highChamber < 0){
                autoSamplesScored=-1;
                autoSpecimensScored=-1;
                teleopSamplesScored=-1;
                teleopSpecimensScored=-1;
            }
            else{
                int points = autoPoints;

                if(points % 2 != 0){
                    points-=3;
                }

                int[] autoPieces = minimize(points);

                autoSamplesScored = autoPieces[1]+autoPieces[3]+autoPieces[4];
                autoSpecimensScored = autoPieces[0]+autoPieces[2];

                teleopSamplesScored = this.net+this.lowBasket+this.highBasket - autoSamplesScored;
                teleopSpecimensScored = this.lowChamber+this.highChamber - autoSpecimensScored;
            }


            teleopPoints = totalPoints - 2*autoPoints - endgamePoints;
        }

        private int[] minimize(int points){
            int[] values = {10, 8, 6, 4, 2, 0};
            int[][] sequences = new int[(int) Math.pow(values.length, 4)][4];

            // CREATE LIST OF ALL POSSIBLE SEQUENCES
            int p = 0;
            for(int i = 0; i < values.length; i++){
                for(int j = 0; j < values.length; j++){
                    for(int k = 0; k < values.length; k++){
                        for(int l = 0; l < values.length; l++){
                            sequences[p] = new int[] {values[i], values[j], values[k], values[l]};
                            p++;
                        }
                    }
                }
            }

            // FIND ALL SEQUENCES THAT HAVE CORRECT POINT VALUE
            ArrayList<int[]> validSequences = new ArrayList<int[]>();
            for(int[] s : sequences){
                int sum = 0;
                for(int i = 0; i < s.length; i++){
                    sum+=s[i];
                }
                if(sum==points){
                    validSequences.add(s);
                }
            }

            //System.out.println("Break");
            // REMOVE REPEAT SEQUENCES
            for(int i = 0; i < validSequences.size(); i++){
                int[] nums = new int[6];

                for(int j = 0; j < validSequences.get(i).length; j++){
                    switch (validSequences.get(i)[j]){
                        case(10):
                            nums[0]++;
                            break;
                        case(8):
                            nums[1]++;
                            break;
                        case(6):
                            nums[2]++;
                            break;
                        case(4):
                            nums[3]++;
                            break;
                        case(2):
                            nums[4]++;
                            break;
                        case(0):
                            nums[5]++;
                            break;
                    }
                }

                for(int j = i+1; j < validSequences.size(); j++){
                    int[] newNums = new int[6];

                    for(int k = 0; k < validSequences.get(j).length; k++){
                        switch (validSequences.get(j)[k]){
                            case(10):
                                newNums[0]++;
                                break;
                            case(8):
                                newNums[1]++;
                                break;
                            case(6):
                                newNums[2]++;
                                break;
                            case(4):
                                newNums[3]++;
                                break;
                            case(2):
                                newNums[4]++;
                                break;
                            case(0):
                                newNums[5]++;
                                break;
                        }
                    }

                    if(nums[0]==newNums[0] && nums[1]==newNums[1] && nums[2]==newNums[2] && nums[3]==newNums[3] && nums[4]==newNums[4] && nums[5]==newNums[5]){
                        validSequences.remove(j);
                        j--;
                    }
                }
            }

            //System.out.println("Break");
            // REMOVE SEQUENCES THAT WE WOULDN'T USE
            for(int i = 0; i < validSequences.size(); i++){
                int[] nums = new int[6];

                for(int j = 0; j < validSequences.get(i).length; j++){
                    switch (validSequences.get(i)[j]){
                        case(10):
                            nums[0]++;
                            break;
                        case(8):
                            nums[1]++;
                            break;
                        case(6):
                            nums[2]++;
                            break;
                        case(4):
                            nums[3]++;
                            break;
                        case(2):
                            nums[4]++;
                            break;
                        case(0):
                            nums[5]++;
                            break;
                    }
                }

                /*
                if(nums[2]>0){
                    validSequences.remove(i);
                    i--;
                    continue;
                }
                */

                // If we score more than one specimen, we wouldn't have any samples scored
                if(nums[0]+nums[2]>1 && nums[1]+nums[3]+nums[4]>0){
                    validSequences.remove(i);
                    i--;
                    continue;
                }
            }
            
            /*
             * Now only one of the possible sequences, when added to the teleop points, would create the total score we earned
             */
            for(int i = 0; i < validSequences.size(); i++){
                int[] teleNums = new int[5];

                int[] nums = new int[5];

                for(int j = 0; j < validSequences.get(i).length; j++){
                    switch (validSequences.get(i)[j]){
                        case(10):
                            nums[0]++;
                            break;
                        case(8):
                            nums[1]++;
                            break;
                        case(6):
                            nums[2]++;
                            break;
                        case(4):
                            nums[3]++;
                            break;
                        case(2):
                            nums[4]++;
                            break;
                    }
                }

                //System.out.println("Break");
                teleNums[0] = this.highChamber-nums[0];
                teleNums[1] = this.highBasket-nums[1];
                teleNums[2] = this.lowChamber-nums[2];
                teleNums[3] = this.lowBasket-nums[3];
                teleNums[4] = this.net-nums[4];

                if((teleNums[0]+nums[0])*10+
                    (teleNums[1]+nums[1])*8+
                    (teleNums[2]+nums[2])*6+
                    (teleNums[3]+nums[3])*4+
                    (teleNums[4]+nums[4])*2 != totalPoints-endgamePoints-autoPoints){
                        validSequences.remove(i);
                        i--;
                        continue;
                    }

                //System.out.println("Break");
                for(int j = 0; j < teleNums.length; j++){
                    if(teleNums[j]<0){
                        validSequences.remove(i);
                        i--;
                        break;
                    }
                }

            }

            int[] nums = new int[5];
            try{
                for(int j = 0; j < validSequences.get(0).length; j++){
                    switch (validSequences.get(0)[j]){
                        case(10):
                            nums[0]++;
                            break;
                        case(8):
                            nums[1]++;
                            break;
                        case(6):
                            nums[2]++;
                            break;
                        case(4):
                            nums[3]++;
                            break;
                        case(2):
                            nums[4]++;
                            break;
                    }
                }
            }catch(IndexOutOfBoundsException e){
                System.out.println("Error: No valid autos found");
            }
            //System.out.println("Ran");
            return nums;
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

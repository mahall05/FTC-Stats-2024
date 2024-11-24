package Core;

public class Settings {
    public static String fileName = "Red Team Data.xlsx";

    public static double relativePracticeWeight = 0.25;

    public static double dateWeightFunction(double days){
        double weight = (3.82257*Math.pow(10,12))*Math.pow(0.789549*days+23.60296,-10.14368) + Math.pow(sigmoid(0.276914*days-4.51361), 0.925817) - 0.0320539;
        return weight;
    }
    private static double sigmoid(double x){
        return 1/(1 + Math.pow(Math.E, x));
    }

    /*
    public static double dateWeightFunction(double days){
        double weight = Math.pow(1.07, -days);
        return weight;
    }
        */
}

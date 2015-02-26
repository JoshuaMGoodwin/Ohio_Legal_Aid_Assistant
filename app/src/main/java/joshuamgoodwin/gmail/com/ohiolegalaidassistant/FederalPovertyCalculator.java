package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

/**
 * Created by joshuagoodwin on 2/21/15.
 */
public class FederalPovertyCalculator {

    private static final int[] fpl2015 = {11770, 4160};
    private static final int[] fpl2014 = {11670, 4060};

    double annualIncome, results;

    int size;

    String year;

    public FederalPovertyCalculator () {

    }

    private int getValues(String year, int pos) {

        switch (year) {
            case "2015":
                return fpl2015[pos];
            case "2014":
                return fpl2014[pos];
            default:
                return fpl2015[pos];
        }
    }

    public double getResults() {

        int povertyStart = getValues(year, 0);
        int povertyIncrement = getValues(year, 1);

        double fpl = ((size - 1) * povertyIncrement) + povertyStart;

        results = Math.floor(((annualIncome / fpl) * 100) * 100) / 100;

        return results;

    }

    public void setSize(int s) {
        size = s;
    }

    public void setYear(String y) {
        year = y;
    }

    public void setAnnualIncome(double i) {
        annualIncome = i;
    }


}

package libai.fuzzy.operators.accumulation;

/**
 * Created by kronenthaler on 14/05/2017.
 */
public class MaxMethod extends Accumulation {
    @Override
    public double eval(double a, double b) {
        return Math.max(a, b);
    }

    @Override
    public String toString() {
        return "MAX";
    }
}

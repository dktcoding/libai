package libai.fuzzy.modifiers;

/**
 * Created by kronenthaler on 14/05/2017.
 */
public class Very extends Modifier {
	@Override
	public double eval(double y) {
		return Math.pow(y, 2);
	}
}

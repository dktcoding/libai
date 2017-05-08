package libai.fuzzy2.operators;

/**
 * Created by kronenthaler on 08/05/2017.
 */
public abstract class Operator {
	public abstract double eval(double a, double b);

	public static Operator fromString(String name){
		try{
			return AndMethod.fromString(name);
		}catch (UnsupportedOperationException ex){
			return OrMethod.fromString(name);
		}
	}
}

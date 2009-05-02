package net.sf.libai.common;

/**
 *	Signum function F(x) = 0 if x &lt; 0 or 1 if x &gt;= 1
 *	This function is not derivable.
 *	@author kronenthaler
 */
public class Signum implements Function{
	public double eval(double x) {
		return x<0 ? 0 : 1;
	}

	public Function getDerivate() {
		return null;
	}
}

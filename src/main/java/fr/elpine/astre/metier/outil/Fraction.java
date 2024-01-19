package fr.elpine.astre.metier.outil;

public class Fraction
{
	public static final String REGEX                  = "^((\\d+([\\.,]\\d+)?)|(\\d+\\/[1-9]\\d*))$";
	public static final String REGEX_FRACTION         = "^\\d+\\/[1-9]\\d*$";
	public static final String REGEX_CARACTERES_VALIDE = "^[\\d\\.,\\/]*$";

	private final int n; // numerator   / entier
	private final int d; // denominator / decimal
	private final boolean fraction;

	private Fraction( String str )
	{
		String[] splt = str.split("[\\/\\.,]");

		this.n = Integer.parseInt(splt[0]);
		this.d = splt.length == 1 ? 0 : Integer.parseInt(splt[1]);

		this.fraction = str.matches(REGEX_FRACTION);
	}

	private Fraction()
	{
		this.n = 0;
		this.d = 0;
		this.fraction = false;
	}

	public boolean equals( Fraction f )
	{
		return this.n == f.n && this.d == f.d && this.fraction == f.fraction;
	}

	public double value()
	{
		if (this.fraction)                   return (double) this.n / this.d;
		else if (this.d == 0 && this.n == 0) return 0d;
		else                                 return Double.parseDouble(String.format("%d.%d", this.n, this.d));
	}

	public String toString()
	{
		return toString( true );
	}

	public String toString( boolean showEmpty )
	{
		if ( this.d == 0 ) return String.format("%s", this.n == 0 && !showEmpty ? "" : this.n );
		else               return String.format( this.fraction ? "%d/%d" : "%d.%d", this.n, this.d );
	}

	public String serialize()
	{
		if (this.d == 0) if (this.n == 0) return ""; else return String.format( "%d", this.n );

		return String.format( this.fraction ? "%d/%d" : "%d.%d", this.n, this.d );
	}


	public static Fraction valueOf( String str )
	{
		if (str == null || ( !str.matches(REGEX) && !str.isEmpty() )) return null;

		return str.isEmpty() ? new Fraction() : new Fraction( str );
	}

	public static String simplifyDouble( Double d, boolean showEmpty )
	{
		int entier = (int) d.doubleValue();
		int decimal = (int) ((d - entier) * 10);

		if (entier != 0 && decimal != 0) return String.format("%d,%d", entier, decimal);
		if (entier != 0)                 return String.format("%d", entier);
		if (showEmpty)                   return "0";

		return "";
	}
}

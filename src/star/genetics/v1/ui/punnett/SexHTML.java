package star.genetics.v1.ui.punnett;

public enum SexHTML
{
	X("X<sup>A</sup>", "XA"), x("X<sup>a</sup>", "Xa"), Y("Y", "Y"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	String str;

	private SexHTML(String str, String str2)
	{
		this.str = str;
		// this.str = str2;
	}
}
package star.genetics.v1.ui.punnett;

public enum SexText
{

	XX("<html>&nbsp; X<sup>A</sup> X<sup>A</sup>&nbsp;</html>", "XX"), // //$NON-NLS-1$ //$NON-NLS-2$
	Xx("<html>&nbsp; X<sup>A</sup> X<sup>a</sup>&nbsp;</html>", "Xx"), // //$NON-NLS-1$ //$NON-NLS-2$
	xx("<html>&nbsp; X<sup>a</sup> X<sup>a</sup>&nbsp;</html>", "xx"), // //$NON-NLS-1$ //$NON-NLS-2$
	XY("<html>&nbsp; X<sup>A</sup> Y &nbsp;</html>", "XY"), xY("<html>&nbsp; X<sup>a</sup> Y &nbsp;</html>", "xY"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	SexText(String html, String code)
	{
		this.html = html;
		this.code = code;
	}

	String e0;
	String e1;

	String html;
	String code;

	@Override
	public String toString()
	{
		return html;
	}

	int length()
	{
		return code.length();
	}

	String charAt(int x)
	{
		return "" + code.charAt(x); //$NON-NLS-1$
	}

	String html(int x)
	{
		return SexHTML.valueOf(charAt(x)).str;
	}
}

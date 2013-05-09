package star.genetics.xls;

import java.util.zip.DataFormatException;

import star.genetics.events.OpenModelRaiser;

public interface PrivateIO
{
	byte[] save(byte[] s);

	byte[] load(byte[] s) throws DataFormatException;

	void openModel(OpenModelRaiser r, OpenModel load);
}

package star.genetics.xls;

import java.io.InputStream;

import star.genetics.genetic.model.Model;

public interface OpenModel
{
	void setModel(Model model, String modelName);

	public void raise_LoadModelEvent();

	public void setErrorMessage(Exception runtimeException);

	public void raise_ErrorDialogEvent();

	public Model load(InputStream is) throws ParseException;
}

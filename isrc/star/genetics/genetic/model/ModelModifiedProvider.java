package star.genetics.genetic.model;

public interface ModelModifiedProvider
{
	void modelSaved();

	boolean isModelModified();

	void saveModel();
}

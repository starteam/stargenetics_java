package star.genetics.genetic.model;

import java.util.Iterator;

public interface CrateSet extends Iterable<CrateModel>
{
	CrateModel current();

	Iterator<CrateModel> iterator();

	CrateModel newCrateModel();

	int size();
}

package star.genetics.genetic.model;

public interface Gel extends Iterable<GelPosition>
{
	String getName();

	int getIndex();

	void addGelPosition(GelPosition gp);

}

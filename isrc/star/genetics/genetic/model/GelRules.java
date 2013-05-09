package star.genetics.genetic.model;

public interface GelRules
{
	Iterable<Gel> getAllGelNames();

	Iterable<GelPosition> getGel(Iterable<Allele> alleles);

	Iterable<GelPosition> getAllGelPositions();

	int sizeGels();
}

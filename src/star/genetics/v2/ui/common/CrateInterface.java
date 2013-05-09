package star.genetics.v2.ui.common;

import java.awt.Component;

public interface CrateInterface
{
	star.genetics.v1.ui.model.CrateModel getModel();

	String getCrateName();

	void setCrateName(String str);

	Component getParent();
}

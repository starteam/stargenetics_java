package star.genetics.visualizers;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Map;

import star.genetics.genetic.model.Creature;

public interface AnimatedVisualizer
{
	int getDelayPerFrame();

	int getNumberOfFrames();

	public void setSex(Creature.Sex sex);

	public void setName(String name);

	public void setNote(String note);

	public void renderFrame(int frame, Graphics2D graphics);

	public void setProperties(Map<String, String> properties, Creature.Sex sex);

	public Map<String, String> getTooltipProperties();

	Dimension getPreferredSize();
}

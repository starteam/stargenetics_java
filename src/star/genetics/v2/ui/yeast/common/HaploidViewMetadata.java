/**
 * 
 */
package star.genetics.v2.ui.yeast.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map.Entry;

import star.genetics.genetic.model.Creature;

class HaploidViewMetadata implements Serializable
{
	private static final long serialVersionUID = 1L;
	ArrayList<Entry<Creature, String>> experiments;
	ArrayList<String> comments;
}
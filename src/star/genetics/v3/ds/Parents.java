package star.genetics.v3.ds;

import org.json.JSONArray;
import org.json.JSONException;

import star.genetics.genetic.model.Creature;
import star.genetics.genetic.model.CreatureSet;

public class Parents extends JSONArray
{
	public Parents(CreatureSet parents, Context context) throws JSONException
    {
		for( Creature c : parents)
		{
			this.put( new Organism(c,context));
		}
    }
}
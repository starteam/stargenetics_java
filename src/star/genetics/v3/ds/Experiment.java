package star.genetics.v3.ds;

import org.json.JSONException;
import org.json.JSONObject;

import star.genetics.genetic.model.CrateModel;

public class Experiment extends JSONObject
{
	public static String name = "name";
	public static String id = "id";
	public static String parents = "parents";
	public static String progenies = "progenies";
	
	public static String count = "count";
	
	public Experiment(CrateModel model, Context context ) throws JSONException
    {
		put(name, model.getName());
		put(id, model.getUUID());
		put(parents, new Parents(model.getParents(),context));
		
		JSONObject counts = new JSONObject();
		counts.put( parents , model.getParents().size() ) ;
		counts.put( progenies , model.getProgenies().size() ) ;
		put(count, counts ) ; 
    }
}
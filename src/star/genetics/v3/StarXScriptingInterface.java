package star.genetics.v3;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONException;

public interface StarXScriptingInterface
{
	void open(String url, String title);

	void save(OutputStream bos);

	void open_stream(String surl, String title, InputStream stream);

	public JSONArray listExperiments(StarXWebSocket socket) throws JSONException;

}

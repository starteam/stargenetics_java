package star.genetics.v3;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public interface StarXScriptingInterface
{

	void open(String url, String title);

	void save(OutputStream bos);

	void open_stream(String surl, String title, InputStream stream);

}

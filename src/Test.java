import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test
{

	static int getIndex(String username)
	{
		try
		{
			username = username.toLowerCase().trim();

			byte[] digest = MessageDigest.getInstance("MD5").digest(username.getBytes());
			int q = Math.abs(digest.length > 2 ? digest[1] : digest[0]);
			long value = 0;
			for (int i = 0; i < Math.min(4, digest.length); i++)
			{
				value = (value << 8) | (digest[i] & 0xff);
			}
			int size = 5;
			String user = username;
			String code = Long.toString(value, 26).toUpperCase();
			String msg = "Your code is: " + code + ".\nThis unique code for provided email address: " + username + "\nPlease write this information down.";
			int index = q % size;
			return index + 1;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return -1;
	}

	static void parse()
	{
		try
		{
			File f = new File("../Variation Mapping.txt");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while (null != (line = br.readLine()))
			{
				String[] parts = line.split("\t");
				boolean changed = false;
				for (int IDX = 4; IDX < parts.length - 2; IDX += 3)
				{
					String username = parts[IDX];
					username = username.toLowerCase().trim();

					byte[] digest = MessageDigest.getInstance("MD5").digest(username.getBytes());
					int q = Math.abs(digest.length > 2 ? digest[1] : digest[0]);
					long value = 0;
					for (int i = 0; i < Math.min(4, digest.length); i++)
					{
						value = (value << 8) | (digest[i] & 0xff);
					}
					int size = 5;
					String user = username;
					String code = Long.toString(value, 26).toUpperCase();
					String msg = "Your code is: " + code + ".\nThis unique code for provided email address: " + username + "\nPlease write this information down.";
					int index = q % size;

					// assertions!
					String vari = "" + (1 + index);
					vari = vari.trim();
					boolean same = vari.equals(parts[IDX + 2].trim());
					if (code.equals(parts[IDX + 1]))
					{
						if (!same)
						{
							changed = true;
						}
					}
					else
					{
						System.err.println("ISSUE WITH " + line);
					}
					parts[IDX + 2] = vari + ((!same) ? " (was " + parts[IDX + 2] + ")" : "");
				}
				StringBuffer sb = new StringBuffer();
				for (String s : parts)
				{
					sb.append(s);
					sb.append("\t");
				}
				if (changed)
				{
					sb.append("changed");
				}
				System.out.println(sb);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		parse();
		// System.out.println( getIndex( args[0] ) ) ;
	}
}

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.io.FileUtils;

public class PostToPHP {
	public static void insertFileIntoDB(String path) {
		/* create a new URL and open a connection */
		try {
			URL url = new URL("http://localhost/xampp_dagr_database/php/insert.php");
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			
			Map<String,String> arguments = new HashMap<>();
			Attributes fileAttributes = new Attributes(path);
			arguments.put("name", fileAttributes.fileName);
			arguments.put("owner", fileAttributes.owner);
			System.out.println(fileAttributes.lastModifiedTime.toString());
			StringJoiner sj = new StringJoiner("&");
			for(Map.Entry<String,String> entry : arguments.entrySet()) {
			    sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" 
			         + URLEncoder.encode(entry.getValue(), "UTF-8"));
			}
			byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
			int length = out.length;
			
			http.setFixedLengthStreamingMode(length);
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			http.connect();
			try(OutputStream os = http.getOutputStream()) {
			    os.write(out);
			}
			BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    http.getInputStream()));
            String str;
			while((str = in.readLine()) != null) {
				System.out.println(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void insertFolder(String path, boolean recursive) {
		Iterator<File> filesIterator = FileUtils.iterateFiles(new File(path), null, recursive);
		while (filesIterator.hasNext()) {
			File file = filesIterator.next();
			insertFileIntoDB(file.getAbsolutePath());
		}
	}
	
}

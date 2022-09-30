import java.net.*;
import java.io.*;
    
public class GetListing {
    public static void main(String[] args) throws Exception{

        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://localhost:8081/3940-assignment1-group5/uploadConsole");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String response = null, line = null;
            while ((line = br.readLine()) != null) {
                response += line;
            }
            br.close();
            conn.disconnect();
            if (response != null && response.length() > 0) {
                System.out.println(response);
            }
        } catch (Exception e) {
            //System.out.println("Web Access:", e.getMessage());
        }
    }
}
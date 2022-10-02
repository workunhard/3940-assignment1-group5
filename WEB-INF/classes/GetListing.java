import javax.servlet.http.Part;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.UUID;

public class GetListing {

    public static void get() throws Exception {
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

    public static void post() {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://localhost:8081/3940-assignment1-group5/uploadConsole");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            Scanner scanner = new Scanner(System.in);
            final String boundary = UUID.randomUUID().toString();
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            String fileName = scanner.next();
            String keyWord = scanner.next();
            String date = scanner.next();


            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            String params = "fileName=" + fileName + "&caption=" +keyWord +"&date=" +date;
            dos.writeChars("--" + boundary +
                    "Content-Disposition: form-data; name=\"fileName\"; " +
                    "filename=\"" + fileName + "\"" +
                    "Content-Type: application/octet-stream");
            dos.flush();
            dos.close();

            conn.setRequestProperty("keyword", keyWord);
            conn.setRequestProperty("date", date);


            conn.connect();

//            InputStream inputStream = conn.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//            String response = null, line = null;
//            while ((line = br.readLine()) != null) {
//                response += line;
//            }
//            br.close();
            conn.disconnect();
//            if (response != null && response.length() > 0) {
//                System.out.println(response);
//            }
        } catch (Exception e) {
            //System.out.println("Web Access:", e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception{
        post();
        get();

    }
}
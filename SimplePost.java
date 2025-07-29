import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class SimplePost {
    public static void main(String[] args) {
        try {
            // Create URL object
            URL url = new URL("http://polaris:8181/api/catalog/v1/oauth/tokens");
            
            // Open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // Set request method to POST
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "application/json");
            
            // Request body
            String body = "grant_type=client_credentials&client_id=d69694f1e84c4b1c&client_secret=11cffff66afef80b57e84709e5432889&scope=PRINCIPAL_ROLE:ALL";
            
            // Write request body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Get response code
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            
            // Read response
            BufferedReader reader;
            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line).append('\n');
            }
            reader.close();
            
            System.out.println("Response Body:");
            System.out.println(response.toString());
            
            // Close connection
            conn.disconnect();
            
        } catch (IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
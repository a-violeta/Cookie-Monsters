package org.example.grayscaleclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        // Path configuration
        String apiUrl = "http://localhost:5296/grayscale";

        Path inputPath = Path.of("C:\\Users\\windo\\Pictures\\Screenshots\\BB.jpg");
        Path outputPath = Path.of("C:\\Users\\windo\\Pictures\\Screenshots\\BBGrey.jpg");

        if (!Files.exists(inputPath)) {
            System.err.println("Source File not found" + inputPath.toAbsolutePath());
            return;
        }

        try {
            // Creation of multipart form data
            String boundary = "---JavaNativeBoundary" + System.currentTimeMillis();
            byte[] fileBytes = Files.readAllBytes(inputPath);

            String header = "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; name=\"file\"; filename=\"" + inputPath.getFileName() + "\"\r\n" +
                    "Content-Type: image/jpeg\r\n\r\n";

            String footer = "\r\n--" + boundary + "--\r\n";

            // Fused the header and footer with image
            byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);
            byte[] footerBytes = footer.getBytes(StandardCharsets.UTF_8);

            byte[] body = new byte[headerBytes.length + fileBytes.length + footerBytes.length];
            System.arraycopy(headerBytes, 0, body, 0, headerBytes.length);
            System.arraycopy(fileBytes, 0, body, headerBytes.length, fileBytes.length);
            System.arraycopy(footerBytes, 0, body, headerBytes.length + fileBytes.length, footerBytes.length);

            // HTTP request for server
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                    .build();

            System.out.println("Sending file to microservice ...");

            // Execute and get the filtered image
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                // Save the image
                Files.write(outputPath, response.body());
                System.out.println("Succes File saved : ");
                System.out.println(outputPath.toAbsolutePath());
            } else {
                System.err.println("API Error Code : " + response.statusCode());
                System.err.println("Details : " + new String(response.body(), StandardCharsets.UTF_8));
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Server not found");
            e.printStackTrace();
        }
    }
}
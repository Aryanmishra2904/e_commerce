package com.aryan.e_commerce.imagekit;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class ImageKitService {

    @Value("${imagekit.publicKey}")
    private String publicKey;

    @Value("${imagekit.privateKey}")
    private String privateKey;

    private final OkHttpClient client = new OkHttpClient();

    public String upload(MultipartFile file) {

        System.out.println("📥 Starting ImageKit upload...");

        try {
            // 1️⃣ Validate file
            if (file == null || file.isEmpty()) {
                System.out.println("❌ File is null or empty");
                return null;
            }

            System.out.println("📄 File name: " + file.getOriginalFilename());
            System.out.println("📦 File size: " + file.getSize());
            System.out.println("🧾 Content type: " + file.getContentType());

            // 2️⃣ Prepare upload URL
            String uploadUrl = "https://upload.imagekit.io/api/v1/files/upload";
            System.out.println("🌐 Upload URL: " + uploadUrl);

            // 3️⃣ Create file body
            RequestBody fileBody = RequestBody.create(
                    file.getBytes(),
                    MediaType.parse(
                            Objects.requireNonNullElse(
                                    file.getContentType(),
                                    "application/octet-stream"
                            )
                    )
            );

            System.out.println("✅ File body created");

            // 4️⃣ Build multipart request
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getOriginalFilename(), fileBody)
                    .addFormDataPart("fileName", file.getOriginalFilename())
                    .addFormDataPart("folder", "/products") // optional but recommended
                    .addFormDataPart("useUniqueFileName", "true")
                    .build();

            System.out.println("✅ Multipart body built");

            // 5️⃣ Create auth header
            String auth = Credentials.basic(publicKey, privateKey);
            System.out.println("🔐 Authorization header created");

            // 6️⃣ Build request
            Request request = new Request.Builder()
                    .url(uploadUrl)
                    .header("Authorization", auth)
                    .post(requestBody)
                    .build();

            System.out.println("🚀 Sending request to ImageKit...");

            // 7️⃣ Execute request
            try (Response response = client.newCall(request).execute()) {

                System.out.println("📡 Response received");
                System.out.println("📟 HTTP Status Code: " + response.code());

                String responseBody = response.body() != null ? response.body().string() : null;

                if (!response.isSuccessful()) {
                    System.out.println("❌ ImageKit upload failed");
                    System.out.println("🧨 Response body: " + responseBody);
                    return null;
                }

                // 8️⃣ Parse JSON
                JSONObject json = new JSONObject(responseBody);

                String imageUrl = json.getString("url");
                String fileId = json.getString("fileId");

                System.out.println("✅ Upload successful!");
                System.out.println("🖼️ Image URL: " + imageUrl);
                System.out.println("🆔 File ID: " + fileId);

                // 👉 Store BOTH in DB ideally
                return imageUrl;
            }

        } catch (Exception e) {
            System.out.println("🔥 Exception during ImageKit upload");
            e.printStackTrace();
            return null;
        }
    }
}

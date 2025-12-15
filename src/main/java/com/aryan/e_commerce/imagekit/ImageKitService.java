package com.aryan.e_commerce.imagekit;

import okhttp3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class ImageKitService {

    private static final Logger log =
            LoggerFactory.getLogger(ImageKitService.class);

    @Value("${imagekit.privateKey}")
    private String privateKey;

    private final OkHttpClient client = new OkHttpClient();

    public String upload(MultipartFile file) {

        log.info("📥 ImageKit upload STARTED");

        try {
            // 1️⃣ Validate file
            if (file == null || file.isEmpty()) {
                log.error("❌ File is null or empty");
                return null;
            }

            log.info("📄 File name: {}", file.getOriginalFilename());
            log.info("📦 File size: {} bytes", file.getSize());
            log.info("🧾 Content type: {}", file.getContentType());

            // 2️⃣ Upload URL
            String uploadUrl = "https://upload.imagekit.io/api/v1/files/upload";
            log.debug("🌐 Upload URL: {}", uploadUrl);

            // 3️⃣ File body
            RequestBody fileBody = RequestBody.create(
                    file.getBytes(),
                    MediaType.parse(
                            Objects.requireNonNullElse(
                                    file.getContentType(),
                                    "application/octet-stream"
                            )
                    )
            );
            log.debug("✅ File body created");

            // 4️⃣ Multipart body
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getOriginalFilename(), fileBody)
                    .addFormDataPart("fileName", file.getOriginalFilename())
                    .addFormDataPart("folder", "/products")
                    .addFormDataPart("useUniqueFileName", "true")
                    .build();

            log.debug("✅ Multipart body built");

            // 5️⃣ Authorization header (CORRECT)
            String auth = Credentials.basic(privateKey, "");
            log.debug("🔐 Authorization header generated (private key only)");

            // 6️⃣ Request
            Request request = new Request.Builder()
                    .url(uploadUrl)
                    .header("Authorization", auth)
                    .post(requestBody)
                    .build();

            log.info("🚀 Sending request to ImageKit");

            // 7️⃣ Execute request
            try (Response response = client.newCall(request).execute()) {

                log.info("📡 Response received from ImageKit");
                log.info("📟 HTTP Status Code: {}", response.code());

                String responseBody =
                        response.body() != null ? response.body().string() : null;

                if (!response.isSuccessful()) {
                    log.error("❌ ImageKit upload FAILED");
                    log.error("🧨 Response body: {}", responseBody);
                    return null;
                }

                // 8️⃣ Parse response
                JSONObject json = new JSONObject(responseBody);

                String imageUrl = json.getString("url");
                String fileId = json.getString("fileId");

                log.info("✅ Image uploaded SUCCESSFULLY");
                log.info("🖼️ Image URL: {}", imageUrl);
                log.info("🆔 ImageKit File ID: {}", fileId);

                return imageUrl;
            }

        } catch (Exception e) {
            log.error("🔥 Exception during ImageKit upload", e);
            return null;
        }
    }
}

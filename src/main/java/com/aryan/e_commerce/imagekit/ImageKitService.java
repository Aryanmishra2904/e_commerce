package com.aryan.e_commerce.imagekit;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageKitService {

    @Value("${imagekit.publicKey}")
    private String publicKey;

    @Value("${imagekit.privateKey}")
    private String privateKey;

    private final OkHttpClient client = new OkHttpClient();

    public String upload(MultipartFile file) {

        String uploadUrl = "https://upload.imagekit.io/api/v1/files/upload";

        try {
            RequestBody fileBody = RequestBody.create(
                    file.getBytes(),
                    MediaType.parse(file.getContentType())
            );

            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getOriginalFilename(), fileBody)
                    .addFormDataPart("fileName", file.getOriginalFilename())
                    .build();

            Request request = new Request.Builder()
                    .url(uploadUrl)
                    .header("Authorization", Credentials.basic(publicKey, privateKey))
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                System.out.println("❌ ImageKit upload failed: " + response.code());
                return null;
            }

            String jsonResponse = response.body().string();
            System.out.println("✅ ImageKit response: " + jsonResponse);

            JSONObject json = new JSONObject(jsonResponse);
            return json.getString("url");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

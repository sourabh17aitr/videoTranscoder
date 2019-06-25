package com.acheron.transcoder.gcp;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;

@Component
public class FFmpegTranscoder {

	Storage storage = null;
	private static String bucketName = "acheron_transcode_video";

	public void startTranscode(MultipartFile files) {
		try {
			authExplicit("./crypto-hallway-244715-3b3f4d3e01b9.json");
			byte[] fileByteArr = (files).getBytes();
			uploadToGCP("my image", fileByteArr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void authExplicit(String jsonPath) throws IOException {
		// You can specify a credential file by providing a path to GoogleCredentials.
		// Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS
		// environment variable.
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
				.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
		storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		System.out.println("credentials:" + credentials.toString());
		System.out.println("Buckets:");
		Page<Bucket> buckets = storage.list();
		for (Bucket bucket : buckets.iterateAll()) {
			System.out.println(bucket.toString());
		}
	}

	@SuppressWarnings("deprecation")
	public void uploadToGCP(String filePath, byte[] file) {
		storage.create(BlobInfo.newBuilder(bucketName, filePath).build(), new ByteArrayInputStream(file));
	}
}

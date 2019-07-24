package com.acheron.transcoder.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GCPUploadService {


	Storage storage = null;
	
	@Autowired
	private Environment env;

	public void authExplicit() throws IOException {
		
		System.out.println("Authenticating");        
		String GCPUserTokenPath = env.getProperty("GCPUserTokenPath");
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(GCPUserTokenPath));
		storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
	}
	
	@SuppressWarnings("deprecation")
	public void uploadToGCP(String filePath, byte[] file) {
		String bucketName = env.getProperty("GCPBUCKETNAME");
		storage.create(BlobInfo.newBuilder(bucketName, filePath).build(), new ByteArrayInputStream(file));
	}

	public String uploadImageFile(String filePath) {
		String fileName = "";
		try {
			authExplicit();
			File file = new File(filePath);
			fileName = file.getName();
			System.out.println("File size" + file.getTotalSpace());
			byte[] fileByteArr = Files.readAllBytes(Paths.get(filePath));
			uploadToGCP(fileName, fileByteArr);
		} catch (IOException e) {
			System.out.println("Exception on image upload " + e);
		}

		return fileName;
	}
}

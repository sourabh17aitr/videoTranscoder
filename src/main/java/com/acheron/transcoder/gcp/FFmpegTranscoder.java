package com.acheron.transcoder.gcp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
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
			getFilePath("./crypto-hallway-244715-3b3f4d3e01b9.json", "my image");
			
			
			generateVideoScreenShots("C:/Users/Sourabh/Pictures/transcode/y2mate.com - nature_beautiful_short_video_720p_hd_668nUCeBHyY_360p.mp4", "C:/Users/Sourabh/Pictures/transcode");
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
	
	@SuppressWarnings("deprecation")
	public void getFilePath(String jsonPath, String blobNames) throws FileNotFoundException, IOException {
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
				.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
		storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		System.out.println("credentials:" + credentials.toString());
		System.out.println("Buckets:");
		Page<Bucket> buckets = storage.list();
		for (Bucket bucket : buckets.iterateAll()) {
			System.out.println(bucket.toString());
			Blob blob = bucket.get(blobNames);
			System.out.println(blob.getSelfLink());
			System.out.println(blob.getMediaLink());
		}
	}

	String ffMpegInitialCommandLineScript = " -y -i ";
	String ffMpegEndCommandLineScript = " -ss 00:00:10 -vframes 1 ";

	/**
	 * Captures a screen from a video and convert it to an image format.
	 * 
	 * @param inputPath
	 * @param outputPath
	 * @return Destination of the output file that got generated
	 */
	public String generateVideoScreenShots(String inputPath, String outputPath) {
		if ((inputPath == null || outputPath == null) || (inputPath == "" || outputPath == "")) {
			throw new IllegalArgumentException("Mandatory parameters missing for generateVideoScreenShots");
		}
		//String videoUtilityExecutionDirectory = System.getenv("ffmpeg_home");
		String videoUtilityExecutionDirectory = "/usr/bin/ffmpeg";
		// String[] commandArray = new String[] { "-y", "-i", inputPath, "-ss",
		// "00:00:10", "-vframes", "1", outputPath };
		List<String> commandList = new ArrayList<String>();
		commandList.add(videoUtilityExecutionDirectory);
		commandList.add("-y");
		commandList.add("-i");
		commandList.add(inputPath);
		commandList.add("-ss");
		commandList.add("00:00:10");
		commandList.add("-vframes");
		commandList.add("1");
		commandList.add(outputPath);

		String executionCommand = ffMpegInitialCommandLineScript + "'" + inputPath + "'" + ffMpegEndCommandLineScript
				+ "'" + outputPath + "'";
		executionCommand = videoUtilityExecutionDirectory + executionCommand;
		System.out.println("Command  is a " + executionCommand);
		System.out.println("Command Array is " + commandList.toString());
		// logger.error("command list is " + commandArray.toString());
		// Runtime rt = Runtime.getRuntime();
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(commandList);
			System.out.println("Pb Command String is " + processBuilder.command().toString());
			Process pr = processBuilder.start();
			// Process pr = rt.exec(commandArray);
			try {
				int exitVal = pr.waitFor();
				System.out.println("Exit value is " + exitVal);
				System.out.println("Error Stream is " + pr.getErrorStream());
				System.out.println("Output Stream is " + pr.getOutputStream().toString());
				pr.getOutputStream();
				BufferedReader input = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
				String line = null;
				while ((line = input.readLine()) != null) {
					System.out.println(line);
				}
				if (exitVal == 0) {
					return outputPath;
				}
			} catch (InterruptedException e) {
				System.out.println("Failed while capturing shots from the provided video in path " + inputPath
						+ " with exception " + e);
			}
		} catch (IOException e) {
			System.out.println("Failed while getting capturing shots from the provided video in path " + inputPath
					+ " with exception " + e);
		}

		return "";
	}
}

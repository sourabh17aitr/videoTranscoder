package com.acheron.transcoder.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;

@RestController
public class MainController {

	@RequestMapping("/")
	public String hello() {
		try {
			String inputPath = "gs://acheron_transcode_video/sample.mp4";
			String outputPath = "gs://acheron_transcode_video/sample1.mp4";
			/*
			 * String inputPath = "C:\\Users\\Sourabh\\Pictures\\transcode\\sample.mp4";
			 * String outputPath = "C:\\Users\\Sourabh\\Pictures\\transcode\\sample1.jpg";
			 */
			generateVideoScreenShots(inputPath, outputPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Hello Spring Boot!";
	}

	String ffMpegInitialCommandLineScript = " -y -i ";
	String ffMpegEndCommandLineScript = " -ss 00:00:10 -vframes 1 ";
	String videoUtilityExecutionDirectory = "C:\\ffmpeg\\bin\\ffmpeg";
	Storage storage = null;
	private static String bucketName = "acheron_transcode_video";
	String blobNames = "my image";
	String jsonPath = "./crypto-hallway-244715-3b3f4d3e01b9.json";

	/**
	 * Captures a screen from a video and convert it to an image format.
	 * 
	 * @param inputPath
	 * @param outputPath
	 * @return Destination of the output file that got generated
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public String generateVideoScreenShots(String inputPath, String outputPath)
			throws FileNotFoundException, IOException {
		if ((inputPath == null || outputPath == null) || (inputPath == "" || outputPath == "")) {
			throw new IllegalArgumentException("Mandatory parameters missing for generateVideoScreenShots");
		}

		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath));
		storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		System.out.println("credentials:" + credentials.toString());
		System.out.println("Buckets:");
		Page<Bucket> buckets = storage.list();
		for (Bucket bucket : buckets.iterateAll()) {
			System.out.println(bucket.toString());
			Blob blob = bucket.get(blobNames);
			if (blob != null) {
				System.out.println(blob.getSelfLink());
				System.out.println(blob.getMediaLink());
			}
		}

		// String videoUtilityExecutionDirectory = System.getenv("ffmpeg_home");

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

package com.acheron.transcoder.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
		String videoUtilityExecutionDirectory = "ffmpeg";
		List<String> commandList = new ArrayList<String>();
		commandList.add(videoUtilityExecutionDirectory);
		commandList.add("-y");
		commandList.add("-i");
		commandList.add(inputPath);
		commandList.add("-ss");
		commandList.add("00:00:30");
		commandList.add("-vframes");
		commandList.add("3");
		commandList.add(outputPath);

		/*String executionCommand = ffMpegInitialCommandLineScript + inputPath + ffMpegEndCommandLineScript
				+ "'" + outputPath + "'";
		executionCommand = videoUtilityExecutionDirectory + executionCommand;
		System.out.println("Command  is a " + executionCommand);
		System.out.println("Command Array is " + commandList.toString());*/
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
	
	public String createVideoMultiplesshots(String inputPath, String outputPath) {
		if ((inputPath == null || outputPath == null) || (inputPath == "" || outputPath == "")) {
			throw new IllegalArgumentException("Mandatory parameters missing for generateVideoScreenShots");
		}
		String videoUtilityExecutionDirectory = "ffmpeg";
		List<String> commandList = new ArrayList<String>();
		commandList.add(videoUtilityExecutionDirectory);
		commandList.add("-y");
		commandList.add("-i");
		commandList.add(inputPath);
		commandList.add(outputPath);
		
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(commandList);
			System.out.println("Pb Command String is " + processBuilder.command().toString());
			Process pr = processBuilder.start();
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

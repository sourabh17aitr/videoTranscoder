package com.acheron.transcoder.gcp;

import org.springframework.stereotype.Component;

public class FFmpegTranscoder {

	/*Storage storage = null;
	private static String bucketName = "acheron_transcode_video";
	
	

	public void startTranscode(MultipartFile files) throws IllegalArgumentException {
		try {
			authExplicit("./crypto-hallway-244715-3b3f4d3e01b9.json");
			byte[] fileByteArr = (files).getBytes();
			// uploadToGCP("my image", fileByteArr);
			startVideoTranscode();
			// getFilePath("./crypto-hallway-244715-3b3f4d3e01b9.json", "my image");

			// generateVideoScreenShots("C:/Users/Sourabh/Pictures/transcode/sample.mp4","C:/Users/Sourabh/Pictures/transcode");
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

	*//**
	 * Captures a screen from a video and convert it to an image format.
	 * 
	 * @param inputPath
	 * @param outputPath
	 * @return Destination of the output file that got generated
	 *//*
	public String generateVideoScreenShots(String inputPath, String outputPath) {
		if ((inputPath == null || outputPath == null) || (inputPath == "" || outputPath == "")) {
			throw new IllegalArgumentException("Mandatory parameters missing for generateVideoScreenShots");
		}
		// String videoUtilityExecutionDirectory = System.getenv("ffmpeg_home");
		String videoUtilityExecutionDirectory = "ffmpeg";
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
		commandList.add("70");
		commandList.add(outputPath);

		String executionCommand = ffMpegInitialCommandLineScript + inputPath + ffMpegEndCommandLineScript
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

	public void startVideoTranscode() throws IllegalArgumentException {

		File source = new File("D:/Work/GCP/transcoder/videoTranscoder/asset/sample.mp4");
		File target = new File("/transcoder/asset/sample2.avi");
	}*/
}

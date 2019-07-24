package com.acheron.transcoder.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@RestController
public class GCPVideoTranscode {
	
	Storage storage = null;
	private static String bucketName = "acheron_transcode_video";
	// private static String GCPUserTokenPath =
	// "/transcoder/file/GCPUserToken.json";

	private static String GCPUserTokenPath = "/transcoder/crypto-hallway-244715-3b3f4d3e01b9.json";

	/*@PostMapping("startVideoTranscode")
	public String startVideoTranscodeWorkflow(@RequestParam(value = "fileName", required = true) String fileName)
			throws IllegalAccessException, IOException, JCodecException {

		String transcodedImageFilePath = getTranscodedFilePath(fileName);
		System.out.println("Transcode image File path "+ transcodedImageFilePath);
		String imgFileName = fileName.replaceFirst("[.][^.]+$", "");
		return "file " + fileName + " successfully transcoded " + fileName;
	}*/
	
	public String getTranscodedFilePath(String fileName) throws IllegalAccessException, IOException, JCodecException {
		byte[] bytes = startTranscode(fileName);
		String imgFileName = fileName.replaceFirst("[.][^.]+$", "");
		Path destFilePath = Paths.get("/mnt/"+imgFileName + ".png");
		Files.write(destFilePath, bytes);
		//File imgFile = destFilePath.toFile();
		return destFilePath.toString();
	}
	
	public byte[] startTranscode(String fileName) throws IOException, JCodecException {

		String imgFileName = fileName.replaceFirst("[.][^.]+$", "");

		File file = getVideoFile(fileName);

		//File file = new File("C:\\Users\\Sourabh\\Pictures\\transcode\\sample.mp4");

		int frameNumber = 43;

		System.out.println(file.getTotalSpace());
		Picture picture = FrameGrab.getFrameFromFile(file, frameNumber);
		BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);

		byte[] imageInByte = getImageInByte(bufferedImage);
		uploadVideo(imageInByte, imgFileName+".png");
		return imageInByte;

	}
	public byte[] getImageInByte(BufferedImage bufferedImage) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}
	
	public void uploadVideo(byte[] fileByteArr, String fileName) {
		try {
			authExplicit();
			uploadToGCP(fileName, fileByteArr);
		} catch (IOException e) {
			System.out.println("Exception is" + e);
		}
	}
	
	public void authExplicit() throws IOException {

		System.out.println("Authenticating");

		BufferedReader in = new BufferedReader(new FileReader(GCPUserTokenPath));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
		in.close();

		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(GCPUserTokenPath));

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
		System.out.println("uploading to GCP");
		storage.create(BlobInfo.newBuilder(bucketName, filePath).build(), new ByteArrayInputStream(file));
	}

	public File getVideoFile(String fileName) throws IOException {

//			Path destFilePath = Paths.get(System.getProperty("java.io.tmpdir")+"/"+filename);
		authExplicit();
//			Path destFilePath = Paths.get("E:\\snapshot\\video.mp4");
		System.out.println("filename is df " + fileName);
		Path destFilePath = Paths.get(System.getProperty("java.io.tmpdir") + "/" + fileName);

		Blob blob = storage.get(BlobId.of(bucketName, fileName));
		blob.downloadTo(destFilePath);
		File file = destFilePath.toFile();
		return file;
	}
}

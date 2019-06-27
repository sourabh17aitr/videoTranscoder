package com.acheron.transcoder.workflow;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.acheron.transcoder.gcp.GCPUpload;


@Component
public class Workflow {
	final static Logger log = Logger.getLogger(Workflow.class);
	
	@Autowired
	GCPUpload gcpUpload;
	
	public void startWorkflow(MultipartFile videofile, String imageName) throws IOException, JCodecException {
		try {
			//byte[] videoFileByteArr = (videofile).getBytes();
			byte[] imageFileByteArr = startTranscode(videofile);
			gcpUpload.uploadFile(imageFileByteArr, imageName);
		} catch (IOException | JCodecException e) {
			log.error("Exception is" + e);
			e.printStackTrace();
		}
	}

	public byte[] startTranscode(MultipartFile multipartFile) throws IOException, JCodecException {
		File file = convert(multipartFile);
		int frameNumber = 43;
		System.out.println(file.getName());
		Picture picture = FrameGrab.getFrameFromFile(file, frameNumber);
		System.out.println(frameNumber);
		BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
		System.out.println(file.getName());
		//ImageIO.write(bufferedImage, "png", new File("C:/Users/Sourabh/Pictures/transcode/news.png"));
		byte[] imageInByte = getImageInByte(bufferedImage);
		return imageInByte;

	}

	public File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public byte[] getImageInByte(BufferedImage bufferedImage) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}

}

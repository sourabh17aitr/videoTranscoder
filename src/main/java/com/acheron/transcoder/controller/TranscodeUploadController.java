package com.acheron.transcoder.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.Logger;
import org.apache.tools.ant.util.FileUtils;
import org.jcodec.api.JCodecException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.acheron.transcoder.gcp.FFmpegTranscoder;
import com.acheron.transcoder.service.GCPUploadService;
import com.acheron.transcoder.workflow.Workflow;


@RestController
@RequestMapping("transcode/")
public class TranscodeUploadController {

	@Autowired
	Workflow workflow;
	final static Logger log = Logger.getLogger(TranscodeUploadController.class);
	
	@Autowired
	FFmpegTranscoder ffmpegTranscoder;
	
	@Autowired
	GCPUploadService gcpUploadService;
	
	@PostMapping("transcode")
	public String transcode(@RequestParam(value = "videoFilePath", required = true) String videoFilePath) throws IOException {
		doTranscode(videoFilePath);
		return "video transcoded";
	}
	
	@GetMapping("startVideoTranscode")
	public String startTranscode() throws IOException {
		doTranscode("http://www.jell.yfish.us/media/jellyfish-20-mbps-hd-hevc-10bit.mkv");
		return "video transcoded";
	}
	
	public void doTranscode(String videoPath) throws IOException {
		Path inputPathPath = download(videoPath, "/usr/bin");
		System.out.println("Video input path" + inputPathPath);
		ffmpegTranscoder.generateVideoScreenShots(inputPathPath.toString(), "/usr/bin/thumb.jpg");
		System.out.println("Uploading Image");
		gcpUploadService.uploadImageFile("/usr/bin/thumb.jpg");
	}
	
	private Path download(String sourceURL, String targetDirectory) throws IOException
	{
	    URL url = new URL(sourceURL);
	    String fileName = sourceURL.substring(sourceURL.lastIndexOf('/') + 1, sourceURL.length());
	    Path targetPath = new File(targetDirectory + File.separator + fileName).toPath();
	    Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

	    return targetPath;
	}
}

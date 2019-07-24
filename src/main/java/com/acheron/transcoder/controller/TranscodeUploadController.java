package com.acheron.transcoder.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.acheron.transcoder.service.FFmpegTranscoder;
import com.acheron.transcoder.service.GCPUploadService;


@RestController
@RequestMapping("transcode/")
public class TranscodeUploadController {
	
	@Autowired
	FFmpegTranscoder ffmpegTranscoder;
	
	@Autowired
	GCPUploadService gcpUploadService;
	
	@PostMapping("transcodeByPath")
	public String transcodeByPath(@RequestParam(value = "videoFilePath", required = true) String videoFilePath) throws IOException {
		doTranscode(videoFilePath);
		return "video transcoded";
	}
	
	@GetMapping("startVideoTranscode")
	public String startTranscode() throws IOException {
		doTranscode("http://www.jell.yfish.us/media/jellyfish-20-mbps-hd-hevc-10bit.mkv");
		return "video transcoded";
	}
	
	public void doTranscode(String videoPath) throws IOException {
		Path inputPathPath = download(videoPath, "/usr/bin/");
		System.out.println("Video input path" + inputPathPath);
		LocalTime time = LocalTime.now();
		String fileName = "/usr/bin/thumb_"+time+".jpg";
		ffmpegTranscoder.generateVideoScreenShots(inputPathPath.toString(), fileName);
		System.out.println("Uploading Image");
		gcpUploadService.uploadImageFile(fileName);
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

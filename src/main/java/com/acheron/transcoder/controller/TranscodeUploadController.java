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
import com.acheron.transcoder.workflow.Workflow;


@RestController
@RequestMapping("startVideoTranscode")
public class TranscodeUploadController {

	@Autowired
	Workflow workflow;
	final static Logger log = Logger.getLogger(TranscodeUploadController.class);
	
	@Autowired
	FFmpegTranscoder ffmpegTranscoder;
	
	/*@PostMapping
	public String startVideoTranscode(@RequestParam("files") MultipartFile file,
			@RequestParam(value = "assetId", required = true) String assetId,
			@RequestParam(value = "fileName", required = true) String fileName,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "parentFolderId", required = false) String parentFolderId,
			@RequestParam(value = "metadata", required = false) String metadataRaw)
			throws IllegalAccessException, IOException, IllegalArgumentException {

		if (assetId == null || assetId == "" || assetId.isEmpty()) {
			throw new IllegalArgumentException("Mandatory Parameters missing, tenantId");
		}
		System.out.println(assetId + fileName);
		
		try {
			workflow.startWorkflow(file, fileName);
		} catch (JCodecException e) {
			log.error("Exception is" + e);
			e.printStackTrace();
		}
		return "Success";
	}*/
	@GetMapping
	public String startTranscode() throws IOException {
		Path inputPathPath = download("http://www.jell.yfish.us/media/jellyfish-20-mbps-hd-hevc-10bit.mkv", "/usr/bin/thumb.jpg");
		System.out.println("Video input path" + inputPathPath);
		ffmpegTranscoder.generateVideoScreenShots("http://www.jell.yfish.us/media/jellyfish-20-mbps-hd-hevc-10bit.mkv", "/usr/bin/thumb.jpg");
		return "video transcoded";
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

package com.acheron.transcoder.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jcodec.api.JCodecException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.acheron.transcoder.workflow.Workflow;


@RestController
@RequestMapping("transcode/api/v1/upload")
public class TranscodeUploadController {

	@Autowired
	Workflow workflow;
	final static Logger log = Logger.getLogger(TranscodeUploadController.class);
	
	@PostMapping
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
	}
}

package com.acheron.transcoder.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

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

	@PostMapping
	public String startVideoTranscode(@RequestParam("files") MultipartFile files,
			@RequestParam(value = "assetId", required = true) String assetId,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "parentFolderId", required = false) String parentFolderId,
			@RequestParam(value = "metadata", required = false) String metadataRaw)
			throws IllegalAccessException, IOException {

		if (assetId == null || assetId == "" || assetId.isEmpty()) {
			throw new IllegalArgumentException("Mandatory Parameters missing, tenantId");
		}
		/*
		 * byte[] byteArr = (files).getBytes(); InputStream inputStream = new
		 * ByteArrayInputStream(byteArr); System.out.println(files.getContentType());
		 */

		workflow.startWorkflow(files);
		System.out.println(assetId);
		return "Success";
	}
}

package com.acheron.transcoder.workflow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.acheron.transcoder.gcp.FFmpegTranscoder;

@Component
public class Workflow {

	@Autowired
	FFmpegTranscoder ffmpegTranscoder;

	public void startWorkflow(MultipartFile files) {
		ffmpegTranscoder.startTranscode(files);
	}

}

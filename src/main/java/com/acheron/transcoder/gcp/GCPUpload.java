package com.acheron.transcoder.gcp;

import org.springframework.stereotype.Component;

@Component
public class GCPUpload {

	/*final static Logger log = Logger.getLogger(GCPUpload.class);

	Storage storage = null;
	private static String bucketName = "acheron_transcode_video";
	private static String GCPUserTokenPath = "./crypto-hallway-244715-3b3f4d3e01b9.json";

	public void uploadVideo(MultipartFile files, String fileName) {
		try {
			authExplicit();
			byte[] fileByteArr = (files).getBytes();
			uploadToGCP(fileName, fileByteArr);
		} catch (IOException e) {
			log.error("Exception is" + e);
		}
	}

	public void uploadFile(byte[] file, String fileName) {
		try {
			log.debug("GCP Authentication");
			authExplicit();
			log.debug("GCP Upload");
			uploadToGCP(fileName, file);
			log.debug("GCP Upload is done");
		} catch (IOException e) {
			log.error("Exception is" + e);
			e.printStackTrace();
		}
	}

	public void authExplicit() throws IOException {

		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(GCPUserTokenPath));

		storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		log.debug("credentials:" + credentials.toString());
		log.debug("Buckets:");
		//System.out.println("credentials:" + credentials.toString());

		Page<Bucket> buckets = storage.list();
		for (Bucket bucket : buckets.iterateAll()) {
			//log.error(bucket.toString());
		}

	}

	@SuppressWarnings("deprecation")
	public CompletableFuture<Boolean> uploadToGCP(String filePath, byte[] file) {
		storage.create(BlobInfo.newBuilder(bucketName, filePath).build(), new ByteArrayInputStream(file));
		return CompletableFuture.completedFuture(true);
	}*/
}

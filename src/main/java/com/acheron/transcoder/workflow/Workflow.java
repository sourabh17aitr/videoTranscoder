package com.acheron.transcoder.workflow;

import org.springframework.stereotype.Component;


@Component
public class Workflow {
	//final static Logger log = Logger.getLogger(Workflow.class);
	/*private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	GCPUpload gcpUpload;
	
	public void startWorkflow(MultipartFile videofile, String imageName) throws IOException, JCodecException {
		try {
			//byte[] videoFileByteArr = (videofile).getBytes();
			byte[] imageFileByteArr = startTranscode(videofile);
			log.debug("file transcode is done");
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
		//file.deleteOnExit();
		return imageInByte;

	}

	public File convert(MultipartFile file) throws IOException {
		File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+"test.mp4");
		file.transferTo(convFile);
		log.debug("Converting Multipart file converted to file object");
	    return convFile;
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		//return convFile;
	}

	public byte[] getImageInByte(BufferedImage bufferedImage) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}*/

}

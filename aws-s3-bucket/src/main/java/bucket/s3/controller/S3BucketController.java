package bucket.s3.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bucket.s3.service.S3BucketService;

@RestController
public class S3BucketController {

	@Autowired
	private S3BucketService s3BucketService;

	@RequestMapping(value = "/createBucket/{bucketName}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public String createBucket(@PathVariable(name = "bucketName", required = true) String bucketName) {
		return s3BucketService.createBucket(bucketName);
	}

	@RequestMapping(value = "/upload/{bucketName}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public String uploadFile(@RequestBody(required = true) MultipartFile file,
			@PathVariable(name = "bucketName", required = true) String bucketName) {
		return s3BucketService.uploadFile(file, bucketName);
	}

	@RequestMapping(value = "/getBuckets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getBuckets() {
		return s3BucketService.getBuckets();
	}

	@RequestMapping(value = "/listObjects/{bucketName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> listObjectsInBucket(@PathVariable(name = "bucketName", required = true) String bucketName) {
		return s3BucketService.listObjectsInBucket(bucketName);
	}

	@RequestMapping(value = "/download/{bucketName}/{fileName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void downloadFile(@PathVariable(name = "bucketName", required = true) String bucketName,
			@PathVariable(name = "fileName", required = true) String fileName, HttpServletResponse res) {
		s3BucketService.downloadFile(bucketName, fileName, res);
	}

	@RequestMapping(value = "/delete/{bucketName}/{fileName}", method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String deleteObject(@PathVariable(name = "bucketName", required = true) String bucketName,
			@PathVariable(name = "fileName", required = true) String fileName) {
		return s3BucketService.deleteObjectFromBucket(bucketName, fileName);
	}

	@RequestMapping(value = "/delete/{bucketName}", method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String deleteBucket(@PathVariable(name = "bucketName", required = true) String bucketName) {
		return s3BucketService.deleteBucket(bucketName);
	}
}

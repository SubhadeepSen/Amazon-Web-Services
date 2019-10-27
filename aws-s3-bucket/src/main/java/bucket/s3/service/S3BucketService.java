package bucket.s3.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;

@Service
public class S3BucketService {

	@Autowired
	private AmazonS3 s3Client;

	@Value("${temp.file.path}")
	private String tempPath;

	public String createBucket(String bucketName) {
		if (!s3Client.doesBucketExist(bucketName)) {
			s3Client.createBucket(bucketName);
			return "Bucket with name " + bucketName + " has been created.";
		}
		return "Bucket with name \'" + bucketName + "\' already exist, " + "please choose a different bucket name.";
	}

	public String uploadFile(MultipartFile file, String bucketName) {
		String response = "Bucket " + bucketName + " does not exit.";
		if (s3Client.doesBucketExist(bucketName)) {
			try {
				File tempFile = new File(tempPath + file.getOriginalFilename());
				FileOutputStream fos = new FileOutputStream(tempFile);
				fos.write(file.getBytes());
				fos.close();
				s3Client.putObject(bucketName, file.getOriginalFilename(), tempFile);
				tempFile.delete();
				response = "File \'" + file.getOriginalFilename() + "\' has been uploaded to \'" + bucketName
						+ "\' bucket.";
			} catch (SdkClientException | IOException e) {
				response = "Exception: " + e.getMessage();
			}
		}
		return response;
	}

	public List<String> getBuckets() {
		List<String> buckets = new ArrayList<>();
		s3Client.listBuckets().forEach(bucket -> buckets.add(bucket.getName()));
		return buckets;
	}

	public List<String> listObjectsInBucket(String bucketName) {
		List<String> objects = new ArrayList<>();
		s3Client.listObjects(bucketName).getObjectSummaries().forEach(obj -> objects.add(obj.getKey()));
		return objects;
	}

	public void downloadFile(@PathVariable(name = "bucketName", required = true) String bucketName,
			@PathVariable(name = "fileName", required = true) String fileName, HttpServletResponse res) {
		try (InputStream in = s3Client.getObject(bucketName, fileName).getObjectContent()) {
			byte[] buffer = new byte[in.available()];
			int bytesRead;
			res.setContentType("application/octet-stream");
			res.setHeader("Content-Disposition", "filename=" + fileName);
			ServletOutputStream os = res.getOutputStream();
			while ((bytesRead = in.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (IOException | AmazonS3Exception e) {
			System.out.println("[" + bucketName + "]" + e.getMessage());
			if (e instanceof AmazonS3Exception) {
				res.setStatus(((AmazonS3Exception) e).getStatusCode());
				res.setHeader("aws-error-msg", ((AmazonS3Exception) e).getErrorMessage());
			}
		}
	}

	public String deleteObjectFromBucket(String bucketName, String fileName) {
		String response = "Bucket " + bucketName + " does not exit.";
		if (s3Client.doesBucketExist(bucketName)) {
			try {
				s3Client.deleteObject(bucketName, fileName);
				response = "File \'" + fileName + "\' has been deleted from \'" + bucketName + "\' bucket.";
			} catch (SdkClientException e) {
				response = "Exception: " + e.getMessage();
			}
		}
		return response;
	}

	public String deleteBucket(String bucketName) {
		String response = "Bucket " + bucketName + " does not exit.";
		if (s3Client.doesBucketExist(bucketName)) {
			try {
				s3Client.deleteBucket(bucketName);
				response = "Bucket \'" + bucketName + "\' has been deleted.";
			} catch (SdkClientException e) {
				response = "Exception: " + e.getMessage();
			}
		}
		return response;
	}
}

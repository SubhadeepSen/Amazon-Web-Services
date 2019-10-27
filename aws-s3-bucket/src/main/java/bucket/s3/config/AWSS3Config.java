package bucket.s3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSS3Config {

	@Value("${aws.s3.user.access.key}")
	private String accessKey;

	@Value("${aws.s3.user.secret.key}")
	private String secretKey;

	@Bean
	public AmazonS3 buildAmazonS3Client() {
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(getAWSCredentials()))
				.withRegion(Regions.AP_SOUTHEAST_1).build();
	}

	private AWSCredentials getAWSCredentials() {
		return new BasicAWSCredentials(accessKey, secretKey);
	}
}

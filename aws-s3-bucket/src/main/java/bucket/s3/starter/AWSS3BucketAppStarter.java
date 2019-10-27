package bucket.s3.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "bucket.s3.*")
public class AWSS3BucketAppStarter {

	public static void main(String[] args) {
		SpringApplication.run(AWSS3BucketAppStarter.class, args);
	}

}

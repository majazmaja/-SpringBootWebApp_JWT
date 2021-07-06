package maja.zmaja.assetsservice.storage.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;


@Service
public class AmazonClient {
    

    @Value("${aws.s3.endpointUrl}")
    private String endpointUrl;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.access_key_id}")
    private String accessId;
    @Value("${aws.secret_access_key}")
    private String secretKey;
    @Value("${aws.s3.region}")
    private String region;


    @Bean
    public AmazonS3 s3client() {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessId, secretKey);
        AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        
        return amazonS3Client;
    }

   

}

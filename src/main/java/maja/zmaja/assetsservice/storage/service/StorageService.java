package maja.zmaja.assetsservice.storage.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class StorageService {
    private Logger logger = LoggerFactory.getLogger(StorageService.class);

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(String keyName, MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucketName, keyName, file.getInputStream(), metadata);
            return  keyName;
        } catch (IOException ioe) {
            logger.error("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
        return  keyName;
    }

    /**
     *
     * Method to get all user's  files from bucket for USER user
     *
     **/
    public List<String> listUserFiles(Set<String> userFilesLst) {

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName);
        List<String> keys = new ArrayList<>();
        ObjectListing objects = amazonS3Client.listObjects(listObjectsRequest);
        while (true) {
            List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();
            if (objectSummaries.size() < 1) {
                break;
            }
            for (S3ObjectSummary item : objectSummaries) {
                if (!item.getKey().endsWith("/") && userFilesLst.contains(item.getKey()))
                    keys.add(item.getKey());
            }
            objects = amazonS3Client.listNextBatchOfObjects(objects);
        }
        return keys;
    }
    /**
     *
     * Method to get all files from bucket for ADMIN user
     *
     **/
    public List<String> listFiles() {

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName);
        List<String> keys = new ArrayList<>();
        ObjectListing objects = amazonS3Client.listObjects(listObjectsRequest);
        while (true) {
            List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();
            if (objectSummaries.size() < 1) {
                break;
            }
            for (S3ObjectSummary item : objectSummaries) {
                if (!item.getKey().endsWith("/"))
                    keys.add(item.getKey());
            }
            objects = amazonS3Client.listNextBatchOfObjects(objects);
        }
        return keys;
    }
    public ByteArrayOutputStream downloadFile(String keyName) {
        try {
            S3Object s3object = amazonS3Client.getObject(new GetObjectRequest(bucketName, keyName));
            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            return outputStream;
        } catch (IOException ioException) {
            logger.error("IOException: " + ioException.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException Message:    " + serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }

        return null;
    }

    public boolean isPublic(String keyName) {
        List<Grant> s3object = amazonS3Client.getObjectAcl(new GetObjectAclRequest(bucketName, keyName)).getGrantsAsList();
        boolean is_Public = false;
        for(Grant g : s3object) {
            is_Public = is_Public || (g.getGrantee().getTypeIdentifier().equals("uri") && g.getGrantee().getIdentifier().equals("http://acs.amazonaws.com/groups/global/AllUsers"));
        }
        
        return is_Public;
    }

}

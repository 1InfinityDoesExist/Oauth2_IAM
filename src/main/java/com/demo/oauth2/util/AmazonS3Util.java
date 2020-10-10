package com.demo.oauth2.util;

import java.io.File;
import java.io.FileOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AmazonS3Util {
    @Value("${amazon.aws.access.key}")
    private String accessKey;
    @Value("${amazon.aws.secret.key}")
    private String secretKey;
    @Value("${amazon.aws.bucken.name}")
    private String bucketName;
    @Value("${amazon.aws.region}")
    private String region;

    private static final String S3_BUCKET = "google-info";
    private static final String KEY = "oauth/user_info";

    public String uploadUserInfoImage(MultipartFile file, String parentTenant, String userName)
                    throws Exception {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(region)
                        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
        if (!s3Client.doesBucketExistV2(S3_BUCKET)) {
            s3Client.createBucket(S3_BUCKET);
        }
        PutObjectRequest putObjectRequest =
                        new PutObjectRequest(S3_BUCKET,
                                        KEY + "/" + decorateFileName(file, userName),
                                        multipartFileToFile(file)).withCannedAcl(
                                                        CannedAccessControlList.PublicRead);
        PutObjectResult putObjectResult = s3Client.putObject(putObjectRequest);
        log.info("::::::putObjectResult {}", putObjectResult);
        return "https://" + putObjectRequest.getBucketName() + ".s3." + region + ".amazonaws.com/"
                        + putObjectRequest.getKey();
    }

    private File multipartFileToFile(MultipartFile multiPartFile) throws Exception {
        File file = new File(multiPartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multiPartFile.getBytes());
        fos.close();
        return file;
    }

    private String decorateFileName(MultipartFile file, String userName) {
        return userName + "-" + file.getOriginalFilename().replace(" ", "_");
    }

}

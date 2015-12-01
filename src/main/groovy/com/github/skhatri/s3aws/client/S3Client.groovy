package com.github.skhatri.s3aws.client

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.S3Object
import org.joda.time.LocalDateTime

public class S3Client {
    private final AmazonS3Client s3Client;

    private static final String AMZ_REDIRECT_LINK = "x-amz-website-redirect-location";

    public S3Client(String awsProfile) {
        s3Client = new AmazonS3Client(new ProfileCredentialsProvider(awsProfile));
    }

    public String uploadFile(String bucketName, String key, String fileName, String link, String region) {
        try {
            if (region != null && !region.equals('')) {
                s3Client.setRegion(Region.getRegion(Regions.fromName(region)))
            }
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, new File(fileName))
            s3Client.putObject(putObjectRequest)
            String linkName = createLinkObject(link, key, bucketName)
            if (linkName == null) {
                linkName = s3Client.generatePresignedUrl(bucketName, key, new LocalDateTime().plusDays(30).toDate())
            }
            return linkName
        } catch (Exception e) {
            throw new RuntimeException("AWS Upload Exception ", e)
        }
    }

    private String createLinkObject(String link, String key, String bucketName) {
        if (link != null && !link.isEmpty()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setHeader(AMZ_REDIRECT_LINK, createRedirectKey(key));
            metadata.setContentLength(0);
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            PutObjectRequest linkPutRequest = new PutObjectRequest(bucketName, link, inputStream, metadata)
            linkPutRequest.setCannedAcl(CannedAccessControlList.Private)
            s3Client.putObject(linkPutRequest);
            return s3Client.generatePresignedUrl(bucketName, link, new LocalDateTime().plusDays(30).toDate());
        }
    }

    private String createRedirectKey(String key) {
        key.startsWith("/") || key.startsWith("http") ? key : "/" + key
    }

    public void downloadFile(String bucketName, String key, String saveTo, String region) {
        try {
            if (region != null && !region.equals('')) {
                s3Client.setRegion(Region.getRegion(Regions.fromName(region)))
            }
            S3Object object = s3Client.getObject(bucketName, key);
            String redirect = object.getObjectMetadata().getRawMetadataValue(AMZ_REDIRECT_LINK)
            if (redirect != null && !redirect.isEmpty()) {
                println "getting redirect resource = $redirect"
                object = s3Client.getObject(bucketName, redirect.substring(1))
            }
            byte[] bytes = object.getObjectContent().getBytes();
            def fs = new FileOutputStream(new File(saveTo));
            fs.write(bytes);
            fs.close();
        } catch (Exception e) {
            throw new RuntimeException("AWS Download Exception ", e);
        }
    }
}

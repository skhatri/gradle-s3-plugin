package com.github.skhatri.s3aws.plugin

import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.github.skhatri.s3aws.client.S3Client
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class S3UploadTask extends DefaultTask {

    @Input
    String bucket
    @Input
    String region
    @Input
    String awsProfile
    @Input
    String key
    @Input
    String file
    @Input @Optional
    String link
    @Input @Optional
    ObjectMetadata metadata
    @Input @Optional
    CannedAccessControlList acl

    public S3UploadTask() {
        bucket = ''
        region = ''
        awsProfile = ''
        metadata = null;
    }

    @TaskAction
    public void perform() {
        String bucketName = getBucket()
        String regionName = getRegion()
        String profileName = getAwsProfile()

        logger.quiet "s3 upload " + bucketName
        logger.quiet "using region " + regionName
        logger.quiet "using aws profile " + profileName
        String fileName = getFile()
        if (fileName == null || fileName == '') {
            return;
        }
        String keyValue = getKey()
        S3Client client = new S3Client(profileName, regionName)
        String presigned = client.uploadFile(bucketName, keyValue, fileName, getLink(), getMetadata(), getAcl())
        logger.quiet "Uploaded \"" + fileName + "\" to \"" + keyValue + "\""
        logger.quiet "Downloadable from " + presigned + " within next 7 days"
    }
}

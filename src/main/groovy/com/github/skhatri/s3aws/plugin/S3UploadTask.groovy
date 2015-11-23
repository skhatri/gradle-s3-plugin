package com.github.skhatri.s3aws.plugin

import com.github.skhatri.s3aws.client.S3Client
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
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
    @Input
    String link

    public S3UploadTask() {
        bucket = ''
        awsProfile = ''
        region = null
    }

    @TaskAction
    public void perform() {
        logger.quiet "s3 upload " + getBucket()
        logger.quiet "using aws profile " + getAwsProfile()
        String fileName = getFile()
        if (fileName == null || fileName == '') {
            return;
        }
        String keyValue = getKey()
        S3Client client = new S3Client(getAwsProfile());
        String presigned = client.uploadFile(getBucket(), keyValue, fileName, getLink(), getRegion())
        logger.quiet "Uploaded \"" + fileName + "\" to \"" + keyValue + "\""
        logger.quiet "Downloadable from " + presigned + " within next 30 days"
    }
}

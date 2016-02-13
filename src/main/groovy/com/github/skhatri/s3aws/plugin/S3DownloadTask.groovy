package com.github.skhatri.s3aws.plugin

import com.github.skhatri.s3aws.client.S3Client
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class S3DownloadTask extends DefaultTask {

    @Input
    String bucket
    @Input
    String region
    @Input
    String awsProfile
    @Input
    String key
    @Input
    String saveTo

    public S3DownloadTask() {
        bucket = ''
        region = ''
        awsProfile = ''
    }

    @TaskAction
    public void perform() {
        String bucketName = getBucket()
        String regionName = getRegion()
        String profileName = getAwsProfile()

        logger.quiet "s3 download " + bucketName
        logger.quiet "using region " + regionName
        logger.quiet "using aws profile " + profileName
        String keyValue = getKey()
        if (keyValue == null || keyValue == '') {
            return;
        }
        S3Client client = new S3Client(profileName, regionName)

        String saveTo = getSaveTo()
        client.downloadFile(bucketName, keyValue, saveTo)
        logger.quiet "Downloaded \"" + keyValue + "\" to \"" + saveTo + "\""
    }
}

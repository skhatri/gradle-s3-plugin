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
        awsProfile = ''
        region = null
    }

    @TaskAction
    public void perform() {
        logger.quiet "s3 download " + getBucket()
        logger.quiet "using aws profile " + getAwsProfile()
        String keyValue = getKey()
        if (keyValue == null || keyValue == '') {
            return;
        }
        S3Client client = new S3Client(getAwsProfile());
        client.downloadFile(getBucket(), keyValue, getSaveTo(), getRegion())
        logger.quiet "Downloaded \"" + keyValue + "\" to \"" + getSaveTo() + "\""
    }
}

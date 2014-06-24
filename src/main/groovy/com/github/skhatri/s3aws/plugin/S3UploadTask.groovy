package com.github.skhatri.s3aws.plugin

import com.github.skhatri.s3aws.client.S3Client
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class S3UploadTask extends DefaultTask {
    @Input
    String bucket
    @Input
    String key
    @Input
    String file

    @Input
    String link

    public S3UploadTask() {
        bucket = ''
    }

    @TaskAction
    public void perform() {
        logger.quiet "s3 upload " + getBucket()
        String fileName = getFile()
        if (fileName == null || fileName == '') {
            return;
        }
        String keyValue = getKey()
        S3Client client = new S3Client();
        String presigned = client.uploadFile(getBucket(), keyValue, fileName, getLink())
        logger.quiet "Uploaded \"" + fileName + "\" to \"" + keyValue + "\""
        logger.quiet "Downloadable from " + presigned + " within next 30 days"
    }
}

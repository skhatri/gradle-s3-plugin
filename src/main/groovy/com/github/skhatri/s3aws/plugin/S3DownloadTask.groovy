package com.github.skhatri.s3aws.plugin

import com.github.skhatri.s3aws.client.S3Client
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class S3DownloadTask extends DefaultTask {

    @Input
    String bucket
    @Input
    String key
    @Input
    String saveTo

    public S3DownloadTask() {
        bucket = ''
    }

    @TaskAction
    public void perform() {
        logger.quiet "s3 download " + getBucket()
        String keyValue = getKey()
        if (keyValue == null || keyValue == '') {
            return;
        }
        S3Client client = new S3Client();
        client.downloadFile(getBucket(), keyValue, getSaveTo())
        logger.quiet "Downloaded \"" + keyValue + "\" to \"" + getSaveTo() + "\""
    }
}

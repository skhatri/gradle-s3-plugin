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
    def filesToDownload

    public S3DownloadTask() {
        bucket = ''
        awsProfile = ''
        region = null
        filesToDownload = []
    }

    @TaskAction
    public void perform() {
        logger.quiet "s3 download " + getBucket()

        logger.quiet "using aws profile " + getAwsProfile()

        def files = getFilesToDownload()

        if (files == null || files.size() == 0) {
            return;
        }

        S3Client client = new S3Client(getAwsProfile());

        Closure downloadFile = { String keyValue, String localFile ->

            client.downloadFile(getBucket(), keyValue, localFile, getRegion())

            logger.quiet "Downloaded \"" + keyValue + "\" to \"" + getSaveTo() + "\""

        }

        if (files instanceof Map) {

            downloadFile(files.awsKey, files.localFile)

        }
        else {
            files.each {
                downloadFile(it.awsKey, it.localFile)
            }
        }


    }
}

package com.moksamedia.gradle.plugin.aws.s3

import com.moksamedia.gradle.plugin.aws.S3Client
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
    def filesToUpload

    public S3UploadTask() {
        bucket = ''
        awsProfile = ''
        region = null
        filesToUpload = []
    }

    @TaskAction
    public void perform() {

        logger.quiet "s3 upload " + getBucket()

        logger.quiet "using aws profile " + getAwsProfile()

        def files = getFilesToUpload()

        if (files == null || files.size() == 0) {
            return;
        }

        S3Client client = new S3Client(getAwsProfile());

        Closure uploadFile = { String keyValue, String fileName, String link ->

            String bucket = getBucket()

            logger.quiet "Uploading \"${fileName}\" to \"${bucket}/${keyValue}\""

            String presigned = client.uploadFile(getBucket(), keyValue, fileName, link, getRegion())

            logger.quiet "Finished uploading \"${keyValue}\""

            //logger.quiet "Downloadable from " + presigned + " within next 30 days"

        }

        if (files instanceof Map) {

            uploadFile(files.awsKey, files.localFile, files?.link)

        }
        else {
            files.each {
                uploadFile(it.awsKey, it.localFile, it?.link)
            }
        }

    }

}

package com.moksamedia.gradle.plugin.aws

import com.moksamedia.gradle.plugin.aws.s3.S3DownloadExtension
import com.moksamedia.gradle.plugin.aws.s3.S3UploadExtension

class AwsUtilsExtension {
    String bucket
    String awsProfile = 'default'
    S3DownloadExtension download = new S3DownloadExtension()
    S3UploadExtension upload = new S3UploadExtension()
}

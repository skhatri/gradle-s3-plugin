package com.moksamedia.gradle.plugin.aws.s3;

public class S3DownloadExtension {
    String bucket;
    String awsProfile
    def filesToDownload = []
    String region = null
}


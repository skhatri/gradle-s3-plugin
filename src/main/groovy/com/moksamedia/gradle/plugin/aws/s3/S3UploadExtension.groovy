package com.moksamedia.gradle.plugin.aws.s3

class S3UploadExtension {
    String bucket
    String awsProfile
    String region = null
    def filesToUpload = []
}

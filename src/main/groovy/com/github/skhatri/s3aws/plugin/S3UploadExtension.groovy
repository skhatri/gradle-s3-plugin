package com.github.skhatri.s3aws.plugin

class S3UploadExtension {
    String bucket
    String awsProfile
    String region = null
    def filesToUpload = []
}

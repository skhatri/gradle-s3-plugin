package com.github.skhatri.s3aws.plugin

class S3Extension {
    String bucket
    S3DownloadExtension download = new S3DownloadExtension()
    S3UploadExtension upload = new S3UploadExtension()
}

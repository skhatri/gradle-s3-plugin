package com.github.skhatri.s3aws.plugin

import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata

class S3UploadExtension {
    String bucket
    String awsProfile
    String key = ''
    String file = ''
    String link = ''
    ObjectMetadata metadata = null
    CannedAccessControlList acl = null
}

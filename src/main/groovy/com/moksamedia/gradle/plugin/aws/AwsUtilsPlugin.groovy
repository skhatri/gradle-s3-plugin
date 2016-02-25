package com.moksamedia.gradle.plugin.aws

import com.moksamedia.gradle.plugin.aws.s3.S3DownloadExtension
import com.moksamedia.gradle.plugin.aws.s3.S3DownloadTask
import com.moksamedia.gradle.plugin.aws.s3.S3UploadExtension
import com.moksamedia.gradle.plugin.aws.s3.S3UploadTask
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AwsUtilsPlugin implements Plugin<Project> {

    void apply(Project project) {

        def awsUtilsExt = project.extensions.create('AwsUtils', AwsUtilsExtension)
        awsUtilsExt.extensions.create('upload', S3UploadExtension)
        awsUtilsExt.extensions.create('download', S3DownloadExtension)
        createUploadTask(project)
        createDownloadTask(project)

    }

    void createUploadTask(Project project) {

        project.tasks.create(name: 's3Upload', type: S3UploadTask) { }

        project.tasks.withType(S3UploadTask) {

            def awsUtilsExt = project.extensions.getByName('AwsUtils')
            def s3UploadExt = awsUtilsExt.extensions.getByName('upload')

            conventionMapping.bucket = { awsUtilsExt.bucket }
            conventionMapping.awsProfile = { awsUtilsExt.awsProfile }
            conventionMapping.filesToUpload = { s3UploadExt.filesToUpload }
            conventionMapping.region = { s3UploadExt.region }

        }

    }

    void createDownloadTask(Project project) {

        project.tasks.create(name: 's3Download', type: S3DownloadTask) { }

        project.tasks.withType(S3DownloadTask) {

            def awsUtilsExt = project.extensions.getByName('AwsUtils')
            def s3DownloadExt = awsUtilsExt.extensions.getByName('download')

            conventionMapping.bucket = { awsUtilsExt.bucket }
            conventionMapping.awsProfile = { awsUtilsExt.awsProfile }
            conventionMapping.filesToDownload = { s3DownloadExt.filesToDownload }
            conventionMapping.region = { s3DownloadExt.region }

        }

    }

}

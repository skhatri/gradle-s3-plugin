package com.github.skhatri.s3aws.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class S3Plugin implements Plugin<Project> {
    void apply(Project project) {
        def s3Ext = project.extensions.create('s3', S3Extension)
        s3Ext.extensions.create('upload', S3UploadExtension)
        s3Ext.extensions.create('download', S3DownloadExtension)
        createUploadTask(project)
        createDownloadTask(project)
    }

    void createUploadTask(Project project) {
        project.tasks.create(name: 's3Upload', type: S3UploadTask) {

        }
        project.tasks.withType(S3UploadTask) {
            def s3Ext = project.extensions.getByName('s3')
            def s3UploadExt = s3Ext.extensions.getByName('upload')

            conventionMapping.bucket = { s3Ext.bucket }
            conventionMapping.awsProfile = { s3Ext.awsProfile }
            conventionMapping.key = { s3UploadExt.key }
            if (s3UploadExt.link) {
                conventionMapping.link = { s3UploadExt.link }
            }
            conventionMapping.file = { s3UploadExt.file }
            if (s3UploadExt.metadata) {
                conventionMapping.metadata = { s3UploadExt.metadata }
            }
            if (s3UploadExt.acl) {
                conventionMapping.acl = { s3UploadExt.acl }
            }
        }
    }

    void createDownloadTask(Project project) {
        project.tasks.create(name: 's3Download', type: S3DownloadTask) {
        }

        project.tasks.withType(S3DownloadTask) {
            def s3Ext = project.extensions.getByName('s3')
            def s3DownloadExt = s3Ext.extensions.getByName('download')
            conventionMapping.bucket = { s3Ext.bucket }
            conventionMapping.awsProfile = { s3Ext.awsProfile }
            conventionMapping.key = { s3DownloadExt.key }
            conventionMapping.saveTo = { s3DownloadExt.saveTo }
        }
    }

}

Gradle S3 Plugin
----------------

This plugin can be used to download files from S3 buckets. You can upload artifacts to S3 as well. The link configuration attribute can be used to create amz-website-redirect-location based redirects. Technically this is not a symbolic link, but it can behave like one when trying to reference same file from multiple locations.

The plugin creates two tasks:

# s3Upload
As the name suggests, the s3Upload task can be used to upload a local file to a bucket with a given key name.

# s3Download
Similarly, the s3Download task can be used to download a given file from a chosen bucket to a local directory.

The plugin uses a ProfileCredentialsProvider strategy to figure out AWS access and secret key.

See com.github.skhatri.s3aws.client.S3Client for implementation.

Pull requests are welcome if you want to modify to your needs.

Plugin Configuration in your build.gradle
-----------------------------------------
```
buildscript {
    dependencies {
        classpath 'com.github.skhatri:gradle-s3-plugin:1.0.0'
        classpath 'joda-time:joda-time:2.3'
    }
    repositories {
        mavenCentral()
    }
}
apply plugin: 's3'

s3 {
    bucket = 'skhatri-bucket'
    upload {
        key =  new org.joda.time.LocalDate().toString('yyyy/MM/dd')+'/gradle-s3-plugin-1.0.0-SNAPSHOT.jar'
        file = '../build/libs/gradle-s3-plugin-1.0.0-SNAPSHOT.jar'
        link = 'latest/gradle-plugin.jar'
    }
    download {
        key = 'latest/gradle-plugin.jar'
        saveTo = 'gradle-pugin.jar'
    }
}


```

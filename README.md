Gradle S3 Plugin
----------------

This plugin can be used to download files from S3 buckets. You can upload artifacts to S3 as well. The link configuration attribute can be used to create amz-website-redirect-location based redirects. Technically this is not a symbolic link, but it can behave like one when trying to reference same file from multiple locations.

The plugin creates two tasks:

# s3Upload
As the name suggests, the s3Upload task can be used to upload a local file to a bucket with a given key name.

# s3Download
Similarly, the s3Download task can be used to download a given file from a chosen bucket to a local directory.

The plugin uses a ProfileCredentialsProvider strategy to figure out AWS access and secret key. The
name of the credentials profile to use when connecting to AWS can be specified by the awsProfile
propterty.

See com.github.skhatri.s3aws.client.S3Client for implementation.

Pull requests are welcome if you want to modify to your needs.

Plugin Configuration in your build.gradle
-----------------------------------------
```
buildscript {
    dependencies {
        classpath 'com.github.skhatri:gradle-s3-plugin:1.0.6'
    }
    repositories {
        mavenCentral()
    }
}
apply plugin: 's3'

s3 {
    bucket = 'skhatri-bucket'
    awsProfile = 'default'
    upload {
        //"key" is the name of the target file in S3 (in this example we create a versioned file)
        key =  java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd")) +'/gradle-s3-plugin-1.0.6.jar'
        
        // "file" is the name of the local source file
        file = '../build/libs/gradle-s3-plugin-1.0.6.jar'
        
        //"link" will create http redirect to the latest uploaded version of the file
        // This will redirect is only available via the "s3 static webpage" option. 
        // walkthorugh link from AWS on how to set it up
        // https://docs.aws.amazon.com/AmazonS3/latest/dev/website-hosting-custom-domain-walkthrough.html
        link = 'latest/gradle-plugin.jar'
    }
    download {
        key = 'latest/gradle-plugin.jar'
        saveTo = 'gradle-pugin.jar'
    }
}


```

### Using Upload Task to upload more artifacts ###
```
task uploadAppJar(type: com.github.skhatri.s3aws.plugin.S3UploadTask) {
    key = 'gradle-s3-plugin-1.0.0-something.jar'
    file = '../build/libs/gradle-s3-plugin-master-1.0.1-SNAPSHOT.jar'
    link = 'latest/gradle-plugin.jar'
}
```
Then I can call "gradle uploadAppJar" to upload yet another artifact to S3.

This can be useful, if uploading hash checksums of the artifacts. For instance, I can upload sha1 value of the artifact so my automated artifact deployment task could download checksum file first to decide whether downloading the big artifact is worth it.

```
task writeHash() {
    String hashValue = computeHash('../build/libs/gradle-s3-plugin-master-1.0.1-SNAPSHOT.jar')
    file('../build/libs/gradle-s3-plugin-master-1.0.1-SNAPSHOT.sha1').write(hashValue)
}

task uploadHash(type: com.github.skhatri.s3aws.plugin.S3UploadTask) {
    key = 'gradle-s3-plugin-1.0.0-something.sha1'
    file = '../build/libs/gradle-s3-plugin-master-1.0.1-SNAPSHOT.sha1'
    link = 'latest/gradle-plugin.sha1'
}

task awsUpload(dependsOn:['uploadAppJar', 'writeHash', 'uploadHash']) {
}
```
Now, calling awsUpload will upload jar and the hash file.

### Downloading more than one artifact ###

Similarly, I can download more artifacts by simply creating ad-hoc Download task. I am downloading the previously uploaded sha1 file of the artifact using the task below.

```
task awsDownload(type: com.github.skhatri.s3aws.plugin.S3DownloadTask) {
    key = 'latest/gradle-plugin.sha1'
    saveTo = 'gradle-plugin.txt'
}
```



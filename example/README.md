
### S3 tasks using maven repo
gradle -b build.gradle s3Upload
gradle -b build.gradle s3Download

### S3 tasks using gradle plugin repo and legacy mode
gradle -b gradle-plugins-legacy.gradle s3Upload
gradle -b gradle-plugins-legacy.gradle s3Download

### S3 tasks using gradle plugin repo
gradle -b gradle-plugins.gradle s3Download
gradle -b gradle-plugins.gradle s3Upload

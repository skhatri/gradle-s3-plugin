plugins {
    id("idea")
    id("groovy")
    kotlin("jvm") version "1.5.21"

    signing

    id("maven-publish")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.14.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    testImplementation(gradleTestKit())
    implementation(localGroovy())
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.68")
}


gradlePlugin {
    plugins {
        create("com.github.skhatri.s3") {
            id = "com.github.skhatri.s3"
            displayName = "Plugin to upload and download files from AWS S3 buckets"
            description = "Plugin to upload and download files from AWS S3 buckets"
            implementationClass = "com.github.skhatri.s3aws.plugin.S3Plugin"
        }
    }
}

pluginBundle {
    website = "${project.extra["scm.url"]}"
    vcsUrl = "${project.extra["scm.url"]}"
    tags = listOf("s3", "bucket", "upload", "download")
}


val sourcesJar by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    from(tasks.javadoc)
    archiveClassifier.set("javadoc")
}


artifacts {
    add("archives", sourcesJar)
    add("archives", javadocJar)
}

publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../local-plugin-repository")
        }
        maven {
            var uploadUrl: String = if (project.extra["release"] == "true") {
                "${project.extra["upload.release.url"]}"
            } else {
                "${project.extra["upload.snapshot.url"]}"
            }
            url = uri(uploadUrl)
            credentials {
                username = "${project.extra["upload.user"]}"
                password = "${project.extra["upload.password"]}"
            }
        }
    }
    publications {
        create<MavenPublication>("pluginMaven") {
            artifact(sourcesJar.get())
            artifact(javadocJar.get())
        }
    }
}

val scmUrl=project.extra["scm.url"]
project.publishing.publications.withType(MavenPublication::class.java).forEach { publication ->

    with(publication.pom) {
        withXml {
            val root = asNode()
            root.appendNode("name", project.name)
            root.appendNode("description", "Plugin to upload and download files from AWS S3 buckets")
            root.appendNode("url", scmUrl)
        }
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("${project.extra["author.handle"]}")
                name.set("${project.extra["author.name"]}")
                email.set("${project.extra["author.email"]}")
            }
        }
        scm {
            connection.set("scm:git:$scmUrl")
            developerConnection.set("scm:git:$scmUrl")
            url.set("${scmUrl}")
        }
    }
}

gradle.taskGraph.whenReady {
    if (allTasks.any { it is Sign }) {
        allprojects {
            extra["signing.keyId"] = "${project.extra["signing.keyId"]}"
            extra["signing.secretKeyRingFile"] = "${project.extra["signing.secretKeyRingFile"]}"
            extra["signing.password"] = "${project.extra["signing.password"]}"
        }
    }
}

signing {
    sign(publishing.publications["pluginMaven"])
}

tasks.withType<Sign>().configureEach {
    onlyIf { project.extra["release"] == "true" }
}


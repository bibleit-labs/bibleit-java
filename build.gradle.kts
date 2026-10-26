import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar

plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.37.0"
}

repositories {
    mavenCentral()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(27))
}

dependencies {
    api("com.fasterxml.jackson.core:jackson-databind:2.22.4")
    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(27)
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.test {
    useJUnitPlatform()
    workingDir = projectDir
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:all,-missing", "-quiet")
}

tasks.processResources {
    from("contract/openapi.json") { into("app/bibleit") }
    from(files("LICENSE", "THIRD_PARTY_NOTICES")) { into("META-INF") }
    from("LICENSES") { into("META-INF/LICENSES") }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.jar {
    manifest.attributes["Automatic-Module-Name"] = "app.bibleit"
}

mavenPublishing {
    configureBasedOnAppliedPlugins(JavadocJar.Javadoc(), SourcesJar.Sources())
    coordinates(project.group.toString(), "bibleit-java", project.version.toString())
    publishToMavenCentral()
    signAllPublications()
    pom {
        name.set("Bibleit Java client")
        description.set("Java SDK for Bibleit REST resources, OAuth and SSH protocol helpers.")
        url.set("https://github.com/bibleit-labs/bibleit-java")
        inceptionYear.set("2026")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/license/mit")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("fmamud")
                name.set("Felipe Mamud")
                url.set("https://github.com/fmamud")
                organization.set("Bibleit Labs")
                organizationUrl.set("https://github.com/bibleit-labs")
            }
        }
        scm {
            connection.set("scm:git:https://github.com/bibleit-labs/bibleit-java.git")
            developerConnection.set("scm:git:ssh://git@github.com/bibleit-labs/bibleit-java.git")
            url.set("https://github.com/bibleit-labs/bibleit-java")
            tag.set("v${project.version}")
        }
    }
}

// Keep license notices with the separately published source archive.
tasks.named<Jar>("sourcesJar") {
    from(files("LICENSE", "THIRD_PARTY_NOTICES")) { into("META-INF") }
    from("LICENSES") { into("META-INF/LICENSES") }
}

// Validate the Javadoc archive during the normal build as well as publication.
tasks.named("build") {
    dependsOn("plainJavadocJar")
}

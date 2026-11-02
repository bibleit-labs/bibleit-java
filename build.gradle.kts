import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

plugins {
    `java-library`
    id("org.openapi.generator") version "7.26.0"
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
    (options as StandardJavadocDocletOptions).apply {
        addStringOption("Xdoclint:all", "-quiet")
        addBooleanOption("Werror", true)
    }
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


// Preserve previously published protocol wrappers, but do not manufacture new
// browser/MCP operations on the pinned REST origin. Those use dedicated clients.
val legacyProtocolOperations = setOf(
    "getOAuthMetadata", "authorizeOAuth", "decideOAuthConsent", "exchangeOAuthToken",
    "revokeOAuthToken", "getConnectedAppsPage", "disconnectOAuthApp"
)
val generatorContract = layout.buildDirectory.file("generator-contract/openapi.json")
val prepareGeneratorContract = tasks.register("prepareGeneratorContract") {
    inputs.file(layout.projectDirectory.file("contract/openapi.json"))
    inputs.property("legacyProtocolOperations", legacyProtocolOperations.sorted())
    outputs.file(generatorContract)
    doLast {
        @Suppress("UNCHECKED_CAST")
        val spec = JsonSlurper().parse(file("contract/openapi.json")) as Map<String, Any?>
        @Suppress("UNCHECKED_CAST")
        val paths = spec["paths"] as Map<String, Map<String, Any?>>
        for (item in paths.values) for (value in item.values) {
            if (value !is MutableMap<*, *>) continue
            @Suppress("UNCHECKED_CAST")
            val operation = value as MutableMap<String, Any?>
            val id = operation["operationId"] as? String ?: continue
            operation["x-java-client-operation"] = operation["x-fern-server-name"] == "rest" || id in legacyProtocolOperations
        }
        generatorContract.get().asFile.apply {
            parentFile.mkdirs()
            writeText(JsonOutput.prettyPrint(JsonOutput.toJson(spec)) + "\n")
        }
    }
}
tasks.named("openApiGenerate") { dependsOn(prepareGeneratorContract) }

openApiGenerate {
    generatorName.set("java")
    library.set("native")
    inputSpec.set(generatorContract.get().asFile.path)
    outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.path)
    templateDir.set(layout.projectDirectory.dir("contract/templates").asFile.path)
    configFile.set(layout.projectDirectory.file("contract/openapi-generator.yaml").asFile.path)
    globalProperties.set(mapOf(
        "apis" to "",
        "supportingFiles" to "",
        "apiTests" to "false",
        "apiDocs" to "false"
    ))
    parameterNameMappings.set(mapOf("wait" to "waitSeconds", "q" to "query"))
    configOptions.set(mapOf("hideGenerationTimestamp" to "true"))
}

tasks.register<Copy>("generateRestOperations") {
    group = "code generation"
    description = "Regenerates the REST directory and wrappers with OpenAPI Generator."
    dependsOn(tasks.openApiGenerate)
    from(layout.buildDirectory.dir("generated/openapi/src/main/java")) {
        include("app/bibleit/Operations.java", "app/bibleit/BibleitClient.java", "app/bibleit/Queries.java")
    }
    into(layout.projectDirectory.dir("src/main/java"))
}


tasks.register<Exec>("generateModels") {
    group = "code generation"
    description = "Regenerates Jackson schema beans with explicit null and field-presence support."
    commandLine("python3", "scripts/generate-models.py")
}

tasks.register("generateCode") {
    group = "code generation"
    description = "Regenerates the checked-in REST operations, queries, and schema models."
    dependsOn("generateModels", "generateRestOperations")
}

tasks.compileJava {
    mustRunAfter("generateRestOperations", "generateModels")
}

tasks.named("sourcesJar") {
    mustRunAfter("generateRestOperations", "generateModels")
}

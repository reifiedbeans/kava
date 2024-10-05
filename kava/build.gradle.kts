import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import java.time.LocalDate

buildscript {
    dependencies {
        classpath(libs.dokka.base)
    }
}

plugins {
    id("maven-publish")
    id("signing")

    alias(libs.plugins.dokka)
}

val projectName = "Kava"
val projectDescription = "Kotlin DSL for building Guice modules"
val projectInceptionYear = "2024"
val githubRepository = "reifiedbeans/kava"
val githubRepositoryUrl = "https://github.com/$githubRepository"

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    api(libs.guice)

    testImplementation(libs.mockk)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

lateinit var mavenPublication: MavenPublication
publishing {
    publications {
        mavenPublication =
            create<MavenPublication>("central") {
                from(components["java"])

                pom {
                    name = projectName
                    description = projectDescription
                    url = githubRepositoryUrl
                    inceptionYear = projectInceptionYear

                    licenses {
                        license {
                            name = "MIT License"
                            url = "https://opensource.org/license/mit"
                        }
                    }

                    developers {
                        developer {
                            name = "Drew Davis"
                            email = "drew@reifiedbeans.dev"
                        }
                    }

                    scm {
                        connection = "scm:git:git://github.com/$githubRepository.git"
                        developerConnection = "scm:git:ssh://github.com:$githubRepository.git"
                        url = githubRepositoryUrl
                    }
                }
            }
    }

    /*
     * Publish to build directory for manual upload
     * since Gradle doesn't support the new Central Portal yet
     * https://github.com/gradle/gradle/issues/28120
     */
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}

tasks.withType<DokkaTask>().configureEach {
    moduleName.set(projectName)

    dokkaSourceSets.configureEach {
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "© $copyrightRange Drew Davis"
        }

        sourceLink {
            localDirectory.set(rootDir)
            remoteUrl.set(uri("$githubRepositoryUrl/tree/v${project.version}").toURL())
            remoteLineSuffix.set("#L")
        }

        externalDocumentationLink {
            val guiceJavadocUri = "https://google.github.io/guice/api-docs/${libs.versions.guice.get()}/javadoc"
            url.set(uri(guiceJavadocUri).toURL())

            // Required until Dokka supports the new element-list standard
            packageListUrl.set(uri("$guiceJavadocUri/element-list").toURL())
        }
    }
}

tasks.replace("javadocJar", Jar::class).apply {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(mavenPublication)
}

val copyrightRange: String
    get() {
        val currentYear = LocalDate.now().year.toString()
        return if (projectInceptionYear == currentYear) {
            projectInceptionYear
        } else {
            "$projectInceptionYear–$currentYear"
        }
    }

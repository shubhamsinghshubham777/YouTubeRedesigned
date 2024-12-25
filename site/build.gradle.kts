
import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
}

group = "com.google.youtube"
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            description.set(
                "Enjoy the videos and music you love, upload original content, and share it " +
                        "all with friends, family, and the world on YouTube."
            )
            head.add {
                link(
                    rel = "preconnect",
                    href = "https://fonts.googleapis.com"
                )
                link(
                    rel = "preconnect",
                    href = "https://fonts.gstatic.com"
                ) {
                    attributes["crossorigin"] = ""
                }
                link(
                    href = "https://fonts.googleapis.com/css2?family=Roboto&display=swap",
                    rel = "stylesheet"
                )
            }
        }
    }
}

kotlin {
    configAsKobwebApplication("youtube")

    sourceSets {
        commonMain.dependencies {
            implementation(compose.animation)
            implementation(compose.runtime)
            implementation(compose.ui)
        }

        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.silk.icons.fa)
        }
    }
}

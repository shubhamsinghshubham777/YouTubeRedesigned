import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link

plugins {
    alias(libs.plugins.kotlin.multiplatform)
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
                // Font - Roboto
                link(
                    href = "https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0," +
                            "300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900" +
                            "&display=swap",
                    rel = "stylesheet"
                )
                // Font - Roboto Condensed
                link(
                    href = "https://fonts.googleapis.com/css2?family=Roboto+Condensed:ital,wght@" +
                            "0,100..900;1,100..900&family=Roboto:ital,wght@0,100;0,300;0,400;0," +
                            "500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap",
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
            implementation(libs.compose.animation)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
        }

        jsMain.dependencies {
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.silk.icons.fa)
        }
    }
}

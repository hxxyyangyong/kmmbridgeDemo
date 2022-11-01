@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("co.touchlab.faktory.kmmbridge")
    `maven-publish`
}

kotlin {
    ios()
    // Note: iosSimulatorArm64 target requires that all dependencies have M1 support
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":breeds"))
                api(project(":analytics"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.bundles.shared.commonTest)
            }
        }
        val iosMain by getting
        val iosTest by getting
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
    }

    cocoapods {
        summary = "KMMBridgeKickStart sample"
        homepage = "https://www.touchlab.com"
        ios.deploymentTarget = "13.5"
        version = "0.1.0"
        extraSpecAttributes["libraries"] = "'c++', 'sqlite3'"
        source = "{ :git => 'https://github.com/hxxyyangyong/kmmspec.git',:tag => spec.version.to_s}"
        framework {
            export(project(":analytics"))
            isStatic = true
            source = "dddd"
        }

        extraSpecAttributes["pod_target_xcconfig"] = """{
                    'PRODUCT_MODULE_NAME' => 'allshared',
                    'KOTLIN_PROJECT_PATH' => ':allshared',
                    'GENERATE_INFOPLIST_FILE' => 'YES',
                }
            """
        extraSpecAttributes["user_target_xcconfig"] = """{
                    'GENERATE_INFOPLIST_FILE' => 'YES'
                }
            """
    }
}

//addGithubPackagesRepository()

kmmbridge {
    mavenPublishArtifacts()
    githubReleaseVersions()
    spm()
    cocoapods("git@github.com:hxxyyangyong/kmmspec.git")
}

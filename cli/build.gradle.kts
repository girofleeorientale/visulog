
plugins {
    java
    application
}

val jar by tasks.getting(Jar::class)  {
    manifest {
        attributes["Main-Class"] = "up.visulog.cli.CLILauncher"
    }
}

application.mainClass.set("up.visulog.cli.CLILauncher")

dependencies {
    implementation(project(":analyzer"))
    implementation(project(":config"))
    implementation(project(":gitrawdata"))
    testImplementation("junit:junit:4.+")
}



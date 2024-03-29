
plugins {
    id("sri-soap-client.kotlin-application-conventions")
}

dependencies {
    implementation(project(":lib"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation("com.offbytwo:docopt:0.6.0.20150202")
}

application {
    mainClass.set("MainKt")
}

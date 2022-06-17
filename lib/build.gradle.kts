val jaxws: Configuration by configurations.creating

plugins {
    id("sri-soap-client.java-library-conventions")
}

dependencies {
    implementation("com.sun.xml.ws:jaxws-rt:3.0.2")
    jaxws("com.sun.xml.ws:jaxws-tools:3.0.2")
}

sourceSets {
    main {
        java {
            srcDir("generatedSrc/main/java")
        }
    }
}


/*
 * Generate Java source for ec.gob.sri.ws services by executing
 * wsimport on the WSDL files located in resources/META-INF/wsdl/.
 *
 * If the WSDL definitions for the services change, replace the
 * WSDL resource files and the Java sources will be automatically updated.
 */
val wsdlDir = "${projectDir}/src/main/resources/META-INF/wsdl/"

fun wsdlFile(name: String) = File(wsdlDir, "${name}.wsdl")
val wsRecepcionOffline: Task = wsImport(
    wsdlFile("cel.sri.gob.ec.RecepcionComprobantesOffline"),
    "ec.gob.sri.ws.recepcion",
    "xsd.xjb")
val wsAutorizacionOffline: Task = wsImport(
    wsdlFile("cel.sri.gob.ec.AutorizacionComprobantesOffline"),
    "ec.gob.sri.ws.autorizacion",
    "xsd.xjb")

tasks.getByName("compileJava") {
    dependsOn(
        wsRecepcionOffline.path,
        wsAutorizacionOffline.path,
    )
}

fun wsImport(wsdl: File, packageName: String, vararg bindings: String): Task {
    val wsdlDir = wsdl.parent
    val taskName = "wsimport-${wsdl.nameWithoutExtension}"
    val bindStr = if (bindings.isNotEmpty()) { bindings.joinToString(",") } else null

    return task(taskName) {
        val sourcedestdir = file("$projectDir/generatedSrc/main/java")
        group = BasePlugin.BUILD_GROUP
        inputs.file(wsdl)
        outputs.dir(sourcedestdir)
        doLast {
            sourcedestdir.mkdirs()
            bindStr?.apply {
                System.setProperty("javax.xml.accessExternalDTD", "all")
                System.setProperty("javax.xml.accessExternalSchema", "all")
            }
            ant.withGroovyBuilder {
                "taskdef"(
                    "name" to "wsimport",
                    "classname" to "com.sun.tools.ws.ant.WsImport",
                    "classpath" to jaxws.asPath
                )

                "wsimport"(
                    "keep" to true,
                    "Xnocompile" to true,
                    "sourcedestdir" to sourcedestdir,
                    "wsdl" to wsdl.absolutePath,
                    "wsdlLocation" to "http://localhost/wsdl/${wsdl.name}",
                    "package" to packageName
                ) {
                    bindStr?.apply {
                        "binding"("dir" to wsdlDir, "includes" to bindStr)
                    }
                    "xjcarg"("value" to "-XautoNameResolution")
                }
            }
        }
    }
}

mavenArtifact {
    artifactId = "sri-soap-client"
    artifactName = "SRI Soap Client"
    artifactDescription = "SOAP client for web services of SRI in Ecuador, to process comprobantes electronicos"
}

/*
 * Publish the generated Jar to a Maven repository.
 */
publishing {
    repositories {
        /*
        maven {
            // change URLs to point to your repos, e.g. http://my.org/repo
            val releasesRepoUrl = uri(layout.buildDirectory.dir("repos/releases"))
            val snapshotsRepoUrl = uri(layout.buildDirectory.dir("repos/snapshots"))
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
         */
    }
}


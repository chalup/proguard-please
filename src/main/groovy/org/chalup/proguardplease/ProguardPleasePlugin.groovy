package org.chalup.proguardplease

import org.ajoberstar.grgit.Grgit
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

class ProguardPleasePlugin implements Plugin<Project> {
    void apply(Project project) {
        if (!project.plugins.findPlugin("com.android.application") && !project.plugins.findPlugin("android")) {
            throw new ProjectConfigurationException("The android or com.android.application plugin must be applied to the project", null)
        }

        def initTask = project.tasks.create("proguardPleaseInit", ProguardPleaseInitTask)
        project.tasks.create("proguardPleaseUpdate", ProguardPleaseUpdateTask)

        project.afterEvaluate {
            if (!project.android["productFlavors"].isEmpty()) {
                throw new ProjectConfigurationException("The build flavors are not supported yet", null)
            }

            def obfuscatedVariants = project.android["applicationVariants"].findAll { v -> v.obfuscation != null }

            obfuscatedVariants.each { variant ->
                def task = project.tasks.create("proguardPlease${variant.name.capitalize()}")
                task.dependsOn variant.javaCompile
                task.dependsOn initTask

                variant.obfuscation.dependsOn task

                task.doLast {
                    project.logger.info "Generating ProGuard configuration for ${variant.name}"

                    def resolvedArtifacts = project.configurations['compile'].resolvedConfiguration.resolvedArtifacts.collect {
                        it.moduleVersion.id
                    }

                    if (!resolvedArtifacts.isEmpty()) {

                        def repoDir = new File(System.properties['user.home'], ".proguard-please-repo")
                        def repo = Grgit.open(repoDir);
                        def sha = getProject().file("proguard-please.properties").text
                        repo.checkout(branch: 'proguard-please-tmp', startPoint: sha, createBranch: true)

                        resolvedArtifacts.each { artifact ->
                            def artifactPath = "${artifact.group}/${artifact.name}/${artifact.version}"
                            def configFile = "${System.properties['user.home']}/.proguard-please-repo/${artifactPath}/proguard.cfg"

                            if (new File(configFile).exists()) {
                                def copiedConfigPath = "build/proguard-please/${variant.name}/${artifactPath}"

                                project.copy {
                                    from configFile
                                    into copiedConfigPath
                                }

                                variant.obfuscation.configuration(new File("${copiedConfigPath}/proguard.cfg"))
                            } else {
                                project.logger.warn "ProGuard configuration for ${artifact} is missing"
                            }
                        }

                        repo.checkout(branch: 'master')
                        repo.branch.remove(names: ['proguard-please-tmp'])
                    }
                }
            }
        }
    }
}

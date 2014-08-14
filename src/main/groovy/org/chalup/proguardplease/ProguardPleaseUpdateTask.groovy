package org.chalup.proguardplease

import org.ajoberstar.grgit.Grgit
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

class ProguardPleaseUpdateTask extends DefaultTask {
    @TaskAction
    def initialize() {
        def proguardPleaseProperties = getProject().file("proguard-please.properties")
        if (!proguardPleaseProperties.exists()) {
            throw new GradleException("proguard-please.properties file doesn't exists. Did you mean to run `gradle proguardPleaseInit`?")
        }

        def repoDir = new File(System.properties['user.home'], ".proguard-please-repo")
        def grGit
        if (!repoDir.exists()) {
            grGit = Grgit.clone(dir: repoDir, uri: 'https://github.com/chalup/proguard-please-repo.git');
        } else {
            grGit = Grgit.open(repoDir);
            grGit.fetch()
        }

        def originMasterSha = grGit.resolve.toCommit('origin/master').id
        proguardPleaseProperties.text = originMasterSha
    }
}

package org.chalup.proguardplease

import org.ajoberstar.grgit.Grgit
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ProguardPleaseInitTask extends DefaultTask {

    ProguardPleaseInitTask() {
        onlyIf {
            !getProject().file("proguard-please.properties").exists()
        }
    }

    @TaskAction
    def initialize() {
        def repoDir = new File(System.properties['user.home'], ".proguard-please-repo")
        def grGit
        if (!repoDir.exists()) {
            grGit = Grgit.clone(dir: repoDir, uri: 'https://github.com/chalup/proguard-please-repo.git');
        } else {
            grGit = Grgit.open(repoDir);
        }

        def originMasterSha = grGit.resolve.toCommit('origin/master').id
        getProject().file("proguard-please.properties").text = originMasterSha
    }
}

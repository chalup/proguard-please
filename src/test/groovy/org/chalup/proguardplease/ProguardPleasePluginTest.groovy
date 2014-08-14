package org.chalup.proguardplease

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.fail

class ProguardPleasePluginTest {

    @Test
    public void shouldFailIfAndroidPluginWasNotApplied() {
        Project project = ProjectBuilder.builder().build()
        try {
            project.apply plugin: 'proguard-please'
            fail();
        } catch (expected) {
        }
    }

    @Test
    public void shouldFailIfProjectHasFlavors() {
        try {
            Project p = ProjectBuilder.builder().build()
            p.apply plugin: 'android'
            p.apply plugin: 'proguard-please'

            p.android {
                compileSdkVersion 19
                buildToolsVersion "19.1"

                defaultConfig {
                    minSdkVersion 14
                    targetSdkVersion 19
                    versionCode 1
                    versionName "1.0"
                }

                productFlavors {
                    flavor1 {}
                }
            }
            p.evaluate()
            fail()
        } catch (expected) {
        }
    }
}

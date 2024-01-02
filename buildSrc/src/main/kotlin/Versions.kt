import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

/**
 * Generated by https://github.com/jmfayard/buildSrcVersions
 *
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version.
 */
object Versions {

  const val document_file_version: String = "1.0.1"

  const val org_jetbrains_kotlinx_kotlinx_coroutines: String = "1.4.1"

  const val androidx_test_espresso: String = "3.5.1"

  const val com_squareup_retrofit2: String = "2.9.0"

  const val com_squareup_okhttp3: String = "4.9.0"

  const val org_jetbrains_kotlin: String = "1.7.0"

  const val androidx_navigation: String = "2.5.3"

  const val navigation_ui_ktx: String = "2.4.1"

  const val com_google_dagger: String = "2.42"

  const val com_jakewharton: String = "10.2.3"

  const val androidx_test: String = "1.5.1"

  const val androidx_test_core: String = "1.5.0"

  const val androidx_test_orchestrator: String = "1.4.1"

  const val io_objectbox: String = "3.5.0"

  const val io_mockk: String = "1.13.5"

  const val android_arch_lifecycle_extensions: String = "1.1.1"

  const val com_android_tools_build_gradle: String = "7.4.2"

  const val de_fayard_buildsrcversions_gradle_plugin: String = "0.7.0"

  const val com_github_triplet_play_gradle_plugin: String = "3.7.0"

  const val javax_annotation_api: String = "1.3.2"

  const val leakcanary_android: String = "2.9.1"

  const val constraintlayout: String = "2.0.4"

  const val swipe_refresh_layout: String = "1.1.0"

  const val collection_ktx: String = "1.1.0"

  const val preference_ktx: String = "1.1.1"

  const val junit_jupiter: String = "5.9.1"

  const val assertj_core: String = "3.18.1"

  const val core_testing: String = "2.1.0"

  const val fragment_ktx: String = "1.2.5"

  const val testing_ktx: String = "1.2.0"

  const val threetenabp: String = "1.3.0"

  const val uiautomator: String = "2.3.0-alpha03"

  const val annotation: String = "1.2.0"

  const val simple_xml: String = "2.7.1"

  const val appcompat: String = "1.2.0"

  const val rxandroid: String = "2.1.1"

  const val core_ktx: String = "1.3.2"

  const val libkiwix: String = "1.0.0"

  const val material: String = "1.2.1"

  const val multidex: String = "2.0.1"

  const val barista: String = "4.3.0"

  const val fetch: String = "3.1.6"

  const val rxjava: String = "2.2.20"

  const val webkit: String = "1.7.0"

  const val junit: String = "1.1.4"

  const val material_show_case_view: String = "1.3.7"
}

/**
 * See issue #47: how to update buildSrcVersions itself
 * https://github.com/jmfayard/buildSrcVersions/issues/47
 */
val PluginDependenciesSpec.buildSrcVersions: PluginDependencySpec
  inline get() =
    id("de.fayard.buildSrcVersions").version(Versions.de_fayard_buildsrcversions_gradle_plugin)

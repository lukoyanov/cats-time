import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val `cats-time` = project
  .in(file("."))
  .disablePlugins(MimaPlugin)
  .enablePlugins(NoPublishPlugin)
  .settings(commonSettings)
  .aggregate(core.jvm, core.jvm)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/core"))
  .settings(commonSettings)
  .settings(
    name := "cats-time"
  )
  .jsSettings(
    libraryDependencies += "io.github.cquiroz" %%% "scala-java-time" % "2.3.0"
  )

lazy val docs = project
  .in(file("modules/docs"))
  .settings(commonSettings, micrositeSettings)
  .dependsOn(core.jvm)
  .disablePlugins(MimaPlugin)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(MdocPlugin)
  .enablePlugins(NoPublishPlugin)
  .settings(mdocIn := sourceDirectory.value / "main" / "mdoc")

// General Settings
lazy val commonSettings = Seq(
  scalaVersion := "3.0.1",
  crossScalaVersions := Seq("3.0.1", "2.12.13", "2.13.6"),
  libraryDependencies ++= Seq(
    "org.typelevel"          %%% "cats-core"               % "2.6.1",
    "org.typelevel"          %%% "cats-testkit-scalatest"  % "2.1.5" % Test,
    "org.scala-lang.modules" %%% "scala-collection-compat" % "2.5.0" % Test,
  )
)

lazy val micrositeSettings = Seq(
  micrositeName := "cats-time",
  micrositeDescription := "Cats Typeclasses for Java Time",
  micrositeAuthor := "Christopher Davenport",
  micrositeGithubOwner := "typelevel",
  micrositeGithubRepo := "cats-time",
  micrositeBaseUrl := "/cats-time",
  micrositeDocumentationUrl := "https://typelevel.org/cats-time",
  micrositeFooterText := None,
  micrositeHighlightTheme := "atom-one-light",
  micrositePalette := Map(
    "brand-primary" -> "#3e5b95",
    "brand-secondary" -> "#294066",
    "brand-tertiary" -> "#2d5799",
    "gray-dark" -> "#49494B",
    "gray" -> "#7B7B7E",
    "gray-light" -> "#E5E5E6",
    "gray-lighter" -> "#F4F3F4",
    "white-color" -> "#FFFFFF"
  ),
  Compile / scalacOptions --= Seq(
    "-Xfatal-warnings",
    "-Ywarn-unused-import",
    "-Ywarn-numeric-widen",
    "-Ywarn-dead-code",
    "-Ywarn-unused:imports",
    "-Xlint:-missing-interpolator,_"
  ),
  libraryDependencies += "com.47deg" %% "github4s" % "0.24.1",
  micrositePushSiteWith := GitHub4s,
  micrositeGitterChannel := true,
  micrositeGitterChannelUrl := "typelevel/cats",
  micrositeGithubToken := sys.env.get("GITHUB_TOKEN")
)

// General Settings
inThisBuild(
  List(
    organization := "io.chrisdavenport",
    developers := List(
      Developer(
        "ChristopherDavenport",
        "Christopher Davenport",
        "chris@christopherdavenport.tech",
        url("https://github.com/ChristopherDavenport")
      )
    ),
    homepage := Some(url("https://github.com/typelevel/cats-time")),
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    pomIncludeRepository := { _ => false },
    Compile / doc / scalacOptions ++= Seq(
      "-groups",
      "-sourcepath",
      (LocalRootProject / baseDirectory).value.getAbsolutePath,
      "-doc-source-url",
      "https://github.com/typelevel/cats-time/blob/v" + version.value + "€{FILE_PATH}.scala"
    )
  )
)

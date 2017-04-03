val pluginName = "gerrit-support"

name := pluginName

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

val scalatraV = "2.5.+"

libraryDependencies ++= Seq(
  // provided by gerrit
  "com.google.inject"     %   "guice"             % "3.0"       % Provided,
  "com.google.gerrit"     %   "gerrit-plugin-api" % "2.11"      % Provided,
  "com.google.code.gson"  %   "gson"              % "2.7"       % Provided,
  "joda-time"             %   "joda-time"         % "2.9.4"     % Provided,

  // added to assembly
  "org.scalatra"          %%  "scalatra"          % scalatraV,
  "org.jhardware"         %   "jHardware"         % "0.8.4",

  // test dependencies
  "org.scalatra"          %%  "scalatra-scalatest"% scalatraV   % Test,
  "org.scalatest"         %%  "scalatest"         % "3.0.1"     % Test,
  "net.codingwell"        %%  "scala-guice"       % "4.1.0"     % Test
  )

assemblyJarName in assembly := s"$pluginName.jar"

packageOptions in (Compile, packageBin) +=  {
  Package.ManifestAttributes(
    "Gerrit-ApiType" -> "plugin",
    "Gerrit-PluginName" -> pluginName)
}

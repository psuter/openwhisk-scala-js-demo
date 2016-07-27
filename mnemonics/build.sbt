enablePlugins(ScalaJSPlugin)

name := "whisk-mnemonics"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature")

scalaJSUseRhino in Global := false

scalaJSOutputWrapper := ("", """
// OpenWhisk action entrypoint
function main(args) {
  return mnemonics.Main().main(require, args);
}
""".stripMargin)

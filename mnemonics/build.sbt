enablePlugins(ScalaJSPlugin)

name := "whisk-mnemonics"

scalaVersion := "2.11.8"

scalaJSUseRhino in Global := false

scalaJSOutputWrapper := ("", """
// OpenWhisk action entrypoint
function main(args) {
  return mnemonics.Main().main(require, args);
}
""".stripMargin)

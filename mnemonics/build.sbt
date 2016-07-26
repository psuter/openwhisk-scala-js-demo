enablePlugins(ScalaJSPlugin)

name := "whisk-mnemonics"

scalaVersion := "2.11.8"

scalaJSUseRhino in Global := false

scalaJSOutputWrapper := ("", """
// OpenWhisk action entrypoint
function main(args) {
  return new Promise(function (resolve, reject) {
    mnemonics.Main().main({
      "require" : require,
      "resolve" : resolve,
      "reject"  : reject,
      "args"    : args
    });
  });
}
""".stripMargin)

enablePlugins(ScalaJSPlugin)

name := "hello-whisk"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature")

scalaJSOutputWrapper := ("", """
// OpenWhisk action entrypoint
function main(args) {
  return hello.Main().main(args);
}
""")

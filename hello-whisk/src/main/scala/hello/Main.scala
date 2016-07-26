package hello

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
object Main {
  @JSExport
  def main(args: js.Dictionary[js.Any]) : js.Dictionary[js.Any] = {
    val name = args.getOrElse("name", "stranger").toString

    js.Dictionary("greeting" -> s"Hello $name!")
  }
}

package mnemonics

import scala.concurrent.Future
import scala.concurrent.Promise
import scala.util.{ Try, Success, Failure }
import scala.language.postfixOps

import scala.scalajs.js
import scala.scalajs.js.DynamicImplicits._
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

@JSExport
object Main {
  @JSExport
  def main(require: js.Dynamic, args: js.Dictionary[js.Any]) : js.Promise[js.Any] = {
    loadWords(require) map { wordList =>
      val number = args("number").toString
      val coder = new Coder(wordList)
      val results = coder.translate(number).toSeq
      js.Dictionary("phrases" -> js.Array(results : _*))
    } recover {
      case failure =>
        js.Dictionary("error" -> failure.getMessage)
    } toJSPromise
  }

  private def loadWords(require: js.Dynamic) : Future[List[String]] = {
    val request = require("request")
    val bodyPromise = Promise[String]

    request("https://users.cs.duke.edu/~ola/ap/linuxwords", (error: js.Dynamic, response: js.Dynamic, body: js.Dynamic) => {
      if(!error && response.statusCode == 200) {
        bodyPromise.trySuccess(body.toString)
      } else {
        bodyPromise.tryFailure(new Exception("Request to word list failed."))
      }
    })

    bodyPromise.future.map { b =>
      b.split("\n").map(_.trim).filter(_.matches("[a-zA-Z]+")).toList 
    }
  }
}

// Reproduced from https://gist.github.com/retronym/1158801
class Coder(words: List[String]) {
  private val mnemonics = Map(
      '2' -> "ABC", '3' -> "DEF", '4' -> "GHI", '5' -> "JKL", 
      '6' -> "MNO", '7' -> "PQRS", '8' -> "TUV", '9' -> "WXYZ")
      
  /** Invert the mnemonics map to give a map from chars 'A' ... 'Z' to '2' ... '9' */
  private val charCode: Map[Char, Char] = 
    for ((digit, str) <- mnemonics; letter <- str) yield letter -> digit
          
  /** Maps a word to the digit string it can represent, e.g. “Java” -> “5282” */
  private def wordCode(word: String): String = word.toUpperCase map charCode
    
  /** A map from digit strings to the words that represent them, 
    * e,g. “5282” -> List(“Java”, “Kata”, “Lava”, ...) 
    * Note: A missing number should map to the empty set, e.g. "1111" -> List()
    */
  private val wordsForNum: Map[String, Seq[String]] = (words groupBy wordCode) withDefaultValue List()
  
  /** Return all ways to encode a number as a list of words */
  def encode(number: String): Set[List[String]] = 
    if (number.isEmpty) Set(List())
    else {
      for {
        split <- 1 to number.length
        word <- wordsForNum(number take split)
        rest <- encode(number drop split)
      } yield word :: rest
    }.toSet

  /** Maps a number to a list of all word phrases that can represent it */
  def translate(number: String): Set[String] = encode(number) map (_ mkString " ")
}

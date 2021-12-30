package sandbox

object Sdbx2 extends App {

  import scala.io.StdIn

  case class IndentAnd[T](indent: Int, item: T) {
    def map[S](f: T => S): IndentAnd[S] = IndentAnd[S](indent, f(item))
  }

  sealed trait InputLine

  object InputLine {
    case class Output(text: String, label: Option[Int]) extends InputLine

    case class Answer(text: String) extends InputLine

    sealed trait Action extends InputLine

    case class Goto(label: Int) extends Action

    case class Conclusion(text: String) extends Action
  }

  // main method in "Solution" will be run as your answer
  object Solution {

    sealed trait Node

    final case class MultipleQuestions(questions: List[Question]) extends Node

    final case class Question(answer: Option[Answer]) extends Node

    MultipleQuestions(List(
      Question(Some(Answer("Yes")))
    ))

    /**
     * Steps:
     * Unify flat Syntax tree with user answers
     *
     */
    def printConversation(flatTree: Seq[IndentAnd[InputLine]], userAnswers: Seq[String]): Unit = {
      flatTree.foreach {
        case IndentAnd(indent, input) =>
      }
      //          Message(
      //            List("asdad", "asda"),
      //            PossibleAnswers(
      //              Answer("yes",
      //                Message("adas")),
      //              Answer("no",
      //                Message("asda"))
      //              ))

    }

    def readLines(): Seq[String] = {
      def loop(lines: Seq[String]): Seq[String] = Option(StdIn.readLine()) match {
        case None => lines
        case Some(line) => loop(lines :+ line)
      }

      loop(Nil)
    }

    def processLine(line: String): IndentAnd[InputLine] = {
      def process(chars: List[Char], count: Int): IndentAnd[String] = {
        chars match {
          case ' ' :: rest => process(rest, count + 1)
          case _ => IndentAnd[String](count, chars.mkString)
        }
      }

      def makeInputLine(line: String): InputLine = {
        def makeOutput(chars: List[Char], digits: Seq[Char]): InputLine.Output =
          chars match {
            case digit :: rest if digit.isDigit => makeOutput(rest, digits :+ digit)
            case ':' :: rest if digits.length > 0 => InputLine.Output(rest.mkString, Some(digits.mkString.toInt))
            case string => InputLine.Output(digits.mkString + string.mkString, None)
          }

        line.toList match {
          case '-' :: answer => InputLine.Answer(answer.mkString)
          case '>' :: label => InputLine.Goto(label.mkString.toInt)
          case '=' :: conclusion => InputLine.Conclusion(conclusion.mkString)
          case output => makeOutput(output, Seq[Char]())
        }
      }

      process(line.toList, 0).map(makeInputLine)
    }

    def processLines(lines: Seq[String]): (Seq[IndentAnd[InputLine]], Seq[String]) =
      (lines.takeWhile(_ != "---").map(processLine), lines.dropWhile(_ != "---").tail)

    def main(args: Array[String]) {
      val (flatTree, userAnswers) = processLines(readLines())
      printConversation(flatTree, userAnswers)
    }
  }
}

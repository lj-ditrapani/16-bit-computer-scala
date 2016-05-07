package info.ditrapani.gameoflife

import scala.util.{Try, Success, Failure}
import java.nio.file.{Files, Paths}

object BinFileReader {
  type Maybe = Either[String, Unit]
  type IfBytes = Either[String, Array[Byte]]
  type IfChars = Either[String, Array[Char]]

  def read(value: String): IfChars = {
    Try(Files.readAllBytes(Paths.get(value))) match {
      case Failure(exception) => Left(exception.toString())
      case Success(byte_array) => processBytes(byte_array)
    }
  }

  def processBytes(byte_array: Array[Byte]): IfChars = {
    def notEmpty(): Maybe =
      wrapInMaybe(!byte_array.isEmpty, "binary file must not be empty")

    def notTooBig(x: Unit): Maybe = {
      val msg = "binary file must be less than or equal to 256 KB (128 KW)"
      wrapInMaybe(byte_array.size <= (256 * 1024), msg)
    }

    def evenNumberOfBytes(x: Unit): Maybe = {
      val msg = """binary file must contain only 16-bit values
                  | (must have an even number of bytes)
                  |""".stripMargin.replaceAll("\n", "")

      wrapInMaybe(byte_array.size % 2 == 0, msg)
    }

    def wrapInMaybe(is_right: Boolean, msg: String): Maybe = {
      is_right match {
        case true => Right()
        case false => Left(msg)
      }
    }

    def makeChars(x: Unit): Array[Char] = {
      byte_array.grouped(2).map(bytePair2Char).toArray
    }

    notEmpty()
      .right.flatMap(notTooBig)
      .right.flatMap(evenNumberOfBytes)
      .right.map(makeChars)
  }

  def bytePair2Char(pair: Array[Byte]): Char = ((pair(0) << 8) + pair(1)).toChar

  sealed abstract class BytesCheck {
    def check(is_good: Boolean, msg: String): BytesCheck
    def get: IfBytes
  }

  case class Fail(msg: String) extends BytesCheck {
    override def check(is_good: Boolean, msg: String): BytesCheck = this

    override def get: IfBytes = Left(msg)
  }

  case class Pass(byte_array: Array[Byte]) extends BytesCheck {
    override def check(is_good: Boolean, msg: String): BytesCheck = is_good match {
      case true => this
      case false => new Fail(msg)
    }

    override def get: IfBytes = Right(byte_array)
  }
}

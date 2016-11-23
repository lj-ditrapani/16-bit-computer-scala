package info.ditrapani.ljdcomputer

import scala.util.{Try, Success, Failure}
import java.nio.file.{Files, Paths}

object BinFileReader {
  type IfChars = Either[String, Vector[Char]]
  type Result = (Boolean, String)
  type StrOption = Option[String]

  def read(value: String): IfChars = {
    Try(Files.readAllBytes(Paths.get(value))) match {
      case Failure(exception) => Left(exception.toString())
      case Success(byte_array) => new ByteProcessor(byte_array).process()
    }
  }

  class ByteProcessor(val byte_array: Array[Byte]) {
    def process(): IfChars = {
      val bytes_check = Pass
        .check(notEmpty)
        .check(notTooBig)
        .check(evenNumberOfBytes)

      bytes_check.get match {
        case None => Right(bytes2Chars(byte_array))
        case Some(msg) => Left(msg)
      }
    }

    def notEmpty: Result =
      (!byte_array.isEmpty, "binary file must not be empty")

    def notTooBig: Result = {
      val msg = "binary file must be less than or equal to 265,248 Bytes (132,624 W)"
      (byte_array.size <= ((128 * 1024 + 1552) * 2), msg)
    }

    def evenNumberOfBytes: Result = {
      val msg = """binary file must contain only 16-bit values
                  | (must have an even number of bytes)
                  |""".stripMargin.replaceAll("\n", "")

      (byte_array.size % 2 == 0, msg)
    }
  }

  def bytes2Chars(byte_array: Array[Byte]): Vector[Char] =
    byte_array.grouped(2).map(bytePair2Char).toVector

  def bytePair2Char(pair: Array[Byte]): Char = {
    val int_pair = pair.map(_ & 0xFF)
    ((int_pair(0) << 8) + int_pair(1)).toChar
  }

  sealed abstract class BinCheck {
    def check(pair: (Boolean, String)): BinCheck

    def get: StrOption
  }

  final case class Fail(msg: String) extends BinCheck {
    override def check(pair: Result): BinCheck = this

    override def get: StrOption = Some(msg)
  }

  object Pass extends BinCheck {
    override def check(pair: (Boolean, String)): BinCheck = pair match {
      case (true, _) => this
      case (false, msg) => new Fail(msg)
    }

    override def get: StrOption = None
  }
}

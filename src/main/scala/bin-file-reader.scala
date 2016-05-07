package info.ditrapani.gameoflife

import scala.util.{Try, Success, Failure}
import java.nio.file.{Files, Paths}

object BinFileReader {
  type IfBytes = Either[String, Array[Byte]]
  type IfChars = Either[String, Array[Char]]

  def read(value: String): IfChars = {
    Try(Files.readAllBytes(Paths.get(value))) match {
      case Failure(exception) =>
        Left(exception.toString())
      case Success(byte_array) => notEmpty(byte_array)
        .right.flatMap(notTooBig)
        .right.flatMap(evenNumberOfBytes)
        .right.map(makeChars)
    }
  }

  def notEmpty(byte_array: Array[Byte]): IfBytes = {
    byte_array.isEmpty match {
      case true => Left("binary file must not be empty")
      case false => Right(byte_array)
    }
  }

  def notTooBig(byte_array: Array[Byte]): IfBytes = {
    val msg = "binary file must be less than or equal to 256 KB (128 KW)"

    byte_array.size > (256 * 1024) match {
      case true => Left(msg)
      case false => Right(byte_array)
    }
  }

  def evenNumberOfBytes(byte_array: Array[Byte]): IfBytes = {
    val msg = """binary file must contain only 16-bit values
                | (must have an even number of bytes)
                |""".stripMargin.replaceAll("\n", "")

    byte_array.size % 2 == 0 match {
      case false => Left(msg)
      case true => Right(byte_array)
    }
  }

  def makeChars(byte_array: Array[Byte]): Array[Char] = {
    byte_array.grouped(2).map(bytePair2Char).toArray
  }

  def bytePair2Char(pair: Array[Byte]): Char = ((pair(0) << 8) + pair(1)).toChar
}

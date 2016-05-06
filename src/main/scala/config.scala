package info.ditrapani.gameoflife

import scala.util.{Try, Success, Failure}
import java.nio.file.{Files, Paths}

case class Config(
  binary_set: Boolean,
  binary: Array[Char],
  pixel_multiplier: Byte
)

object Config {
  type IfConfig = Either[String, Config]
  type IfInt = Either[String, Int]

  val emptyConfig: Config = Config(false, Array(), 4)

  def load(help_params: Seq[String], params: Map[String,String]): IfConfig = {
    if (help_params.exists(p => p == "--help")) {
      Left("Printing help text...")
    } else if (!help_params.isEmpty) {
      Left(s"Unknown command line parameter '${help_params.head}'")
    } else {
      val if_config1: IfConfig = Right(Config.emptyConfig)
      val if_config2 = params.foldLeft(if_config1) { (if_config, kv) =>
        if_config.right.flatMap { addParams(kv, _) }
      }
      if_config2.right.flatMap { (config) =>
        config.binary_set match {
          case false => Left("Must define a binary file to execute with --f")
          case true => Right(config)
        }
      }
    }
  }

  def addParams(kv: (String, String), config: Config): IfConfig = {
    val (flag, value) = kv
    flag match {
      case "f" => handleFile(value, config)
      case "m" => handlePixelMultiplier(value, config)
      case _ => Left(s"Unknown command line parameter '--${flag}'")
    }
  }

  def handleFile(value: String, config: Config): IfConfig = {
    Try(Files.readAllBytes(Paths.get(value))) match {
      case Failure(exception) =>
        Left(exception.toString())
      case Success(byte_array) => {
        def bytePair2Char(pair: Array[Byte]): Char =
          ((pair(0) << 8) + pair(1)).toChar
        Right(
          config.copy(
            binary_set = true,
            binary = byte_array.grouped(2).map(bytePair2Char).toArray
          )
        )
      }
    }
  }

  def handlePixelMultiplier(value: String, config: Config): IfConfig = {
    parseIntAndDo(value, 1, 64, "--m") {
      (i) => config.copy(pixel_multiplier = i.toByte)
    }
  }

  def parseInt(value: String, lower: Int, upper: Int, prefix: String): IfInt = {
    val left = Left(prefix + s" must be an integer between $lower and $upper")
    def onSuccess(num: Int): IfInt = {
      (num < lower || num > upper) match {
        case true => left
        case false => Right(num)
      }
    }

    Try(value.toInt) match {
      case Failure(_) => left
      case Success(num) => onSuccess(num)
    }
  }

  def parseIntAndDo(value: String, lower: Int, upper: Int, prefix: String)
  (onSuccess: Int => Config): IfConfig = {
    parseInt(value, lower, upper, prefix) match {
      case Left(s) => Left(s)
      case Right(num) => Right(onSuccess(num))
    }
  }
}

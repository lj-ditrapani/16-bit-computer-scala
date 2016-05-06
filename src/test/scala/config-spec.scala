package info.ditrapani.gameoflife

import org.scalatest.{FunSpec, Matchers}

class ConfigSpec extends FunSpec with Matchers {
  describe("Config Class") {
    it("has instance values") {
      val config = Config.emptyConfig
      config.binary_set should be (false)
    }
  }

  describe("Config Object") {
    describe("load") {
      val bin_path = "src/test/resources/abcd.bin"
      describe("--help") {
        it("returns Left") {
          Config.load(List("--help"), Map()) should === (
            Left("Printing help text...")
          )
        }
      }

      it("returns Left if unknown parameters found in help_params") {
        Config.load(List("foo"), Map()) should === (
          Left("Unknown command line parameter 'foo'")
        )
      }

      it("returns Left if unknown parameters found in params") {
        Config.load(List(), Map("f" -> bin_path, "a" -> "")) should === (
          Left("Unknown command line parameter '--a'")
        )
      }

      describe("--f") {
        it("returns Left if not set") {
          Config.load(List(), Map()) should === (
            Left("Must define a binary file to execute with --f")
          )
        }

        it("returns a Right if --f and --m are set") {
          Config.load(List(), Map("f" -> bin_path, "m" -> "2")) match {
            case Right(Config(binary_set, binary, multiplier)) => {
              binary_set should === (true)
              binary should === (Array('\u6162', '\u6364'))
              multiplier should === (2)
            }
          }
        }

        it("returns a Right if --f is set") {
          Config.load(List(), Map("f" -> bin_path)) match {
            case Right(Config(binary_set, binary, multiplier)) => {
              binary_set should === (true)
              binary should === (Array('\u6162', '\u6364'))
              multiplier should === (4)
            }
          }
        }

        it("returns a Left if --f is not a file") {
          val f = "src/main/resources/"
          val msg = "java.io.IOException: Is a directory"
          Config.load(List(), Map("f" -> f)) should === (Left(msg))
        }
      }

      describe("--m") {
        it("returns a Left if not a number") {
          Config.load(List(), Map("f" -> bin_path, "m" -> "foo")) should === (
            Left("--m must be an integer between 1 and 64")
          )
        }

        it("returns a Left if --m is 0") {
          Config.load(List(), Map("f" -> bin_path, "m" -> "0")) should === (
            Left("--m must be an integer between 1 and 64")
          )
        }

        it("returns a Left if --m is negative") {
          Config.load(List(), Map("f" -> bin_path, "m" -> "-1")) should === (
            Left("--m must be an integer between 1 and 64")
          )
        }
      }
    }

    describe("parseInt") {
      it("returns Right(Int) if string is int within bounds") {
        Config.parseInt("5", 4, 6, "") should === (Right(5))
      }

      it("can pass an optional prefix") {
        Config.parseInt("5", 4, 6, "foo") should === (Right(5))
      }

      it("returns Left if string is not int") {
        Config.parseInt("foo", 4, 6, "Prefix") should === (
          Left("Prefix must be an integer between 4 and 6")
        )
      }

      it("returns Left if string int below bound") {
        Config.parseInt("9", 10, 20, "Prefix") should === (
          Left("Prefix must be an integer between 10 and 20")
        )
      }

      it("returns Left if string int above bound") {
        Config.parseInt("21", 10, 20, "") should === (
          Left(" must be an integer between 10 and 20")
        )
      }
    }
  }
}

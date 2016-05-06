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
        Config.load(List(), Map("f" -> "abcd.bin", "a" -> "")) should === (
          Left("Unknown command line parameter '--a'")
        )
      }

      describe("--f") {
        it("returns Left if not set") {
          Config.load(List(), Map()) should === (
            Left("Must define --f as binary file source")
          )
        }

        it("returns a Right if --f and --m are set") {
          Config.load(List(), Map("f" -> "abcd.bin", "m" -> "2")) should === (
            Right(
              Config.emptyConfig.copy(
                binary_set = true,
                binary = Array('\u6162', '\u6364'),
                pixel_multiplier = 2
              )
            )
          )
        }

        it("returns a Right if --f is set") {
          val f = "src/main/resources/blinker.txt"
          Config.load(List(), Map("f" -> "abcd.bin")) should === (
            Right(
              Config.emptyConfig.copy(
                binary_set = true,
                binary = Array('\u6162', '\u6364')
              )
            )
          )
        }

        it("returns a Left if --f is not a file") {
          val f = "src/main/resources/"
          val msg = """java.io.FileNotFoundException: src/main/resources
                      |(Is a directory)""".stripMargin.replaceAll("\n", " ")
          Config.load(List(), Map("f" -> f)) should === (Left(msg))
        }
      }

      describe("--m") {
        it("returns a Left if not a number") {
          Config.load(List(), Map("f" -> "abcd.bin", "t" -> "foo")) should === (
            Left("--m must be an integer between 1 and 256")
          )
        }

        it("returns a Left if --m is 0") {
          Config.load(List(), Map("f" -> "abcd.bin", "m" -> "0")) should === (
            Left("--m must be an integer between 1 and 256")
          )
        }

        it("returns a Left if --m is negative") {
          Config.load(List(), Map("f" -> "abcd.bin", "m" -> "-1")) should === (
            Left("--t must be an integer between 1 and 256")
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

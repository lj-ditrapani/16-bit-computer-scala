package info.ditrapani.ljdcomputer.config

import info.ditrapani.ljdcomputer.Spec

class ConfigSpec extends Spec {
  describe("Config Class") {
    it("has instance values") {
      val config = Config.emptyConfig
      config.binary_set shouldBe false
    }
  }

  describe("Config Object") {
    describe("load") {
      val prefix = "src/test/resources"
      val bin_path = s"${prefix}/abcd.bin"

      describe("--help") {
        it("returns Help") {
          Config.load(List("--help"), Map()) shouldBe Help
        }
      }

      it("returns Error if unknown parameters found in help_params") {
        Config.load(List("foo"), Map()) shouldBe Error(
          "Unknown command line parameter 'foo'"
        )
      }

      it("returns Error if unknown parameters found in params") {
        Config.load(List(), Map("f" -> bin_path, "a" -> "")) shouldBe Error(
          "Unknown command line parameter '--a'"
        )
      }

      describe("--f") {
        it("returns Error if --f not set") {
          Config.load(List(), Map()) shouldBe Error(
            "Must define a binary file to execute with --f"
          )
        }

        it("returns a Good(config) if --f and --m are set") {
          Config.load(List(), Map("f" -> bin_path, "m" -> "2")) match {
            case Good(Config(binary_set, binary, multiplier)) => {
              binary_set shouldBe true
              binary shouldBe Vector('\u6162', '\u6364')
              multiplier shouldBe 2
            }
          }
        }

        it("returns a Good(config) if --f is set") {
          Config.load(List(), Map("f" -> bin_path)) match {
            case Good(Config(binary_set, binary, multiplier)) => {
              binary_set shouldBe true
              binary shouldBe Vector('\u6162', '\u6364')
              multiplier shouldBe 4
            }
          }
        }

        it("returns an Error if --f is not a file") {
          val msg = "java.io.IOException: Is a directory"
          Config.load(List(), Map("f" -> prefix)) shouldBe Error(msg)
        }

        it("returns an Error if file is not made of only 16-bit values") {
          val bin_path = s"${prefix}/abc.bin"
          val msg = """binary file must contain only 16-bit values
                      | (must have an even number of bytes)
                      |""".stripMargin.replaceAll("\n", "")
          Config.load(List(), Map("f" -> bin_path)) shouldBe Error(msg)
        }

        it("returns an Error if file is empty") {
          val bin_path = s"${prefix}/empty.bin"
          val msg = "binary file must not be empty"
          Config.load(List(), Map("f" -> bin_path)) shouldBe Error(msg)
        }
      }

      describe("--m") {
        it("returns an Error if --m not a number") {
          Config.load(List(), Map("f" -> bin_path, "m" -> "foo")) shouldBe Error(
            "--m must be an integer between 1 and 64"
          )
        }

        it("returns an Error if --m is 0") {
          Config.load(List(), Map("f" -> bin_path, "m" -> "0")) shouldBe Error(
            "--m must be an integer between 1 and 64"
          )
        }

        it("returns an Error if --m is negative") {
          Config.load(List(), Map("f" -> bin_path, "m" -> "-1")) shouldBe Error(
            "--m must be an integer between 1 and 64"
          )
        }
      }
    }

    describe("parseInt") {
      it("returns Right(Int) if string is int within bounds") {
        Config.parseInt("5", 4, 6, "") shouldBe Right(5)
      }

      it("can pass an optional prefix") {
        Config.parseInt("5", 4, 6, "foo") shouldBe Right(5)
      }

      it("returns Left if string is not int") {
        Config.parseInt("foo", 4, 6, "Prefix") shouldBe Left(
          "Prefix must be an integer between 4 and 6"
        )
      }

      it("returns Left if string int below bound") {
        Config.parseInt("9", 10, 20, "Prefix") shouldBe Left(
          "Prefix must be an integer between 10 and 20"
        )
      }

      it("returns Left if string int above bound") {
        Config.parseInt("21", 10, 20, "") shouldBe Left(
          " must be an integer between 10 and 20"
        )
      }
    }
  }
}

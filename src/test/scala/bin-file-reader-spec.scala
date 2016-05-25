package info.ditrapani.ljdcomputer

class BinFileReaderSpec extends Spec {
  describe("read") {
    it("returns Success(char_vector) if no issues") {
      val file = "src/test/resources/abcd.bin"
      BinFileReader.read(file) match {
        case Right(vector) => {
          vector.length should === (2)
          vector(0) should === (24930.toChar)
        }
      }
    }

    it("returns Success(char_vector) if no issues (negative bytes)") {
      val file = "src/test/resources/0xFFFFAAAA.bin"
      BinFileReader.read(file) match {
        case Right(vector) => {
          vector.length should === (2)
          vector(0) should === (0xFFFF.toChar)
          vector(1) should === (0xAAAA.toChar)
        }
      }
    }

    it("returns Failure(exception) if issues arrise") {
      BinFileReader.read("not_a_file") match {
        case Left(msg) => msg should include ("NoSuchFileException")
      }
    }
  }

  describe("bytePair2Char") {
    it("handles negative bytes: 0xFF, 0xFF => 0xFFFF") {
      val bytes: Array[Byte] = Vector(0xFF, 0xFF).map(_.toByte).toArray
      BinFileReader.bytePair2Char(bytes) should === (0xFFFF.toChar)
    }

    it("handles negative bytes: 0xAA, 0xAA => 0xAAAA") {
      val bytes: Array[Byte] = Vector(0xAA, 0xAA).map(_.toByte).toArray
      BinFileReader.bytePair2Char(bytes) should === (0xAAAA.toChar)
    }
  }
}

class ByteProcessorSpec extends Spec {
  def process(byte_array: Array[Byte]): BinFileReader.IfChars = {
    new BinFileReader.ByteProcessor(byte_array).process()
  }

  describe("process") {
    it("returns Left if byte_array is empty") {
      process(Array()) should === (Left("binary file must not be empty"))
    }

    it("returns Left if byte_array is greater than 256 KB") {
      val a: Array[Byte] = Array.fill[Byte](256 * 1024 + 1)(0)
      process(a) match {
        case Left(msg) => msg should include ("less than or equal to 256")
      }
    }

    it("returns Left if byte_array has odd number of bytes") {
      process(Array(0x61)) match {
        case Left(msg) => msg should include ("even number of bytes")
      }
    }

    it("returns Right if 256 KB byte_array passes all checks") {
      val a: Array[Byte] = Array.fill[Byte](256 * 1024)(0)
      process(a) match { case Right(_) => Unit }
    }

    it("returns Right if byte_array passes all checks") {
      process(Array(0x61, 0x62)) match {
        case Right(Vector(x)) => x should === (24930.toChar)
      }
    }
  }
}

class BinCheckSpec extends Spec {
  val fail_check = BinFileReader.Fail("foo")

  describe("Fail class") {
    describe("check") {
      it("returns self") {
        fail_check.check((false, "bar")) should === (fail_check)
      }
    }

    describe("get") {
      it("returns Some(msg)") {
        fail_check.get should === (Some("foo"))
      }
    }
  }

  describe("Pass class") {
    val pass_check = BinFileReader.Pass

    describe("check") {
      it("returns self on true") {
        pass_check.check(true, "bar") should === (pass_check)
      }

      it("returns Fail(msg) on false") {
        pass_check.check(false, "foo") should === (fail_check)
      }
    }

    describe("get") {
      it("returns None") {
        pass_check.get should === (None)
      }
    }
  }
}

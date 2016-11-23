package info.ditrapani.ljdcomputer

import org.scalatest.EitherValues._

class BinFileReaderSpec extends Spec {
  describe("read") {
    it("returns Success(char_vector) if no issues") {
      val file = "src/test/resources/abcd.bin"
      val vector = BinFileReader.read(file).right.value
      vector.length shouldBe 2
      vector(0) shouldBe 24930.toChar
    }

    it("returns Success(char_vector) if no issues (negative bytes)") {
      val file = "src/test/resources/0xFFFFAAAA.bin"
      val vector = BinFileReader.read(file).right.value
      vector.length shouldBe 2
      vector(0) shouldBe 0xFFFF.toChar
      vector(1) shouldBe 0xAAAA.toChar
    }

    it("returns Failure(exception) if issues arrise") {
      val msg = BinFileReader.read("not_a_file").left.value
      msg should include ("NoSuchFileException")
    }
  }

  describe("bytePair2Char") {
    it("handles negative bytes: 0xFF, 0xFF => 0xFFFF") {
      val bytes: Array[Byte] = Vector(0xFF, 0xFF).map(_.toByte).toArray
      BinFileReader.bytePair2Char(bytes) shouldBe 0xFFFF.toChar
    }

    it("handles negative bytes: 0xAA, 0xAA => 0xAAAA") {
      val bytes: Array[Byte] = Vector(0xAA, 0xAA).map(_.toByte).toArray
      BinFileReader.bytePair2Char(bytes) shouldBe 0xAAAA.toChar
    }
  }
}

class ByteProcessorSpec extends Spec {
  def process(byte_array: Array[Byte]): BinFileReader.IfChars = {
    new BinFileReader.ByteProcessor(byte_array).process()
  }

  describe("process") {
    it("returns Left if byte_array is empty") {
      process(Array()) shouldBe Left("binary file must not be empty")
    }

    it("returns Left if byte_array is greater than 265,248 B") {
      val a: Array[Byte] = Array.fill[Byte](265249)(0)
      process(a).left.value should include ("less than or equal to 265,248")
    }

    it("returns Left if byte_array has odd number of bytes") {
      process(Array(0x61)).left.value should include ("even number of bytes")
    }

    it("returns Right if 256 KB byte_array passes all checks") {
      val a: Array[Byte] = Array.fill[Byte](256 * 1024)(0)
      process(a).right.value.length shouldBe 128 * 1024
    }

    it("returns Right if byte_array passes all checks") {
      process(Array(0x61, 0x62)).right.value shouldBe Vector(24930.toChar)
    }
  }
}

class BinCheckSpec extends Spec {
  val fail_check = BinFileReader.Fail("foo")

  describe("Fail class") {
    describe("check") {
      it("returns self") {
        fail_check.check((false, "bar")) shouldBe fail_check
      }
    }

    describe("get") {
      it("returns Some(msg)") {
        fail_check.get shouldBe Some("foo")
      }
    }
  }

  describe("Pass class") {
    val pass_check = BinFileReader.Pass

    describe("check") {
      it("returns self on true") {
        pass_check.check((true, "bar")) shouldBe pass_check
      }

      it("returns Fail(msg) on false") {
        pass_check.check((false, "foo")) shouldBe fail_check
      }
    }

    describe("get") {
      it("returns None") {
        pass_check.get shouldBe None
      }
    }
  }
}

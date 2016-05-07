package info.ditrapani.ljdcomputer

import org.scalatest.{FunSpec, Matchers}

class BinFileReaderSpec extends FunSpec with Matchers {
}

class ByteProcessorSpec extends FunSpec with Matchers {
  describe("ByteProcessor class") {
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
          case Right(Array(x)) => x should === (24930.toChar)
        }
      }
    }
  }
}

class BytesCheckSpec extends FunSpec with Matchers {
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

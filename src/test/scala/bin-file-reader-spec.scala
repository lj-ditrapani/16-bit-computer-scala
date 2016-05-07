package info.ditrapani.ljdcomputer

import org.scalatest.{FunSpec, Matchers}

class BinFileReaderSpec extends FunSpec with Matchers {
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

package info.ditrapani.ljdcomputer.cpu

import info.ditrapani.ljdcomputer.Spec

class BitUtilsSpec extends Spec {
  def int2bool(i: Int): Boolean = i match {
    case 0 => false
    case _ => true
  }

  describe("getNibbles") {
    it("splits a word into 4 4-bit nibbles") {
      BitUtils.getNibbles(0xABCD.toChar).shouldBe((0xA, 0xB, 0xC, 0xD))
    }

    it("splits another word into 4 4-bit nibbles") {
      BitUtils.getNibbles(0x7712.toChar).shouldBe((0x7, 0x7, 0x1, 0x2))
    }
  }

  describe("isPositiveOrZero") {
    val tests = List(
      (0, true),
      (1, true),
      (0x7FFF, true),
      (0x8000, false),
      (0x8001, false),
      (0xFFFF, false)
    )

    for (test <- tests) {
      val (word, result) = test
      it(s"${word} is ${result}") {
        BitUtils.isPositiveOrZero(word.toChar) shouldBe result
      }
    }
  }

  describe("isNegative") {
    val tests = List(
      (0, false),
      (1, false),
      (0x7FFF, false),
      (0x8000, true),
      (0x8001, true),
      (0xFFFF, true)
    )

    for (test <- tests) {
      val (word, result) = test
      it(s"${word} is ${result}") {
        BitUtils.isNegative(word.toChar) shouldBe result
      }
    }
  }

  describe("isTruePositive") {
    val tests = List(
      (0, false),
      (1, true),
      (0x7FFF, true),
      (0x8000, false),
      (0x8001, false),
      (0xFFFF, false)
    )

    for (test <- tests) {
      val (word, result) = test
      it(s"${word} is ${result}") {
        BitUtils.isTruePositive(word.toChar) shouldBe result
      }
    }
  }

  describe("hasOverflowedOnAdd") {
    val tests = List(
      (0x0000, 0x0000, 0x0000, false),
      (0x8000, 0x8000, 0x0000, true),
      (0xFFFF, 0xFFFF, 0xFFFE, false),
      (0x8000, 0xFFFF, 0x7FFF, true),
      (0x8000, 0x7FFF, 0xFFFF, false),
      (0x7FFE, 0x0001, 0x7FFF, false),
      (0x7FFF, 0x0000, 0x7FFF, false),
      (0x7FFF, 0x0001, 0x8000, true),
      (0x0001, 0x7FFF, 0x8000, true)
    )

    for (test <- tests) {
      val (a, b, sum, result) = test
      it(s"${a} + ${b} = ${sum} -> ${result}") {
        BitUtils.hasOverflowedOnAdd(a.toChar, b.toChar, sum.toChar) shouldBe result
      }
    }
  }

  describe("positionOfLastBitShifted") {
    val tests = List(
      (DLeft, 1, 15),
      (DRight, 1, 0),
      (DLeft, 4, 12),
      (DRight, 4, 3),
      (DLeft, 8, 8),
      (DRight, 8, 7)
    )

    for (test <- tests) {
      val (direction, amount, position) = test
      it(s"on shift ${direction} by ${amount} = ${position}") {
        BitUtils.positionOfLastBitShifted(direction, amount) shouldBe position
      }
    }
  }

  describe("oneBitWordMask") {
    val tests = List(
      (0, 0x0001),
      (1, 0x0002),
      (3, 0x0008),
      (4, 0x0010),
      (8, 0x0100),
      (15, 0x8000),
      (14, 0x4000)
    )

    for ((position, mask) <- tests) {
      it(s"given position ${position} produces mask ${mask}") {
        BitUtils.oneBitWordMask(position) shouldBe mask
      }
    }
  }

  describe("getShiftCarry") {
    val tests = List(
      (DLeft, 1, 0x8000, true),
      (DLeft, 1, 0x7FFF, false),
      (DRight, 1, 0x0001, true),
      (DRight, 1, 0xFFFE, false),
      (DLeft, 4, 0x1000, true),
      (DRight, 4, 0xFFF7, false),
      (DLeft, 8, 0xFEFF, false),
      (DRight, 8, 0x0080, true)
    )

    for ((direction, amount, value, carry) <- tests) {
      it(s"shift value ${value} ${direction} by ${amount} => carry is ${carry}") {
        BitUtils.getShiftCarry(value.toChar, direction, amount) shouldBe carry
      }
    }
  }

  describe("match methods") {
    def b(s: String): Int = Integer.parseInt(s, 2)

    describe("matchValue") {
      val tests = List(
        //  NZP
        (b("000"), 0xFFFF, false),
        (b("111"), 0xFFFF, true),
        (b("011"), 0xFFFF, false),
        (b("100"), 0xFFFF, true),
        (b("100"), 0x8000, true),
        (b("110"), 0x0000, true),
        (b("101"), 0x0000, false),
        (b("010"), 0x0000, true),
        (b("001"), 0x7FFF, true),
        (b("110"), 0x7FFF, false),
        (b("101"), 0x7FFF, true)
      )

      for ((cond, value, result) <- tests) {
        it(s"(cond:${cond}, value:${value}) => ${result}") {
          BitUtils.matchValue(value.toChar, cond) shouldBe result
        }
      }
    }

    describe("matchFlags") {
      val tests = List(
        //  VC
        (b("00"), 0, 0, true),
        (b("00"), 1, 0, false),
        (b("00"), 0, 1, false),
        (b("11"), 0, 1, true),
        (b("11"), 1, 0, true),
        (b("11"), 1, 1, true),
        (b("11"), 0, 0, false),
        (b("10"), 0, 0, false),
        (b("10"), 0, 1, false),
        (b("10"), 1, 0, true),
        (b("10"), 1, 1, true),
        (b("01"), 0, 0, false),
        (b("01"), 0, 1, true),
        (b("01"), 1, 0, false),
        (b("01"), 1, 1, true)
      )

      for ((cond, int_overflow, int_carry, result) <- tests) {
        val (overflow, carry) = (int2bool(int_overflow), int2bool(int_carry))
        it(s"(cond:${cond}, overflow:${int_overflow}, carry:${int_carry}) => ${result}") {
          BitUtils.matchFlags(overflow, carry, cond) shouldBe result
        }
      }
    }
  }
}

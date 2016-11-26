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
}

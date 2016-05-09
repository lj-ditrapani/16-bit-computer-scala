package info.ditrapani.ljdcomputer

import org.scalatest.{FunSpec, Matchers}

class ComputerSpec extends FunSpec with Matchers {
  describe("Computer object") {
    val rom_size = 64 * 1024
    val ram_size = rom_size
    val video_ram_size = 4096

    describe("load") {
      it("fills rom, ram & video_ram with zeros for small program") {
        val binary = Vector(0xFFF0.toChar, 0xFFF1.toChar)
        val computer: Computer = Computer.load(binary)
        val rom = computer.cpu.rom
        rom.size should === (64 * 1024)
        rom(0) should === (0xFFF0.toChar)
        rom(1) should === (0xFFF1.toChar)
        rom(64 * 1024 - 1) should === (0.toChar)
        val ram = computer.ram
        ram.size should === (64 * 1024)
        ram(0) should === (0)
        ram(64 * 1024 - 1) should === (0)
        val video_ram = computer.video.video_ram
        video_ram.size should === (4096)
        video_ram(0) should === (0)
        video_ram(4096 - 1) should === (0)
      }

      it("sets rom, ram & video_ram for large program") {
        val binary =
          Vector.fill(rom_size)(2.toChar) ++
          Vector.fill(0xF000)(3.toChar) ++
          Vector.fill(video_ram_size)(4.toChar)
        val computer: Computer = Computer.load(binary)
        val rom = computer.cpu.rom
        rom.size should === (rom_size)
        rom(0) should === (2)
        rom(rom_size - 1) should === (2)
        val ram = computer.ram
        ram.size should === (ram_size)
        ram(0) should === (3)
        ram(ram_size - video_ram_size - 1) should === (3)
        ram(ram_size - video_ram_size) should === (4)
        ram(ram_size - 1) should === (4)
        val video_ram = computer.video.video_ram
        video_ram.size should === (video_ram_size)
        video_ram(0) should === (4)
        video_ram(video_ram_size - 1) should === (4)
      }
    }
  }
}

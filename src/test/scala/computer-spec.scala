package info.ditrapani.ljdcomputer

import info.ditrapani.ljdcomputer.video.Color8

class ComputerSpec extends Spec {
  describe("whole program tests") {
    it("runs an adding program where 27 + 73 = 100 and IC = 8") {
      // RA (register 10) is used for all addresses
      // A is stored in ram[0100]
      // B is stored in ram[0101]
      // Add A and B and store in ram[0102]
      // Put A in R1
      // Put B in R2
      // Add A + B and put in R3
      // Store R3 into ram[0102]
      val program = Vector(
        0x101A,    // HBY 0x01 RA
        0x200A,    // LBY 0x00 RA
        0x3A01,    // LOD RA R1
        0x201A,    // LBY 0x01 RA
        0x3A02,    // LOD RA R2
        0x5123,    // ADD R1 R2 R3
        0x202A,    // LBY 0x02 RA
        0x4A30,    // STR RA R3
        0x0000     // END
      ).map(_.toChar)

      val binary = {
        val rom_filler = Vector.fill(64 * 1024 - program.size + 1552)(0.toChar)
        val ram = Vector.fill(0x0100)(0.toChar) ++ Vector(27, 73).map(_.toChar)
        program ++ rom_filler ++ ram
      }

      val computer = Computer.load(binary)
      val computer2 = computer.runFrame()
      computer2.ram(0x0102).toInt shouldBe 100
      computer2.cpu.instruction_counter.toInt shouldBe 8
    }

    it("runs a branching program where 101 - 99 < 3 => 255 and PC = 20") {
      // RA (register 10) is used for all value addresses
      // RB has address of 2nd branch
      // RC has address of final, common, end of program
      // A is stored in ram[0100]
      // B is stored in ram[0101]
      // If A - B < 3, store 255 in ram[0102], else store 1 in ram[0102]
      // Put A in R1
      // Put B in R2
      // Sub A - B and put in R3
      // Load const 3 into R4
      // Sub R3 - R4 => R5
      // If R5 is negative, 255 => R6, else 1 => R6
      // Store R6 into ram[0102]
      val program = Vector(
        // Load 2nd branch address into RB
        0x100B,    // 00 HBY 0x00 RB
        0x210B,    // 01 LBY 0x10 RB

        // Load end of program address int RC
        0x7B2C,    // 02 ADI RB 2 RC

        // Load A value into R1
        0x101A,    // 03 HBY 0x01 RA
        0x200A,    // 04 LBY 0x00 RA
        0x3A01,    // 05 LOD RA R1

        // Load B value into R2
        0x201A,    // 06 LBY 0x01 RA
        0x3A02,    // 07 LOD RA R2

        0x6123,    // 08 SUB R1 R2 R3

        // Load constant 3 to R4
        0x1004,    // 09 HBY 0x00 R4
        0x2034,    // 0A LBY 0x03 R4

        0x6345,    // 0B SUB R3 R4 R5

        // Branch to ? if A - B >= 3
        0xE5B3,    // 0C BRV R5 RB ZP

        // Load constant 255 into R6
        0x1006,    // 0D HBY 0x00 R6
        0x2FF6,    // 0E LBY 0xFF R6
        0xE0C7,    // 0F BRV R0 RC NZP (Jump to end)

        // Load constant 0x01 into R6
        0x1006,    // 10 HBY 0x00 R6
        0x2016,    // 11 LBY 0x01 R6

        // Store final value into ram[0102]
        0x202A,    // 12 LBY 0x02 RA
        0x4A60,    // 13 STR RA R6
        0x0000     // 14 END
      ).map(_.toChar)

      val binary = {
        val rom_filler = Vector.fill(64 * 1024 - program.size + 1552)(0.toChar)
        val ram = Vector.fill(0x0100)(0.toChar) ++ Vector(101, 99).map(_.toChar)
        program ++ rom_filler ++ ram
      }

      val computer = Computer.load(binary)
      val computer2 = computer.runFrame()
      computer2.ram(0x0100).toInt shouldBe 101
      computer2.ram(0x0101).toInt shouldBe 99
      computer2.cpu.registers.vector.map(_.toInt) shouldBe Vector(
        0, 101, 99, 2, 3, -1.toChar.toInt, 255, 0, 0, 0, 0x102, 0x10, 0x12, 0, 0, 0
      )
      computer2.ram(0x0102).toInt shouldBe 255
      computer2.cpu.instruction_counter.toInt shouldBe 20
    }
  }

  /*
  describe 'Program with while loop', ->
    # Run a complete program
    # Uses storage I/O
    #   - input/read $E800
    #   - output/write $EC00
    # Input: n followed by a list of n integers
    # Output: -2 * sum(list of n integers)
    program = [
      # R0 gets address of beginning of input from storage space
      0x1E80      # 0 HBY 0xE8 R0       0xE8 -> Upper(R0)
      0x2000      # 1 LBY 0x00 R0       0x00 -> Lower(R0)

      # R1 gets address of begining of output to storage space
      0x1EC1      # 2 HBY 0xEC R1       0xEC -> Upper(R1)
      0x2001      # 3 LBY 0x00 R1       0x00 -> Lower(R1)

      # R2 gets n, the count of how many input values to sum
      0x3002      # 4 LOD R0 R2         First Input (count n) -> R2

      # R3 and R4 have start and end of while loop respectively
      0x2073      # 5 LBY 0x07 R3       addr start of while loop -> R3
      0x20D4      # 6 LBY 0x0D R4       addr to end while loop -> R4

      # Start of while loop
      0xE242      # 7 BRV R2 R4 Z       if R2 is zero (0x.... -> PC)
      0x7010      # 8 ADI R0 1 R0       increment input address
      0x3006      # 9 LOD R0 R6         Next Input -> R6
      0x5565      # A ADD R5 R6 R5      R5 + R6 (running sum) -> R5
      0x8212      # B SBI R2 1 R2       R2 - 1 -> R2
      0xE037      # C BRV R0 R3 NZP     0x.... -> PC (unconditional)

      # End of while loop
      0xD506      # D SHF R5 left 1 R6  Double sum

      # Negate double of sum
      0x6767      # E SUB R7 R6 R7      0 - R6 -> R7

      # Output result
      0x4170      # F STR R1 R7         Output value of R7
      0x0000      #   END
    ]
    it "Outputs 0xDOA8 (#{0xD0A8}) and PC is 16", ->
      length = 101
      ram[0xE800..(0xE800 + length)] = [length].concat [10..110]
      cpu.loadProgram program
      cpu.run()
      # n = length(10..110) = 101
      # sum(10..110) = 6060
      # -2 * 6060 = -12120
      # 16-bit hex(+12120) = 0x2F58
      # 16-bit hex(-12120) = 0xD0A8
      expect(rom.length).to.equal Math.pow(2, 16)
      expect(ram.length).to.equal Math.pow(2, 16)
      expect(ram[0xE800]).to.equal 101
      expect(ram[0xE801]).to.equal 10
      expect(ram[0xE800 + 101]).to.equal 110
      expect(ram[0xEC00]).to.equal 0xD0A8
      expect(cpu.pc).to.equal 16
      */

  describe("Computer object") {
    type LoadTest = (Vector[Char], Int, Int, Int, Int, Int, Int, Int, Int, String)
    val rom_size = 64 * 1024
    val video_colors_size = 16
    val video_tiles_size = 256
    val video_rom_size = video_colors_size + 1024 + 512
    val ram_size = rom_size
    val video_ram_size = 1021
    val game_pad_and_interrupt_ram_size = 3
    val ram_size_up_to_video_ram = ram_size -
      video_ram_size -
      game_pad_and_interrupt_ram_size

    describe("load") {
      describe("loads memories") {
        def runTest(test: LoadTest): Unit = {
          val (
            binary,
            rom0, rom1, end_rom,
            video_rom_value,
            start_ram, mid_ram,
            video_ram_value,
            last_3_ram_value,
            _
          ) = test
          val computer: Computer = Computer.load(binary)
          val video = computer.video_obj
          val rom = computer.cpu.rom
          rom.size shouldBe rom_size
          rom(0) shouldBe rom0
          rom(1) shouldBe rom1
          rom(rom_size - 1) shouldBe end_rom
          val video_rom = computer.video_roms
          val video_colors = video_rom.custom_colors.vector
          video_colors.size shouldBe video_colors_size
          video_colors(0) shouldBe Color8(
            0, 0, video_rom_value.toByte
          )
          video_colors(video_colors_size - 1) shouldBe Color8(
            0, 0, video_rom_value.toByte
          )
          val video_tiles = video_rom.custom_tiles.vector
          video_tiles.size shouldBe video_tiles_size
          video_tiles(0).rows(1)(7) shouldBe (video_rom_value > 0)
          video_tiles(video_tiles_size - 1).rows(1)(7) shouldBe (video_rom_value > 0)
          val ram = computer.ram
          ram.size shouldBe ram_size
          ram(0) shouldBe start_ram
          ram(ram_size_up_to_video_ram - 1) shouldBe mid_ram
          ram(ram_size_up_to_video_ram) shouldBe video_ram_value
          ram(ram_size_up_to_video_ram + video_ram_size - 1) shouldBe video_ram_value
          ram(ram_size_up_to_video_ram + video_ram_size) shouldBe last_3_ram_value
          ram(
            ram_size_up_to_video_ram + video_ram_size +
            game_pad_and_interrupt_ram_size - 1
          ) shouldBe last_3_ram_value
          val cells = video.cells
          cells.size shouldBe 640
          cells(0) shouldBe 0
          cells(640 - 1) shouldBe 0
          video.enable_custom_video_rom shouldBe false
        }

        val small_program =
          Vector(9.toChar, 8.toChar)
        val rom_program =
          Vector.fill(rom_size)(2.toChar)
        val temp_program =
          Vector.fill(rom_size)(2.toChar) ++
          Vector.fill(video_rom_size)(3.toChar)
        val mid_program_partial_ram =
          temp_program ++
          Vector.fill(1)(4.toChar)
        val mid_program_full_ram =
          temp_program ++
          Vector.fill(ram_size_up_to_video_ram)(4.toChar)
        val large_program =
          mid_program_full_ram ++
          Vector.fill(video_ram_size)(5.toChar) ++
          Vector.fill(game_pad_and_interrupt_ram_size)(6.toChar)

        val tests: List[LoadTest] = List(
          (small_program, 9, 8, 0, 0, 0, 0, 0, 0, "small"),
          (rom_program, 2, 2, 2, 0, 0, 0, 0, 0, "rom"),
          (mid_program_partial_ram, 2, 2, 2, 3, 4, 0, 0, 0, "medium partial ram"),
          (mid_program_full_ram, 2, 2, 2, 3, 4, 4, 0, 0, "medium full ram"),
          (large_program, 2, 2, 2, 3, 4, 4, 5, 6, "large")
        )

        for (test <- tests) {
          it(s"sets rom, ram & video_ram for ${test._10} program") {
            runTest(test)
          }
        }
      }
    }
  }
}

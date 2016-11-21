package info.ditrapani.ljdcomputer

import info.ditrapani.ljdcomputer.video.Color8

class ComputerSpec extends Spec {
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
          val video_colors = video_rom.custom_colors
          video_colors.size shouldBe video_colors_size
          video_colors(0) shouldBe Color8(
            0, 0, video_rom_value.toByte
          )
          video_colors(video_colors_size - 1) shouldBe Color8(
            0, 0, video_rom_value.toByte
          )
          val video_tiles = video_rom.custom_tiles
          video_tiles.size shouldBe video_tiles_size
          video_tiles(0)(1)(7) shouldBe (video_rom_value > 0)
          video_tiles(video_tiles_size - 1)(1)(7) shouldBe (video_rom_value > 0)
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

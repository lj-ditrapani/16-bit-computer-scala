package info.ditrapani.ljdcomputer

class ComputerSpec extends Spec {
  describe("Computer object") {
    type LoadTest = (Vector[Char], Int, Int, Int, Int, Int, Int, Int, Int, String)
    val rom_size = 64 * 1024
    val video_rom_size = 512 * 3 + 16
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
          val video_rom = video.custom_video_rom
          video_rom.size shouldBe video_rom_size
          video_rom(0) shouldBe video_rom_value
          video_rom(video_rom_size - 1) shouldBe video_rom_value
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
          cells(0) shouldBe video_ram_value
          cells(640 - 1) shouldBe video_ram_value
          video.enable_custom_video_rom shouldBe false
        }

        val small_program =
          Vector(0xFFF0.toChar, 0xFFF1.toChar)
        val rom_program =
          Vector.fill(rom_size)(2.toChar)
        val temp_program =
          Vector.fill(rom_size)(2.toChar) ++
          Vector.fill(video_rom_size)(3.toChar)
        val mid_program_partial_video_ram =
          temp_program ++
          Vector.fill(1)(4.toChar)
        val mid_program_full_video_ram =
          temp_program ++
          Vector.fill(ram_size_up_to_video_ram)(4.toChar)
        val large_program =
          mid_program_full_video_ram ++
          Vector.fill(video_ram_size)(5.toChar) ++
          Vector.fill(game_pad_and_interrupt_ram_size)(6.toChar)

        val tests: List[LoadTest] = List(
          (small_program, 0xFFF0, 0xFFF1, 0, 0, 0, 0, 0, 0, "small"),
          (rom_program, 2, 2, 2, 0, 0, 0, 0, 0, "rom"),
          (mid_program_partial_video_ram, 2, 2, 2, 3, 4, 0, 0, 0, "medium partial video"),
          (mid_program_full_video_ram, 2, 2, 2, 3, 4, 4, 0, 0, "medium full video"),
          (large_program, 2, 2, 2, 3, 4, 4, 5, 6, "large")
        )

        for (test <- tests) {
          it(s"sets rom, ram & video_ram for ${test._10} program") {
            runTest(test)
          }
        }
      }

      describe("loads video flags") {
        //               is_set   enable   custom_video_rom
        type FlagTest = (Boolean, Boolean, Boolean)

        val tests: List[FlagTest] = List(
          (false, false, false),
          (true, false, false),
          (true, true, false),
          (true, false, true),
          (true, true, true)
        )

        def bool2int(b: Boolean): Int = b match {
          case true => 1
          case false => 0
        }

        def runTest(test: FlagTest): Unit = {
          val (is_set, enable, custom_video_rom) = test
          val enable_bits: Char =
            ((bool2int(custom_video_rom) << 1) +  bool2int(enable)).toChar
          val binary =
            is_set match {
              case false => Vector(0.toChar, 0.toChar)
              case true =>
                Vector.fill(rom_size)(9.toChar) ++
                Vector.fill(video_rom_size)(8.toChar) ++
                Vector.fill(0xFE80)(7.toChar) ++
                Vector(enable_bits)
            }
          val computer: Computer = Computer.load(binary)
          val video = computer.video_obj
          it(s"is_set $is_set enable $enable custom_video_rom $custom_video_rom") {
            video.enable should === (enable)
            video.enable_custom_video_rom should === (custom_video_rom)
          }
        }

        tests.map { runTest(_) }
      }
    }
  }
}

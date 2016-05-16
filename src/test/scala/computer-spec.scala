package info.ditrapani.ljdcomputer

class ComputerSpec extends Spec {
  describe("Computer object") {
    type LoadTest = (Vector[Char], Int, Int, Int, Int, Int, Int, Int, String)
    val rom_size = 64 * 1024
    val ram_size = rom_size
    val video_ram_size = 4096

    describe("load") {
      describe("loads memories") {
        def runTest(test: LoadTest): Unit = {
          val (
            binary,
            rom0, rom1, end_rom,
            start_ram, mid_ram,
            start_video_ram, end_video_ram,
            _
          ) = test
          val computer: Computer = Computer.load(binary)
          val rom = computer.cpu.rom
          rom.size should === (rom_size)
          rom(0) should === (rom0)
          rom(1) should === (rom1)
          rom(rom_size - 1) should === (end_rom)
          val ram = computer.ram
          ram.size should === (ram_size)
          ram(0) should === (start_ram)
          ram(ram_size - video_ram_size - 1) should === (mid_ram)
          ram(ram_size - video_ram_size) should === (start_video_ram)
          ram(ram_size - 1) should === (end_video_ram)
          val video = computer.video
          val video_ram = video.video_ram
          video_ram.size should === (video_ram_size)
          video_ram(0) should === (start_video_ram)
          video_ram(video_ram_size - 1) should === (end_video_ram)
          video.custom_tiles should === (false)
        }

        val small_program =
          Vector(0xFFF0.toChar, 0xFFF1.toChar)
        val rom_program =
          Vector.fill(rom_size)(2.toChar)
        val mid_program = 
          Vector.fill(rom_size)(2.toChar) ++
          Vector.fill(0xF000)(3.toChar) ++
          Vector.fill(1024)(4.toChar)
        val large_program = 
          Vector.fill(rom_size)(2.toChar) ++
          Vector.fill(0xF000)(3.toChar) ++
          Vector.fill(video_ram_size)(4.toChar)

        val tests: List[LoadTest] = List(
          (small_program, 0xFFF0, 0xFFF1, 0, 0, 0, 0, 0, "small"),
          (rom_program, 2, 2, 2, 0, 0, 0, 0, "rom"),
          (mid_program, 2, 2, 2, 3, 3, 4, 0, "medium"),
          (large_program, 2, 2, 2, 3, 3, 4, 4, "large")
        )

        for (test <- tests) {
          it(s"sets rom, ram & video_ram for ${test._9} program") {
            runTest(test)
          }
        }
      }

      describe("loads video flags") {
        //               is_set   enable   custom_tiles
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
          val (is_set, enable, custom_tiles) = test
          val enable_bits: Char =
            ((bool2int(custom_tiles) << 2) +  (bool2int(enable) << 1)).toChar
          val binary =
            is_set match {
              case false => Vector(0.toChar, 0.toChar)
              case true =>
                Vector.fill(rom_size)(8.toChar) ++
                Vector.fill(0xDDF3)(7.toChar) ++
                Vector(enable_bits)
            }
          val computer: Computer = Computer.load(binary)
          val video = computer.video
          it(s"is_set $is_set enable $enable custom_tiles $custom_tiles") {
            video.enable should === (enable)
            video.custom_tiles should === (custom_tiles)
          }
        }

        tests.map { runTest(_) }
      }
    }
  }
}

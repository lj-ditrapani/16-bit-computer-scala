package info.ditrapani.ljdcomputer.video

import scalafx.scene.paint.Color

case class Video(
  cells: Ram,
  custom_video_rom: Ram,
  enable: Boolean,
  enable_custom_video_rom: Boolean) {
  assert(custom_video_rom.length == 1552)
  assert(cells.length == 640)

  def buffer: VideoBuffer =
    if (enable) {
      val (colors, tiles) =
        if (enable_custom_video_rom) {
          split_rom
        } else {
          (Video.built_in_colors, Video.built_in_tiles)
        }
      val video_state = VideoState.make(cells, colors, tiles)
      video_state.buffer
    } else {
      Video.disabledBuffer
    }

  def split_rom: (Ram, Ram) = {
    val (colors, tiles) = custom_video_rom.splitAt(16)
    assert(colors.length == 16)
    assert(tiles.length == 1024 + 512)
    (cells, colors)
  }
}

object Video {
  def make(ram: Ram, custom_video_rom: Ram): Video = {
    assert(custom_video_rom.size == 1552)
    assert(ram.size == 64 * 1024)
    val enable_bits = ram(0xFE80)
    val enable: Boolean = (enable_bits & 1) > 0
    val enable_custom_video_rom: Boolean = (enable_bits & 2) > 0
    val cells = ram.slice(0xFC00, 0xFE80)
    Video(cells, custom_video_rom, enable, enable_custom_video_rom)
  }

  val disabledBuffer: VideoBuffer = Seq.fill(240, 256)(Color.rgb(0, 0, 0))

  private def b(s: String): Char = Integer.parseInt(s, 2).toChar

  val built_in_colors = Vector(
    b("00000000"),  // 0    white
    b("11111111"),  // 1    black
    b("11100000"),  // 2    red
    b("00011100"),  // 3    green
    b("00000011"),  // 4    blue
    b("11111100"),  // 5    yellow
    b("11100011"),  // 6    magenta
    b("00011111"),  // 7    cyan
    b("11110010"),  // 8    light red
    b("10011110"),  // 9    light green
    b("10010011"),  // A    light blue
    b("01000000"),  // B    dark red
    b("00001000"),  // C    dark green
    b("00000010"),  // D    dark blue
    b("10010010"),  // E    light grey
    b("01001001")   // F    dark grey
  )

  val built_in_tiles: Ram = {
    import scala.util.{Try, Success, Failure}
    import java.nio.file.{Files, Paths}

    def bytePair2Char(pair: Array[Byte]): Char = {
      val int_pair = pair.map(_ & 0xFF)
      ((int_pair(0) << 8) + int_pair(1)).toChar
    }

    def toRam(byte_array: Array[Byte]): Ram = {
      byte_array.grouped(2).map(bytePair2Char).toVector
    }

    // actually should be loaded from resources like in game-of-life
    val path = "src/main/resources/tiles.bin"
    Try(Files.readAllBytes(Paths.get(path))) match {
      // case Failure(exception) => printaln(exception) // scalastyle:ignore regex
      case Success(byte_array) => toRam(byte_array)
    }
  }
}

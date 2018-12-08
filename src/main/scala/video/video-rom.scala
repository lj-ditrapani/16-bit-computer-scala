package info.ditrapani.ljdcomputer.video

import info.ditrapani.ljdcomputer.{BitHelper, BinFileReader}

final case class VideoRoms(
  built_in_colors: Colors,
  built_in_tiles: TileSet,
  custom_colors: Colors,
  custom_tiles: TileSet
)

object VideoRoms {
  val built_in_colors: Rom = Vector(
    b("00000000"),  // 0    black
    b("11111111"),  // 1    white
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

  val built_in_tiles: Rom = {
    val input_stream = getClass.getResourceAsStream("/tiles.bin")
    val stream: Stream[Int] = Stream.continually(input_stream.read)
    BinFileReader.bytes2Chars(stream.takeWhile(_ != -1).map(_.toByte).toArray)
  }

  private def b(s: String): Char = BitHelper.b(s).toChar

  def make(custom_rom: Rom): VideoRoms = {
    assert(custom_rom.size == 1552)
    val (custom_colors, custom_tiles) = splitCustomRom(custom_rom)
    VideoRoms(
      new Colors(built_in_colors),
      new TileSet(built_in_tiles),
      new Colors(custom_colors),
      new TileSet(custom_tiles)
    )
  }

  private def splitCustomRom(custom_rom: Rom): (Rom, Rom) = {
    val (colors, tiles) = custom_rom.splitAt(16)
    assert(colors.length == 16)
    assert(tiles.length == 1024 + 512)
    (colors, tiles)
  }
}

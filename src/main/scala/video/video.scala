package info.ditrapani.ljdcomputer.video

import scalafx.scene.paint.Color

case class Video(video_ram: Ram, enable: Boolean, custom_tiles: Boolean) {
  assert(video_ram.length == 2816)

  def buffer: VideoBuffer =
    if (enable) {
      val (cells, colors, ram_tiles) = split_ram
      val tiles =
        if (custom_tiles) {
          ram_tiles
        } else {
          // ram_tiles
          Video.built_in_tiles
        }
      val video_state = VideoState.make(cells, colors, tiles)
      video_state.buffer
    } else {
      Video.disabledBuffer
    }

  def split_ram: (Ram, Ram, Ram) = {
    val (cells, rest1) = video_ram.splitAt(640)
    val (_, rest2) = rest1.splitAt(128)
    val (colors, rest3) = rest2.splitAt(16)
    val (_, tiles) = rest3.splitAt(496)
    assert(cells.length == 640)
    assert(colors.length == 16)
    assert(tiles.length == 1536)
    (cells, colors, tiles)
  }
}

object Video {
  def make(ram: Ram): Video = {
    val enable_bits = ram(0xF403)
    val enable: Boolean = (enable_bits & 2) > 0
    val custom_tiles: Boolean = (enable_bits & 4) > 0
    val video_ram = ram.slice(0xF500, 0x10000)
    Video(video_ram, enable, custom_tiles)
  }

  val disabledBuffer: VideoBuffer = Seq.fill(240, 256)(Color.rgb(0, 0, 0))

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
      // case Failure(exception) => println(exception)
      case Success(byte_array) => toRam(byte_array)
    }
  }
}

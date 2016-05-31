package info.ditrapani.ljdcomputer.video

import scalafx.scene.paint.Color

case class Video(video_ram: Ram, enable: Boolean, custom_tiles: Boolean) {
  def buffer: VideoBuffer =
    if (enable) {
      val (ram_tiles, cells, colors, sprites) = split_ram
      val tiles =
        if (custom_tiles) {
          ram_tiles
        } else {
          // ram_tiles
          Video.built_in_tiles
        }
      val video_state = VideoState(tiles, cells, colors, sprites)
      video_state.buffer
    } else {
      Video.disabledBuffer
    }

  def split_ram: (Ram, Ram, Ram, Ram) = {
    val (tiles, rest1) = video_ram.splitAt(3072)
    val (cells, rest2) = rest1.splitAt(736)
    val (colors, sprites) = rest2.splitAt(32)
    (tiles, cells, colors, sprites)
  }
}

object Video {
  def make(ram: Ram): Video = {
    val enable_bits = ram(0xDDF3)
    val enable: Boolean = (enable_bits & 2) > 0
    val custom_tiles: Boolean = (enable_bits & 4) > 0
    val video_ram = ram.slice(0xF000, 0x10000)
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

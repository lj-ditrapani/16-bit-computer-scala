package info.ditrapani.ljdcomputer.video

import scalafx.scene.paint.Color

case class Video(video_ram: Ram, enable: Boolean, custom_tiles: Boolean) {
  def buffer: VideoBuffer =
    if (enable) {
      // val (ram_tiles, cells, colors, sprites) = expload(video_ram)
      // tiles =
      //   if (custom_tiles)
      //     ram_tiles
      //   else
      //     built_in_tiles
      // video_state = VideoState.make(tiles, cells, colors, sprites)
      // video_state.buffer
      (for (i <- 0 until 240) yield {
        (for (j <- 0 until 256) yield Color.rgb(200, 200, 255)).to[Vector]
      }).to[Vector]
    } else {
      Video.disabledBuffer
    }
}

object Video {
  type VideoBuffer = Vector[Vector[Color]]
  type LargeTile = Vector[Vector[(Boolean, Boolean)]]
  type SmallTile = Vector[Vector[(Boolean, Boolean)]]
  type TextCharTile = Vector[Vector[Boolean]]

  def make(ram: Ram): Video = {
    val enable_bits = ram(0xDDF3)
    val enable: Boolean = (enable_bits & 2) > 0
    val custom_tiles: Boolean = (enable_bits & 4) > 0
    val video_ram = ram.slice(0xF000, 0x10000)
    Video(video_ram, enable, custom_tiles)
  }

  val disabledBuffer: VideoBuffer =
    (for (i <- 0 until 240) yield {
      (for (j <- 0 until 256) yield Color.rgb(0, 0, 0)).to[Vector]
    }).to[Vector]
}

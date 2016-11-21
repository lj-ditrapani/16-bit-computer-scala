package info.ditrapani.ljdcomputer.video

import scalafx.scene.paint.Color

final case class Video(
  cells: Ram,
  video_roms: VideoRoms,
  enable: Boolean,
  enable_custom_video_rom: Boolean
) {
  assert(cells.length == 640)

  def buffer: VideoBuffer =
    if (enable) {
      val (colors, tiles) =
        if (enable_custom_video_rom) {
          (video_roms.custom_colors, video_roms.custom_tiles)
        } else {
          (video_roms.built_in_colors, video_roms.built_in_tiles)
        }
      val video_state = VideoState.make(cells, colors, tiles)
      video_state.buffer
    } else {
      Video.disabledBuffer
    }
}

object Video {
  def init(video_roms: VideoRoms): Video =
    Video(Vector.fill(640)(0.toChar), video_roms, false, false)

  def make(ram: Ram, video_roms: VideoRoms): Video = {
    assert(ram.size == 64 * 1024)
    val enable_bits = ram(0xFE80)
    val enable: Boolean = (enable_bits & 1) > 0
    val enable_custom_video_rom: Boolean = (enable_bits & 2) > 0
    val cells = ram.slice(0xFC00, 0xFE80)
    Video(cells, video_roms, enable, enable_custom_video_rom)
  }

  val disabledBuffer: VideoBuffer = Seq.fill(240, 256)(Color.rgb(0, 0, 0))
}

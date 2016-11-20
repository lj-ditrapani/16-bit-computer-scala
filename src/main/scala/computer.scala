package info.ditrapani.ljdcomputer

import info.ditrapani.ljdcomputer.video.{Video, VideoRoms, Ram}
import scalafx.scene.paint.Color

case class Computer(cpu: Cpu, video_roms: VideoRoms, ram: Ram, video_obj: Video) {
  type VideoBuffer = video.VideoBuffer

  def runFrame(key_presses: Byte): (VideoBuffer, Computer) = {
    val ram2 = addKeyPresses(key_presses)
    val (ram3, new_cpu) = cpu.step(400000, ram2)
    val (new_video, ram4) = swapVideoRam(ram3)
    val new_computer = Computer(new_cpu, video_roms, ram4, new_video)
    (new_video.buffer, new_computer)
  }

  def addKeyPresses(key_presses: Byte): Vector[Char] = ram

  def swapVideoRam(new_ram: Vector[Char]): (Video, Vector[Char]) =
    (video_obj, new_ram)
}

object Computer {
  def load(binary: Vector[Char]): Computer = {
    val end = 132624
    assert(binary.size <= end)
    val size64k = 64 * 1024
    val video_rom_size = 1024 + 512 + 16
    val rom_start = 0
    val video_rom_start = size64k
    val ram_start = video_rom_start + video_rom_size
    val rom = binary
      .slice(0, video_rom_start)
      .padTo(size64k, 0.toChar)
    val custom_video_rom = binary
      .slice(video_rom_start, ram_start)
      .padTo(video_rom_size, 0.toChar)
    val ram = binary.slice(ram_start, end).padTo(size64k, 0.toChar)
    val video_roms = VideoRoms.make(custom_video_rom)
    Computer(Cpu(rom), video_roms, ram, Video.make(ram, video_roms))
  }
}

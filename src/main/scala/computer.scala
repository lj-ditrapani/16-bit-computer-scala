package info.ditrapani.ljdcomputer

import info.ditrapani.ljdcomputer.video.{Video, VideoRoms, Ram}

final case class Computer(cpu: Cpu, video_roms: VideoRoms, ram: Ram, video_obj: Video) {
  type VideoBuffer = video.VideoBuffer

  def runFrame(): (Computer) = {
    val (ram2, new_cpu) = cpu.run(400000, ram)
    Computer(new_cpu, video_roms, ram2, video_obj)
  }

  def swapRam(key_presses: Byte): Computer = {
    val ram2 = addKeyPresses(key_presses)
    val (new_video, ram3) = swapVideoRam(ram2)
    Computer(cpu, video_roms, ram3, new_video)
  }

  def renderVideoBuffer(): VideoBuffer = {
    video_obj.buffer
  }

  private def addKeyPresses(key_presses: Byte): Vector[Char] = ram

  private def swapVideoRam(old_ram: Vector[Char]): (Video, Vector[Char]) = {
    val new_ram =
      old_ram.slice(0, 0xFC00) ++
      video_obj.getVideoRam ++
      old_ram.slice(0xFFFD, old_ram.size)
    (Video.make(old_ram, video_roms), new_ram)
  }
}

object Computer {
  def load(binary: Vector[Char]): Computer = {
    val size64k = 64 * 1024
    val video_rom_size = 1024 + 512 + 16
    val video_rom_start = size64k
    val ram_start = video_rom_start + video_rom_size
    val end = 132624
    assert(binary.size <= end)
    val rom = binary
      .slice(0, video_rom_start)
      .padTo(size64k, 0.toChar)
    val custom_video_rom = binary
      .slice(video_rom_start, ram_start)
      .padTo(video_rom_size, 0.toChar)
    val ram = binary
      .slice(ram_start, end)
      .padTo(size64k, 0.toChar)
    val video_roms = VideoRoms.make(custom_video_rom)
    Computer(Cpu.initialize(rom), video_roms, ram, Video.init(video_roms))
  }
}

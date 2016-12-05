package info.ditrapani.ljdcomputer

import info.ditrapani.ljdcomputer.BitHelper.bool2int
import javafx.event.EventHandler;
import javafx.scene.input.{KeyEvent => JavaKeyEvent};
import scalafx.scene.input.KeyCode
import scalafx.scene.input.KeyEvent

/* The gamepad register layout
 *
 *  F E D C B A 9 8 7 6 5 4 3 2 1 0
 * ---------------------------------
 * | 8 Unused bits |U|D|L|R|A|S|D|F|
 * ---------------------------------
 */

object Gamepad extends EventHandler[JavaKeyEvent] {
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private var up: Boolean = false
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private var down: Boolean = false
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private var left: Boolean = false
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private var right: Boolean = false
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private var a: Boolean = false
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private var s: Boolean = false
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private var d: Boolean = false
  @SuppressWarnings(Array("org.wartremover.warts.Var"))
  private var f: Boolean = false

  def clear(): Unit = {
    up = false
    down = false
    left = false
    right = false
    a = false
    s = false
    d = false
    f = false
  }

  def getKeyPresses(): Char = (
    bool2int(up) << 7 |
    bool2int(down) << 6 |
    bool2int(left) << 5 |
    bool2int(right) << 4 |
    bool2int(a) << 3 |
    bool2int(s) << 2 |
    bool2int(d) << 1 |
    bool2int(f)
  ).toChar

  override def handle(event: JavaKeyEvent): Unit = {
    new KeyEvent(event).code match {
      case KeyCode.Up | KeyCode.K => up = true
      case KeyCode.Down | KeyCode.J => down = true
      case KeyCode.Left | KeyCode.H => left = true
      case KeyCode.Right | KeyCode.L => right = true
      case KeyCode.A => a = true
      case KeyCode.S | KeyCode.O => s = true
      case KeyCode.D | KeyCode.E => d = true
      case KeyCode.F | KeyCode.U => f = true
      case _ => (): Unit
    }
  }
}

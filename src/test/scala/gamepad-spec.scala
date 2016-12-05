package info.ditrapani.ljdcomputer

import javafx.scene.input.{KeyEvent => JavaKeyEvent};
import javafx.scene.input.KeyCode

class GamepadSpec extends Spec {
  def makeEvent(code: KeyCode): JavaKeyEvent = new JavaKeyEvent(
    JavaKeyEvent.KEY_RELEASED, "", "", code, false, false, false, false
  )

  describe("clear") {
    it("sets the key presses to all 0 (not pressed)") {
      Gamepad.handle(makeEvent(KeyCode.A))
      Gamepad.handle(makeEvent(KeyCode.S))
      Gamepad.clear()
      Gamepad.getKeyPresses().toInt shouldBe 0
    }
  }

  describe("handle") {
    it("sets the appropriate bit in the key presses to 1") {
      Gamepad.handle(makeEvent(KeyCode.A))
      Gamepad.getKeyPresses().toInt shouldBe 8
      Gamepad.handle(makeEvent(KeyCode.S))
      Gamepad.handle(makeEvent(KeyCode.S))
      Gamepad.getKeyPresses().toInt shouldBe 12
      Gamepad.clear()
      Gamepad.handle(makeEvent(KeyCode.RIGHT))
      Gamepad.getKeyPresses().toInt shouldBe 16
      Gamepad.clear()
      Gamepad.handle(makeEvent(KeyCode.UP))
      Gamepad.getKeyPresses().toInt shouldBe 128
      Gamepad.handle(makeEvent(KeyCode.DOWN))
      Gamepad.handle(makeEvent(KeyCode.LEFT))
      Gamepad.handle(makeEvent(KeyCode.RIGHT))
      Gamepad.handle(makeEvent(KeyCode.A))
      Gamepad.handle(makeEvent(KeyCode.S))
      Gamepad.handle(makeEvent(KeyCode.D))
      Gamepad.handle(makeEvent(KeyCode.F))
      Gamepad.getKeyPresses().toInt shouldBe 255
    }
  }
}

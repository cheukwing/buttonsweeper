package ButtonSweeper.util;

import javax.swing.*;
import java.awt.event.MouseListener;

public class MouseButton extends JButton {

  public MouseButton() {
  }

  public MouseButton(Icon icon) {
    super(icon);
  }

  public MouseButton(String s) {
    super(s);
  }

  public MouseButton(Action action) {
    super(action);
  }

  public MouseButton(String s, Icon icon) {
    super(s, icon);
  }

  public MouseButton(String s, MouseListener mouseListener) {
    super(s);
    addMouseListener(mouseListener);
  }

  public void replaceMouseListener(MouseListener mouseListener) {
    removeAllMouseListeners();
    addMouseListener(mouseListener);
  }

  private void removeAllMouseListeners() {
    MouseListener[] mls = getMouseListeners();
    for (MouseListener ml : mls) {
      this.removeMouseListener(ml);
    }
  }
}

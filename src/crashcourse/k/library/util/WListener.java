package crashcourse.k.library.util;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

public class WListener extends WindowAdapter implements ActionListener {

	public volatile boolean closed;
	private JDialog frame = null;

	public WListener(JDialog jd) {
		frame = jd;
		frame.addWindowListener(this);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.err.println("Someone is closing me!");
		closed = true;
		WUtils.returnValue = WUtils.returnValue == JFileChooser.ERROR_OPTION ? JFileChooser.CANCEL_OPTION
				: WUtils.returnValue;
	}

	public void windowClosed(WindowEvent e) {
		System.err.println("Someone closed me!");
		closed = true;
		WUtils.returnValue = JFileChooser.CANCEL_OPTION;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.err.println(e.getActionCommand());
		if (e.getActionCommand().equals("CancelSelection")) {
			close(frame);
		} else if (e.getActionCommand().equals("ApproveSelection")) {
			WUtils.returnValue = JFileChooser.APPROVE_OPTION;
			close(frame);
		}
	}

	private static void close(JDialog d) {
		Toolkit.getDefaultToolkit().getSystemEventQueue()
				.postEvent(new WindowEvent(d, WindowEvent.WINDOW_CLOSING));
	}
}

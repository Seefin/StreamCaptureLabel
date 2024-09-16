package io.findlays.capturelabel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MainApplication extends JFrame implements ActionListener {
	private static final long serialVersionUID = -7838216541542080967L;

	public MainApplication(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(480,560);
		
		CaptureLabel<String> label = new CaptureLabel<>();
		label.isErr = true;
		System.setOut(new PrintStream(new CaptureStream("STDERR",label,System.out)));
		
		JTextArea doodleArea = new JTextArea(10, 50);
		JScrollPane view = new JScrollPane(doodleArea);
		view.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		view.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(view, BorderLayout.CENTER);
		getContentPane().add(label, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		MainApplication ma = new MainApplication("MessageLabel Test");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ma.setVisible(true);
			}
		});
		
		Timer t = new Timer(1000, ma);
		t.setRepeats(true);
		t.setActionCommand("Tick");
		t.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		switch(cmd) {
		case "Tick":
			System.out.println("Activity at \"</p>" + System.currentTimeMillis());
			break;
		default:
			System.err.println("Error!");
		}
	}
}

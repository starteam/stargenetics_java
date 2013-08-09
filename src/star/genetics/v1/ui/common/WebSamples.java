package star.genetics.v1.ui.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import star.annotations.SignalComponent;
import star.annotations.Wrap;
import star.annotations.Wrap.Types;
import star.genetics.Messages;
import star.genetics.events.OpenModelRaiser;
import utils.FileUtils;
import utils.UIHelpers;

@SignalComponent(extend = JDialog.class, raises = { OpenModelRaiser.class })
public class WebSamples extends WebSamples_generated {
	private static final long serialVersionUID = 1L;
	private WebSamples self = this;
	final private String url = Messages.getString("WebSamples.2"); //$NON-NLS-1$
	private URL getAlternate_url() {
		String alt_url = Messages.getString("WebSamples.4");
		System.out.println( "Alternate url:" + alt_url);
		URL ret = this.getClass().getResource(alt_url); //$NON-NLS-1$
		return ret ;
	}
	private URL samplesURL = null;
	private String openURL = null;
	private boolean isAlternate = false;
	private JEditorPane label;
	private byte[] bytes;
	private String modelFileName;
	private java.net.URL modelURL;
	private String NO_CONNECTION = Messages.getString("WebSamples.1"); //$NON-NLS-1$

	public URL getModelURL() {
		return modelURL;
	}

	public WebSamples(Frame frame) {
		super(frame);
		setTitle(Messages.getString("WebSamples.3")); //$NON-NLS-1$
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setModal(true);
	}

	private String getAlternateText() {
		String text = ""; //$NON-NLS-1$
		try {
			isAlternate = true;
			samplesURL = getAlternate_url();
			text = new String(FileUtils.getStreamToByteArray(samplesURL
					.openStream()), Charset.forName("UTF-8"));
		} catch (Throwable t2) {
			samplesURL = null;
			text = Messages.getString("WebSamples.5"); //$NON-NLS-1$
		}
		return text;
	}

	private String getText() {
		String text = ""; //$NON-NLS-1$
		try {
			samplesURL = new URL(url);
			URLConnection c = samplesURL.openConnection();
			c.setConnectTimeout(3000);
			c.setReadTimeout(3000);
			text = new String(
					FileUtils.getStreamToByteArray(c.getInputStream()),
					Charset.forName("UTF-8"));
		} catch (Throwable t) {
			text = getAlternateText();
		}
		if (true) {
			int loc = text.indexOf("<html"); //$NON-NLS-1$
			if (loc != -1) {
				text = text.substring(loc);
			}
		}
		return text;
	}

	private URL getURL(HyperlinkEvent e) {
		URL ret = e.getURL();
		if (ret == null) {
			try {
				ret = new URL(samplesURL, e.getDescription());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ret;
	}

	@Wrap(type = Wrap.Types.SwingUtilitiesInvokeLater)
	void openURL() {
		// new Thread(new Runnable()
		// {
		//
		// public void run()
		// {
		// try
		// {
		// Thread.sleep(1750);
		// self.dispose();
		// }
		// catch (Exception ex)
		// {
		//
		// }
		// }
		// }).start();
		// JOptionPane.showMessageDialog(self,
		// "Attempting to open external browser. External URL is: " + openURL);
		// setVisible(false);
		// dispose();
		UIHelpers.openWebBrowser(openURL);
	}

	JLabel loading = new JLabel("Loading..."); //$NON-NLS-1$

	public void addNotify() {
		super.addNotify();
		setLayout(new BorderLayout());
		add(loading);
		addContent_SwingUtilitiesInvokeLater();
	}

	@Wrap(type = Types.SwingUtilitiesInvokeLater)
	public void addContent() {
		loading.setVisible(false);
		setLayout(new BorderLayout());
		label = new JEditorPane("text/html", ""); //$NON-NLS-1$ //$NON-NLS-2$
		String text = getText();
		text = text.replaceAll("class=\"[^\"]*\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
		label.setText("<html>" + text + "</html>"); //$NON-NLS-1$ //$NON-NLS-2$
		label.setEditable(false);

		label.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == EventType.ACTIVATED) {
					final URL url = getURL(e);
					if (url != null) {
						modelFileName = url.toExternalForm();
						if (modelFileName.endsWith(".sgz") || modelFileName.endsWith(".xls")) //$NON-NLS-1$ //$NON-NLS-2$
						{
							try {
								modelFileName = label
										.getText(e.getSourceElement()
												.getStartOffset(), e
												.getSourceElement()
												.getEndOffset()
												- e.getSourceElement()
														.getStartOffset());
							} catch (Throwable t) {
								modelFileName = e.getDescription();
								t.printStackTrace();
							}
							new Thread(new Runnable() {
								public void run() {
									try {
										ProgressMonitorInputStream pis = new ProgressMonitorInputStream(
												self,
												MessageFormat.format(
														Messages.getString("WebSamples.0"), modelFileName), url.openStream()); //$NON-NLS-1$
										pis.getProgressMonitor()
												.setMillisToPopup(0);
										pis.getProgressMonitor()
												.setMillisToDecideToPopup(0);
										bytes = FileUtils
												.getStreamToByteArray(new BufferedInputStream(
														pis));
										modelURL = url;
										SwingUtilities
												.invokeLater(new Runnable() {
													public void run() {
														raise_OpenModelEvent();
														setVisible(false);
														dispose();
													}
												});
										UIHelpers.track("New/" + modelFileName); //$NON-NLS-1$
									} catch (Throwable t) {
										t.printStackTrace();
									}

								};
							}).start();
						} else {
							if (!isAlternate) {
								openURL = url.toExternalForm();
								openURL_SwingUtilitiesInvokeLater();
							} else {
								JOptionPane.showMessageDialog(self,
										NO_CONNECTION);
							}
						}
					} else {
						JOptionPane.showMessageDialog(self, NO_CONNECTION);
					}
				}
			}
		});
		JScrollPane p = new JScrollPane(label);
		p.setPreferredSize(new Dimension(800, 600));
		add(p);
		JButton close = new JButton(Messages.getString("WebSamples.19")); //$NON-NLS-1$
		JPanel p2 = new JPanel();
		p2.add(close);
		add(BorderLayout.SOUTH, p2);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();

			}
		});
		pack();
		UIHelpers.centerOnParent(this);
		UIHelpers.fixBoundsOnce(this, null);

	}

	public String getModelFileName() {
		return modelFileName;
	}

	public InputStream getOpenModelStream() {
		return new ByteArrayInputStream(bytes);
	}
}

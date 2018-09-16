import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class WalkNow implements ActionListener {
	//Sleep one hour
	private static long SLEEPTIME = 3600_000L;
	private static DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	
	private SystemTray tray = SystemTray.getSystemTray();
	private TrayIcon trayIcon = null;
	
	private PopupMenu popupMenu = new PopupMenu();
	private MenuItem infoItem = new MenuItem("stand up/walk at 00:00:00");
	private MenuItem exitItem = new MenuItem("Exit");
	
	private boolean repeat = true;
	
	public WalkNow() throws AWTException {
		initializeTrayIcon();
		
		while(repeat) {
			long sleepUntil = System.currentTimeMillis() + SLEEPTIME;
			long timeDifference = SLEEPTIME;
			while(timeDifference > 0 && repeat) {
				Date date = new Date(sleepUntil);
				String dateFormatted = formatter.format(date);
				infoItem.setLabel("stand up/walk at " + dateFormatted);
		        trayIcon.setToolTip("stand up/walk at " + dateFormatted);
				try {
					Thread.sleep(timeDifference);
				} catch (Exception e) {}
				
				long currentTime = System.currentTimeMillis();
				timeDifference = sleepUntil - currentTime;
			}
			displayMessage();
			
			//Remove after 10 seconds
			try {
	            Thread.sleep(10_000L);
	        } catch (InterruptedException ie) {}
			removeMessage();
		}
	}

	public static void main(String[] args) throws AWTException {
		if (!SystemTray.isSupported()) {
			System.err.println("System tray not supported!");
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
			scanner.close();
			return;
        }
		new WalkNow();
	}
	
	private void initializeTrayIcon() throws AWTException  {
        Image image = Toolkit.getDefaultToolkit().createImage("image.png");
        
        trayIcon = new TrayIcon(image, "WalkNow");
        trayIcon.setImageAutoSize(true);
        
        popupMenu.add(infoItem);
        popupMenu.add(exitItem);
        exitItem.addActionListener(this);
        
        trayIcon.setPopupMenu(popupMenu);
        
        tray.add(trayIcon);
	}
	
	
	public void displayMessage(){
        trayIcon.displayMessage("WalkNow", "1h expired, time to Walk!", MessageType.INFO);
    }
	
	public void removeMessage() throws AWTException {
		tray.remove(trayIcon);
		tray.add(trayIcon);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		repeat = false;
		System.exit(0);
	}

}

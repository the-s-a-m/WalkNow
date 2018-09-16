import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class WalkNow {
	//Sleep one hour
	private static long SLEEPTIME = 3600_000L;
	
	private static DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	private SystemTray tray = null;
	private TrayIcon trayIcon = null;

	public static void main(String[] args) throws MalformedURLException, AWTException {
		if (!SystemTray.isSupported()) {
			System.err.println("System tray not supported!");
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
			scanner.close();
			return;
        }
		
		WalkNow td = new WalkNow();
		td.createTrayIcon();
		
		System.out.println("Started WalkNow");
		while(true) {
			long sleepUntil = System.currentTimeMillis() + SLEEPTIME;
			long timeDifference = SLEEPTIME;
			while(timeDifference > 0) {
				Date date = new Date(sleepUntil);
				String dateFormatted = formatter.format(date);
				System.out.println("Sleeping until " + dateFormatted);
				try {
					Thread.sleep(timeDifference);
				} catch (Exception e) {}
				
				long currentTime = System.currentTimeMillis();
				timeDifference = sleepUntil - currentTime;
			}
			td.displayTray();
			
			//Remove after 10 seconds
			try {
	            Thread.sleep(10_000L);
	            td.removeTray();
	        } catch (InterruptedException ie) {}
		}
	}
	
	private void createTrayIcon() {
		tray = SystemTray.getSystemTray();
		
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        
        trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
	}
	
	
	public void displayTray() throws AWTException, MalformedURLException {
		tray.add(trayIcon);
        trayIcon.displayMessage("WalkNow", "1h expired, time to Walk!", MessageType.INFO);
    }
	
	public void removeTray() throws AWTException, MalformedURLException {
		tray.remove(trayIcon);
    }

}

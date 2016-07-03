package yaml;

import java.util.HashMap;

import lirc.Lirc;
import base.work.Listen;

import com.github.boukefalos.lirc.LircButton;
import com.github.boukefalos.lirc.implementation.Local;
import com.github.boukefalos.lirc.util.SignalObject;
public class TestParsing extends Listen<SignalObject<LircButton>> {
	public static void main(String[] args) {
		new TestParsing().start();
	}

	protected HashMap<String, LircButton[]> buttonMap;
    //protected LircTaskMapCycle taskMapCycle;
    //buttonMap = new HashMap<String, LircButton[]>();

	public void start() {
		Local lirc = new Local();

		   /*public void put(String name, LircButton[] LircButtonArray) {
        buttonMap.put(name, LircButtonArray);
    }*/
		
		
    /*public LircButton parseButton(Scanner scanner) throws UnknownButtonException {
    try {

        LircButton[] buttonArray = buttonMap.get(remote);
        f (buttonArray != null) {            
            for (LircButton button : buttonArray) {
                if (button.getCode().equals(code)) {
                    return button;
                }
            }
        }
    } catch (InputMismatchException e) {
        logger.error("", e);
    } catch (NoSuchElementException e) {
        logger.error("", e);
    }
    throw new UnknownButtonException();
}*/
		
        /*put(PhiliphsRCLE011Button.NAME, PhiliphsRCLE011Button.values());
        put(DenonRC176Button.NAME, DenonRC176Button.values());
        put(SamsungBN5901015AButton.NAME, SamsungBN5901015AButton.values());*/

		lirc.Lirc.Signal x = Lirc.Signal.BEGIN;
		//lirc.put(PhiliphsRCLE011Button.NAME, PhiliphsRCLE011Button.values());
		lirc.start();
		super.start();
		
		try {
			Thread.sleep(100000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    public void put(String name, LircButton[] LircButtonArray) {
        buttonMap.put(name, LircButtonArray);
    }
}

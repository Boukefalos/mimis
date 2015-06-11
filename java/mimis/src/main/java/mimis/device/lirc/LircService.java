/**
 * Copyright (C) 2015 Rik Veenboer <rik.veenboer@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package mimis.device.lirc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import mimis.exception.button.UnknownButtonException;
import mimis.util.Native;
import mimis.value.Registry;
import base.exception.worker.ActivateException;
import base.server.socket.TcpClient;

public class LircService extends TcpClient {
    public static final String IP = "atom";
    public static final int PORT = 8765;

    protected ArrayList<LircButtonListener> lircButtonListenerList;

    // Pluggable reader and writer?
    // Receive strings via callback?
    protected BufferedReader bufferedReader;
    protected PrintWriter printWriter;
    protected HashMap<String, LircButton[]> buttonMap;
    protected String send;

    public static void main(String[] args) {
    	LircService lircService = new LircService();
    	lircService.start(false);
    }

    public LircService() {
        super(IP, PORT);
        buttonMap = new HashMap<String, LircButton[]>();
        send = Native.getValue(Registry.CURRENT_USER, "Software\\LIRC", "password");
        lircButtonListenerList = new ArrayList<LircButtonListener>();
    }

    public void put(String name, LircButton[] LircButtonArray) {
        buttonMap.put(name, LircButtonArray);
    }

    public void add(LircButtonListener lircButtonListener) {
        lircButtonListenerList.add(lircButtonListener);
    }

    public void remove(LircButtonListener lircButtonListener) {
        lircButtonListenerList.remove(lircButtonListener);
    }


    public void activate() throws ActivateException {
    	super.activate();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        printWriter = new PrintWriter(outputStream);
    }

    public void work() {
        try {
            String line = bufferedReader.readLine();
            while (line.equals("BEGIN")) {
                while (!bufferedReader.readLine().equals("END"));
                line = bufferedReader.readLine();
            }
            try {
                LircButton lircButton = parseButton(new Scanner(line));
                for (LircButtonListener lircbuttonListener : lircButtonListenerList) {
                    lircbuttonListener.add(lircButton);
                }
            } catch (UnknownButtonException e) {
                logger.error("", e);
            }
        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public LircButton parseButton(Scanner scanner) throws UnknownButtonException {
        try {
            scanner.next();
            scanner.next();
            String code = scanner.next();
            String remote = scanner.next();
            logger.trace(String.format("%s: %s", remote, code));
            LircButton[] buttonArray = buttonMap.get(remote);
            if (buttonArray != null) {            
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
    }

    public void send(LircButton button) {
        send(button, 0);
    }

    public void send(LircButton button, int repeat) {
        if (send == null) {
            return;
        }
        String command = String.format("%s %s %s \n", send, button.getName(), button.getCode());
        printWriter.append(command);
        printWriter.flush();
    }
}

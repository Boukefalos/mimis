/**
 * Copyright (C) 2016 Rik Veenboer <rik.veenboer@gmail.com>
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
package winapi;
import java.util.HashMap;
import java.util.Random;

import org.synthuse.Api;
import org.synthuse.objects.MenuItem;

import com.sun.jna.platform.win32.WinDef.HWND;

public class Test {    
    protected Api api;

    protected HashMap<Slider, HashMap<Amount, MenuItem>> sliderMap;
    protected HashMap<Slider, HWND> valueMap;

    public Test() {
        api = new Api();
        sliderMap = new HashMap<Slider, HashMap<Amount, MenuItem>>();
        valueMap = new HashMap<Slider, HWND>();
    }

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.start();

        //test.moveSlider(Slider.CONTRAST, Amount.INCREASE_LITTLE);

        for (int k = 0; k < 5; ++k) {
            Slider slider = Slider.values()[new Random().nextInt(Slider.values().length)];
            for (int j = 0; j < 5; ++j) {
                for (int i = 0; i < 10; ++i) {
                    test.moveSlider(slider, Amount.INCREASE_LITTLE);
                    System.out.println(test.getValue(slider));
                    Thread.sleep(200);
                }
                Thread.sleep(400);
                for (int i = 0; i < 10; ++i) {
                    test.moveSlider(slider, Amount.INCREASE_LITTLE);
                    Thread.sleep(200);
                }
            }
        }
    }

    public Slider[] getSliders() {
        return sliderMap.keySet().toArray(new Slider[0]);
    }

    public void moveSlider(Slider slider, Amount amount) throws Exception {
        MenuItem menuItem = sliderMap.get(slider).get(amount);
        api.activateItem(menuItem);
    }

    public float getValue(Slider slider) {
        if (valueMap.containsKey(slider)) {
            HWND hWnd = valueMap.get(slider);
            String text = Api.getWindowText(hWnd);
            return Float.valueOf(text.replace(" ", ""));
        } else {
            return 0;
        }
    }

    public void start() throws Exception {  
        // Find Lightroom window
        HWND hWndTopWindow = api.findTopWindow("Lightroom", "AgWinMainFrame");
        if (hWndTopWindow == null) {
            throw new Exception("Can't find top window");
        }

        // Find menu options from Keyboard Tamer
        String[] path = {"&File", "Pl&ug-in Extras", ""};
        for (Slider slider : Slider.values()) {
            HashMap<Amount, MenuItem> amountMap = new HashMap<Amount, MenuItem>();
            for (Amount amount : Amount.values()) {                
                String label = slider.getLabel(amount);
                path[2] = String.format("   %s", label);
                MenuItem menuItem = api.loadMenuItem(hWndTopWindow, true, path);
                amountMap.put(amount, menuItem);
            }
            sliderMap.put(slider, amountMap);
        }

        // Find develop sliders
        path = new String[]{"", "Top", "Main", "Panel", "Last Panel", "View", "ClipView", "Accordion", "Accordion"};
        HWND hWnd = api.findChildWindow(hWndTopWindow, path);
        if (hWnd == null) {
            throw new Exception("Can't find window");
        }
        for (HWND hWndLoop : api.findAllChildWindow(hWnd, "Collapsible")) {
            path = new String[]{"Basic", "View"};
            hWnd = api.findChildWindow(hWndLoop, path);
            if (hWnd != null) {
                Slider slider = null;
                for (HWND hWndSubLoop : api.findAllChildWindow(hWnd, "")) {
                    //String className = Api.getWindowClassName(hWndSubLoop);
                    String text = Api.getWindowText(hWndSubLoop);
                    if (!text.contains("Bridge") && !text.contains("View") && text.length() > 0) {
                        if (slider != null) {
                            System.out.printf("%s = %s (%.2f)\n", slider.getLabel(), text, Float.valueOf(text.replace(" ", "")));
                            valueMap.put(slider, hWndSubLoop);
                            slider = null;
                        } else {
                            for (Slider sliderLoop : Slider.values()) {
                                if (sliderLoop.getLabel().equals(text)) {
                                    slider = sliderLoop;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
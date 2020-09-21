package Univale.Tcc.RL.Pogamut;

import Univale.Tcc.RL.Pogamut.Gui.DefaultLineChart;
import Univale.Tcc.RL.Pogamut.Gui.MainForm;
import Univale.Tcc.RL.Pogamut.Gui.MainForm2;
import org.eclipse.swt.widgets.Display;

/**
 * @author Didula
 */
public class Initializer {

    public static void main(String[] args) {
        Display display = new Display();
        MainForm form = new MainForm(display);
        form.open();
        while (!form.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}


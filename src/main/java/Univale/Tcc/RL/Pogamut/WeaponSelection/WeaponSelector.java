package Univale.Tcc.RL.Pogamut.WeaponSelection;

import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;
import com.thoughtworks.xstream.XStream;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.WeaponPref;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.WeaponPrefs;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.ItemPickedUp;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Didula
 */
public class WeaponSelector {

    private WeaponPrefs weaponPrefs;
    private Map<Integer, String> damageAndWeaponComp;
    private ArrayList<UT2004ItemType> weaponList;
    final XStream xstream = new XStream();

    public WeaponSelector(WeaponPrefs weaponPrefs) {
        this.weaponPrefs = weaponPrefs;
//        damageAndWeaponComp = new TreeMap<>(Collections.reverseOrder());
        weaponList = new ArrayList<UT2004ItemType>();
        loadWeaponPref();
        addPrefToList();
    }

    //finding weapon choice
    public void weaponPref(String weaponName, Integer damage) {
        weaponList.clear();
        weaponPrefs.clearAllPrefs();
        damageAndWeaponComp.put(damage, weaponName);
        addPrefToList();
    }

    //sort map to an arraylist
    public void addPrefToList() {
        for (Map.Entry<Integer, String> entry : damageAndWeaponComp.entrySet()) {
            UT2004ItemType item = weaponChecker(entry.getValue());
            System.out.println("\n\n\nKey: " + entry.getKey() + ". Value: " + entry.getValue() + " Actual Weapon Type : " + item);
            weaponList.add(item);
        }
        sortWeaponPref(weaponList);
    }

    //Filtering the weapons according to catagory 
    public UT2004ItemType weaponChecker(String weapon) {
        if (weapon.toLowerCase().equals(UT2004ItemType.LIGHTNING_GUN.getGroup().toString().toLowerCase().split("_")[0] + "" + UT2004ItemType.LIGHTNING_GUN.getGroup().toString().toLowerCase().split("_")[1])) {
            return UT2004ItemType.LIGHTNING_GUN;
        } else if (weapon.toLowerCase().equals(UT2004ItemType.SHOCK_RIFLE.getGroup().toString().toLowerCase().split("_")[0] + "" + UT2004ItemType.SHOCK_RIFLE.getGroup().toString().toLowerCase().split("_")[1])) {
            return UT2004ItemType.SHOCK_RIFLE;
        } else if (weapon.toLowerCase().equals(UT2004ItemType.MINIGUN.getGroup().toString().toLowerCase())) {
            return UT2004ItemType.MINIGUN;
        } else if (weapon.toLowerCase().equals(UT2004ItemType.FLAK_CANNON.getGroup().toString().toLowerCase().split("_")[0] + "" + UT2004ItemType.FLAK_CANNON.getGroup().toString().toLowerCase().split("_")[1])) {
            return UT2004ItemType.FLAK_CANNON;
        } else if (weapon.toLowerCase().equals(UT2004ItemType.ROCKET_LAUNCHER.getGroup().toString().toLowerCase().split("_")[0] + "" + UT2004ItemType.ROCKET_LAUNCHER.getGroup().toString().toLowerCase().split("_")[1])) {
            return UT2004ItemType.ROCKET_LAUNCHER;
        } else if (weapon.toLowerCase().equals(UT2004ItemType.LINK_GUN.getGroup().toString().toLowerCase().split("_")[0] + "" + UT2004ItemType.LINK_GUN.getGroup().toString().toLowerCase().split("_")[1])) {
            return UT2004ItemType.LINK_GUN;
        } else if (weapon.toLowerCase().equals(UT2004ItemType.ASSAULT_RIFLE.getGroup().toString().toLowerCase().split("_")[0] + "" + UT2004ItemType.ASSAULT_RIFLE.getGroup().toString().toLowerCase().split("_")[1])) {
            return UT2004ItemType.ASSAULT_RIFLE;
        } else if (weapon.toLowerCase().equals(UT2004ItemType.BIO_RIFLE.getGroup().toString().toLowerCase().split("_")[0] + "" + UT2004ItemType.BIO_RIFLE.getGroup().toString().toLowerCase().split("_")[1])) {
            return UT2004ItemType.BIO_RIFLE;
        } else {
            return null;
        }
    }

    //add the arraylist to weapon preferences 
    public void sortWeaponPref(ArrayList<UT2004ItemType> weaponArray) {
        for (UT2004ItemType weapon : weaponArray) {
            weaponPrefs.addGeneralPref(weapon, true);
        }
    }

    private void loadWeaponPref() {
        FileInputStream file = null;
        try {
            file = new FileInputStream("weaponPrefs.xml");

            if (file == null) {
                damageAndWeaponComp = new TreeMap<>(Collections.reverseOrder());
            } else {
                damageAndWeaponComp = (Map<Integer, String>) xstream.fromXML(file);
                file.close();
                if (weaponList == null) {
                    damageAndWeaponComp = new TreeMap<>(Collections.reverseOrder());
                }
            }
        } catch (Exception e) {
            damageAndWeaponComp = new TreeMap<>(Collections.reverseOrder());
        }
    }

    public void saveWeaponPref() {
        XStream xstream = new XStream();
        String xml = xstream.toXML(damageAndWeaponComp);

        try {
            PrintWriter writer = new PrintWriter("weaponPrefs.xml", "UTF-8");
            writer.write(xml);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.err.println("Cannot save weapon prefs");
        }
    }

}

/*
 * ConverterXML.java
 *
 * Copyright (c) 2018 dr wilkinson <dr-wilkinson@users.noreply.github.com>.
 *
 * This file is part of Traveller.
 *
 * Traveller is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Traveller is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Traveller.  If not, see <http ://www.gnu.org/licenses/>.
 */
package io.github.drw.desktop.characters;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import io.github.drw.rules.characters.Character;
import io.github.drw.rules.characters.money.CreditBill;
import io.github.drw.rules.characters.skills.ATVSkill;
import io.github.drw.rules.characters.skills.AdminSkill;
import io.github.drw.rules.characters.skills.AirRaftSkill;
import io.github.drw.rules.characters.skills.BladeCombatSkill;
import io.github.drw.rules.characters.skills.BladeSpecialism;
import io.github.drw.rules.characters.skills.BrawlingSkill;
import io.github.drw.rules.characters.skills.BriberySkill;
import io.github.drw.rules.characters.skills.ComputerSkill;
import io.github.drw.rules.characters.skills.ElectronicsSkill;
import io.github.drw.rules.characters.skills.EngineeringSkill;
import io.github.drw.rules.characters.skills.ForgerySkill;
import io.github.drw.rules.characters.skills.ForwardObserverSkill;
import io.github.drw.rules.characters.skills.GamblingSkill;
import io.github.drw.rules.characters.skills.GunCombatSkill;
import io.github.drw.rules.characters.skills.GunSpecialism;
import io.github.drw.rules.characters.skills.GunnerySkill;
import io.github.drw.rules.characters.skills.JOATSkill;
import io.github.drw.rules.characters.skills.LeaderSkill;
import io.github.drw.rules.characters.skills.MechanicalSkill;
import io.github.drw.rules.characters.skills.MedicalSkill;
import io.github.drw.rules.characters.skills.NavigationSkill;
import io.github.drw.rules.characters.skills.PilotSkill;
import io.github.drw.rules.characters.skills.ShipsBoatSkill;
import io.github.drw.rules.characters.skills.StewardSkill;
import io.github.drw.rules.characters.skills.StreetwiseSkill;
import io.github.drw.rules.characters.skills.TacticsSkill;
import io.github.drw.rules.characters.skills.VacuumSuitSkill;
import io.github.drw.rules.characters.weapons.AutomaticPistol;
import io.github.drw.rules.characters.weapons.AutomaticRifle;
import io.github.drw.rules.characters.weapons.Bayonet;
import io.github.drw.rules.characters.weapons.BodyPistol;
import io.github.drw.rules.characters.weapons.BroadSword;
import io.github.drw.rules.characters.weapons.Carbine;
import io.github.drw.rules.characters.weapons.Cudgel;
import io.github.drw.rules.characters.weapons.Cutlass;
import io.github.drw.rules.characters.weapons.Dagger;
import io.github.drw.rules.characters.weapons.Foil;
import io.github.drw.rules.characters.weapons.Halberd;
import io.github.drw.rules.characters.weapons.Knife;
import io.github.drw.rules.characters.weapons.LaserCarbine;
import io.github.drw.rules.characters.weapons.LaserRifle;
import io.github.drw.rules.characters.weapons.Pike;
import io.github.drw.rules.characters.weapons.Polearm;
import io.github.drw.rules.characters.weapons.Revolver;
import io.github.drw.rules.characters.weapons.Rifle;
import io.github.drw.rules.characters.weapons.SMG;
import io.github.drw.rules.characters.weapons.Shotgun;
import io.github.drw.rules.characters.weapons.Spear;
import io.github.drw.rules.characters.weapons.Sword;
import io.github.drw.rules.passages.HighPassage;
import io.github.drw.rules.passages.LowPassage;
import io.github.drw.rules.passages.MiddlePassage;
import io.github.drw.rules.services.terms.Term;
import io.github.drw.rules.timing.Date;
import io.github.drw.rules.timing.Period;
import java.util.List;

/**
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
class ConverterXML {

    public static String convert(List<Character> characters) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("character", Character.class);
        xstream.alias("term", Term.class);
        aliasSkills(xstream);
        aliasWeapons(xstream);
        xstream.alias("high-passage", HighPassage.class);
        xstream.alias("middle-passage", MiddlePassage.class);
        xstream.alias("low-passage", LowPassage.class);
        xstream.alias("credit-bill", CreditBill.class);
        handleOmmissions(xstream);
        String xml = xstream.toXML(characters);
        return xml;
    }

    private static void handleOmmissions(XStream xstream) {
        xstream.omitField(CreditBill.class, "textual");
        xstream.omitField(Date.class, "days");
        xstream.omitField(Date.class, "calendar");
        xstream.omitField(Period.class, "year");
        xstream.omitField(Period.class, "day");
        xstream.omitField(Period.class, "hour");
        xstream.omitField(Period.class, "minute");
        xstream.omitField(Period.class, "second");
    }

    private static void aliasWeapons(XStream xstream) {
        xstream.alias("automatic-pistol", AutomaticPistol.class);
        xstream.alias("automatic-rifle", AutomaticRifle.class);
        xstream.alias("bayonet", Bayonet.class);
        xstream.alias("body-pistol", BodyPistol.class);
        xstream.alias("broadsword", BroadSword.class);
        xstream.alias("carbine", Carbine.class);
        xstream.alias("cudgel", Cudgel.class);
        xstream.alias("cutlass", Cutlass.class);
        xstream.alias("dagger", Dagger.class);
        xstream.alias("foil", Foil.class);
        xstream.alias("halbard", Halberd.class);
        xstream.alias("knife", Knife.class);
        xstream.alias("laser-carbine", LaserCarbine.class);
        xstream.alias("laser-rifle", LaserRifle.class);
        xstream.alias("pike", Pike.class);
        xstream.alias("polearm", Polearm.class);
        xstream.alias("revolver", Revolver.class);
        xstream.alias("rifle", Rifle.class);
        xstream.alias("smg", SMG.class);
        xstream.alias("shotgun", Shotgun.class);
        xstream.alias("spear", Spear.class);
        xstream.alias("sword", Sword.class);
    }

    private static void aliasSkills(XStream xstream) {
        xstream.alias("atv-skill", ATVSkill.class);
        xstream.alias("admin-skill", AdminSkill.class);
        xstream.alias("air-raft-skill", AirRaftSkill.class);
        xstream.alias("blade-combat-skill", BladeCombatSkill.class);
        xstream.alias("blade-specialism", BladeSpecialism.class);
        xstream.alias("brawling-skill", BrawlingSkill.class);
        xstream.alias("bribery-skill", BriberySkill.class);
        xstream.alias("computer-skill", ComputerSkill.class);
        xstream.alias("electronics-skill", ElectronicsSkill.class);
        xstream.alias("engineering-skill", EngineeringSkill.class);
        xstream.alias("forgery-skill", ForgerySkill.class);
        xstream.alias("forward-observer-skill", ForwardObserverSkill.class);
        xstream.alias("gambling-skill", GamblingSkill.class);
        xstream.alias("gun-combat-skill", GunCombatSkill.class);
        xstream.alias("gun-specialism", GunSpecialism.class);
        xstream.alias("gunnery-skill", GunnerySkill.class);
        xstream.alias("joat-skill", JOATSkill.class);
        xstream.alias("leader-skill", LeaderSkill.class);
        xstream.alias("mechanical-skill", MechanicalSkill.class);
        xstream.alias("medical-skill", MedicalSkill.class);
        xstream.alias("navigation-skill", NavigationSkill.class);
        xstream.alias("pilot-skill", PilotSkill.class);
        xstream.alias("ships-boat-skill", ShipsBoatSkill.class);
        xstream.alias("steward-skill", StewardSkill.class);
        xstream.alias("streetwise-skill", StreetwiseSkill.class);
        xstream.alias("tactics-skill", TacticsSkill.class);
        xstream.alias("vacuum-skill", VacuumSuitSkill.class);
    }

}

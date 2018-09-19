/*
 * Copyright (C) 2018 dr wilkinson <dr-wilkinson@users.noreply.github.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.drw.desktop.campaigns;

import io.github.drw.rules.campaigns.Campaign;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class Campaigns {

    private static List<Campaign> campaigns = new ArrayList<>();
    private static Campaign selected;

    static {
        loadOpenCampaigns();
    }

    public static Campaign getSelected() {
        return selected;
    }

    public static void setSelected(String title) {
        for (Campaign campaign : campaigns) {
            if (campaign.getTitle().equals(title)) {
                selected = campaign;
                break;
            }
        }
    }

    public static void addCampaign(Campaign campaign) {
        campaigns.add(campaign);
    }

    public static List<String> getCampaigns() {
        List<String> titles = new ArrayList<>();
        for (Campaign campaign : campaigns) {
            titles.add(campaign.getTitle());
        }
        return Collections.unmodifiableList(titles);
    }

    private static void loadOpenCampaigns() {
        System.out.println("TODO : Load previously open Campaigns from resources file.");
        Campaign red = new Campaign("Red Campaign");
        Campaign green = new Campaign("Green Campaign");
        Campaign blue = new Campaign("Blue Campaign");
        campaigns.add(red);
        campaigns.add(green);
        campaigns.add(blue);
    }

}

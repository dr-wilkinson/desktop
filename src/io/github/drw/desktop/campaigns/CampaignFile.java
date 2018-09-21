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
import java.io.File;

/**
 * A composition of a {@link Campaign} and a {@link File}. Has a
 * {@code nameTitle} that is shared by both the Campaign.title and File.name,
 * plus maintains its own {@code saved} state which denotes whether changes to
 * the Campaign have been saved to the File.
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class CampaignFile {

    private String nameTitle = "Untitled";
    private boolean saved = false;
    private Campaign campaign = null;
    private File file = null;

    public String getNameTitle() {
        return nameTitle;
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle = nameTitle;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}

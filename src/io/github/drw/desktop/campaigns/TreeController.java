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

import io.github.drw.desktop.DesktopApplication;
import io.github.drw.desktop.eventbus.Event;
import io.github.drw.desktop.eventbus.Eventbus;
import io.github.drw.desktop.eventbus.Listener;
import io.github.drw.desktop.eventbus.events.AdventureEvent;
import io.github.drw.desktop.eventbus.events.CampaignEvent;
import io.github.drw.desktop.eventbus.events.StatusEvent;
import io.github.drw.rules.adventures.Adventure;
import io.github.drw.rules.campaigns.Campaign;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.FileChooser;
import javafx.util.Callback;

/**
 * Controller for the {@link TreeView}.fxml
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class TreeController implements Initializable, Listener {

    private List<Campaign> campaigns = new ArrayList<>();
    private ContextMenu treeContextMenu;

    @FXML
    private TreeView<String> treeView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Eventbus.getInstance().add(this);
        initCampaignsTreeView();
        initTreeContextMenu();
    }

    @Override
    public Event handle(Event event) {
        if (event instanceof CampaignEvent) {
            event.addListener(this);
            if (event.getType().equals(CampaignEvent.Type.Open)) {
                displayOpenFileDialog();
                event.consume(this);
            }
            if (event.getType().equals(CampaignEvent.Type.Save)) {
                displaySaveFileDialog();
                event.consume(this);;
            }
        }
        if (event instanceof AdventureEvent) {
            event.addListener(this);
            if (event.getType().equals(AdventureEvent.Type.New)) {
                TreeItem<String> selectedTreeItem = treeView.getSelectionModel().getSelectedItem();
                String title = selectedTreeItem.getValue();
                Campaign campaign = getCampaign(title);
                if (campaign != null) {
                    String adventureTitle = "Untitled";
                    campaign.addAdventure(new Adventure(adventureTitle));
                    AdventureTreeItem<String> adventureTreeItem = new AdventureTreeItem<>(adventureTitle);
                    selectedTreeItem.getChildren().add(adventureTreeItem);
                }
            }
        }
        return event;
    }

    private void initCampaignsTreeView() {
        TreeItem<String> root = new TreeItem<>();
//        Campaign redCampaign = new Campaign("Red");
//        Campaign greenCampaign = new Campaign("Green");
//        Campaign blueCampaign = new Campaign("Blue");
//        campaigns.add(redCampaign);
//        campaigns.add(greenCampaign);
//        campaigns.add(blueCampaign);
//        for (Campaign campaign : campaigns) {
//            CampaignTreeItem<String> treeItem = new CampaignTreeItem<>(campaign.getTitle());
//            root.getChildren().add(treeItem);
//        }
        root.setExpanded(true);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                treeContextMenu.show(DesktopApplication.stage, event.getScreenX(), event.getScreenY());
            }
        });
        treeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> param) {
                return new CustomTreeCell();
            }
        });
    }

    private void initTreeContextMenu() {
        treeContextMenu = new ContextMenu();
        MenuItem newMenuItem = new MenuItem("New Campaign");
        newMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String title = "Untitled";
                TreeItem<String> treeItem = new TreeItem<>(title);
                campaigns.add(new Campaign(title));
                treeView.getRoot().getChildren().add(treeItem);
                Eventbus.fire(new StatusEvent(StatusEvent.Type.Update, this, "Campaign " + title + " created."));
            }
        });
        treeContextMenu.getItems().add(newMenuItem);
        MenuItem openMenuItem = new MenuItem("Open Campaign");
        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Eventbus.fire(new CampaignEvent(CampaignEvent.Type.Open, TreeController.this, null));
            }
        });
        treeContextMenu.getItems().add(openMenuItem);
    }

    private void displayOpenFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Campaign File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Campaign File", "*.cpn"));
        File selectedFile = fileChooser.showOpenDialog(DesktopApplication.stage);
        if (selectedFile != null) {
            FileInputStream fileInputStream = null;
            ObjectInputStream objectInputStream = null;
            try {
                fileInputStream = new FileInputStream(selectedFile);
                objectInputStream = new ObjectInputStream(fileInputStream);
                Campaign campaign = (Campaign) objectInputStream.readObject();
                System.out.println(campaign);
                for (Campaign c : campaigns) {
                    System.out.println(c);
                }
                if (campaigns.contains(campaign)) {
                    Eventbus.fire(new StatusEvent(StatusEvent.Type.Error, this, selectedFile.getName() + " is already open!"));
                    try {
                        fileInputStream.close();
                        objectInputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return;
                }
                CampaignTreeItem<String> campaignTreeItem = new CampaignTreeItem<>(campaign.getTitle().replace(".cpn", ""));
                treeView.getRoot().getChildren().add(campaignTreeItem);
                System.out.println("TODO : Implement this as lazy instantiation when campaign TreeItem is opened.");
                for (Adventure adventure : campaign.getAdventures()) {
                    AdventureTreeItem<String> adventureTreeItem = new AdventureTreeItem<>(adventure.getTitle());
                    campaignTreeItem.getChildren().add(adventureTreeItem);
                }
                campaigns.add(campaign);
                Eventbus.fire(new StatusEvent(StatusEvent.Type.Update, this, "File " + campaignTreeItem.getValue() + " opened."));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fileInputStream.close();
                    objectInputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void displaySaveFileDialog() {
        if (treeView.getSelectionModel().getSelectedItem() == null) {
            Eventbus.fire(new StatusEvent(StatusEvent.Type.Error, this, "Select a campaign to save."));
            return;
        } else {
            Campaign campaign = getCampaign(treeView.getSelectionModel().getSelectedItem().getValue());
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save " + campaign.getTitle() + " to file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Campaign File", "*.cpn"));
            fileChooser.initialFileNameProperty().setValue(campaign.getTitle());
            File savedFile = fileChooser.showSaveDialog(DesktopApplication.stage);
            if (savedFile != null) {
                if (!savedFile.getName().endsWith(".cpn")) {
                    savedFile = new File(savedFile.getAbsolutePath() + ".cpn");
                }
                FileOutputStream fileOutputStream = null;
                ObjectOutputStream objectOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(savedFile);
                    objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    String title = savedFile.getName().replace(".cpn", "");
                    campaign.setTitle(title);
                    treeView.getSelectionModel().getSelectedItem().setValue(title);
                    objectOutputStream.writeObject(campaign);
                    Eventbus.fire(new StatusEvent(StatusEvent.Type.Update, this, "File " + title + " saved."));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        fileOutputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        objectOutputStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private Campaign getCampaign(String title) {
        for (Campaign campaign : campaigns) {
            if (campaign.getTitle().equals(title)) {
                return campaign;
            }
        }
        return null;
    }

}

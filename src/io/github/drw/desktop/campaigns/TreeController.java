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
import io.github.drw.desktop.eventbus.events.ApplicationEvent;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.util.Callback;

/**
 * Controller for the {@link TreeView}.fxml
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class TreeController implements Initializable, Listener {

    private ContextMenu treeContextMenu;

    @FXML
    private TreeView<Object> treeView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Eventbus.getInstance().add(this);
        initCampaignsTreeView();
        initTreeContextMenu();
    }

    @Override
    public Event handle(Event event) {
        if (event instanceof ApplicationEvent) {
            event.addListener(this);
            if (event.getType().equals(ApplicationEvent.Type.Cleanup)) {
                saveUnsavedCampaigns();
            }
        }
        if (event instanceof CampaignEvent) {
            event.addListener(this);
            if (event.getType().equals(CampaignEvent.Type.Open)) {
                displayOpenFileDialog();
                event.consume(this);
            }
            if (event.getType().equals(CampaignEvent.Type.Save)) {
                displaySaveFileDialog();
                event.consume(this);
            }
            if (event.getType().equals(CampaignEvent.Type.Rename)) {
                displayRenameDialog();
                event.consume(this);
            }
        }
        if (event instanceof AdventureEvent) {
            event.addListener(this);
            if (event.getType().equals(AdventureEvent.Type.New)) {
                CampaignTreeItem<CampaignFile> selectedTreeItem = (CampaignTreeItem<CampaignFile>) treeView.getSelectionModel().getSelectedItem();
                Campaign campaign = (Campaign) ((CampaignFile) selectedTreeItem.getValue()).getCampaign();
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
        TreeItem<Object> root = new TreeItem<>();
        root.setExpanded(true);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                treeContextMenu.show(DesktopApplication.stage, event.getScreenX(), event.getScreenY());
            }
        });
        treeView.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {
            @Override
            public TreeCell<Object> call(TreeView<Object> param) {
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
                displayNewCampaignDialog();
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

    private void serialiseCampaign(Campaign campaign, File file) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(campaign);
        } catch (FileNotFoundException ex) {
            Eventbus.fire(new StatusEvent(StatusEvent.Type.Error, this, "File " + file.getName() + " not found!"));
            Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Eventbus.fire(new StatusEvent(StatusEvent.Type.Error, this, "Problem reading file " + file.getName()));
            Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileOutputStream.close();
                objectOutputStream.close();
            } catch (IOException ex) {
                Eventbus.fire(new StatusEvent(StatusEvent.Type.Error, this, "Problem closing output stream"));
                Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Campaign deserialiseCampaign(File file) {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            Campaign campaign = (Campaign) objectInputStream.readObject();
            return campaign;
        } catch (FileNotFoundException ex) {
            Eventbus.fire(new StatusEvent(StatusEvent.Type.Error, this, "File " + file.getName() + " not found!"));
            Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Eventbus.fire(new StatusEvent(StatusEvent.Type.Error, this, "Problem reading file " + file.getName()));
            Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileInputStream.close();
                objectInputStream.close();
            } catch (IOException ex) {
                Eventbus.fire(new StatusEvent(StatusEvent.Type.Error, this, "Problem closing output stream"));
                Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private void displayOpenFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Campaign File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Campaign File", "*.cpn"));
        File file = fileChooser.showOpenDialog(DesktopApplication.stage);
        if (file != null) {
            if (isFileAlreadyOpen(file)) {
                Eventbus.fire(new StatusEvent(StatusEvent.Type.Update, this, file.getName() + " is already open!"));
                return;
            }
            Campaign campaign = deserialiseCampaign(file);
            CampaignFile campaignFile = new CampaignFile();
            campaignFile.setCampaign(campaign);
            campaignFile.setFile(file);
            campaignFile.setSaved(true);
            campaignFile.syncNameTitle();
            CampaignTreeItem<Object> campaignTreeItem = addCampaignFileToTreeView(campaignFile);
            addAdventuresToCampaignTreeItem(campaign.getAdventures(), campaignTreeItem);
            Eventbus.fire(new StatusEvent(StatusEvent.Type.Update, this, "File " + campaignFile.getNameTitle() + " opened."));
        }
    }

    /**
     * Determines whether the supplied {@link File} is currently opened and
     * displayed within the {@link TreeView}.
     *
     * @param file The File to be checked for.
     * @return {@code true} if the File is open and displayed, otherwise
     * {@code false}.
     */
    private boolean isFileAlreadyOpen(File file) {
        for (Object object : treeView.getRoot().getChildren()) {
            if (object instanceof CampaignTreeItem) {
                CampaignTreeItem campaignTreeItem = (CampaignTreeItem) object;
                CampaignFile campaignFile = (CampaignFile) campaignTreeItem.getValue();
                if (file.equals(campaignFile.getFile())) {
                    return true;
                }
            }
        }
        return false;
    }

    private CampaignTreeItem<Object> addCampaignFileToTreeView(CampaignFile campaignFile) {
        CampaignTreeItem<Object> campaignTreeItem = new CampaignTreeItem<>(campaignFile);
        treeView.getRoot().getChildren().add(campaignTreeItem);
        return campaignTreeItem;
    }

    private void addAdventuresToCampaignTreeItem(List<Adventure> adventures, CampaignTreeItem campaignTreeItem) {
        System.out.println("TODO : Implement this as lazy instantiation when campaign TreeItem is opened.");
        for (Adventure adventure : adventures) {
            AdventureTreeItem<String> adventureTreeItem = new AdventureTreeItem<>(adventure.getTitle());
            campaignTreeItem.getChildren().add(adventureTreeItem);
        }
    }

    private void displaySaveFileDialog() {
        if (treeView.getSelectionModel().getSelectedItem() == null) {
            Eventbus.fire(new StatusEvent(StatusEvent.Type.Error, this, "Select a campaign to save."));
            return;
        } else {
            CampaignTreeItem<String> campaignTreeItem = (CampaignTreeItem<String>) treeView.getSelectionModel().getSelectedItem();
            CampaignFile oldCampaignFile = (CampaignFile) campaignTreeItem.getValue();
            String fileName = oldCampaignFile.getNameTitle();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save " + fileName + " to file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Campaign File", "*.cpn"));
            fileChooser.initialFileNameProperty().setValue(fileName);
            File savedFile = fileChooser.showSaveDialog(DesktopApplication.stage);
            if (savedFile != null) {
                if (!savedFile.getName().endsWith(".cpn")) {
                    savedFile = new File(savedFile.getAbsolutePath() + ".cpn");
                }
                Campaign campaign = (Campaign) oldCampaignFile.getCampaign();
                serialiseCampaign(campaign, savedFile);
                CampaignFile newCampaignFile = new CampaignFile();
                newCampaignFile.setCampaign(campaign);
                newCampaignFile.setFile(savedFile);
                newCampaignFile.syncNameTitle();
                newCampaignFile.setSaved(true);
                campaignTreeItem.setValue(newCampaignFile);
                Eventbus.fire(new StatusEvent(StatusEvent.Type.Update, this, newCampaignFile.getNameTitle() + " saved."));
            }
        }
    }

    private void displayNewCampaignDialog() {
        String headerText = "New Campaign";
        String contentText = "Enter the name of the new Campaign.";
        String extension = ".cpn";
        TextInputDialog textInputDialog = new TextInputDialog("Untitled");
        textInputDialog.setHeaderText(headerText);
        textInputDialog.setContentText(contentText);
        textInputDialog.initOwner(DesktopApplication.stage);
        textInputDialog.initModality(Modality.APPLICATION_MODAL);
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent() && !result.get().isEmpty()) {
            String name = result.get();
            CampaignFile campaignFile = new CampaignFile();
            campaignFile.setFile(null);
            campaignFile.setCampaign(new Campaign(name));
            campaignFile.syncNameTitle();
            campaignFile.setSaved(false);
            String nameTitle = campaignFile.getNameTitle();
            CampaignTreeItem<String> campaignTreeItem = new CampaignTreeItem<String>(campaignFile.getNameTitle());
            campaignTreeItem.setValue(campaignFile);
            treeView.getRoot().getChildren().add(campaignTreeItem);
            Eventbus.fire(new StatusEvent(StatusEvent.Type.Update, TreeController.this, "Campaign " + campaignFile.getNameTitle() + " created."));
        }
    }

    private void displayRenameDialog() {
        CampaignFile oldCampaignFile = (CampaignFile) treeView.getSelectionModel().getSelectedItem().getValue();
        if (!oldCampaignFile.isSaved()) {
            Eventbus.fire(new StatusEvent(StatusEvent.Type.Update, this, "Campaign " + oldCampaignFile.getNameTitle() + " must be saved before being renamed."));
            displaySaveFileDialog();
            return;
        }
        String oldName = oldCampaignFile.getNameTitle();
        TextInputDialog textInputDialog = new TextInputDialog(oldCampaignFile.getNameTitle());
        textInputDialog.setHeaderText("Rename Campaign");
        textInputDialog.setContentText("Enter the new name.");
        textInputDialog.initOwner(DesktopApplication.stage);
        textInputDialog.initModality(Modality.APPLICATION_MODAL);
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent() && !result.get().isEmpty()) {
            String newName = result.get();
            if (!newName.endsWith(".cpn")) {
                newName = newName.concat(".cpn");
            }
            File file = oldCampaignFile.getFile();
            Path path = file.toPath();
            try {
                Path newPath = Files.move(path, path.resolveSibling(newName), StandardCopyOption.REPLACE_EXISTING);
                File newFile = new File(newPath.toString());
                CampaignFile newCampaignFile = new CampaignFile();
                newCampaignFile.setCampaign(oldCampaignFile.getCampaign());
                newCampaignFile.setFile(newFile);
                newCampaignFile.setSaved(oldCampaignFile.isSaved());
                newCampaignFile.syncNameTitle();
                treeView.getSelectionModel().getSelectedItem().setValue(newCampaignFile);
                Eventbus.fire(new StatusEvent(StatusEvent.Type.Update, this, oldName + " was renamed to " + newName));
            } catch (IOException ex) {
                Eventbus.fire(new StatusEvent(StatusEvent.Type.Error, this, "Failed to rename file " + oldName));
                Logger.getLogger(TreeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void saveUnsavedCampaigns() {
        treeView.getSelectionModel().clearSelection();
        int index = 0;
        for (TreeItem<Object> treeItem : treeView.getRoot().getChildren()) {
            if (treeItem instanceof CampaignTreeItem) {
                CampaignFile campaignFile = (CampaignFile) treeItem.getValue();
                if (!campaignFile.isSaved()) {
                    if (campaignFile.getFile() == null) {
                        treeView.getSelectionModel().select(index);
                        displaySaveFileDialog();
                    } else {
                        Campaign campaign = campaignFile.getCampaign();
                        File file = campaignFile.getFile();
                        serialiseCampaign(campaign, file);
                        Eventbus.fire(new StatusEvent(StatusEvent.Type.Update, this, campaignFile.getNameTitle() + " saved."));
                    }
                }
                index++;
            }
        }
    }

}

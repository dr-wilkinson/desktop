/*
 * Controller.java
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

import io.github.drw.rules.characters.Character;
import io.github.drw.rules.characters.CharacterGenerator;
import io.github.drw.rules.characters.CharacterViewer;
import io.github.drw.rules.characters.genders.Gender;
import io.github.drw.rules.characters.resume.ResumeViewer;
import io.github.drw.rules.characters.skills.SkillsViewer;
import io.github.drw.rules.characters.weapons.WeaponsViewer;
import io.github.drw.rules.services.Army;
import io.github.drw.rules.services.Career;
import io.github.drw.rules.services.Marines;
import io.github.drw.rules.services.Merchants;
import io.github.drw.rules.services.Navy;
import io.github.drw.rules.services.Other;
import io.github.drw.rules.services.Recruitment;
import io.github.drw.rules.services.Scouts;
import io.github.drw.rules.services.Service;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class Controller implements Initializable {

    private Model model;

    private boolean saved = false;
    private int total = 0;
    private GenderOptions gender = GenderOptions.Both;
    private List<Service> services = new ArrayList<>();
    private List<Character> characters = new ArrayList<>();

    @FXML
    private TextField totalTextField;
    @FXML
    private ChoiceBox<GenderOptions> genderChoiceBox;
    @FXML
    private CheckBox navyCheckBox;
    @FXML
    private CheckBox marinesCheckBox;
    @FXML
    private CheckBox armyCheckBox;
    @FXML
    private CheckBox scoutsCheckBox;
    @FXML
    private CheckBox merchantsCheckBox;
    @FXML
    private CheckBox otherCheckBox;
    @FXML
    private Button createButton;
    @FXML
    private TextArea outputTextArea;
    @FXML
    private MenuItem newMenuItem;
    @FXML
    private MenuItem openMenuItem;
    @FXML
    private Menu openRecentMenu;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem saveAsMenuItem;
    @FXML
    private MenuItem quitMenuItem;

    private void displaySaveAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "If you choose not to save, the currently generated characters will be lost.", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Save File");
        alert.setHeaderText("Do you want to save to file?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.YES) {
            displaySaveFile();
        } else if (option.get() == ButtonType.NO) {
            reset();
        }
    }

    private void displaySaveFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Traveller Files (*.trv)", "*.trv");
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        File file = fileChooser.showSaveDialog(ApplicationGui.stage);
        if (file != null) {
            saveFile(file);
            reset();
        }
    }

    private void saveFile(File file) {
        String content = ConverterXML.convert(characters);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reset() {
        total = 0;
        totalTextField.textProperty().setValue("0");
        genderChoiceBox.getSelectionModel().selectFirst();
        navyCheckBox.setSelected(false);
        marinesCheckBox.setSelected(false);
        armyCheckBox.setSelected(false);
        scoutsCheckBox.setSelected(false);
        merchantsCheckBox.setSelected(false);
        otherCheckBox.setSelected(false);
        services.clear();
        outputTextArea.clear();
        characters.clear();
        totalTextField.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!saved) {
                    displaySaveAlert();
                }
            }
        });
        totalTextField.textProperty().setValue("0");
        ObservableList<GenderOptions> gendersList = FXCollections.observableArrayList(GenderOptions.Both, GenderOptions.Male, GenderOptions.Female);
        genderChoiceBox.setItems(gendersList);
        genderChoiceBox.getSelectionModel().selectFirst();
        ChangeListener<GenderOptions> genderListener = new ChangeListener<GenderOptions>() {
            @Override
            public void changed(ObservableValue<? extends GenderOptions> observable, GenderOptions oldValue, GenderOptions newValue) {
                if (newValue != null) {
                    gender = newValue;
                }
            }
        };
        genderChoiceBox.getSelectionModel().selectedItemProperty().addListener(genderListener);
        navyCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    services.add(new Navy());
                } else {
                    int index = 0;
                    for (Service service : services) {
                        if (service instanceof Navy) {
                            index = services.indexOf(service);
                            break;
                        }
                    }
                    services.remove(index);
                }
            }
        });
        marinesCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    services.add(new Marines());
                } else {
                    int index = 0;
                    for (Service service : services) {
                        if (service instanceof Marines) {
                            index = services.indexOf(service);
                            break;
                        }
                    }
                    services.remove(index);
                }
            }
        });
        armyCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    services.add(new Army());
                } else {
                    int index = 0;
                    for (Service service : services) {
                        if (service instanceof Army) {
                            index = services.indexOf(service);
                            break;
                        }
                    }
                    services.remove(index);
                }
            }
        });
        scoutsCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    services.add(new Scouts());
                } else {
                    int index = 0;
                    for (Service service : services) {
                        if (service instanceof Scouts) {
                            index = services.indexOf(service);
                            break;
                        }
                    }
                    services.remove(index);
                }
            }
        });
        merchantsCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    services.add(new Merchants());
                } else {
                    int index = 0;
                    for (Service service : services) {
                        if (service instanceof Merchants) {
                            index = services.indexOf(service);
                            break;
                        }
                    }
                    services.remove(index);
                }
            }
        });
        otherCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    services.add(new Other());
                } else {
                    int index = 0;
                    for (Service service : services) {
                        if (service instanceof Other) {
                            index = services.indexOf(service);
                            break;
                        }
                    }
                    services.remove(index);
                }
            }
        });
        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                outputTextArea.clear();
                characters.clear();
                total = Integer.parseInt(totalTextField.textProperty().getValue());
                Character character = null;
                Service service = null;
                while (characters.size() < total) {
                    character = new Character();
                    if (!gender.equals(GenderOptions.Both)) {
                        character.setGender(new Gender(gender.name()));
                    }
                    CharacterGenerator.roll(character);
                    if (!services.isEmpty()) {
                        service = services.get(new Random().nextInt(services.size()));
                        Recruitment.recruit(character, service);
                    } else {
                        Recruitment.recruit(character);
                    }
                    Career.pursue(character);
                    if (character.isAlive()) {
                        characters.add(character);
                    }
                }
                for (Character c : characters) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(CharacterViewer.view(c)).append(System.lineSeparator());
                    sb.append(SkillsViewer.view(c.getSkills())).append(System.lineSeparator());
                    sb.append(WeaponsViewer.view(c.getWeapons())).append(System.lineSeparator());
                    sb.append(ResumeViewer.view(c.getResume())).append(System.lineSeparator());
                    outputTextArea.appendText(sb.toString());
                }
            }
        });
    }

    enum GenderOptions {
        Both,
        Male,
        Female;
    }

}

package fr.elpine.astre.metier.outil;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Recuperation
{
    public static ArrayList<TextField> getAllTextFieldsPn(GridPane gridPane) {
        ArrayList<TextField> textFields = new ArrayList<>();

        for (Node node : gridPane.getChildren()) {
            if (node instanceof FlowPane flowPane) {
                textFields.addAll(getTextFieldsFromFlowPane(flowPane));
            }
        }
        return textFields;
    }

    private static ArrayList<TextField> getTextFieldsFromFlowPane(FlowPane flowPane) {
        ArrayList<TextField> textFields = new ArrayList<>();

        for (Node node : flowPane.getChildren()) {
            if (node instanceof TextField) {
                textFields.add((TextField) node);
            }
        }
        return textFields;
    }


    public static ArrayList<TextField> extractTextFields(TabPane tabPane) {
        ArrayList<TextField> textFields = new ArrayList<>();
        tabPane.getTabs().forEach(tab -> extractTextFieldsFromTab(tab, textFields));
        return textFields;
    }

    private static void extractTextFieldsFromTab(Tab tab, ArrayList<TextField> textFields) {
        Node content = tab.getContent();
        if (content instanceof GridPane) {
            extractTextFieldsFromGridPane((GridPane) content, textFields);
        }
    }

    private static void extractTextFieldsFromGridPane(GridPane gridPane, ArrayList<TextField> textFields) {
        gridPane.getChildren().forEach(node -> {
            if (node instanceof FlowPane) {
                extractTextFieldsFromFlowPane((FlowPane) node, textFields);
            } else if (node instanceof TextField) {
                textFields.add((TextField) node);
            }
        });
    }

    private static void extractTextFieldsFromFlowPane(FlowPane flowPane, ArrayList<TextField> textFields) {
        flowPane.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                textFields.add((TextField) node);
            }
        });
    }
}

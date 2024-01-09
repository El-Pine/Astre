package fr.elpine.astre.metier.outil;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.util.List;

public class Creation
{
    public static TextField creerTextField(String id) {
        TextField textField = new TextField();
        textField.setId(id);
        textField.setPrefSize(50, 26);
        return textField;
    }
    public static FlowPane creerFlowPane() {
        FlowPane fp = new FlowPane();
        fp.setAlignment(Pos.CENTER);
        return fp;
    }
    public static int indexTextFied(List<TextField> listTxt1, List<TextField> listTxt2, List<TextField> listTxt3, TextField txtf)
    {
        if(listTxt1.contains(txtf)) return listTxt1.indexOf(txtf);
        if(listTxt2.contains(txtf)) return listTxt2.indexOf(txtf);
        if(listTxt3.contains(txtf)) return listTxt3.indexOf(txtf);
        return -1;
    }

}

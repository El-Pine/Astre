package fr.elpine.astre.ihm.outil;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class TableCellTextFieldModifiable<S> extends TableCell<S, String>
{
	private TextField textField;

	public void startEdit() {
		if (this.isEditable() && this.getTableView().isEditable() && this.getTableColumn().isEditable() && this.getText() != null) {
			super.startEdit();
			if (this.isEditing()) {
				if (this.textField == null) {
					this.textField = this.createTextField();
					this.textField.setOnAction(event -> {
						if (this.isTextFieldValid())
							this.commitEdit(this.textField.getText());
						else
							this.cancelEdit();
						event.consume();
					});

					this.textField.setOnKeyReleased(event -> {
						if (event.getCode() == KeyCode.ESCAPE) {
							this.cancelEdit();
							event.consume();
						}
					});
				}

				this.setGraphic(this.textField);
				this.textField.setText(this.getText());
				this.textField.selectAll();
				this.textField.requestFocus();
				this.setText(null);
			}

		}
	}

	public TextField createTextField()  { return new TextField(); }
	public boolean   isTextFieldValid() { return true;            }

	public void cancelEdit() {
		super.cancelEdit();
		this.setText(this.getItem());
		this.setGraphic(this.textField = null);
	}

	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else if (this.isEditing()){
			textField.setText(item);
			setGraphic(textField);
		}else{
			setText(item);
			setGraphic(null);
		}
	}
}

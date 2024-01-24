package fr.elpine.astre.ihm.outil;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;

public class TableCellChoiceBoxModifiable<S, T> extends TableCell<S, T>
{
	private ComboBox<T> choiceBox;

	public void startEdit() {
		if (this.isEditable() && this.getTableView().isEditable() && this.getTableColumn().isEditable() && this.getText() != null) {
			super.startEdit();
			if (this.isEditing()) {
				if (this.choiceBox == null)
				{
					this.choiceBox = this.createChoiceBox();
					this.choiceBox.setOnHidden(event -> this.cancelEdit());
					this.choiceBox.setOnAction(event -> {
						if ( choiceBox.getValue() != null && !this.getItem().equals(choiceBox.getValue()) ) {
							this.commitEdit(choiceBox.getValue());
						}
					});
				}

				this.setGraphic(this.choiceBox);
				this.choiceBox.setValue(this.getItem());
				this.choiceBox.show();
				this.setText(null);
			}
		}
	}

	public ComboBox<T> createChoiceBox() { return new ComboBox<>(); }

	public void cancelEdit() {
		super.cancelEdit();

		if (this.choiceBox != null) {
			this.choiceBox.setVisible(false);
			this.choiceBox = null;
		}

		this.setGraphic(null);
		this.setText(this.getItem().toString());
	}

	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else if (this.isEditing()) {
			choiceBox.setValue(item);
			setGraphic(choiceBox);
		} else {
			setText(item.toString());
			setGraphic(null);
		}
	}
}

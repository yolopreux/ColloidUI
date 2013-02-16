package yolopreux.colloid;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CombatTableView {

    Recount recount = Recount.getInstance();
    TableView<CombatData> recountView = new TableView<CombatData>();
    ObservableList<CombatData> data;

    @SuppressWarnings("unchecked")
    public CombatTableView() {
        // @TODO
        @SuppressWarnings("rawtypes")
        TableColumn idCol = new TableColumn("id");
        @SuppressWarnings("rawtypes")
        TableColumn abilityCol = new TableColumn("ability");
        abilityCol.setMinWidth(100);
        @SuppressWarnings("rawtypes")
        TableColumn valueCol = new TableColumn("value");
        valueCol.setMinWidth(100);

        recountView.getColumns().addAll(idCol, abilityCol, valueCol);
        data = FXCollections.observableArrayList();
    }
}

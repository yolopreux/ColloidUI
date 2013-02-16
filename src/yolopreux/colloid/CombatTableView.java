package yolopreux.colloid;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CombatTableView {

    Recount recount = Recount.getInstance();
    TableView<CombatData> recountView = new TableView<CombatData>();

    /**
     * @TODO
     */
    public void init() {
        @SuppressWarnings("rawtypes")
        TableColumn idCol = new TableColumn("id");
        @SuppressWarnings("rawtypes")
        TableColumn abilityCol = new TableColumn("ability");
        abilityCol.setMinWidth(100);
        @SuppressWarnings("rawtypes")
        TableColumn valueCol = new TableColumn("value");
        valueCol.setMinWidth(100);

        idCol.setCellFactory(new PropertyValueFactory<CombatData, Long>("id"));
        abilityCol.setCellFactory(new PropertyValueFactory<CombatData, String>(
                "ability"));
        valueCol.setCellValueFactory(new PropertyValueFactory<CombatData, Double>(
                "value"));
        recountView.getColumns().addAll(idCol, abilityCol);
        recountView.setEditable(false);
        CombatData combat = new CombatData(1, "test", 12.12);
        ObservableList<CombatData> data;
        data = FXCollections.observableArrayList();
        data.add(combat);
        // recountView.setItems(data);
        // recount.setTable(recountView);
    }
}

package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import it.unisa.diem.wordageddong17.service.PrendiClassificheService;
import it.unisa.diem.wordageddong17.util.AlertUtils;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller per la schermata delle classifiche globali (facile, media, difficile).
 */
public class LeaderboardController implements Initializable {

    /**
     * Costruttore predefinito utilizzato da FXMLLoader.
     */
    public LeaderboardController() {}

    @FXML
    private TableView<Classifica> facileTable;
    @FXML
    private TableColumn<Classifica, Integer> facilePosizione;
    @FXML
    private TableColumn<Classifica, String> facileNome;
    @FXML
    private TableColumn<Classifica, Float> facilePunteggio;

    @FXML
    private TableView<Classifica> mediaTable;
    @FXML
    private TableColumn<Classifica, Integer> mediaPosizione;
    @FXML
    private TableColumn<Classifica, String> mediaNome;
    @FXML
    private TableColumn<Classifica, Float> mediaPunteggio;

    @FXML
    private TableView<Classifica> difficileTable;
    @FXML
    private TableColumn<Classifica, Integer> difficilePosizione;
    @FXML
    private TableColumn<Classifica, String> difficileNome;
    @FXML
    private TableColumn<Classifica, Float> difficilePunteggio;

    private ObservableList<Classifica> classificaFacile;
    private ObservableList<Classifica> classificaMedia;
    private ObservableList<Classifica> classificaDifficile;

    private final NavigationService nav = NavigationService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        classificaFacile = FXCollections.observableArrayList();
        classificaMedia = FXCollections.observableArrayList();
        classificaDifficile = FXCollections.observableArrayList();

        setupTable(facileTable, facilePosizione, facileNome, facilePunteggio, classificaFacile);
        setupTable(mediaTable, mediaPosizione, mediaNome, mediaPunteggio, classificaMedia);
        setupTable(difficileTable, difficilePosizione, difficileNome, difficilePunteggio, classificaDifficile);

        caricaClassifiche();
    }

    private void setupTable(TableView<Classifica> table,
                            TableColumn<Classifica, Integer> posizioneCol,
                            TableColumn<Classifica, String> nomeCol,
                            TableColumn<Classifica, Float> punteggioCol,
                            ObservableList<Classifica> data) {
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        punteggioCol.setCellValueFactory(new PropertyValueFactory<>("punti"));

        posizioneCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer posizione, boolean empty) {
                super.updateItem(posizione, empty);
                if (empty || posizione == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    switch (posizione) {
                        case 1 -> setText("🥇");
                        case 2 -> setText("🥈");
                        case 3 -> setText("🥉");
                        default -> setText(posizione.toString());
                    }
                }
            }
        });

        posizioneCol.setCellValueFactory(cellData -> {
            int index = cellData.getTableView().getItems().indexOf(cellData.getValue()) + 1;
            return new ReadOnlyIntegerWrapper(index).asObject();
        });

        table.setItems(data);
        punteggioCol.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(punteggioCol);
    }

    private void caricaClassifiche() {
        nav.setLoading(true);
        PrendiClassificheService pcs = new PrendiClassificheService();

        pcs.setOnSucceeded(e -> {
            nav.setLoading(false);
            Map<LivelloPartita, List<Classifica>> classifiche = pcs.getValue();
            if (classifiche != null) {
                if (classifiche.containsKey(LivelloPartita.FACILE)) {
                    classificaFacile.setAll(classifiche.get(LivelloPartita.FACILE));
                }
                if (classifiche.containsKey(LivelloPartita.MEDIO)) {
                    classificaMedia.setAll(classifiche.get(LivelloPartita.MEDIO));
                }
                if (classifiche.containsKey(LivelloPartita.DIFFICILE)) {
                    classificaDifficile.setAll(classifiche.get(LivelloPartita.DIFFICILE));
                }
            }
        });

        pcs.setOnCancelled(e -> nav.setLoading(false));

        pcs.setOnFailed(e -> {
            nav.setLoading(false);
            AlertUtils.mostraAlert("Errore", "Caricamento delle classifiche fallito.", Alert.AlertType.ERROR);
        });

        pcs.start();
    }

    @FXML
    private void chiudiClassificheOnAction(ActionEvent event) {
        nav.navigateTo(View.HOME);
    }
}

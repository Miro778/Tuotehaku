package fxTuote;

import Tuotehaku.Kauppa;
import Tuotehaku.Tuote;
import Tuotehaku.Tuotehaku;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * @author Miro
 * @version 29.3.2021
 *
 */
public class TuoteMuokkausGUIController implements ModalControllerInterface<Kauppa> {
	// tyhjä
    private Kauppa valittuKauppa;
    
    @FXML private Button ok;
    @FXML private Button peruuta;
    @FXML private TextField tuoteNimi;
    @FXML private TextField tuoteHinta;
    @FXML private TextField tuoteArvostelut;

    @Override
    public void setDefault(Kauppa oletus) {
        valittuKauppa = oletus;   
    }
    
    /**
     * Käsittelee syötteen ja tekee sen perusteella ohjelmaan muutoksia.
     */
    @FXML private void handleOK() {
        if (onkouusi)  paivitaUusi();
        else paivitaVanha();
    }

    /**
     * Ei tee mitään ja palaa pääikkunaan.
     */
    @FXML private void handlePeruuta() {
        ModalController.closeStage(ok);
    }
    
    //==========================================================================================
    // Alla ei käyttöliittymään suoraan liittyvää koodia
    
    private Tuotehaku tuotehaku;
    private Tuote valittuTuote;
    private boolean onkouusi;
    
    /**
     * Asettaa tiedot-palkkiin viimeksi valitun tuotteen tiedot
     */
    private void nykyisenTiedot() {
        tuoteNimi.setText(valittuTuote.getNimi());
        tuoteHinta.setText(Double.toString(valittuTuote.getHinta()));
        tuoteArvostelut.setText(Double.toString(valittuTuote.getArvostelut()));
    }
    
    /**
     * Lisää tuotteen tuotteiden listaan ja antaa sille tiedot syötteiden perusteella. 
     */
    private void paivitaUusi() {
        try {
        double hinta = Double.parseDouble(tuoteHinta.getText());
        if ((Double.parseDouble(tuoteArvostelut.getText()) > 0.99) & (Double.parseDouble(tuoteArvostelut.getText()) < 5.01) & hinta > 0) {
            Tuote lisattava = new Tuote(tuoteNimi.getText(),Double.parseDouble(tuoteHinta.getText()),Double.parseDouble(tuoteArvostelut.getText()),valittuKauppa.getTunnusNro());
            lisattava.rekisteroi();
            tuotehaku.getTuotteet().lisaa(lisattava);
            ModalController.closeStage(ok);
          }
        } catch (NumberFormatException nfe) { Dialogs.showMessageDialog("Virheellinen syöte. Tarkasta antamasi tiedot.");
      }
          
        
    }
    
    /**
     * Päivittää valitun kaupan tiedot annetun syötteen perusteella.
     */
    private void paivitaVanha() {    
        try {
        if ((Double.parseDouble(tuoteArvostelut.getText()) > 0.99) & (Double.parseDouble(tuoteArvostelut.getText()) < 5.01) & Double.parseDouble(tuoteHinta.getText()) > 0) {
            valittuTuote.muutaTiedot(tuoteNimi.getText(),Double.parseDouble(tuoteHinta.getText()),Double.parseDouble(tuoteArvostelut.getText()));
            ModalController.closeStage(ok);
        }
        } catch (NumberFormatException nfe) { Dialogs.showMessageDialog("Virheellinen syöte. Tarkasta antamasi tiedot.");
        }
    }
    
    @Override
    public Kauppa getResult() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }
    /**
     * Hakee pääikkunan tuotehaun
     * @param tuotehaku pääikkunan tuotehaku
     * @param valittuTuote tuote jota halutaan käsitellä
     * @param onkouusi ollaanko lisäämässä uutta tuotetta vai muokkaamassa jo olemassa olevaa
     */
    public void setTuotehaku(Tuotehaku tuotehaku,Tuote valittuTuote,boolean onkouusi) {
        this.tuotehaku = tuotehaku;
        this.valittuTuote = valittuTuote;
        this.onkouusi = onkouusi;
        if (!onkouusi) nykyisenTiedot();
    }

    
}


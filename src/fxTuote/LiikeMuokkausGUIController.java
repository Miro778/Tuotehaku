package fxTuote;

import Tuotehaku.Kauppa;
import Tuotehaku.SailoException;
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
public class LiikeMuokkausGUIController implements ModalControllerInterface<Kauppa>  {
	
    private Kauppa valittuKauppa;
    
    @FXML private Button ok;
    @FXML private Button peruuta;
    @FXML private TextField kauppaNimi;
    @FXML private TextField kauppaTyyppi;
    @FXML private TextField kauppaOsoite;
    @FXML private TextField kauppaSivut;
    @FXML private TextField kauppaArvostelut;
    
    @Override
    public void setDefault(Kauppa oletus) {
        valittuKauppa = oletus;    
    }    

    /**
     * @throws SailoException jos uuden kaupan lisäämisessä ongelmia
     */
    @FXML public void handleOK() throws SailoException {
        if (onkouusi) paivitaUusi();        
        else paivitaVanha();        
    }
    
    @FXML private void handlePeruuta() {
        ModalController.closeStage(ok);
    }
    
    
    //======================================================================================
    // Tästä alaspäin ei käyttöliittymään suoraan liittyvää koodia
    
    private Tuotehaku tuotehaku;
    private boolean onkouusi;
    
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
     * Hakee tuotehaun toisesta GUIControllerista
     * @param tuotehaku ohjelman tuotehaku-luokka
     * @param onkouusi ollaanko lisäämässä uutta kauppaa vai muokkaamassa aiempaa
     */
    public void setTuotehaku(Tuotehaku tuotehaku, boolean onkouusi) {
        this.tuotehaku = tuotehaku;
        this.onkouusi = onkouusi;
        if (!onkouusi) nykyisenTiedot();   
    }

    
    /**
     * Asettaa tekstikenttiin viimeksi käyttäjän valitseman kaupan tiedot muokkausta varten
     */
    private void nykyisenTiedot() {
        kauppaNimi.setText(valittuKauppa.getNimi());
        kauppaTyyppi.setText(valittuKauppa.getTyyppi());
        kauppaOsoite.setText(valittuKauppa.getOsoite());
        kauppaSivut.setText(valittuKauppa.getSivut());
        kauppaArvostelut.setText(Double.toString(valittuKauppa.getArvostelut()));       
    }
    
    /**
     * Lisää uuden kaupan annettujen tietojen perusteella. 
     * @throws SailoException jos uuden kaupan lisäämisessä ongelmia
     */
    private void paivitaUusi() throws SailoException {
         try {   
            if ((Double.parseDouble(kauppaArvostelut.getText()) > 0.99) & (Double.parseDouble(kauppaArvostelut.getText()) < 5.01)) {
              Kauppa lisattava = new Kauppa(kauppaNimi.getText(),kauppaTyyppi.getText(),kauppaOsoite.getText(),kauppaSivut.getText(),Double.parseDouble(kauppaArvostelut.getText()));
              tuotehaku.getKaupat().lisaa(lisattava);
              tuotehaku.getKaupat().muutos();
              ModalController.closeStage(ok);
            } else {
                Dialogs.showMessageDialog("Virheellinen syöte. Tarkasta antamasi tiedot.");     
            } 
         } catch (NumberFormatException nfe) {
            Dialogs.showMessageDialog("Virheellinen syöte. Tarkasta antamasi tiedot.");    
         }        
    }
    
    /** Päivittää valitun kaupan tiedot annetuilla tiedoilla
     */
    private void paivitaVanha() {      
        try {
            if ((Double.parseDouble(kauppaArvostelut.getText()) > 0.99) & (Double.parseDouble(kauppaArvostelut.getText()) < 5.01)) {
            valittuKauppa.muutaTiedot(kauppaNimi.getText(),kauppaTyyppi.getText(),kauppaOsoite.getText(),kauppaSivut.getText(),Double.parseDouble(kauppaArvostelut.getText()));
            tuotehaku.getKaupat().muutos();
            ModalController.closeStage(ok);
        } else {
                        Dialogs.showMessageDialog("Virheellinen syöte. Tarkasta antamasi tiedot."); 
                        }
                    } catch (NumberFormatException nfe) { 
                Dialogs.showMessageDialog("Virheellinen syöte. Tarkasta antamasi tiedot.");
            }
        }
    }


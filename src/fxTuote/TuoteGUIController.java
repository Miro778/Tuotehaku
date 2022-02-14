package fxTuote;

import Tuotehaku.Kauppa;
import Tuotehaku.SailoException;
import Tuotehaku.Tuotehaku;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

/**
 * @author Miro
 * @version 10.4.2021
 *
 */


public class TuoteGUIController implements Initializable {
    
    @FXML private Button siirryValikoimaan;
    @FXML private MenuItem tallenna;
    @FXML private MenuItem lopeta;
    @FXML private MenuItem uusiKauppa;
    @FXML private MenuItem poistaKauppa;
    @FXML private MenuItem muokkaaKauppaa;
    @FXML private MenuItem tietoa;
    @FXML private MenuItem apua;
    @FXML private ListChooser<Kauppa> kaupatLista;
    @FXML private StringGrid<Kauppa> kauppaTiedot;
    @FXML private TextField hakuKentta;
    @FXML private Button hakuPainike;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            alusta();
        } catch (SailoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    
    @FXML private void valitseListalta() {
        if (kaupatLista.getSelectedObject() != null) {
            valittuKauppa = kaupatLista.getSelectedObject();           
            asetaTiedot();
            }
    }
    
    @FXML private void handleHaeKauppaa() {
        kaupanHaku(hakuKentta.getText());
    }

    @FXML private void handleSiirryValikoimaan() {        
        ModalController.<Kauppa,ValikoimaGUIController>showModal(ValikoimaGUIController.class.getResource("TuotevalikoimaGUIView.fxml"), "Tuotevalikoima", null, valittuKauppa,ctrl->{
            try {
                ctrl.setTuotehaku(tuotehaku);
            } catch (SailoException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
    @FXML private void handleTallenna() {
        tallennus();
        Dialogs.showMessageDialog("Muutokset tallennettu");
    }

    @FXML private void handleLopeta() {
        ModalController.closeStage(siirryValikoimaan);
    }
    
    @FXML private void handleUusiKauppa() {
        onkouusi = true;
        int kauppoja = tuotehaku.getKauppoja();
        ModalController.<Kauppa,LiikeMuokkausGUIController>showModal(LiikeMuokkausGUIController.class.getResource("liikemuokkausGUIView.fxml"), "Tiedot", null, valittuKauppa,ctrl->ctrl.setTuotehaku(tuotehaku,onkouusi));
        if (tuotehaku.getKauppoja() > kauppoja) kaupatLista.add(tuotehaku.getKaupat().anna(tuotehaku.getKaupat().getLkm()-1).getNimi(),tuotehaku.getKaupat().anna(tuotehaku.getKaupat().getLkm()-1));
    }
    @FXML private void handlePoistaKauppa() {
        boolean poistetaanko = Dialogs.showQuestionDialog("Huom:", "Haluatko varmasti poistaa valitun kaupan?", "Poista", "Peruuta");
        if (poistetaanko) poistaKauppa();
        paivitaLista();
    }

    @FXML private void handleMuokkaaKauppaa() {
        onkouusi = false;
        ModalController.<Kauppa,LiikeMuokkausGUIController>showModal(LiikeMuokkausGUIController.class.getResource("liikemuokkausGUIView.fxml"), "Tiedot", null, valittuKauppa,ctrl->ctrl.setTuotehaku(tuotehaku,onkouusi));
        
        
    }
    @FXML private void handleTietoa() {
        var resurssi = TiedotGUIController.class.getResource("tiedotGUIView.fxml");
        ModalController.showModal(resurssi, "Tiedot", null, "");
        
    }
    @FXML private void handleApua() {
        try {
            Desktop.getDesktop().browse(new URL("https://tim.jyu.fi/view/kurssit/tie/ohj2/2021k/ht/mirmatok#8us2CVmqdnqq").toURI());
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    //===========================================================================================    
    // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia  
    
    private Kauppa valittuKauppa;
    private boolean onkouusi = false;
    private Tuotehaku tuotehaku = new Tuotehaku();

    
    /** Tekee tarvittavat muut alustukset ohjelman käynnistyessä. 
     * @throws SailoException jos tiedoston lukemisessa ongelmia
     */
    public void alusta() throws SailoException {
        kaupatLista.clear();
        tuotehaku.getKaupat().lueTiedostosta("kaupat.dat");
        tuotehaku.getTuotteet().lueTiedostosta("tuotteet.dat");
        
        
        for (Kauppa kauppa : tuotehaku.getKaupat()) {
            kaupatLista.add(kauppa.getNimi(),kauppa);
        }
    }
        
    /**
     * Tallentaa kauppoihin ja tuotteisiin tehdyt muutokset tiedostoon.
     * @return virheviesti jos ongelmia esiintyy.
     */
    public String tallennus() {
        try {
            tuotehaku.getKaupat().tallenna();
            
            tuotehaku.getTuotteet().lueTiedostosta("tuotteet.dat");
            
            int n = 0;
            int[] kauppojenID = new int[tuotehaku.getKauppoja()];
            for (Kauppa kauppa : tuotehaku.getKaupat()) {
                kauppojenID[n] = kauppa.getTunnusNro();
                n++;
            }
            tuotehaku.getTuotteet().tallenna(kauppojenID);
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    /**
     * Poistaa valitun kaupan kauppojen listalta ja kaikki kaupan tuotteet tuotteiden listalta
     */
    private void poistaKauppa() {
        int poistettavaid = valittuKauppa.getTunnusNro();
        tuotehaku.getTuotteet().poistatuotteet(poistettavaid);
        tuotehaku.getKaupat().poista(poistettavaid);
        }
    
    /**
     * Palauttaa listalta valitun kaupan
     * @return listalta viimeksi valittu kauppa
     */
    public Kauppa getValittu() {
        return valittuKauppa;
    }    
        
    /**
     * Asettaa tiedot-palkkiin viimeksi käyttäjän valitseman kaupan tiedot.
     */
    private void asetaTiedot() {
        kauppaTiedot.set("Liike: " + valittuKauppa.getNimi(), 0, 0);
        kauppaTiedot.set("Tyyppi: " + valittuKauppa.getTyyppi(), 1, 0);
        kauppaTiedot.set("Osoite: " + valittuKauppa.getOsoite(), 2, 0);
        kauppaTiedot.set("Verkkosivut: " + valittuKauppa.getSivut(), 3, 0);
        kauppaTiedot.set("Arvostelut: " + valittuKauppa.getArvostelut() + "/5", 4, 0);       
    }

    
    /**
     * Päivittää käyttöliittymän kauppojen listan vastaamaan kauppojen listaa
     */
    private void paivitaLista() {
        kaupatLista.clear();
               for (Kauppa kauppa : tuotehaku.getKaupat()) {
                   if (kauppa != null) {
            kaupatLista.add(kauppa.getNimi(),kauppa);
        }}
        
    }
    
    /**
     * Päivittää listan vastaamaan sellaisia kauppoja, joiden nimi vastaa hakukenttään asetettua syötettä.
     * @param hakusyote syote jonka perusteella kauppoja haetaan
     */
    private void kaupanHaku(String hakusyote) {
        kaupatLista.clear();
        String kaupanNimi;
        String haku = hakusyote.toLowerCase();
        int loytyneita = 0;
        for (Kauppa kauppa : tuotehaku.getKaupat()) {
            if (kauppa != null) {
                kaupanNimi = kauppa.getNimi().toLowerCase();
                if (kaupanNimi.contains(haku)) {
                    kaupatLista.add(kauppa.getNimi(),kauppa);
                    loytyneita++;
                }
    }
        }
        if (loytyneita == 0) Dialogs.showMessageDialog("Syötteellä ei löytynyt tuloksia!");
}


}


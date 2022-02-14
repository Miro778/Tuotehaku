package fxTuote;

import Tuotehaku.Tuote;
import Tuotehaku.Tuotehaku;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import Tuotehaku.Kauppa;
import Tuotehaku.SailoException;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * @author Miro
 * @version 29.3.2021
 *
 */
public class ValikoimaGUIController implements ModalControllerInterface<Kauppa> {
	    
    @FXML private BorderPane tausta;
    @FXML private MenuItem tallenna;
    @FXML private MenuItem lopeta;
    @FXML private MenuItem uusiTuote;
    @FXML private MenuItem poistaTuote;
    @FXML private MenuItem muokkaaTuotetta;
    @FXML private MenuItem tietoa;
    @FXML private MenuItem apua;
    @FXML private Label kaupanNimi;
    @FXML private ListChooser<Tuote> tuoteLista;
    @FXML private StringGrid<Tuote> tuoteTiedot;
    @FXML private ChoiceBox<String> lajitteluValinta;
    @FXML private TextField hakuKentta;


    @Override
    public void setDefault(Kauppa oletus) {
            valittuKauppa = oletus;              
    }
    
    @FXML private void handleHaeTuotetta() {
        tuotteenHaku(hakuKentta.getText());
    }

    @FXML private void valitseValikoimasta() {
        if (tuoteLista.getSelectedObject() != null) {
        valittuTuote = tuoteLista.getSelectedObject();
        asetaTiedot();
        }
    }
    
    @FXML private void lajitteleLista() {
        if (!(lajitteluValinta.getSelectionModel().isEmpty())) {
         if (lajitteluValinta.getSelectionModel().getSelectedItem().equals("Halvin ensin")) lajitteleHalvinEnsin();
         else if (lajitteluValinta.getSelectionModel().getSelectedItem().equals("Kallein ensin")) lajitteleKalleinEnsin();
         else if (lajitteluValinta.getSelectionModel().getSelectedItem().equals("Suosituin ensin")) lajitteleSuosituinEnsin();
         else if (lajitteluValinta.getSelectionModel().getSelectedItem().equals("A->Z")) lajitteleAakkosjarjestys();
        }
    }    

    @FXML private void handleTallenna() throws SailoException {
        int n = 0;
        int[] kauppojenID = new int[tuotehaku.getKauppoja()];
        for (Kauppa kauppa : tuotehaku.getKaupat()) {
            kauppojenID[n] = kauppa.getTunnusNro();
            n++;
        }
        tuotehaku.getTuotteet().tallenna(kauppojenID);
        Dialogs.showMessageDialog("Muutokset tallennettu");
    }
    @FXML private void handleLopeta() {
        tuoteLista.clear();
        tuotehaku.nollaaTuotteet();
        ModalController.closeStage(tausta);
    }
    @FXML private void handleUusiTuote() {
        onkouusi = true;
        ModalController.<Kauppa,TuoteMuokkausGUIController>showModal(TuoteMuokkausGUIController.class.getResource("tuotemuokkausGUIView.fxml"), "Tiedot", null, valittuKauppa,ctrl->ctrl.setTuotehaku(tuotehaku,valittuTuote,onkouusi));
        paivitaLista();
    }

    @FXML private void handlePoistaTuote() {
        boolean poistetaanko = Dialogs.showQuestionDialog("Huom:", "Haluatko varmasti poistaa valitun tuotteen?", "Poista", "Peruuta");
        if (poistetaanko) poistaTuote();
        paivitaLista();
    }

    @FXML private void handleMuokkaaTuotetta() {
        onkouusi = false;
        ModalController.<Kauppa,TuoteMuokkausGUIController>showModal(TuoteMuokkausGUIController.class.getResource("tuotemuokkausGUIView.fxml"), "Tiedot", null, valittuKauppa,ctrl->ctrl.setTuotehaku(tuotehaku,valittuTuote,onkouusi));
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
    
    private Tuote valittuTuote;
    private Tuotehaku tuotehaku;
    private Kauppa valittuKauppa;
    private boolean onkouusi = false;
    private int tuotteitaKaupassa = 0;
    ObservableList<String> lajitteluVaihtoehdot = FXCollections.observableArrayList("A->Z","Halvin ensin","Kallein ensin","Suosituin ensin");
    
    /**
     * Tekee tarvittavat alustukset siirryttäessä tuotevalikoimaan
     * @throws SailoException jos tuotteiden tiedoston lukemisessa on ongelmia
     */
    private void alusta() throws SailoException {
        tuoteLista.clear();
        tuotehaku.getTuotteet().lueTiedostosta("tuotteet.dat");
        kaupanNimi.setText(valittuKauppa.getNimi());
        lajitteluValinta.setItems(lajitteluVaihtoehdot);
        tuotteitaKaupassa = 0;
        for (Tuote tuote : tuotehaku.getTuotteet()) {
            if (tuote.kauppaid == valittuKauppa.getTunnusNro()) {
            tuoteLista.add(tuote.getNimi(),tuote);
            tuotteitaKaupassa++;
            }
        }

    }

    /**
     * Päivittää käyttöliittymän listan vastaamaan tuotteiden listaa
     */
    private void paivitaLista() {
        tuoteLista.clear();
        tuotteitaKaupassa = 0;
        for (Tuote tuote : tuotehaku.getTuotteet()) {
            if (tuote.kauppaid == valittuKauppa.getTunnusNro()) {
                tuoteLista.add(tuote.getNimi(),tuote);
                tuotteitaKaupassa++;
            }
        }
        
    }
    
    /**
     * Päivittää käyttöliittymän tiedot-palkkiin viimeksi valitun tuotteen tiedot
     */
    private void asetaTiedot() {
        tuoteTiedot.set("Tuote: " + valittuTuote.getNimi(), 0, 0);
        tuoteTiedot.set("Hinta: " + valittuTuote.getHinta() + "€", 1, 0);
        tuoteTiedot.set("Arvostelut: " + valittuTuote.getArvostelut() + "/5", 2, 0);        
    }
    
    /**
     * Lajittelee tuotteiden listan halvimmasta kalleimpaan.
     */
    private void lajitteleHalvinEnsin() {
        tuoteLista.clear();

        Tuote[] temp = new Tuote[tuotehaku.getTuotteet().getLkm()];
        double halvinhinta = Double.MAX_VALUE;
        Tuote apu = new Tuote("temp",99999, 5.0,0);
        Tuote halvintuote = apu;
        int n = 0;
        int pieninIndeksi = 0;
        for (Tuote tuote : tuotehaku.getTuotteet()) {
            temp[n] = tuote;
            n++;
        }
        for (int i = 0;i < tuotteitaKaupassa;i++) {
            int k = 0;
        for (Tuote tuote1 : temp)
        {
           if (tuote1.getHinta() < halvinhinta & tuote1.getKauppaid() == valittuKauppa.getTunnusNro()) {
               halvinhinta = tuote1.getHinta();
               halvintuote = tuote1;
               pieninIndeksi = k;
           }
           k++;
        }
        tuoteLista.add(halvintuote.getNimi(),halvintuote);
        temp[pieninIndeksi] = apu;
        halvinhinta = Double.MAX_VALUE;
        }
    }
    
    /**
     * Lajittelee tuotteiden listan kalleimmasta halvimpaan.
     */
    private void lajitteleKalleinEnsin() {
        tuoteLista.clear();

        Tuote[] temp = new Tuote[tuotehaku.getTuotteet().getLkm()];
        double kalleinhinta = Double.MIN_VALUE;
        Tuote apu = new Tuote("temp",0.0, 5.0,0);
        Tuote kalleintuote = apu;
        int n = 0;
        int suurinIndeksi = 0;
        for (Tuote tuote : tuotehaku.getTuotteet()) {
            temp[n] = tuote;
            n++;
        }
        for (int i = 0;i < tuotteitaKaupassa;i++) {
            int k = 0;
        for (Tuote tuote1 : temp)
        {
           if (tuote1.getHinta() > kalleinhinta & tuote1.getKauppaid() == valittuKauppa.getTunnusNro()) {
               kalleinhinta = tuote1.getHinta();
               kalleintuote = tuote1;
               suurinIndeksi = k;
           }
           k++;
        }
        tuoteLista.add(kalleintuote.getNimi(),kalleintuote);
        temp[suurinIndeksi] = apu;
        kalleinhinta = Double.MIN_VALUE;
        }
    }
    
    /**
     * Lajittelee tuotteet arvostelujen perusteella parhaimmasta huonoimpaan.
     */
    private void lajitteleSuosituinEnsin() {
        tuoteLista.clear();

        Tuote[] temp = new Tuote[tuotehaku.getTuotteet().getLkm()];
        double parasArvostelu = Double.MIN_VALUE;
        Tuote apu = new Tuote("temp",0.0, 0.0,0);
        Tuote suosituinTuote = apu;
        int n = 0;
        int suosituinIndeksi = 0;
        for (Tuote tuote : tuotehaku.getTuotteet()) {
            temp[n] = tuote;
            n++;
        }
        for (int i = 0;i < tuotteitaKaupassa;i++) {
            int k = 0;
        for (Tuote tuote1 : temp)
        {
           if (tuote1.getArvostelut() > parasArvostelu & tuote1.getKauppaid() == valittuKauppa.getTunnusNro()) {
               parasArvostelu = tuote1.getArvostelut();
               suosituinTuote = tuote1;
               suosituinIndeksi = k;
           }
           k++;
        }
        tuoteLista.add(suosituinTuote.getNimi(),suosituinTuote);
        temp[suosituinIndeksi] = apu;
        parasArvostelu = Double.MIN_VALUE;
        }
    }
    
    /**
     * Lajittelee tuotteet aakkosjärjestyksessä.
     */
    private void lajitteleAakkosjarjestys() {
        tuoteLista.clear();

        Tuote[] temp = new Tuote[tuotehaku.getTuotteet().getLkm()];
        int suurinabc = Integer.MAX_VALUE;
        Tuote apu = new Tuote("zemp",0.0, 0.0,0);
        Tuote seuraavaTuote = apu;
        int n = 0;
        int pieninIndeksi = 0;
        for (Tuote tuote : tuotehaku.getTuotteet()) {
            temp[n] = tuote;
            n++;
        }
        for (int i = 0;i < tuotteitaKaupassa;i++) {
            int k = 0;
        for (Tuote tuote1 : temp)
        {
           if (tuote1.getNimi().charAt(0) < suurinabc & tuote1.getKauppaid() == valittuKauppa.getTunnusNro()) {
               suurinabc = tuote1.getNimi().charAt(0);
               seuraavaTuote = tuote1;
               pieninIndeksi = k;
           }
           k++;
        }
        tuoteLista.add(seuraavaTuote.getNimi(),seuraavaTuote);
        temp[pieninIndeksi] = apu;
        suurinabc = Integer.MAX_VALUE;
        }
    }
    
    /**
     * Päivittää tuotteiden listalle vain syötettä vastaavat tuotteet
     */
    private void tuotteenHaku(String hakusyote) {
        tuoteLista.clear();
        String tuotteenNimi;
        String haku = hakusyote.toLowerCase();
        for (Tuote tuote : tuotehaku.getTuotteet()) {
            if (tuote != null) {
                if (tuote.getKauppaid() == valittuKauppa.getTunnusNro()) {
                    tuotteenNimi = tuote.getNimi().toLowerCase();
                    if (tuotteenNimi.contains(haku)) {
                        tuoteLista.add(tuote.getNimi(),tuote);
                    }
                }
            }
        }
    }
    
    /**
     * Poistaa valitun tuotteen tuotteiden listalta
     */
    private void poistaTuote() {
            int poistuoteid = valittuTuote.getTuoteid();
            tuotehaku.getTuotteet().poistaTuote(poistuoteid);
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
     * @throws SailoException jos tuotteiden alustamisessa ongelmia
     */
    public void setTuotehaku(Tuotehaku tuotehaku) throws SailoException {
        this.tuotehaku = tuotehaku;
        alusta();   
    }


}

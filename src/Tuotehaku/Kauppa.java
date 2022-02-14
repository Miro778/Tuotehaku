package Tuotehaku;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Kauppa-luokka sovelluksen kauppoja varten.
 * @author Miro
 * @version 27.3.2021
 *
 */

public class Kauppa {
    
    private int kauppaid;
    private String nimi = "";
    private String osoite = "";
    private String tyyppi = "";
    private String verkkosivut = "";
    private double arvostelut = 3;
    
    private static int seuraavaNro    = 1;
    
    /** Palauttaa kaupan nimen
    * @return kaupan nimi
    * <pre name="test">
    * Kauppa XXL = new Kauppa();
    * XXL.vastaaXXL();
    * XXL.getNimi() === "XXL";
    * </pre>
    */
    public String getNimi() {
        return this.nimi;
    }
    
    /** Apumetodi, jolla saadaan asetettua testiarvot kaupalle.
     */
    public void vastaaXXL() {
        this.kauppaid = 2;
        this.nimi = "XXL";
        this.osoite = "Ahjokatu 3-5";
        this.tyyppi = "Urheilu";
        this.verkkosivut = "xxl.fi";
        this.arvostelut = 4.0;
    }
    
    /** Palauttaa kaupan id:n
     * @return kaupan id
     */
    public int getTunnusNro() {
        return kauppaid;
    }
    
    /**
    * Asettaa tunnusnumeron ja samalla varmistaa että
    * seuraava numero on aina suurempi kuin tähän mennessä suurin.
    * @param nr asetettava tunnusnumero
    */
    private void setTunnusNro(int nr) {
         kauppaid = nr;
        if (kauppaid >= seuraavaNro) seuraavaNro = kauppaid + 1;
    }
    
    /** Palauttaa kaupan osoitteen
     * @return kaupan osoite
     */
    public String getOsoite() {
        return this.osoite;
    }
    
    /** Palauttaa kaupan tyypin
     * @return kaupan tyyppi
     */
    public String getTyyppi() {
        return this.tyyppi;
    }
    
    /** Palauttaa kaupan nettisivut
     * @return kaupan verkkosivut
     */
    public String getSivut() {
        return this.verkkosivut;
    }
    
    /** Palauttaa kaupan arvostelut
     * @return kaupan arvostelut
     */
    public double getArvostelut() {
        return this.arvostelut;
    }
    
    /** Tulostetaan kaupan tiedot. 
    * @param out tietovirta johon tulostetaan.
    */
    public void tulosta(PrintStream out) {
          out.println(String.format("%03d", kauppaid, 3) + "  " + nimi + "  "+ tyyppi + " " + arvostelut);
          out.println("  " + osoite + "  " + verkkosivut);
    }
    
    /** Tulostetaan kaupan tiedot. 
    * @param os tietovirta johon tulostetaan.
    */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /** Päivittää kaupan tiedot vastaamaan annettuja parametrejä
     * @param nimi1 uusi nimi kaupalle
     * @param tyyppi1 uusi tyyppi kaupalle
     * @param osoite1 uusi osoite kaupalle
     * @param verkkosivut1 uudet verkkosivut string-muotoisena tietovirtana.
     * @param arvostelut1 uudet arvostelut kaupalle
     */
    public void muutaTiedot(String nimi1,String tyyppi1,String osoite1,String verkkosivut1,double arvostelut1)
    {
        this.nimi = nimi1;
        this.tyyppi = tyyppi1;
        this.osoite = osoite1;
        this.verkkosivut = verkkosivut1;
        this.arvostelut = arvostelut1;
    }
    
    /** Asettaa kaupalle parametreissä annetut tiedot.
     * @param nimi kaupan nimi
     * @param tyyppi kaupan tyyppi
     * @param osoite kaupan osoite
     * @param verkkosivut kaupan verkkosivut
     * @param arvostelut kaupan arvostelut
     */
    public Kauppa(String nimi,String tyyppi,String osoite,String verkkosivut,double arvostelut)
    {
        this.nimi = nimi;
        this.tyyppi = tyyppi;
        this.osoite = osoite;
        this.verkkosivut = verkkosivut;
        this.arvostelut = arvostelut;
        
    }
    
    /** Kauppa tyhjillä/muuttamattomilla tiedoilla
     */
    public Kauppa() {
        // Tällä muodostajalla kaupan attribuuteille asetetaan oletusarvot.
    }
    
    /** Antaa seuraavalle kaupalle oman id:n
    * @return kaupan id
    * @example
    * <pre name="test">
    * Kauppa a = new Kauppa();
    * a.getTunnusNro() === 0;
    * a.rekisteroi();
    * Kauppa b = new Kauppa();
    * b.rekisteroi();
    * int n1 = a.getTunnusNro();
    * int n2 = b.getTunnusNro();
    * n1 === n2-1;
    * </pre>
    */
    public int rekisteroi() {
        this.kauppaid = seuraavaNro;
        seuraavaNro++;
        return this.kauppaid;
    }
    
    /** Testiohjelma
    * @param args ei käytössä.
    */
    public static void main(String[] args) {
        Kauppa Gigantti = new Kauppa("Gigantti","Elektroniikka","Ahjokatu 11","gigantti.fi",3.5),
        XXL = new Kauppa("XXL","Urheilu","Ahjokatu 3-5","xxl.fi",4.0);
        Gigantti.rekisteroi();
        XXL.rekisteroi();
        
        Gigantti.tulosta(System.out);
        XXL.tulosta(System.out);
        

    }
    
    /** Selvittää kaupan tiedot annetusta merkkijonosta.
    * Pitää huolen että seuraava tunnusnro on aiempaa suurempi.
    * @param rivi = rivi josta kaupan tiedot annetaan.
    */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb,'|',getTunnusNro()));
        nimi = Mjonot.erota(sb, '|', nimi);
        tyyppi = Mjonot.erota(sb, '|', tyyppi);
        osoite = Mjonot.erota(sb, '|', osoite);
        verkkosivut = Mjonot.erota(sb, '|', verkkosivut);
        arvostelut = Mjonot.erota(sb, '|', arvostelut);
        
    }
    
    /** Palauttaa kaupan tiedot merkkijonona, jonka voi tallentaa tiedostoon.
    * @return tolppaeroteltuna merkkijonona.
    */
    @Override
    public String toString() {
        return "" + getTunnusNro() + "|" + getNimi() + "|" + getTyyppi() + "|" + getOsoite() + "|" + getSivut() + "|" + getArvostelut(); 
    }

}

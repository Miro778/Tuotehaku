package Tuotehaku;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/** Tuote-luokka sovelluksen tuotteita varten.
 * 
 * @author Miro
 * @version 27.3.2021
 *
 */

public class Tuote {
    
    /**
     * Tunnistaa, mihin kauppaan kyseinen tuote kuuluu.
     */
    public int kauppaid;
    
    private int tuoteid;
    private String nimi = "";
    private double hinta = 0;
    private double arvostelut = 1;
    
    private static int seuraavaNro = 1;
    
    /**
     * Muodostaja oletusarvoilla
     */
    public Tuote() {
        // Tyhjä
    }
    
    /**
     * Muodostaja parametreinä annetuilla arvoilla
     * @param nimi tuotteelle asetettava nimi
     * @param hinta tuotteelle asetettava hinta
     * @param arvostelut tuotteelle asetettavat arvostelut
     * @param kauppaid määrittää relaation siitä, mihin kauppaan tuote kuuluu. 
     */
    public Tuote(String nimi,double hinta, double arvostelut,int kauppaid) {
        this.nimi = nimi;
        this.hinta = hinta;
        this.arvostelut = arvostelut;
        this.kauppaid = kauppaid;
    }
    
    /** Päivittää tuotteen tiedot vastaamaan annettuja parametrejä
     * @param nimi1 uusi nimi tuotteelle
     * @param hinta1 uusi hinta tuotteelle
     * @param arvostelut1 uudet arvostelut tuotteelle
     */
    public void muutaTiedot(String nimi1,double hinta1,double arvostelut1)
    {
        this.nimi = nimi1;
        this.hinta = hinta1;
        this.arvostelut = arvostelut1;
    }
    
    /** Alustetaan tietyn kaupan tuote. 
    * @param kauppaid tietyn kaupan viite.
    */
    public Tuote(int kauppaid) {
        this.kauppaid = kauppaid;
    }
    
    /** Apumetodi, jolla saadaan täytettyä testiarvot tuotteelle
    * @param nro viite kauppaan, jonka tuotteesta on kyse
    */
    public void vastaaGhostSpyoraa(int nro) {
        kauppaid = nro;
        nimi = "Ghost Sähköpyörä";
        hinta = 3499;
        arvostelut = 3.1;
    }
    
    /** Tulostetaaan tuotteen tiedot.
    * @param out tietovirta johon tulostetaan.
    */ 
    public void tulosta(PrintStream out) {
        out.println(nimi + " " + hinta + " " + arvostelut);
    }
    
    /** Tulostetaan kaupan tiedot.
    * @param os tietovirta johon tulostetaan.
    */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /** Antaa tuotteelle seuraavan id:n
    * @return tuotteen uusi tuoteid
    * @example
    * <pre name="test">
    *   Tuote tuote1 = new Tuote();
    *   tuote1.getTuoteid() === 0;
    *   tuote1.rekisteroi();
    *   Tuote tuote2 = new Tuote();
    *   tuote2.rekisteroi();
    *   int n1 = tuote1.getTuoteid();
    *   int n2 = tuote2.getTuoteid();
    *   n1 === n2-1;
    * </pre>
    */
    public int rekisteroi() {
        tuoteid = seuraavaNro;
        seuraavaNro++;
        return tuoteid;
    }
    
    /** Palautetaan tuotteen oma id
    * @return tuotteen id
    */
    public int getTuoteid() {
        return this.tuoteid;
    }
    
    /** Palautetaan tuotteen kaupan id
    * @return kaupan id, johon tuote kuuluu
    */
    public int getKauppaid() {
        return this.kauppaid;
    }
    
    /** Palautetaan tuotteen nimi
    * @return tuotteen id
    */
    public String getNimi() {
        return this.nimi;
    }
    
    /** Palautetaan tuotteen hinta
    * @return tuotteen hinta
    */
    public double getHinta() {
        return this.hinta;
    }
    
    /** Palautetaan tuotteen arvostelut
    * @return tuotteen arvostelut
    */
    public double getArvostelut() {
        return this.arvostelut;
    }
    
    /** Selvittää tuotteen tiedot annetusta merkkijonosta.
    * Pitää huolen että seuraava tunnusnro on aiempaa suurempi.
    * @param rivi = rivi josta kaupan tiedot annetaan.
    * <pre name="test">
    * Tuote t = new Tuote();
    * t.parse(" 3 | Pyora | 599 | 4.3 ");
    * t.getKauppaid() === 3;
    * t.getNimi() === "Pyora";
    * t.getHinta() ~~~ 599.0;
    * t.getArvostelut() ~~~ 4.3;
    * t.toString() === "3|Pyora|599.0|4.3";
    * </pre>
    */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        kauppaid = Mjonot.erota(sb, '|', kauppaid);
        nimi = Mjonot.erota(sb, '|', nimi);
        hinta = Mjonot.erota(sb, '|', hinta);
        arvostelut = Mjonot.erota(sb, '|', arvostelut);
        
    }
    
    /** Palauttaa tuotteen tiedot merkkijonona, jonka voi tallentaa tiedostoon.
    * @return tolppaeroteltuna merkkijonona.
    */
    @Override
    public String toString() {
        return getKauppaid() + "|" + getNimi() + "|" + getHinta() + "|" + getArvostelut(); 
    }


    /** Testiohjelma tuotteelle.
    * @param args ei käytössä.
    */
    public static void main(String[] args) {
        Tuote pyora1 = new Tuote();
        pyora1.vastaaGhostSpyoraa(2);
        pyora1.tulosta(System.out);

    }

}

package Tuotehaku;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Tuotteet-luokka joka osaa mm. lisätä uuden tuotteen. 
 * @author Miro
 * @version 9.4.2021
 *
 */

public class Tuotteet implements Iterable<Tuote> {
    
    private String tiedostonNimi = "tuotteet.dat";
    
    /**
     * Taulukko sovelluksen tuotteista
     */
    public Collection<Tuote> alkiot = new ArrayList<Tuote>();

    /** Tuotteet-muodostaja
     */
    public Tuotteet() {
        // Toistaiseksi tyhjä.
    }
    
    /** Lisää tuotteen tietorakenteeseen.
    * @param tuote lisättävä tuote.
    */
    public void lisaa(Tuote tuote) {
        alkiot.add(tuote);
        tuote.rekisteroi();
    }
    
    /** Palauttaa viitteen i:teen tuotteeseen.
    * @param i viittaa monennenko tuotteen viite halutaan.
    * @return viite tuotteeseen, jonka indeksi on i
    * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella.
    */   
     public Tuote anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || alkiot.size() <= i)
        throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return ((ArrayList<Tuote>) alkiot).get(alkiot.size()-1);
     }
    
    /** Lukee tuotteiden tiedostosta. 
    * @param tied tiedosto josta luetaan.
    * @throws SailoException jos lukeminen epäonnistuu.
    * 
    * @example
    * <pre name="test">
    * #THROWS SailoException
    * #import java.io.File;
    * Tuotteet tuotteet = new Tuotteet();
    * Tuote tuote1 = new Tuote(); tuote1.parse("1|Harry Potter & Viisaasten Kivi|19.95|4.7");
    * Tuote tuote2 = new Tuote(); tuote2.parse("1|Fingerpori 14|17.95|3.3");
    * Tuote tuote3 = new Tuote(); tuote3.parse("1|Suuri ristikkokirja|4.95|2.7");
    * Tuote tuote4 = new Tuote(); tuote4.parse("2|OnePlus Nord 5G älypuhelin|399.0|4.6");
    * tuotteet.lueTiedostosta("tuotteet.dat");
    * Iterator<Tuote> i = tuotteet.iterator();
    * i.next().toString() === "1|Harry Potter & Viisaasten Kivi|19.95|4.7";
    * i.next().toString() === "1|Fingerpori 14|17.95|3.3";
    * i.next().toString() === "1|Suuri ristikkokirja|4.95|2.7";
    * i.next().toString() === "2|OnePlus Nord 5G älypuhelin|399.0|4.6";
    * i.hasNext() === true;
    * tuotteet.lisaa(tuote4);
    * </pre>
    */
    public void lueTiedostosta(String tied) throws SailoException {
        alkiot = new ArrayList<Tuote>();
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi = fi.readLine();
            if (rivi == null) throw new SailoException("Maksimikoko puuttuu");
            
            while ( (rivi = fi.readLine()) != null) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Tuote tuote = new Tuote();
                tuote.parse(rivi);
                lisaa(tuote);
            }
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    /** Luetaan aikaisemmin annetun nimisestä tiedostosta.
    * @throws SailoException jos tulee poikkeus.
    */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    /**
    * Palauttaa tiedoston nimen, jota käytetään tallennukseen
    * @return tallennustiedoston nimi
    */
    public String getTiedostonPerusNimi() {
        return tiedostonNimi;
    }
    
    /**
    * Asettaa tiedoston perusnimen ilan tarkenninta
    * @param nimi tallennustiedoston perusnimi
    */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonNimi = nimi;
    }
 
    /**
    * Palauttaa tiedoston nimen, jota käytetään tallennukseen
    * @return tallennustiedoston nimi
    */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi();
    }
    
    /** Tallentaa tuotteiden tiedostoon.
     * @param kauppojenID Taulukko kauppojen tunnusluvuista, joiden tuotteet tallennetaan tiedostoon.
    * @throws SailoException jos talletus epäonnistuu
    */
    public void tallenna(int[] kauppojenID) throws SailoException {
        try ( PrintWriter fo = new PrintWriter(new FileWriter("tuotteet.dat")) ) {
            fo.println("Sovelluksesta löytyvät tuotteet:");
            for (Tuote tuote : this) {
                for (int i = 0;i < kauppojenID.length;i++)
                    if (tuote.getKauppaid() == kauppojenID[i]) {
                        fo.println(tuote.toString());
                        break;
                    }
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + tiedostonNimi + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + tiedostonNimi + " kirjoittamisessa ongelmia");
        }
            
    }
    
    /** Palauttaa tuotteiden lukumäärän
    * @return tuotteiden lkm
    * @example 
    * <pre name="test">
    * #import java.util.*;
    * Tuotteet tuotteet = new Tuotteet(); 
    * Tuote imuri = new Tuote("imuri",79,3,2);
    * Tuote limu = new Tuote("limu",2,4,2);
    * tuotteet.getLkm() === 0;
    * tuotteet.lisaa(imuri);
    * tuotteet.lisaa(limu);
    * tuotteet.getLkm() === 2;
    */
    public int getLkm() {
        return alkiot.size();
    }
    
    /** Iteraattori kaikkien tuotteiden läpikäymiseen.
    * @return tuoteiteraattori.
    * 
    * @example
    * <pre name="test">
    * #PACKAGEIMPORT
    * #import java.util.*;
    * Tuotteet tuotteet = new Tuotteet();
    * Tuote tuote1 = new Tuote(); tuote1.parse("1|Harry Potter & Viisaasten Kivi|19.95|4.7");
    * Tuote tuote2 = new Tuote(); tuote2.parse("1|Fingerpori 14|17.95|3.3");
    * Tuote tuote3 = new Tuote(); tuote3.parse("1|Suuri ristikkokirja|4.95|2.7");
    * Tuote tuote4 = new Tuote(); tuote4.parse("2|OnePlus Nord 5G älypuhelin|399.0|4.6");
    * int j = 0;
    * for (Tuote tuote : tuotteet)
    * {
    * tuote.getKauppaid() === 1;
    * j++;
    * if (j > 3) break;
    * }
    * </pre>
    */ 
    @Override
    public Iterator<Tuote> iterator() {
        return alkiot.iterator();
    }
    
    /** Haetaan kaikki tietyn kaupan tuotteet.
    * @param id kaupan id jonka tuotteita haetaan.
    * @return tietorakenne jossa viitteet löydettyihin tuotteisiin.
    * </pre>
    */
    public ArrayList<Tuote> annaTuotteet(int id) {
        ArrayList<Tuote> loydetyt = new ArrayList<Tuote>();
        for (Tuote tuote : alkiot)
            if (tuote.getKauppaid() == id) loydetyt.add(tuote);
        return loydetyt;
    }
    
    /** Testiohjelma tuotteille
    * @param args ei käytössä
    */
    public static void main(String[] args) {
        Tuotteet tuotteet = new Tuotteet();
        Tuote pyora2 = new Tuote();
        pyora2.vastaaGhostSpyoraa(2);
        tuotteet.lisaa(pyora2);
        
        System.out.println("Tuotteet testi:");
        
        ArrayList<Tuote> tuotteet2 = tuotteet.annaTuotteet(2);
        
        for (Tuote tuote : tuotteet2) {
            System.out.println(tuote.getTuoteid() + " ");
            tuote.tulosta(System.out);
        }
    }
    
    /** Poistaa tuotteen tuotteiden listalta.
    * @param poistuoteid poistettavan tuotteen id.
    */
     public void poistaTuote(int poistuoteid) {
         for (Tuote tuote : this) {
             if (tuote.getTuoteid() == poistuoteid) {
                 alkiot.remove(tuote);
                 break;
             }
         }
    }
     
     /**
      * Poistaa tietyn kaupan kaikki tuotteet
      * @param kauppaTunnus kaupan id, jonka tuotteet poistetaan
      */
     public void poistatuotteet(int kauppaTunnus) {
         for (int i = 0; i < alkiot.size();i++) {
         for (Tuote tuote : this) {
             if (tuote.getKauppaid() == kauppaTunnus) {
                 alkiot.remove(tuote);
                 break;
                 }
             }
         }
     }
    

}

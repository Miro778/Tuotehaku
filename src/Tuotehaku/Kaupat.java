package Tuotehaku;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * 
 * @author Miro
 * @version 27.3.2021
 *
 */
public class Kaupat implements Iterable<Kauppa>{
    
    private int lkm = 0;
    private String tiedostonPerusNimi = "kaupat.dat";
    private boolean muutettu = false;
    private Kauppa alkiot[] = new Kauppa[5];
    
    /** Oletusmuodostaja
     */
    public Kaupat() {
        // Attribuuttien oma alustus riittää. 
    };
    
    /** Lisää uuden kaupan tietorakenteeseen.
    * @param kauppa lisättävän kaupan viite. 
    * @throws SailoException jos tietorakenne on jo täynnä.
    * @example
    * <pre name="test">
    * #THROWS SailoException
    * Kaupat kaupat = new Kaupat();
    * Kauppa a = new Kauppa(), b = new Kauppa();
    * kaupat.getLkm() === 0;
    * kaupat.lisaa(a); kaupat.getLkm() === 1;
    * kaupat.lisaa(b); kaupat.getLkm() === 2;
    * kaupat.anna(0) === a;
    * kaupat.anna(1) === b;
    * kaupat.anna(1) == a === false;
    * kaupat.anna(0) == b === false;
    * kaupat.anna(2) === a; #THROWS IndexOutOfBoundsException
    * kaupat.lisaa(a); kaupat.getLkm() === 3;
    * kaupat.lisaa(b); kaupat.getLkm() === 4;
    * kaupat.poista(7); kaupat.getLkm() === 3;
    * </pre>
    */  
    public void lisaa(Kauppa kauppa) throws SailoException {
        if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm+20); 
        alkiot[lkm] = kauppa;
        kauppa.rekisteroi();
        lkm++;
    }
    
    /** Lisää uuden kaupan tietorakenteeseen ilman rekisteröintiä.
    * @param kauppa lisättävän kaupan viite
    * @throws SailoException jos tietorakenne on jo täynnä
    */
    public void lisaaIlmanR(Kauppa kauppa) throws SailoException {
        if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm+20); 
        alkiot[lkm] = kauppa;
        lkm++;
    }
    
    /** Poistaa kaupan tietorakenteesta.
    * @param id poistettavan kaupan viite.
    */
    public void poista(int id) {
        for (int i = 0;i < alkiot.length;i++)
        {
            if (alkiot[i].getTunnusNro() == id) 
            {
                alkiot[i] = null;
                for (int j = i;j < alkiot.length-1;j++) alkiot[j] = alkiot[j+1];
                alkiot[alkiot.length-1] = null;
                lkm--;
                muutettu = true;
                break;               
            }            
        }

    }
    
    /** Palauttaa viitteen i:teen kauppaan.
    * @param i viittaa monennenko kaupan viite halutaan.
    * @return viite jäseneen, jonka indeksi on i
    * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella.
    */
     public Kauppa anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
        throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
     }
     
     /** Kauppojen listaa muutettu
      */
     public void muutos() {
         muutettu = true;
     }
     
     /** Lukee kaupan tiedostosta.
     * @param tied tiedoston hakemisto.
     * @throws SailoException jos tiedostoa ei löydy tai sen kanssa on ongelmia.
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.*;
     * #import java.util.*;
     * Kaupat kaupat = new Kaupat();
     * Kauppa a = new Kauppa(), b = new Kauppa();
     * a.vastaaXXL(); b.vastaaXXL();
     * kaupat.lisaa(a);
     * kaupat.lisaa(b);
     * kaupat.tallenna();
     * kaupat = new Kaupat();
     * kaupat.lueTiedostosta("kaupat.dat");
     *  kaupat.lisaa(b);
     *  kaupat.tallenna();
     *  </pre>
     */    
     public void lueTiedostosta(String tied) throws SailoException {
          setTiedostonPerusNimi(tied);
          try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
              String rivi = fi.readLine();
              if (rivi == null) throw new SailoException("Maksimikoko puuttuu");
              
              while ( (rivi = fi.readLine()) != null) {
                  rivi = rivi.trim();
                  if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                  Kauppa kauppa = new Kauppa();
                  kauppa.parse(rivi);
                  lisaaIlmanR(kauppa);
              }
              muutettu = false;
          } catch ( FileNotFoundException e ) {
              throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
          } catch ( IOException e ) {
              throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
          }
      }
     
     /** Luetaan aikaisemmin annetun nimisestä tiedostosta.
     * @throws SailoException jos tulee poikkeus.
     * */
     public void lueTiedostosta() throws SailoException {
         lueTiedostosta(getTiedostonPerusNimi());
     }
     
     
     /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
     public String getTiedostonPerusNimi() {
         return tiedostonPerusNimi;
     }
     
     /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
     public void setTiedostonPerusNimi(String nimi) {
         tiedostonPerusNimi = nimi;
     }
  
     /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
     public String getTiedostonNimi() {
         return getTiedostonPerusNimi();
     }
     
     /** Tallettaa kaupan tiedostoon. 
      * Tiedoston muoto:
      * <pre>
      * 1  |Suomalainen kirjakauppa|kirjakauppa  |Kauppakatu 33|suomalainen.com |4.5      |
      * 2  |Gigantti               |elektroniikka|Ahjokatu 11  |gigantti.fi     |3.5      |
      * 4  |XXL                    |urheilu      |Ahjokatu 3-5 |xxl.fi          |4.0      |
      * </pre>
     * @throws SailoException jos talletus epäonnistuu.
     */
      public void tallenna() throws SailoException {
          if ( !muutettu ) return;
          
          
          
          try ( PrintWriter fo = new PrintWriter(new FileWriter("kaupat.dat")) ) {
              fo.println("Sovelluksesta löytyvät kaupat:");
              for (Kauppa kauppa : this) {
                  fo.println(kauppa.toString());
              }
          } catch ( FileNotFoundException ex ) {
              throw new SailoException("Tiedosto " + tiedostonPerusNimi + " ei aukea");
          } catch ( IOException ex ) {
              throw new SailoException("Tiedoston " + tiedostonPerusNimi + " kirjoittamisessa ongelmia");
          }
 
                 muutettu = false;
              
      }

    /** Palauttaa kauppojen lukumäärän.
     * @return kauppojen lukumäärä
     */
     public int getLkm() {
         return lkm;
     }
     
     /** Luokka kauppojen iteroimiseksi
      */
     public class KaupatIterator implements Iterator<Kauppa> {
         private int kohdalla = 0;
         
         /** Katsoo, onko olemassa seuraavaa kauppaa
          * @return onko seuraavaa kauppaa
          */
         @Override
        public boolean hasNext() {
             return kohdalla < getLkm();
         }
         
         /** Annetaan seuraava kauppa
         * @return seuraava kauppa
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         */
         @Override
        public Kauppa next() throws NoSuchElementException {
             if ( !hasNext() ) throw new NoSuchElementException("Ei ole");
             return anna(kohdalla++);
         }

     }
     
     /** Palautetaan iteraattori kaupoistaan
      * @return iteraattori kauppojen läpikäymiseen
      */
     @Override
    public Iterator<Kauppa> iterator() {
         return new KaupatIterator();
     }
     
     /** Palauttaa taulukossa hakuehtoon vastaavien kauppojen viitteet.
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenteen löytyneistä kaupoista
     */
     @SuppressWarnings("unused")
     public Collection<Kauppa> etsi(String hakuehto, String k) {
         Collection<Kauppa> loytyneet = new ArrayList<Kauppa>();
         for (Kauppa kauppa : this) {
             loytyneet.add(kauppa);
         }
         return loytyneet;
     }

     /** Testiohjelma
     * @param args ei käytössä. 
     */
    public static void main(String[] args) {
        Kaupat kaupat = new Kaupat();
        
        Kauppa Gigantti = new Kauppa("Gigantti","Elektroniikka","Ahjokatu 11","gigantti.fi",3.5),
        XXL = new Kauppa("XXL","Urheilu","Ahjokatu 3-5","xxl.fi",4.0);
        Gigantti.rekisteroi();
        XXL.rekisteroi();
        
        try {
            kaupat.lisaa(XXL);
            kaupat.lisaa(Gigantti);
            
            System.out.println("==================== Testi ====================");
            
            for (int i = 0; i < kaupat.getLkm(); i++) {
                Kauppa kauppa = kaupat.anna(i);
                System.out.println("Kaupan id: " + i);
                kauppa.tulosta(System.out);
            }
            
        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }

    }

}

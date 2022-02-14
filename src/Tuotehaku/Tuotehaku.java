package Tuotehaku;
/**
 * Tuotehaku-luokka joka hoitaa kauppojen ja tuotteiden välisen yhteistyön.
 * @author Miro
 * @version 9.4.2021
 *
 */
public class Tuotehaku {
    
    private Kaupat kaupat = new Kaupat();
    private Tuotteet tuotteet = new Tuotteet();
    
    /** Palauttaa sovelluksen kauppojen määrän
    * @return kauppojen määrä
    */
    public int getKauppoja() {
        return kaupat.getLkm();
    }
    
    /** Palauttaa sovelluksen kaupat
    * @return kaupat
    */
    public Kaupat getKaupat() {
        return kaupat;
    }
    
    /** Palauttaa sovelluksen tuotteet
    * @return tuotteet
    */
    public Tuotteet getTuotteet() {
        return tuotteet;
    }
    
    /** Tyhjentää tuotteet
     */
    public void nollaaTuotteet() {
        tuotteet = new Tuotteet();
    }
    
}

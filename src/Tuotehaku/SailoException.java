package Tuotehaku;


/** Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille.
 * 
 * @author Miro
 * @version 27.3.2021
 *
 */

public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;
 
    
    /** Poikkeuksen muodostaja, jolle tuodaan poikkeuksessa käytettävä viesti
     * @param viesti joka lähetetään virheilmoituksena.
     */
    
    public SailoException(String viesti) {
        super(viesti);
    }

}

package Tuotehaku.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import Tuotehaku.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2021.04.20 01:37:39 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class TuoteTest {



  // Generated by ComTest BEGIN
  /** testRekisteroi96 */
  @Test
  public void testRekisteroi96() {    // Tuote: 96
    Tuote tuote1 = new Tuote(); 
    assertEquals("From: Tuote line: 98", 0, tuote1.getTuoteid()); 
    tuote1.rekisteroi(); 
    Tuote tuote2 = new Tuote(); 
    tuote2.rekisteroi(); 
    int n1 = tuote1.getTuoteid(); 
    int n2 = tuote2.getTuoteid(); 
    assertEquals("From: Tuote line: 104", n2-1, n1); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testParse151 */
  @Test
  public void testParse151() {    // Tuote: 151
    Tuote t = new Tuote(); 
    t.parse(" 3 | Pyora | 599 | 4.3 "); 
    assertEquals("From: Tuote line: 154", 3, t.getKauppaid()); 
    assertEquals("From: Tuote line: 155", "Pyora", t.getNimi()); 
    assertEquals("From: Tuote line: 156", 599.0, t.getHinta(), 0.000001); 
    assertEquals("From: Tuote line: 157", 4.3, t.getArvostelut(), 0.000001); 
    assertEquals("From: Tuote line: 158", "3|Pyora|599.0|4.3", t.toString()); 
  } // Generated by ComTest END
}
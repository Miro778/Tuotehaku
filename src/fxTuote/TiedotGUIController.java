package fxTuote;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * @author Miro
 * @version 20.1.2021
 *
 */
public class TiedotGUIController implements ModalControllerInterface<String> {
	// tyhjä
    
    
    @FXML private Button ok;

    
    
    @FXML private void handleOK() {
        ModalController.closeStage(ok);
    }



    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }



    @Override
    public void setDefault(String arg0) {
        // TODO Auto-generated method stub
        
    }
    
}


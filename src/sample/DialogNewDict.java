package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class DialogNewDict {
    private DBDealer dealer;

    @FXML
    private TextField newDictName;

    @FXML
    private TextField newDictFullName;

    @FXML
    private TextField newDictDate;



    public void initialize() {
        dealer = DBDealer.getInstance();
    }

    public void createNewDict(String newDictName){
//        Put name like HP3-16-22 ???!!!!
//        ALTER TABLE tableStatus ADD testDict INTEGER;
//        UPDATE tableStatus SET testDict = 0;
//        ALTER TABLE tableStatus RENAME COLUMN testDict TO HP3_16_22;
    }


    public String getDictShortname() {
        return newDictName.getText();
    }

    public String getNewDictFullName() {
        return newDictFullName.getText();
    }

    public String getNewDictDate() {
        return newDictDate.getText();
    }
}

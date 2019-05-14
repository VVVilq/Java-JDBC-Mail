package sample;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.sql.ResultSet;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;


import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;


public class TableViewDAO {

    @FXML
    private TableView tableView;

    private String queryToSend;

    private ObservableList<ObservableList> data;


    public String getQueryToSend() {
        return queryToSend;
    }

  public void setStudentQuery(){

        queryToSend="Select * from dziekanat.studenci";
        showTable();
    }

  public void setOsobisteQuery(){

      queryToSend="Select * from dziekanat.osobiste";
      showTable();
  }

  public void setAdresyQuery(){

      queryToSend="Select * from dziekanat.adresy";
      showTable();

    }

  public void setWojskoQuery(){

      queryToSend="Select * from dziekanat.wojsko";
      showTable();
    }

  public void setStudenci_studiow_stacjonarnychQuery(){

      queryToSend="Select * from dziekanat.kierunki_studiow_stacjonarnych";
      showTable();
    }

  public void setKierunki_studiowQuery(){

      queryToSend="Select * from dziekanat.kierunki_studiow";
      showTable();

  }

  public void setStudenci_kierunkowQuery(){

      queryToSend="Select * from dziekanat.studenci_kierunkow";
      showTable();

  }

  public void setZapisyQuery(){

      queryToSend="Select * from dziekanat.zapisy";
      showTable();
  }

  public void setOcenyQuery(){

      queryToSend="Select * from dziekanat.oceny";
      showTable();
  }

  public void setPrzedmiotyQuery(){

      queryToSend="Select * from dziekanat.przedmioty";
      showTable();


  }

  public void setFakultetyQuery(){

      queryToSend="Select * from dziekanat.fakultety";
      showTable();

  }

  public void setJednostki_organizacyjneQuery(){

      queryToSend="Select * from kadry.jednostki_organizacyjne";
      showTable();
    }

  public void setProwadzacyQuery(){

      queryToSend="Select * from kadry.prowadzacy";
      showTable();
    }

    public  void showEmail(ActionEvent event) throws IOException{
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("EmailHandlerScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    public void logout(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("Logger.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    public void showTable(){
        try {
            data = FXCollections.observableArrayList();

            Statement st = Controller.getConnection().createStatement();
            ResultSet rs = st.executeQuery(queryToSend);
            tableView.getColumns().clear();

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        String cellValue="";
                        try {
                            cellValue=param.getValue().get(j).toString();
                        } catch (NullPointerException exception) {
                            cellValue="null";
                        }

                        return new SimpleStringProperty(cellValue);
                    }
                });

                tableView.getColumns().addAll(col);


            }

            while(rs.next()){
                ObservableList<Serializable> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    row.add(rs.getString(i));
                }

                data.add(row);

            }


            tableView.setItems(data);






        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e);
        }



    }
}

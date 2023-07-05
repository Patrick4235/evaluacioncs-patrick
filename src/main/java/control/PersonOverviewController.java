package control;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.MainApp;
import model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import util.AgendaEntity;

import javax.persistence.EntityManager;
import java.util.List;


public class PersonOverviewController {
    @FXML private TableView<Person> personTable;
    @FXML private TableColumn<Person, String> firstNameColumn;
    @FXML private TableColumn<Person, String> lastNameColumn;

    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label streetLabel;
    @FXML private Label postalCodeLabel;
    @FXML private Label cityLabel;
    @FXML private Label birthdayLabel;

    private List<Person> persons;

    @FXML
    private void initialize() {
        showPersonDetails(null);

        listPerson();

        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    public void listPerson(){
        EntityManager em = AgendaEntity.getEntityManager();

        firstNameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getFirstName())); ;
        lastNameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getLastName()));

        String jpql = "SELECT p FROM Person AS p";
        persons  = em.createQuery(jpql, Person.class).getResultList();
        ObservableList<Person> obPerson = FXCollections.observableArrayList(persons);
        personTable.setItems(obPerson);

        em.close();
    }

    private void showPersonDetails(Person person) {
        EntityManager em = AgendaEntity.getEntityManager();

        person = personTable.getSelectionModel().getSelectedItem();

        if (person != null) {
            String jpql = "SELECT p FROM Person AS p WHERE p.id =: id";
            Person personSelected = em.createQuery(jpql, Person.class).setParameter("id", person.getId()).getSingleResult();

            firstNameLabel.setText(personSelected.getFirstName());
            lastNameLabel.setText(personSelected.getLastName());
            streetLabel.setText(personSelected.getStreet());
            postalCodeLabel.setText(personSelected.getPostalCode());
            cityLabel.setText(personSelected.getCity());
            birthdayLabel.setText(personSelected.getBirthday());
        }
        else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }

        em.close();

    }
    @FXML
    private void btnDeletePerson() {
        EntityManager em = AgendaEntity.getEntityManager();
        Person personSelected = personTable.getSelectionModel().getSelectedItem();

        if (personSelected != null) {
            String jpql = "DELETE FROM Person FROM Person AS p WHERE p.id =: id";
            Person personToDelete = em.createQuery(jpql, Person.class).setParameter("id", personSelected.getId()).getSingleResult();
        }
        else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No person selected");

            alert.showAndWait();
        }

        em.close();
    }

    @FXML
    private void btnNewPerson() {

        MainApp mainApp = new MainApp();

        mainApp.showPersonEditDialog();

    }

    @FXML
    private void btnEditPerson() {

    }




    public void setMainApp(MainApp mainApp) {
    }
}

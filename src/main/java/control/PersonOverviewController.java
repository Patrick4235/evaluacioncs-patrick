package control;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Person;
import agenda.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import util.AgendaEntity;

import javax.persistence.EntityManager;
import java.time.format.DateTimeFormatter;
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
    @FXML private Label birthdayLabel = new Label("");

    private List<Person> persons;

    @FXML
    private void initialize() {
        showPersonDetails(null);

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
        if (person != null) {
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(person.getBirthday());
        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }
    @FXML
    private void btnDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Error");
            alert.setHeaderText("No person selected");

            alert.showAndWait();
        }
    }

    @FXML
    private void btnNewPerson() {
        EntityManager em = AgendaEntity.getEntityManager();
        Person person = new Person();

        person.setFirstName(firstNameColumn.getText());
        person.setLastName(lastNameColumn.getText());
        person.setApellidos(txtApellidos.getText());
        person.setGrado(cbGrado.getValue());
        person.setFechaNacimiento(dpFechaNacimiento.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        try {
            em.getTransaction().begin();
            em.persist(estudiante);
            em.getTransaction().commit();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Estudiante Agregado");
            alert.showAndWait();
            listarEstudiantes();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }



    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        personTable.setItems(mainApp.getPersonData());
    }
}

package cqu.wis;

import cqu.wis.data.WhiskeyData;
import cqu.wis.data.UserData;
import cqu.wis.roles.SceneCoordinator;
import cqu.wis.roles.SceneCoordinator.SceneKey;
import cqu.wis.roles.WhiskeyDataManager;
import cqu.wis.roles.WhiskeyDataValidator;
import cqu.wis.roles.UserDataManager;
import cqu.wis.roles.UserDataValidator;
import cqu.wis.view.LoginController;
import cqu.wis.view.PasswordController;
import cqu.wis.view.QueryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        SceneCoordinator sc = new SceneCoordinator(stage);
        try {
            // Create data related objects for whiskey data
            WhiskeyData wd = new WhiskeyData("jdbc:mysql://localhost:3306/WhiskeyDB", "root", "root");
            WhiskeyDataManager wdm = new WhiskeyDataManager(wd);
            WhiskeyDataValidator wdv = new WhiskeyDataValidator();
            wd.connect();

            // Create data related objects for user data
            UserData ud = new UserData("jdbc:mysql://localhost:3306/USERS", "root", "root");
            UserDataManager udm = new UserDataManager(ud);
            UserDataValidator udv = new UserDataValidator();
            ud.connect();

            // Create the query scene
            Scene qs = makeScene(SceneKey.QUERY);
            QueryController qc = (QueryController) qs.getUserData();  
            qc.inject(sc, wdm, wdv);
            sc.addScene(SceneKey.QUERY, qs);

            // Create the login scene
            Scene ls = makeScene(SceneKey.LOGIN);
            LoginController lc = (LoginController) ls.getUserData();
            lc.inject(sc, udm, udv);
            sc.addScene(SceneKey.LOGIN, ls);

            // Create the change password scene
            Scene ps = makeScene(SceneKey.PASSWORD);
            PasswordController pc = (PasswordController) ps.getUserData();
            pc.inject(sc, udm, udv);
            sc.addScene(SceneKey.PASSWORD, ps);
        }
        catch(Exception e) {
            e.printStackTrace(); // Print full stack trace for debugging
            System.exit(1);
        }
        sc.start();
    }

    private static Scene makeScene(SceneKey key) throws Exception {
        // construct path name for fxml file
        String fxml = "/cqu/wis/view/" + key.name().toLowerCase() + ".fxml";
        // create scene object and add a reference to its controller object
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        Scene scene = new Scene(loader.load());
        scene.setUserData(loader.getController());
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
}
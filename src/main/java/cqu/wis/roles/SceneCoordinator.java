package cqu.wis.roles;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Hashtable;

/**
 * Manages scene transitions in the Whiskey Information System.
 */
public class SceneCoordinator {
    public enum SceneKey {LOGIN, PASSWORD, QUERY}

    private final Stage stage;
    private final Hashtable<SceneKey, Scene> scenes;

    public SceneCoordinator(Stage stage) {
        this.stage = stage;
        this.scenes = new Hashtable<>();
    }

    /**
     * Adds a scene to the coordinator's hashtable.
     * @param key The SceneKey identifier for the scene
     * @param value The Scene object to add
     */
    public void addScene(SceneKey key, Scene value) {
        scenes.put(key, value);
    }

    /**
     * Sets the current scene to the one identified by the given key.
     * @param key The SceneKey identifier for the scene to display
     */
    public void setScene(SceneKey key) {
        Scene s = scenes.get(key);
        stage.setScene(s);
        stage.setTitle("Whiskey Information System");
        stage.show();
    }

    /**
     * Starts the application by displaying the initial scene (LOGIN).
     */
    public void start() {
        setScene(SceneKey.LOGIN); // Changed from QUERY to LOGIN
    }
}
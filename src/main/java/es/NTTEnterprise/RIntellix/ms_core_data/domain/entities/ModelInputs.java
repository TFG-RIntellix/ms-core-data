package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.util.HashMap;

/**
 * Class representing the input features used for scoring. This class encapsulates all the raw data inputs that the scoring model requires to perform its calculations.
 * The features are stored in a HashMap with String keys (feature names) and Object values (feature values), allowing for flexibility in the types of features that can be included.
 * This design allows the model to evolve and include new features without needing to change the class structure, as long as the scoring logic can handle the new features appropriately.
 * @author: Lucía Fernández Mancebo
 * Date: 03-02-2026
 */
public class ModelInputs {

    private HashMap<String, Object> features;
    
    /**
     * Default constructor for ModelInputs. Initializes the features HashMap.
     */
    public ModelInputs() {
        this.features = new HashMap<>();
    }

    /**
     * Parameterized constructor for ModelInputs. Allows setting the features HashMap at initialization.
     * @param features The HashMap containing the input features.
     */
    public ModelInputs(HashMap<String, Object> features) {
        this.features = features;
    }

    // Getters and Setters

    public HashMap<String, Object> getFeatures() {
        return features;
    }
    
    public void setFeatures(HashMap<String, Object> features) {
        this.features = features;
    }

}

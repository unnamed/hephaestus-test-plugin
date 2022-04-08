package team.unnamed.hephaestus.plugin;

import org.jetbrains.annotations.Nullable;
import team.unnamed.hephaestus.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModelRegistry {

    private final Map<String, Model> models = new HashMap<>();

    public void registerModel(Model model) {
        models.put(model.name(), model);
    }

    public @Nullable Model model(String name) {
        return models.get(name);
    }

    public Collection<String> modelNames() {
        return models.keySet();
    }

    public Collection<Model> models() {
        return models.values();
    }

}

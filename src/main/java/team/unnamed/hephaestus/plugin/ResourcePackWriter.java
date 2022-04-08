package team.unnamed.hephaestus.plugin;

import team.unnamed.creative.base.Writable;
import team.unnamed.creative.file.FileTree;
import team.unnamed.creative.file.FileTreeWriter;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.hephaestus.writer.ModelWriter;

public class ResourcePackWriter implements FileTreeWriter {

    private final ModelRegistry modelRegistry;

    public ResourcePackWriter(ModelRegistry modelRegistry) {
        this.modelRegistry = modelRegistry;
    }

    @Override
    public void write(FileTree tree) {
        // REQUIRED!
        // Write the resource-pack metadata so the client
        // interprets it as a resource-pack
        tree.write(Metadata.builder()
                .add(PackMeta.of(8, "Generated resource pack"))
                .build());

        // OPTIONAL
        // Writes the resource-pack icon
        Writable icon = Writable.resource(getClass().getClassLoader(), "pack.png");
        tree.write("pack.png", icon);

        // Writes the models to the resource-pack
        ModelWriter.resource().write(tree, modelRegistry.models());
    }

}

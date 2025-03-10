package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.util.Logger;
import dev.lefley.reportlm.view.components.burp.BurpIcon;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static dev.lefley.reportlm.view.components.burp.BurpIcon.Builder.icon;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SAVE;

public class SaveButton extends SimpleIconButton
{
    private static final BurpIcon SAVE_ICON = icon(SAVE).fontSized().build();

    private final Supplier<Path> directorySupplier;

    public SaveButton(Supplier<Path> directorySupplier)
    {
        super(SAVE_ICON);
        this.directorySupplier = directorySupplier;

        setToolTipText("Save report");

        addClickListener(this::openFileChooser);
    }

    private void openFileChooser()
    {
        Path source = directorySupplier.get();

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Saving " + source.getFileName());
        fileChooser.setApproveButtonText("Save");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showSaveDialog(SwingUtilities.getWindowAncestor(SaveButton.this));
        if (result == JFileChooser.APPROVE_OPTION)
        {
            Path destination = fileChooser.getSelectedFile().toPath();

            copyDirectory(source, destination.resolve(source.getFileName()));
        }
    }

    private void copyDirectory(Path source, Path target)
    {
        try(Stream<Path> pathStream = Files.walk(source))
        {
            pathStream.forEach(sourcePath -> {
                Path targetPath = target.resolve(source.relativize(sourcePath));
                try
                {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
                catch (IOException e)
                {
                    Logger.logToError("Could not copy file: " + sourcePath, e);
                }
            });
        }
        catch (IOException e)
        {
            Logger.logToError("Could not copy directory: " + source, e);
        }
    }
}
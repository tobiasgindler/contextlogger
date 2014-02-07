package de.holisticon.util.tracee.contextlogger.presets;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.contextlogger.TraceeContextLoggerConstants;

/**
 * Enum for getting the preset configuration for contextual logger output.
 * Defaults to BASIC and uses BASIC as fallback if error occur.
 * Created by Tobias Gindler, holisticon AG on 07.02.14.
 */
public enum Preset {

    FULL(),
    BASIC(),
    ENHANCED(),
    CUSTOM();


    private static Preset preset = null;
    private static final TraceeLogger LOGGEER = Tracee.getBackend().getLogger(Preset.class);

    private PresetConfig presetConfig;

    Preset() {
    }

    public PresetConfig createOrGetPresetConfig() {
        PresetConfig tmpPreset = null;

        switch (this) {
            case FULL:
                tmpPreset = new FullPreset();
                break;
            case BASIC:
                tmpPreset = new BasicPreset();
                break;
            case ENHANCED:
                tmpPreset = new EnhancedPreset();
                break;
            case CUSTOM:
                tmpPreset = new CustomPreset(Preset.useCustomPreset(), Preset.getCustomClassName());
                break;
        }


        return tmpPreset;
    }


    static String getCustomClassName() {
        return System.getProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET_CLASS);
    }

    static boolean useCustomPreset() {
        return "CUSTOM".equals(System.getProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET))
                && getCustomClassName() != null;
    }

    public static Preset getPreset() {

        if (preset == null) {
            Preset tmpPreset;

            String presetName = System.getProperty(
                    TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_PRESET,
                    BASIC.name());
            try {
                tmpPreset = Preset.valueOf(presetName);
            } catch (IllegalArgumentException e) {

                // LOG WARNING and use BASIC as fallback
                LOGGEER.error("Couldn't convert configured preset '" + presetName + "' to Preset enum value.");
                tmpPreset = BASIC;

            }

            preset = tmpPreset;
        }

        return preset;

    }

    /**
     * Create preset at first access.
     *
     * @return the preset configuration
     */
    public PresetConfig getPresetConfig() {
        if (presetConfig == null) {
            presetConfig = createOrGetPresetConfig();
        }
        return presetConfig;
    }

    public static void reload() {

        for (Preset preset : Preset.values()) {
            preset.presetConfig = preset.createOrGetPresetConfig();
        }
        preset = null;

    }



}

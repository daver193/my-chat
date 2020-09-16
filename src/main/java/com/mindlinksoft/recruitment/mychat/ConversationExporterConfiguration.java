package com.mindlinksoft.recruitment.mychat;

/**
 * Represents the configuration for the exporter.
 */
public final class ConversationExporterConfiguration {
    /**
     * Gets the input file path.
     */
    private String inputFilePath;

    /**
     * Gets the output file path.
     */
    private String outputFilePath;

    private String inputFilters;

    /**
     * Initializes a new instance of the {@link ConversationExporterConfiguration} class.
     * @param inputFilePath The input file path.
     * @param outputFilePath The output file path.
     */
    public ConversationExporterConfiguration(String inputFilePath, String outputFilePath, String inputFilters) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        this.inputFilters = inputFilters;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public String getInputFilters() {
        return inputFilters;
    }
}

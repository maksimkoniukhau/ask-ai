package io.mk.ask_ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record EmbedContentRequest(
        @Schema(
                description = "Directory path to read files from",
                example = "/path/to/project"
        )
        @NotEmpty
        String directoryPath,
        @Schema(
                description = "List of file extensions to include",
                example = "[\".ts\", \".tsx\", \".md\"]"
        )
        List<String> includedFileExtensions,
        @Schema(
                description = "List of directories to exclude",
                example = "[\"node_modules\", \"dist\", \"build\", \"coverage\", \"__tests__\"]"
        )
        List<String> excludedDirectories) {
}

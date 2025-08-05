package io.mk.ask_ai.service;

import io.mk.ask_ai.dto.EmbedContentRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class RepoReaderService {

    public List<String> extractChunks(EmbedContentRequest embedContentRequest) {
        List<String> chunks = new ArrayList<>();

        List<String> excludedDirs = embedContentRequest.excludedDirectories();
        List<String> includedExts = embedContentRequest.includedFileExtensions();

        try (Stream<Path> walk = Files.walk(Paths.get(embedContentRequest.directoryPath()))) {
            walk
                    .filter(path -> CollectionUtils.isEmpty(excludedDirs) || excludedDirs.stream()
                            .noneMatch(excluded -> path.toString().contains(File.separator + excluded + File.separator)))
                    .filter(Files::isRegularFile)
                    .filter(path -> CollectionUtils.isEmpty(includedExts) || includedExts.stream()
                            .anyMatch(ext -> path.getFileName().toString().endsWith(ext)))
                    .forEach(path -> {
                        try {
                            String content = Files.readString(path);
                            // Simple split by 20 lines per chunk
                            String[] lines = content.split("\n");
                            for (int i = 0; i < lines.length; i += 20) {
                                int end = Math.min(i + 20, lines.length);
                                String chunkText = String.join("\n", Arrays.copyOfRange(lines, i, end));
                                chunks.add(chunkText);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return chunks;
    }
}

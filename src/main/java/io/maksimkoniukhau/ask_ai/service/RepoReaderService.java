package io.maksimkoniukhau.ask_ai.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class RepoReaderService {

    public List<String> extractChunks(Path repoPath) {
        List<String> chunks = new ArrayList<>();
        List<String> excludedDirs = List.of("node_modules", "dist", "build", "coverage", "__tests__");
        try (Stream<Path> walk = Files.walk(repoPath)) {
            walk
                    .filter(path -> excludedDirs.stream()
                            .noneMatch(excluded -> path.toString().contains(File.separator + excluded + File.separator)))
                    .filter(Files::isRegularFile)
                    .filter(path -> Stream.of(".ts", ".tsx", ".md")
                            .anyMatch(ext -> path.getFileName().toString().endsWith(ext)))
                    .forEach(p -> {
                        try {
                            String content = Files.readString(p);
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

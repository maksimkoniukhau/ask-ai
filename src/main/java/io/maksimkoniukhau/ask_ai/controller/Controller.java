package io.maksimkoniukhau.ask_ai.controller;

import io.maksimkoniukhau.ask_ai.service.RepoReaderService;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;
import java.util.List;

@AllArgsConstructor
@RestController
public class Controller {

    private final RepoReaderService repoReaderService;
    private final VectorStore vectorStore;
    private final OllamaChatModel chatModel;

    @PostMapping("/add-documents")
    public void populateVectorStore(@RequestBody String path) {
        List<String> chunks = repoReaderService.extractChunks(Paths.get(path));
        vectorStore.add(chunks.stream().map(Document::new).toList());
    }

    @PostMapping("/ask")
    public String ask(@RequestBody String question) {
        PromptTemplate customPromptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("""
                        <query>

                        Context information is below.

                        ---------------------
                        <question_answer_context>
                        ---------------------

                        Given the context information and no prior knowledge, answer the query.

                        Follow these rules:

                        1. If the answer is not in the context, just say that you don't know.
                        2. Avoid statements like "Based on the context..." or "The provided information...".
                        """)
                .build();

        return ChatClient.builder(chatModel)
                .build().prompt()
                .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .promptTemplate(customPromptTemplate)
                        .build())
                .user(question)
                .call()
                .content();
    }
}

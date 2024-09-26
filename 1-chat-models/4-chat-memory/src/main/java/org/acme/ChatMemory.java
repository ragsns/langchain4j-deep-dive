package org.acme;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

@QuarkusMain
public class ChatMemory implements QuarkusApplication {

    @Inject
    ChatLanguageModel model;

    @Override
    public int run(String... args) {
        var memory = MessageWindowChatMemory.builder()
                .id("user-id")
                .maxMessages(3) // Only 3 messages will be stored
                .build();

        memory.add(new SystemMessage("You are a useful AI assistant."));
        memory.add(new UserMessage("Hello, my name is Clement and I live in Valence, France"));
        memory.add(new UserMessage("What is my name?"));

        var response = model.generate(memory.messages());
        System.out.println("Answer 1: " + response.content().text());

        memory.add(response.content());

        memory.add(new UserMessage("Where do I leave?"));
        response = model.generate(memory.messages());
        System.out.println("Answer 2: " + response.content().text());

        return 0;
    }

}
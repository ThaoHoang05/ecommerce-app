package com.stationeryshop.controller;

import com.stationeryshop.chatbot.GeminiApiService;
import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.dao.InvoiceDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.dao.SupplierDAO;
import com.stationeryshop.model.Chatbot;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

public class ChatbotController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField userInput;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button sendBtn;

    private Chatbot chatbot;

    @FXML
    public void initialize() {
        chatbot = new Chatbot(
            new GeminiApiService(),
            new ProductDAO(),
            new SupplierDAO(),
            new CategoryDAO(),
            new InventoryDAO(),
            new InvoiceDAO()
        );

        // Gửi khi nhấn Enter
        userInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSend();
                event.consume();
            }
        });
    }

    @FXML
    private void handleSend() {
        String userMessage = userInput.getText().trim();
        if (userMessage.isEmpty()) return;
        chatArea.appendText("Bạn: " + userMessage + "\n");
        userInput.clear();

        // Có thể chạy thread riêng nếu muốn không bị đứng UI
        String botResponse = chatbot.getResponse(userMessage);
        chatArea.appendText("Bot: " + botResponse + "\n");

        // Tự động cuộn xuống cuối
        chatArea.positionCaret(chatArea.getText().length());
        scrollPane.setVvalue(1.0);
    }
}

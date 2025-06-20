package com.stationeryshop.controller;

import com.stationeryshop.controller.inventory.ProductController;
import com.stationeryshop.model.InventoryProduct;
import com.stationeryshop.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class MainViewController {
    @FXML
    private VBox onlyForStaff;

    @FXML
    private VBox onlyForCustomer;

    @FXML
    private HBox inventoryTab;

    @FXML
    private ImageView customerAvatar;

    @FXML
    private Button loginBtn;

    @FXML
    private StackPane ContentArea;

    @FXML
    private TextField searchitemField;

    @FXML
    private Label customerName;

    @FXML
    private Button signupBtn;

    @FXML
    private HBox reportTab;

    @FXML
    private HBox AccountHbox;

    @FXML
    private Region spacer;

    @FXML
    private HBox supplierTab;

    @FXML
    private Button AIChatbotBtn;

    @FXML

    private HBox cartTab;

    @FXML
    private HBox LoginHbox;

    @FXML
    private Button logoutBtn;

    @FXML
    private VBox accountVbox;

    @FXML
    private Label accountDropdownArrow;

    @FXML
    private HBox searchHbox;

    @FXML
    private VBox accountDropdown;

    @FXML
    private HBox allProductsTab;

    @FXML
    private HBox categoriesTab;

    @FXML
    private Button categoryBtn3;

    @FXML
    private Button categoryBtn2;

    @FXML
    private Button categoryBtn1;

    @FXML
    private HBox bestSellersTab;

    @FXML
    private AnchorPane mainPane;

    private VBox intialMain;

    // Method để clear nội dung cũ trước khi load nội dung mới
    private void clearMainPane() {
        mainPane.getChildren().clear();
    }

    // Method để load content vào mainPane
    private void loadContentToMainPane(Node content) {
        clearMainPane(); // Xóa nội dung cũ
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
        mainPane.getChildren().add(content);
    }

    @FXML
    void gotoCartForm(MouseEvent event) throws IOException {
        String role = Session.getCurrentRole();
        if("customer".equals(role)) {
            final String CAR_PATH = "/fxml/Cart.fxml";
            FXMLLoader loader= new FXMLLoader(getClass().getResource(CAR_PATH));
            Pane cartPane = loader.load();
            CartController cartController = loader.getController();
            cartController.setParentContainer(mainPane);
            loadContentToMainPane(cartPane);
            cartController.syncWithSession();
        }
        else{
            JOptionPane.showMessageDialog(null,"You are not a customer");
        }
    }

    @FXML
    void gotoProductForm(MouseEvent event) throws IOException {
        String role = Session.getCurrentRole();
        if("admin".equals(role)) {
            final String PRODUCT_PATH = "/fxml/Inventory/ProductForm.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PRODUCT_PATH));
            Pane productPane = loader.load();
            loadContentToMainPane(productPane);
        }else{
            JOptionPane.showMessageDialog(null,"You are not an admin","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void gotoSupplierForm(MouseEvent event) throws IOException {
        String role = Session.getCurrentRole();
        if("admin".equals(role)) {
            final String REPORT_PATH = "/fxml/Supplier.fxml";
            BorderPane supplierPane = (new FXMLLoader(getClass().getResource(REPORT_PATH))).load();
            loadContentToMainPane(supplierPane);
        }else{
            JOptionPane.showMessageDialog(null,"You are not an admin","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void gotoReportForm(MouseEvent event) throws IOException {
        String role = Session.getCurrentRole();
        if("admin".equals(role)) {
            final String REPORT_PATH = "/fxml/Report.fxml";
            ScrollPane reportPane = (new FXMLLoader(getClass().getResource(REPORT_PATH))).load();
            loadContentToMainPane(reportPane);
        }else{
            JOptionPane.showMessageDialog(null,"You are not an admin","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void gotoHistoryForm(MouseEvent mouseEvent) throws IOException {
        String role = Session.getCurrentRole();
        if("customer".equals(role)) {
            final String HISTORY_PATH = "/fxml/History.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(HISTORY_PATH));
            Pane historyPane = loader.load();
            HistoryController historyController = loader.getController();
            historyController.setParentContainer(mainPane);
            historyController.setCurrentUser();
            if(mainPane == null){System.out.println("Null");}
            loadContentToMainPane(historyPane);
        }
        else{
            JOptionPane.showMessageDialog(null,"You are not a customer");
        }
    }

    @FXML
    private void gotoAIChatbot(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Chatbot.fxml"));
            VBox chatbotPane = loader.load();
            mainPane.getChildren().setAll(chatbotPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toggleLogoutDropdown(MouseEvent mouseEvent) {
        accountDropdown.setVisible(true);
    }

    @FXML
    void hideLogoutDropdown(MouseEvent event) {
        accountDropdown.setVisible(false);
    }

    @FXML
    void gotoShopViewForm(MouseEvent mouseEvent) throws IOException {
        // Load lại trang main với categories
            final String SHOPVIEW_PATH = "/fxml/ShopView.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(SHOPVIEW_PATH));
            Pane shopviewPane = loader.load();
            ShopViewController controller = loader.getController();
            controller.setup(null);
            if(controller == null){
                System.out.println("controller is null");
                System.exit(0);
            }
            controller.setItemSelectedHandler(product ->{
                try{
                    handleItemSelected(product);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            loadContentToMainPane(shopviewPane);
    }

    @FXML
    void toggleCategoriesDropdown(MouseEvent mouseEvent) {
        // Load categories view
        loadCategoriesView();
    }

    // Method để load categories view
    private void loadCategoriesView() {
        try {
            VBox categoryBox = categoryBox();
            loadContentToMainPane(categoryBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method để load main view
    private void loadMainView() {
        try {
            clearMainPane();
            initialMain();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogout(ActionEvent actionEvent) {
        Session.setCurrentUser(null);
        System.out.println(Session.getCurrentRole());
        refreshMainView();
        loadMainView();
    }

    @FXML
    void gotoLoginForm(ActionEvent event) {
        try{
            final String LOGIN_PATH = "/fxml/Login.fxml";
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(LOGIN_PATH));
            Parent root = fxmlloader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void gotoSignupForm(ActionEvent event) {
        try{
            final String SIGNUP_PATH = "/fxml/Signup.fxml";
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(SIGNUP_PATH));
            Parent root = fxmlloader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Signup");
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        loadMainView();
        if(Session.isLoggedIn()){
            String role = Session.getCurrentRole();
            if("admin".equals(role)){
                onlyForCustomer.setManaged(false);
                onlyForCustomer.setVisible(false);
            }else if("customer".equals(role)){
                onlyForStaff.setManaged(false);
                onlyForStaff.setVisible(false);
            }
            if(role != null){
                customerName.setText(Session.getCurrentUsername());
                AccountHbox.setVisible(true);
                LoginHbox.setVisible(false);
            }else{
                AccountHbox.setVisible(false);
                LoginHbox.setVisible(true);
                accountDropdown.setVisible(false);
            }
            accountDropdown.setVisible(false);
        }
    }

    void refreshMainView(){
        onlyForCustomer.setManaged(true);
        onlyForCustomer.setVisible(true);
        accountDropdown.setVisible(false);
        onlyForStaff.setManaged(true);
        onlyForStaff.setVisible(true);
        AccountHbox.setVisible(false);
        LoginHbox.setVisible(true);

        // Clear và load lại main view
        clearMainPane();
    }

    void initialMain() throws IOException {
        final String INITIALMAIN_PATH = "/fxml/InitialMainPane.fxml";
        final String BESTSELLERTOP5 = "/fxml/BestSellers_Top5.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(INITIALMAIN_PATH));
        intialMain = loader.load();
        FXMLLoader bestsellerLoader = new FXMLLoader(getClass().getResource(BESTSELLERTOP5));
        Node bestSellersInclude = bestsellerLoader.load();
        BestSellers_Top5Controller bestSellerController = bestsellerLoader.getController();
        bestSellerController.setupType(5);
        intialMain.getChildren().add(bestSellersInclude);
        AnchorPane.setTopAnchor(intialMain, 0.0);
        AnchorPane.setBottomAnchor(intialMain, 0.0);
        AnchorPane.setLeftAnchor(intialMain, 0.0);
        AnchorPane.setRightAnchor(intialMain, 0.0);
        mainPane.getChildren().add(intialMain);
    }

    VBox categoryBox() throws IOException{
        final String CAT = "/fxml/Categories.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CAT));
        VBox category = loader.load();
        CategoriesController controller = loader.getController();
        controller.setItemSelectedHandler(category1 -> {
            try {
                System.out.println(category1);
                handleItemSelected(category1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return category;
    }
    @FXML
    void returnInitialViewClicked(MouseEvent event) {
        clearMainPane();
        loadMainView();
    }
    VBox bestSellerBox() throws IOException{
        final String BESTSELLER_PATH = "/fxml/BestSellers_Top5.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(BESTSELLER_PATH));
        VBox best = loader.load();
        BestSellers_Top5Controller controller = loader.getController();
        controller.setupType(5);
        return best;
    }

    @FXML
    void gotoBestSeller(MouseEvent event) throws IOException {
        final String BESTSELLER_PATH = "/fxml/BestSellers_Top5.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(BESTSELLER_PATH));
        VBox best = loader.load();
        BestSellers_Top5Controller controller = loader.getController();
        controller.setupType(20);
        AnchorPane.setTopAnchor(best, 0.0);
        AnchorPane.setBottomAnchor(best, 0.0);
        AnchorPane.setLeftAnchor(best, 0.0);
        AnchorPane.setRightAnchor(best, 0.0);
        mainPane.getChildren().setAll(best);
    }

    void handleItemSelected(String category) throws IOException {
        System.out.println(category);

        // Ví dụ: hiển thị chi tiết sản phẩm
        showShopViewByCategory(category);
    }

    void handleItemSelected(InventoryProduct product) throws IOException {
        System.out.println(product.getProductName());
        showProductDetails(product);
    }

    void showShopViewByCategory(String category) throws IOException {
        final String SHOPVIEW_CATEGORY = "/fxml/ShopView.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(SHOPVIEW_CATEGORY));
        Pane pane = loader.load();
        ShopViewController controller = loader.getController();
        controller.setup(category);
        loadContentToMainPane(pane);
    }

    void showProductDetails(InventoryProduct product) throws IOException {
        final String PRODUCT_DETAIL_PATH = "/fxml/ProductDetail.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PRODUCT_DETAIL_PATH));
        ScrollPane pane = loader.load();
        ProductDetailController controller = loader.getController();
        controller.setParentContainer(mainPane);
        controller.setNavigationHandler(product1 -> {
            try {
                handleItemSelected(product1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }); // Truyền handler
        controller.setup(product);
        if(pane == null){
            System.out.println("pane is null");
        }
        loadContentToMainPane(pane);
    }
}
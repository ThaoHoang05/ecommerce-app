package view;

import com.stationeryshop.controller.LoginFormController;
import com.stationeryshop.controller.UserController;
import com.stationeryshop.dao.UserDAO;
import com.stationeryshop.model.User;
import com.stationeryshop.utils.Session;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginFormControllerTestRenamed {
    private LoginFormController controller;
    private UserController mockUserController;
    private UserDAO mockUserDAO;

    @BeforeEach
    void setUp() {
        controller = new LoginFormController();
        mockUserController = mock(UserController.class);
        mockUserDAO = mock(UserDAO.class);

        controller.usernameField = new TextField();
        controller.passwordField = new PasswordField();
    }

    @Test
    void testHandleLoginSuccess() {
        controller.usernameField.setText("admin");
        controller.passwordField.setText("123456");

        when(mockUserController.handleLogin("admin", "123456")).thenReturn(1);
        when(mockUserDAO.getUser("admin")).thenReturn(new User());

        controller.handleLogin(mock(ActionEvent.class));

        assertEquals(Session.getCurrentUser(), mockUserDAO.getUser("admin"));
    }

    @Test
    void testHandleLoginWrongPassword() {
        controller.usernameField.setText("admin");
        controller.passwordField.setText("wrongpass");

        when(mockUserController.handleLogin("admin", "wrongpass")).thenReturn(0);

        controller.handleLogin(mock(ActionEvent.class));

        verifyStatic(JOptionPane.class, times(1));
        JOptionPane.showMessageDialog(null, "Wrong Password", "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private void verifyStatic(Class<JOptionPane> jOptionPaneClass, VerificationMode times) {

    }

    @Test
    void testHandleLoginUserNotFound() {
        controller.usernameField.setText("nonexistentUser");
        controller.passwordField.setText("password");

        when(mockUserController.handleLogin("nonexistentUser", "password")).thenReturn(-1);

        controller.handleLogin(mock(ActionEvent.class));

        verifyStatic(JOptionPane.class, times(1));
        JOptionPane.showMessageDialog(null, "User Not Found", "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
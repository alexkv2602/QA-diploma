package ru.netology.web.data;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbHelper {
    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.username");
    private static final String password = System.getProperty("db.password");

    public DbHelper() {
    }

    @SneakyThrows
    public static String getPaymentStatusByDebitCard() {
        String statusBD = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement payStatus = connection.createStatement();
             ResultSet rs = payStatus.executeQuery(statusBD)
        ) {
            if (rs.next()) {
                String status = rs.getString(1);
                return status;
            }
            return null;
        }
    }

    @SneakyThrows
    public static String getPaymentStatusByCreditCard() {
        String statusBD = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement payStatus = connection.createStatement();
             ResultSet rs = payStatus.executeQuery(statusBD)
        ) {
            if (rs.next()) {
                String status = rs.getString(1);
                return status;
            }
            return null;
        }
    }
}

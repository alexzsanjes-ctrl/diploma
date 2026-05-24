package ru.netology.diploma.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    private SQLHelper() {

    }

    @SneakyThrows
    public static DataHelper.Status getPaymentStatus() {
        var statusSQL = "SELECT *FROM payment_entity pe\n" +
                "ORDER BY created DESC LIMIT 1";
        try (var conn = getConnection()) {
            return runner.query(conn, statusSQL, new BeanHandler<>(DataHelper.Status.class));
        }
    }

    @SneakyThrows
    public static DataHelper.Status getCreditStatus() {
        var statusSQL = "SELECT *FROM credit_request_entity cre\n" +
                "ORDER BY created DESC LIMIT 1";
        try (var conn = getConnection()) {
            return runner.query(conn, statusSQL, new BeanHandler<>(DataHelper.Status.class));
        }
    }

    @SneakyThrows
    public static void clearPaymentTable () {
        var paymentSQL = "DELETE FROM payment_entity";
        try (var conn = getConnection()) {
            runner.update(conn, paymentSQL);
        }
    }

    @SneakyThrows
    public static void clearCreditTables () {
        var creditSQL = "DELETE FROM credit_request_entity";
        try (var conn = getConnection()) {
            runner.update(conn,creditSQL);
        }
    }

}


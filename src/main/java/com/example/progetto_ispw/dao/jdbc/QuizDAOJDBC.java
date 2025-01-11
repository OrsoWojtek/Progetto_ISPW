package com.example.progetto_ispw.dao.jdbc;

import com.example.progetto_ispw.Connessione;
import com.example.progetto_ispw.exception.ConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class QuizDAOJDBC {
    private final Connessione connessione;
    private final Connection connection;
    private PreparedStatement statement = null;

    public QuizDAOJDBC() throws ConnectionException {
        connection = Connessione.getInstance().getConnect();
        connessione = Connessione.getInstance();
    }
}

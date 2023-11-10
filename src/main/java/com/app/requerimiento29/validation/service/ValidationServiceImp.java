package com.app.requerimiento29.validation.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImp implements ValidationService{

    private final JdbcTemplate jdbcTemplate;

    public ValidationServiceImp(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Call procedure to update status to Pagado
    @Override
    public void validatePayment(Long propertyId) {
        String procedureCall = "{call PKG_DISCOUNT_REGISTRATION.UPDATESTATUS(?)}";
        try  {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            CallableStatement callableStatement = connection.prepareCall(procedureCall);
            callableStatement.setLong(1, propertyId);
            callableStatement.execute();
            callableStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar el pago.", e);
        }
    }
    
    // Call procedur to execute batch processing
    @Override
    public void executeValidation() {
        String procedureCall = "{call PKG_DISCOUNT_REGISTRATION.CHECKANDUPDATESTATUS}";
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            CallableStatement callableStatement = connection.prepareCall(procedureCall);
            callableStatement.execute();
            callableStatement.close();;
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar.", e);
        }
    }

}

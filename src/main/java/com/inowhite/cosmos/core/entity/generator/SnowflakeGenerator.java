package com.inowhite.cosmos.core.entity.generator;

import lombok.SneakyThrows;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SnowflakeGenerator implements IdentifierGenerator {

  private static final String PROCEDURE_CALL = "select id_generator() as id";

  @SneakyThrows
  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
    long result;

    Statement stmt = null;
    ResultSet rs = null;

    try {
      Connection connection = session.connection();
      stmt = connection.createStatement();
      rs = stmt.executeQuery(PROCEDURE_CALL);

      if (rs.next()) {
        result = rs.getLong("id");
      } else {
        throw new SQLException("failed to get new id from database");
      }

    } catch (SQLException e) {
      throw new HibernateException(e);
    } finally {
      if (rs != null) {
        rs.close();
      }

      if (stmt != null) {
        stmt.close();
      }
    }

    return result;
  }

}

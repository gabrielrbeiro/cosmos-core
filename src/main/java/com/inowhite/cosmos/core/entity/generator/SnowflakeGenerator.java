/*
 * Cosmos - IT Management and Service Desk System
 * Copyright (C) 2020  Gabriel Ribeiro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

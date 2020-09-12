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

package com.inowhite.cosmos.core.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControllerUtil {

  public static List<String> getOrderSpecAsList(String specs) {
    if (specs != null && !specs.isBlank()) {
      var chunks = specs.split(";");
      return new ArrayList<>(Arrays.asList(chunks));
    }

    return null;
  }

}

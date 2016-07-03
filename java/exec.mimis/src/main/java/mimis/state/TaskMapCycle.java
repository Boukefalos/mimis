/**
 * Copyright (C) 2015 Rik Veenboer <rik.veenboer@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package mimis.state;

import mimis.input.Input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.util.ArrayCycle;

public class TaskMapCycle extends ArrayCycle<TaskMap> implements Input {
    protected static final long serialVersionUID = 1L;
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
}

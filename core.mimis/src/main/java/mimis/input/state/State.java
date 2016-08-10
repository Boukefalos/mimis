/**
 * Copyright (C) 2016 Rik Veenboer <rik.veenboer@gmail.com>
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
package mimis.input.state;

import mimis.Component;
import mimis.input.Button;
import mimis.input.Input;
import mimis.input.state.State;

public abstract class State implements Input {
    protected static final long serialVersionUID = 1L;

    protected Button button;
    protected Component component;

    public Button getButton() {
        return button;
    }

    public State(Button button) {
        this.button = button;
    }

    public void setComponent(Component component) {
        this.component = component;        
    }

    public Component getComponent() {
        return component;
    }

    public boolean equals(State state, boolean type) {
        return (type || state.getClass().equals(getClass())) && state.getButton().equals(button);
    }
}

package pm;

import java.lang.reflect.InvocationTargetException;

import pm.action.Action;
import pm.exception.ActionException;
import pm.exception.action.InvokeActionException;

public abstract class Target {
    public void invoke(Action action) throws ActionException {
        try {
            action.getMethod(this).invoke(this);
            return;
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {}
        throw new InvokeActionException();
        // Todo: informatie doorgeven over wat er precies is foutgegaan
    }
}

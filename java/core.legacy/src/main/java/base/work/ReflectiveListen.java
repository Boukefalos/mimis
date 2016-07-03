package base.work;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import base.worker.Worker;

public class ReflectiveListen extends Listen<Object> {
    public ReflectiveListen() {
        super();
    }
    
    public ReflectiveListen(Worker.Type workerType) {
        super(workerType);
    }

    public void input(Object object) {
        Class<?> clazz = object.getClass();        
        MethodType methodType = MethodType.methodType(void.class, clazz);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle methodHandle;
        try {
            methodHandle = lookup.findVirtual(getClass(), "input", methodType);
            methodHandle.invoke(this, object);
        } catch (Exception e) {
            logger.error("", e);
        } catch (Throwable e) {
            logger.error("", e);
        }
    }
}

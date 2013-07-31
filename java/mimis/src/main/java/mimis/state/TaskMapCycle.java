package mimis.state;

import mimis.input.Input;
import mimis.util.ArrayCycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskMapCycle extends ArrayCycle<TaskMap> implements Input {
    protected static final long serialVersionUID = 1L;
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
}

package Networking;
import java.io.Serializable;

public class Command implements Serializable {
    private String methodName;
    private Object[] parameters;

    public Command(String methodName, Object[] parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }
}


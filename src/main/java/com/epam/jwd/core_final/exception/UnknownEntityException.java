package com.epam.jwd.core_final.exception;

public class UnknownEntityException extends RuntimeException {

    private final String entityName;
    private final Object[] args;

    public UnknownEntityException(String entityName) {
        super();
        this.entityName = entityName;
        this.args = null;
    }

    public UnknownEntityException(String entityName, Object[] args) {
        super();
        this.entityName = entityName;
        this.args = args;
    }

    @Override
    public String getMessage() {
        // todo
        // you should use entityName, args (if necessary)
        StringBuilder message = new StringBuilder("Entity {" + entityName + "} - ");
        if(args != null) {
            message.append("[ Args:");
            for(Object arg: args){
                message.append(arg.toString() + " ");
            }
            message.append("]");
        }
        return message.append("is unknown.").toString();
    }
}

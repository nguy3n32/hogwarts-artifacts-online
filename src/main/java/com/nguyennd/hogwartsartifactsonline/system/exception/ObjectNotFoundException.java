package com.nguyennd.hogwartsartifactsonline.system.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String objectType, String id) {
        super("Could not find "+ objectType +" with Id - "+ id +" :(");
    }

    public ObjectNotFoundException(String objectType, int id) {
        super("Could not find "+ objectType +" with Id - "+ id +" :(");
    }

}

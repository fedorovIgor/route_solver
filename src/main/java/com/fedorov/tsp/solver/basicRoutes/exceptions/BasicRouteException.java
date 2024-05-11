package com.fedorov.tsp.solver.basicRoutes.exceptions;

public class BasicRouteException extends RuntimeException{

    public BasicRouteException(String message) {
        super(message);
    }

    public BasicRouteException(String message, Throwable cause) {
        super(message, cause);
    }
}

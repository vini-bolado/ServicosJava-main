package com.soulcode.Servicos.Services.Exceptions;

public class DataIntegrityViolationException  extends RuntimeException{

    public DataIntegrityViolationException(String msg){
        super(msg);
    }
}

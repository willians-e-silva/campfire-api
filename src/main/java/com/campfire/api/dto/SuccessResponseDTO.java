package com.campfire.api.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponseDTO {
    private String message;
    private Object response;

    // Getter
    public String getMessage() {
        return message;
    }

    // Setter
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter
    public Object getResponse(){
        return response;
    };

    // Setter
    public void setResponse(Object response){
        this.response = response;
    };
}
package com.asi.hsg;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

// https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html#java17-handler-example
public class HelloWorldHandler implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String s, Context context) {
        return "Hello World: " + s + "!";
    }

}

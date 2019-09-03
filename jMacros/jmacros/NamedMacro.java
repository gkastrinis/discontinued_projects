package jmacros;

import java.util.ArrayList;

/**
 *
 * @author George Kastrinis
 */
public class NamedMacro
{
    String              name;
    ArrayList<String>   args;
    String              body;
    
    
    NamedMacro (String name_, ArrayList<String> args_, String body_)
    {
        name = name_;
        args = args_;
        body = body_;
    }

}

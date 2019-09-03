package jmacros;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


/**
 *
 * @author George Kastrinis
 */
public class JMacros
{
    static int                          lines;
    static Scanner                      scanner;
    static HashMap<String, Integer>     flags       = new HashMap<String, Integer>();
    static HashMap<String, NamedMacro>  namedMacros = new HashMap<String, NamedMacro>();
    static ArrayList<Loop>              activeLoops = new ArrayList<Loop>();
    static ArrayList<VarValue>          activeMacros = new ArrayList<VarValue>();


    public static void main (String[] args)
    {
        try
        {
            flags.put("TRUE", 1);
            for (int i = 0 ; i < args.length - 1 ; i++)
               if ( args[i].startsWith("-D") ) flags.put(args[i].substring(2), 1);

            File file = new File(args[args.length - 1]);
            // TODO: code here to get path for usage in future includes
            scanner = new Scanner(file);

            parse();
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Cannot open file `" + args[args.length - 1] + "`");
        }
        catch (IOException e)
        {
            System.err.println("Error encountered while parsing: " + e.getMessage());
        }
        catch (ParseException e)
        {
            System.err.println("Parse Error: " + e.getMessage());
        }
    }


    static void parse () throws IOException, ParseException
    {
        while ( scanner.hasNext() )
        {
            // Doen't start with a delimeter
            if ( !scanner.hasNext("^[\\@\\$].*") )
            {
                scanner.useDelimiter("[\\@\\$]");
                String text = scanner.next();
                System.out.print(text);
                for (int i = 0 ; i < text.length() ; i++) if ( text.charAt(i) == '\n' ) lines++;
                scanner.reset();
            }
            
            if ( scanner.hasNext("\\@.*") )
                jmacroStart();
            else if ( scanner.hasNext("\\$.*") )
                expression();
        }
    }

    static void jmacroStart () throws IOException, ParseException
    {
        // '@' marked the end of a macro
        if ( !scanner.hasNext("\\@\\{.*") ) return; ///// check that @ was needed to close (not in define etc)

        scanner.skip("\\@\\{");
        
        if      ( scanner.hasNext("\\:.*") )         include();
        else if ( scanner.hasNext("\\#.*") )         define();
        else if ( scanner.hasNext("[\\?\\!\\(].*") ) condition();
        else                                         loop();
    }

    static void include() throws IOException, ParseException
    {
        scanner.skip("\\:");
        scanner.useDelimiter("\\}");
        String path = scanner.next();
        scanner.skip("\\}");

        Scanner temp = scanner;
        try
        {
            scanner = new Scanner(new File(path));
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Cannot open file `" + path + "`");
        }

        parse();

        scanner = temp;
        scanner.reset();
    }
    
    static void define () throws IOException, ParseException
    {
        scanner.skip("\\#");

        // Defining a flag
        if ( scanner.hasNext("[a-zA-Z\\_][a-zA-Z\\_0-9]*\\}.*") )
        {
            scanner.useDelimiter("\\}");

            String flag = scanner.next("[a-zA-Z\\_][a-zA-Z\\_0-9]*");
            if ( flag.equals("TRUE") ) throw new ParseException("Cannot redifine flag `TRUE`", lines);
            flags.put(flag, 1);

            scanner.skip("\\}");
        }
        // Defining a named macro
        else if ( scanner.hasNext("[a-zA-Z\\_][a-zA-Z\\_0-9]*\\(.*") )
        {
            scanner.useDelimiter("\\(");

            String macro = scanner.next("[a-zA-Z\\_][a-zA-Z\\_0-9]*");
            scanner.skip("\\(");
            ArrayList<String> arguments = readArguments(true);
            scanner.skip("\\}");
            String body = readBody();
            namedMacros.put(macro, new NamedMacro(macro, arguments, body));
        }
        else
            throw new ParseException("Only Flags and Named Macros are supported", lines);

        scanner.reset();
    }
    
    static void condition () throws IOException, ParseException
    {
        if ( !checkFlagPrologue() ) readBody();
    }
    
    static void loop () throws IOException, ParseException
    {
        scanner.useDelimiter("\\,");
        int loops = scanner.nextInt();
        scanner.skip("\\,");
        scanner.useDelimiter("\\}");
        scanner.next("\\s*([a-zA-Z\\_][a-zA-Z\\_0-9]*)\\s*.*");
        String loopVar = scanner.match().group(1);
        scanner.skip("\\}");

        Loop thisLoop = new Loop(loops, loopVar);
        activeLoops.add(thisLoop);

        byte[] byteArray = readBody().getBytes();

        Scanner temp = scanner;
        for (int i = 0 ; i < loops ; i++, thisLoop.current++)
        {
            scanner = new Scanner(new ByteArrayInputStream(byteArray));
            parse();
        }
        activeLoops.remove(thisLoop);
        scanner = temp;
        scanner.reset();
    }
    
    static void expression () throws IOException, ParseException
    {
        scanner.skip("\\$"); 

        // Named Macro Expression
        if ( scanner.hasNext("[a-zA-Z\\_][a-zA-Z\\_0-9]*\\(.*") )
        {
            scanner.useDelimiter("\\(");

            String macro = scanner.next("[a-zA-Z\\_][a-zA-Z\\_0-9]*");
            NamedMacro namedMacro = namedMacros.get(macro);
            if ( namedMacro == null ) throw new ParseException("Named Macro used but not yet defined", lines);
            scanner.skip("\\(");
            int currentStart = activeMacros.size();
            ArrayList<String> formal = namedMacro.args;
            ArrayList<String> arguments = readArguments(false);
            for (int i = 0 ; i < arguments.size() && i < formal.size() ; i++)
            {
                activeMacros.add(new VarValue(formal.get(i), Integer.parseInt(arguments.get(i))));
            }

            Scanner temp = scanner;
            scanner = new Scanner(new ByteArrayInputStream(namedMacro.body.getBytes()));

            parse();

            for (int i = currentStart ; i < activeMacros.size() ; i++) activeMacros.remove(i);
            scanner = temp;
            scanner.reset();
        }
        // Variable Expression
        else if ( scanner.hasNext("[a-zA-Z\\_][a-zA-Z\\_0-9]*.*") )
        {
            String token = scanner.next("[a-zA-Z\\_][a-zA-Z\\_0-9]*");
            
            int pos = activeMacros.lastIndexOf(new VarValue(token));
            if ( pos == -1 )
            {
                pos = activeLoops.lastIndexOf(new Loop(token));
                if ( pos == -1  ) throw new ParseException("Undefined Variable used in Expression", lines);
                System.out.print(activeLoops.get(pos).current);
            }
            else
            {
                System.out.print(activeMacros.get(pos).value);
            } 
        }
        else
            throw new ParseException("Only Named Macros and Variables are supported", lines);
    }



    static boolean checkFlagPrologue () throws IOException, ParseException
    {
        boolean negative = false;

        scanner.useDelimiter("[\\!\\(\\?]");
        scanner.skip("\\s*");

        if ( scanner.findWithinHorizon("\\!", 1) != null )
        {
            negative = true;
            scanner.findWithinHorizon("\\?", 1);
        }
        else if ( scanner.findWithinHorizon("\\(", 1) != null )
            return checkFlagPrologue();
        else if ( scanner.findWithinHorizon("\\?", 1) == null )
            throw new ParseException("Parse error3", -1);
        
        scanner.reset();
        
        return ( negative ? !checkFlag() : checkFlag() );
    }

    static boolean checkFlag () throws IOException, ParseException
    {
        scanner.useDelimiter("[\\|\\&\\)\\}]");
        
        scanner.next("([a-zA-Z\\_][a-zA-Z\\_0-9]*)\\s*.*");
        boolean exists = flags.get(scanner.match().group(1)) != null;

        if ( scanner.findWithinHorizon("[\\)\\}]", 1) != null ) return exists;

        char oper = scanner.findWithinHorizon("[\\|\\&]", 1).charAt(0);
        // Short-circuit evaluation
        if ( oper == '|' && exists == true )
        {
            scanner.skip(".*}");
            return true;
        }
        else if ( oper == '&' && exists == false )
        {
            scanner.skip(".*}");
            return false;
        }

        boolean rest = checkFlagPrologue();

        return ( oper == '|' ? exists | rest : exists & rest );
    }

    static ArrayList<String> readArguments (boolean formalParameters) throws IOException, ParseException
    {
        scanner.useDelimiter("[\\,\\)]");
        
        ArrayList<String> arguments = new ArrayList<String>();
        while ( scanner.hasNext() )
        {
            if ( scanner.findWithinHorizon("\\)", 1) != null ) break;

            if ( formalParameters )
                scanner.next("\\s*([a-zA-Z\\_][a-zA-Z\\_0-9]*)\\s*");
            else
                scanner.next("\\s*([0-9]*)\\s*");

            arguments.add(scanner.match().group(1));
        }
        
        scanner.reset();
        
        return arguments;
    }
    
    static String readBody() throws IOException, ParseException
    {
        scanner.useDelimiter("\\@");
        
        int active = 0;
        StringBuilder tmp = new StringBuilder();
        while ( scanner.hasNext() )
        {
            tmp.append(scanner.next());
            // Start of a macro
            if ( scanner.findWithinHorizon("\\@\\{", 2) != null )
            {
                active++;
                tmp.append("@{");
            }
            // End of an internal macro
            else if ( active != 0 )
            {
                active--;
                tmp.append('@');
                scanner.findWithinHorizon("\\@", 1);
            }
            // End of the external macro
            else
                break;
        }
        
        scanner.reset();
        
        return tmp.toString();
    }
    
}
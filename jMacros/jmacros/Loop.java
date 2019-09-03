package jmacros;

/**
 *
 * @author George Kastrinis
 */
class Loop
{
    int     total;
    int     current;
    String  variable;

    Loop (int total_, String variable_)
    {
        total = total_;
        current = 0;
        variable = variable_;
    }

    Loop (String variable_)
    {
        variable = variable_;
    }

    @Override
    public boolean equals (Object o)
    {
        if(o instanceof Loop)
        {
            Loop tmp = (Loop) o;
            if( this.variable.equals(tmp.variable) )
                return true;
            return false;
        }
        return false;
    }

    @Override
    public int hashCode ()
    {
        int hash = 7;
        hash = 29 * hash + (this.variable != null ? this.variable.hashCode() : 0);
        return hash;
    }
}

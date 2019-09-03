package jmacros;

/**
 *
 * @author George Kastrinis
 */
class VarValue
{
    String  variable;
    int     value;
    
    VarValue(String variable_, int value_)
    {
        variable = variable_;
        value = value_;
    }

    VarValue(String variable_)
    {
        variable = variable_;
    }

    @Override
    public boolean equals (Object o)
    {
        if(o instanceof VarValue)
        {
            VarValue tmp = (VarValue) o;
            if( this.variable.equals(tmp.variable) )
                return true;
            return false;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 59 * hash + (this.variable != null ? this.variable.hashCode() : 0);
        return hash;
    }
}

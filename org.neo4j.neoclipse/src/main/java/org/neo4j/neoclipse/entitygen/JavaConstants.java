package org.neo4j.neoclipse.entitygen;


public final class JavaConstants
{
    public static final String[] JAVA_KEYWORDS = new String[] {//
    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default",
            "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "if", "goto", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected",
            "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw",
            "throws", "transient", "try", "void", "volatile", "while", "true", "false", "null" };

    public static final String JAVA_IDENTIFIER_REG_EX = "[a-zA-z]\\w*";

    public static boolean isValidJavaIdentifier( final String keyWord )
    {
        if ( !keyWord.matches( JAVA_IDENTIFIER_REG_EX ) )
        {
            return false;
        }
        for ( String str : JAVA_KEYWORDS )
        {
            if ( str.equals( keyWord ) )
            {
                return false;
            }
        }


        return true;
    }

}

package org.neo4j.neoclipse.entitygen;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.neoclipse.editor.NodeWrapper;

public final class EntityList
{

    private final String packageName;
    private final String classNamePattern;
    private final Set<NodeWrapper> nodeWrappers = new HashSet<NodeWrapper>();

    public EntityList( String packageName, String classNamePattern )
    {
        if ( packageName.trim().isEmpty() )
        {
            throw new RuntimeException( "Package name required" );
        }
        if ( packageName.contains( " " ) )
        {
            throw new RuntimeException( "Space not allowed in package name" );
        }
        if ( !packageName.matches( EntityWriter.PACKAGE_REG_EX ) )
        {
            throw new RuntimeException( "Invalid Package name" );
        }

        this.packageName = packageName;
        this.classNamePattern = classNamePattern;
    }

    public Set<NodeWrapper> getNodeWrappers()
    {
        return nodeWrappers;
    }

    public void addNodeWrapper( NodeWrapper nodeWrapper )
    {
        this.nodeWrappers.add( nodeWrapper );
    }

    public String getPackageName()
    {
        return packageName;
    }

    public String getClassNamePattern()
    {
        return classNamePattern;
    }

}

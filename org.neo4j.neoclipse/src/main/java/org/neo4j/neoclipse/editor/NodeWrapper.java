package org.neo4j.neoclipse.editor;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.neoclipse.util.ApplicationUtil;

public class NodeWrapper extends BaseWrapper
{

    private static final long serialVersionUID = 1L;
    private Set<RelationshipWrapper> relation = new HashSet<RelationshipWrapper>();

    public NodeWrapper( long id )
    {
        super( id );
    }

    public Set<RelationshipWrapper> getRelations()
    {
        return relation;
    }


    public void addRelation( RelationshipWrapper relation )
    {
        this.relation.add( relation );
    }


    @Override
    public String toString()
    {
        return ApplicationUtil.toJson( this );
    }

}

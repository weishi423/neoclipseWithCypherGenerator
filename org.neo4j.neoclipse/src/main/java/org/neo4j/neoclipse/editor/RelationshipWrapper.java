package org.neo4j.neoclipse.editor;



public class RelationshipWrapper extends BaseWrapper
{

    private static final long serialVersionUID = 1L;
    private NodeWrapper endNode;
    private String relationshipType;

    public RelationshipWrapper( long id )
    {
        super( id );
    }

    public NodeWrapper getEndNode()
    {
        return endNode;
    }

    public void setEndNode( NodeWrapper endNode )
    {
        this.endNode = endNode;
    }

    public String getRelationshipType()
    {
        return relationshipType;
    }

    public void setRelationshipType( String relationshipType )
    {
        this.relationshipType = relationshipType;
    }

}

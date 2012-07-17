package org.neo4j.neoclipse.entitygen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.neo4j.neoclipse.Activator;
import org.neo4j.neoclipse.editor.NodeWrapper;
import org.neo4j.neoclipse.editor.RelationshipWrapper;

public class EntityWriter
{

    private static final String ROOT_FOLDER = "templates" + File.separator;
    private static final String BASE_NODE_JAVA = ROOT_FOLDER + "BaseNode.java";
    private static final String NODE_ENTITY_JAVA = ROOT_FOLDER + "NodeEntity.java";
    private static final String RELATIONSHIP_ENTITY_JAVA = ROOT_FOLDER + "RelationShipEntity.java";
    private static final String GLOBAL_RELATION_JAVA = ROOT_FOLDER + "GlobalRelationShip.java";
    //
    public static final String PACKAGE_REG_EX = "([a-zA-Z_]{1}[a-zA-Z0-9_]*(.[a-z_]{1}[a-zA-Z0-9_]*)*)+$";
    public static final String CLASSNAME_REG_EX = "([a-zA-Z_]{1}[a-zA-Z0-9_]*)+$";

    public static void generateEntities( final File directory, final EntityList entityList ) throws Exception
    {

        validate( directory, entityList );
        String packageName = entityList.getPackageName();
        // CREATE BASENODE
        createBaseEntity( packageName, directory );
        String classNamePattern = entityList.getClassNamePattern();

        for ( NodeWrapper nw : entityList.getNodeWrappers() )
        {
            Map<String, Object> propertyMap = nw.getPropertyMap();
            final String className = (String) propertyMap.get( classNamePattern );
            propertyMap.remove( classNamePattern );
            Set<Entry<String, Object>> entrySet = propertyMap.entrySet();
            // Java Contents
            StrBuilder nodeStringBuilder = getJavaFileContent( NODE_ENTITY_JAVA, packageName );
            nodeStringBuilder.replaceAll( "_ENTITY_", className );
            // MEMBER FIELDS
            StrBuilder memberFieldsString = addMemberFields( entrySet );
            nodeStringBuilder.replaceAll( "_FIELDS_", memberFieldsString.toString() );
            // Create Setters and Getters for each Properties
            StrBuilder setterAndGetterBuilder = createSettersAndGetters( entrySet );
            nodeStringBuilder.replaceAll( "_GETTER_AND_SETTER_", setterAndGetterBuilder.toString() );
            //
            createJavaFile( nodeStringBuilder, packageName, directory, className + ".java" );
            Set<RelationshipWrapper> relations = nw.getRelations();
            for ( RelationshipWrapper rw : relations )
            {
                // RELATION
                createJavaFileForEachRelation( nw, rw, packageName, directory, classNamePattern );
            }

        }

        zip( directory );
    }

    private static void createJavaFile( final StrBuilder strBuilder, final String packageName, final File directory,
            final String className ) throws IOException
    {
        final String fileName = packageName.replace( ".", File.separator ) + File.separator + className;
        File javaFile = new File( directory, fileName );
        FileUtils.writeStringToFile( javaFile, strBuilder.toString(), "UTF-8" );
    }

    private static StrBuilder createSettersAndGetters( final Set<Entry<String, Object>> entrySet )
    {
        StrBuilder strBuilder = new StrBuilder();
        // Create Setters and Getters
        for ( Entry<String, Object> entry : entrySet )
        {
            String field = entry.getKey();
            String clazz = entry.getValue().getClass().getSimpleName();
            // GETTER
            addGetter( strBuilder, field, clazz );
            // SETTER
            addSetter( strBuilder, field, clazz );
        }
        strBuilder.appendNewLine();
        return strBuilder;

    }

    private static StrBuilder addMemberFields( final Set<Entry<String, Object>> entrySet )
    {
        StrBuilder strBuilder = new StrBuilder();
        for ( Entry<String, Object> entry : entrySet )
        {
            String field = entry.getKey();
            Class<? extends Object> fieldClass = entry.getValue().getClass();
            // MEMBER_FIELDS
            strBuilder.append( "\tprivate " );
            strBuilder.append( fieldClass.getSimpleName() + " " );
            strBuilder.append( field + ";" );
            strBuilder.appendNewLine();
        }
        return strBuilder;
    }

    private static void addSetter( final StrBuilder strBuilder, final String field, final String clazz )
    {
        strBuilder.append( "\tpublic void " );
        strBuilder.append( "set" + StringUtils.capitalize( field ) + "(" + clazz + " " + field + "){" );
        strBuilder.appendNewLine();
        strBuilder.append( "\t\tthis." + field + " = " + field + ";" );
        strBuilder.appendNewLine();
        strBuilder.append( "\t}" );
        strBuilder.appendNewLine();
    }

    private static void addGetter( final StrBuilder strBuilder, final String fieldName, final String clazz )
    {
        strBuilder.append( "\tpublic " );
        strBuilder.append( clazz + " " );
        strBuilder.append( "get" + StringUtils.capitalize( fieldName ) + "{" );
        strBuilder.appendNewLine();
        strBuilder.append( "\t\treturn this." + fieldName + ";" );
        strBuilder.appendNewLine();
        strBuilder.append( "\t}" );
        strBuilder.appendNewLine();
    }

    private static void createJavaFileForEachRelation( final NodeWrapper startNode, final RelationshipWrapper rw,
            final String packageName, File directory, final String classNamePattern ) throws IOException
    {

        Map<String, Object> propertyMap = rw.getPropertyMap();
        final String relationShipType = rw.getRelationshipType();

        final String className = (String) propertyMap.get( classNamePattern );
        propertyMap.remove( classNamePattern );
        Set<Entry<String, Object>> entrySet = propertyMap.entrySet();
        // Java Contents
        StrBuilder nodeStringBuilder = getJavaFileContent( RELATIONSHIP_ENTITY_JAVA, packageName );
        nodeStringBuilder.replaceAll( "_RELATIONSHIP_TYPE_", relationShipType );
        nodeStringBuilder.replaceAll( "_ENTITY_", className );
        // MEMBER FIELDS
        StrBuilder memberFieldsString = addMemberFields( entrySet );
        nodeStringBuilder.replaceAll( "_FIELDS_", memberFieldsString.toString() );
        // Create Setters and Getters for each Properties
        StrBuilder setterAndGetterBuilder = createSettersAndGetters( entrySet );
        nodeStringBuilder.replaceAll( "_GETTER_AND_SETTER_", setterAndGetterBuilder.toString() );

        createJavaFile( nodeStringBuilder, packageName, directory, className + ".java" );

    }

    private static void zip( final File directory )
    {
        // TODO Auto-generated method stub

    }

    private static void createBaseEntity( final String packageName, final File directory ) throws IOException
    {
        StrBuilder strBuilder = getJavaFileContent( BASE_NODE_JAVA, packageName );
        createJavaFile( strBuilder, packageName, directory, "BaseNode.java" );
    }

    private static StrBuilder getJavaFileContent( final String templateFile, final String packageName )
            throws IOException
    {
        StrBuilder sb = new StrBuilder( "package " + packageName + ";" );
        InputStream in = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try
        {
            final Path path = new Path( templateFile );
            in = FileLocator.openStream( Activator.getDefault().getBundle(), path, false );
            isr = new InputStreamReader( in );
            br = new BufferedReader( isr );
            sb.appendNewLine();
            String theLine;
            while ( ( theLine = br.readLine() ) != null )
            {
                sb.append( theLine );
                sb.appendNewLine();
            }

        }
        finally
        {
            br.close();
            isr.close();
            in.close();
        }
        return sb;
    }

    private static void validate( final File directory, final EntityList entityList )
    {
        if ( !directory.isDirectory() || !directory.exists() )
        {
            throw new RuntimeException( "Please select valid directory" );
        }
        if ( !directory.canWrite() )
        {
            throw new RuntimeException( "Permission denied to write in the given directory " + directory.getPath()
                                        + " . Please select a different directory" );
        }

        for ( NodeWrapper nw : entityList.getNodeWrappers() )
        {
            Map<String, Object> propertyMap = nw.getPropertyMap();
            if ( nw.getId() != 0 && !propertyMap.containsKey( entityList.getClassNamePattern() ) )
            {
                throw new RuntimeException( String.format( "The node id: %s is missing the property %s.", nw.getId(),
                        entityList.getClassNamePattern() ) );
            }

            Set<Entry<String, Object>> entrySet = propertyMap.entrySet();
            for ( Entry<String, Object> entry : entrySet )
            {
                String fieldName = entry.getKey();
                if ( !JavaConstants.isValidJavaIdentifier( fieldName ) )
                {
                    throw new RuntimeException(
                            String.format(
                                    "The node id: %s is containing invalid property %s. Please use only valid java identifiers+",
                                    nw.getId(), fieldName ) );
                }
            }

            final Set<RelationshipWrapper> relations = nw.getRelations();
            for ( RelationshipWrapper rw : relations )
            {
                if ( !rw.getPropertyMap().containsKey( entityList.getClassNamePattern() ) )
                {
                    throw new RuntimeException( String.format(
                            "The relation id: %s between %s & %s is missing the classname property %s.", rw.getId(),
                            nw.getId(), rw.getEndNode().getId(), entityList.getClassNamePattern() ) );
                }

                Set<Entry<String, Object>> relationEntrySet = rw.getPropertyMap().entrySet();
                for ( Entry<String, Object> entry : relationEntrySet )
                {
                    String fieldName = entry.getKey();
                    if ( !JavaConstants.isValidJavaIdentifier( fieldName ) )
                    {
                        throw new RuntimeException(
                                String.format(
                                        "The Relation id: %s between %s & %s is containing invalid property %s. Please use only valid java identifiers+",
                                        rw.getId(), nw.getId(), rw.getEndNode().getId(), fieldName ) );
                    }
                }
            }
        }
    }

}

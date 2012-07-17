/**
 * Licensed to Neo Technology under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo Technology licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.neo4j.neoclipse.entitygen;

import java.io.File;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.neo4j.neoclipse.Activator;
import org.neo4j.neoclipse.editor.NodeWrapper;
import org.neo4j.neoclipse.graphdb.GraphDbServiceManager;
import org.neo4j.neoclipse.view.ErrorMessage;

/**
 * @author Radhakrishna Kalyan
 * 
 */
public class GenerateEntityDialog extends TitleAreaDialog
{

    private static final int SIZING_TEXT_FIELD_WIDTH = 250;

    private Text packageName;
    private Text classNamePattern;
    private DirectoryFieldEditor directory;

    public GenerateEntityDialog( Shell parentShell )
    {
        super( parentShell );
    }

    @Override
    protected void configureShell( Shell shell )
    {
        super.configureShell( shell );
        shell.setText( "Entity Generation" );
    }

    @Override
    protected void createButtonsForButtonBar( Composite parent )
    {

        super.createButtonsForButtonBar( parent );
        validate();
    }

    @Override
    protected Control createContents( Composite parent )
    {

        Control contents = super.createContents( parent );
        setTitle( "Entity Generation" );
        return contents;
    }

    @Override
    protected Control createDialogArea( Composite parent )
    {

        // top level composite
        Composite parentComposite = (Composite) super.createDialogArea( parent );

        // create a composite with standard margins and spacing
        Composite composite = new Composite( parentComposite, SWT.NONE );
        GridLayout layout = new GridLayout();
        layout.marginHeight = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_MARGIN );
        layout.marginWidth = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_MARGIN );
        layout.verticalSpacing = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_SPACING );
        layout.horizontalSpacing = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_SPACING );
        composite.setLayout( layout );
        composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
        composite.setFont( parentComposite.getFont() );

        Composite nameGroup = new Composite( composite, SWT.NONE );
        layout = new GridLayout();
        layout.numColumns = 3;
        layout.marginWidth = 10;
        nameGroup.setLayout( layout );
        GridData data = new GridData( SWT.FILL, SWT.CENTER, true, false );
        nameGroup.setLayoutData( data );

        Label label = new Label( nameGroup, SWT.WRAP );
        label.setText( ( "Package Name *" ) );
        packageName = new Text( nameGroup, SWT.BORDER );
        data = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL );
        data.horizontalSpan = 2;
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        packageName.setText( "com.example.neo4j.persistence" );
        packageName.setLayoutData( data );
        packageName.addKeyListener( new KeyListener()
        {

            @Override
            public void keyPressed( org.eclipse.swt.events.KeyEvent e )
            {

                validate();
            };

            @Override
            public void keyReleased( org.eclipse.swt.events.KeyEvent e )
            {

                validate();
            };
        } );

        Label label1 = new Label( nameGroup, SWT.WRAP );
        label1.setText( ( "Class Name Property*" ) );
        classNamePattern = new Text( nameGroup, SWT.BORDER );
        data = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL );
        data.horizontalSpan = 2;
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        classNamePattern.setText( "classname" );
        classNamePattern.setLayoutData( data );
        classNamePattern.addKeyListener( new KeyListener()
        {

            @Override
            public void keyPressed( org.eclipse.swt.events.KeyEvent e )
            {

                validate();
            };

            @Override
            public void keyReleased( org.eclipse.swt.events.KeyEvent e )
            {

                validate();
            };
        } );

        directory = new DirectoryFieldEditor( "Directory", "Directory *", nameGroup );
        directory.getTextControl( nameGroup ).addKeyListener( new KeyListener()
        {

            @Override
            public void keyReleased( KeyEvent arg0 )
            {
                validate();
            }

            @Override
            public void keyPressed( KeyEvent arg0 )
            {
                validate();
            }
        } );
        directory.setPropertyChangeListener( new IPropertyChangeListener()
        {

            @Override
            public void propertyChange( PropertyChangeEvent event )
            {
                validate();

            }
        } );

        new Label( nameGroup, SWT.NONE );
        Label label3 = new Label( nameGroup, SWT.WRAP );
        label3.setText( ( "ex: C:/neo4j/stubs " ) );
        data = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL );
        data.horizontalSpan = 2;
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        label3.setLayoutData( data );

        new Label( nameGroup, SWT.NONE );

        Composite connectionPropertiesComposite = new Composite( nameGroup, SWT.NONE );
        connectionPropertiesComposite.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 0;
        connectionPropertiesComposite.setLayout( gridLayout );
        data = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL );
        data.horizontalSpan = 2;
        connectionPropertiesComposite.setLayoutData( data );

        data = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL );
        data.horizontalSpan = 2;
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;

        data = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL );
        data.horizontalSpan = 2;
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;

        new Label( nameGroup, SWT.NONE );

        connectionPropertiesComposite = new Composite( nameGroup, SWT.NONE );
        connectionPropertiesComposite.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 0;
        connectionPropertiesComposite.setLayout( gridLayout );
        data = new GridData( GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL );
        data.horizontalSpan = 2;
        connectionPropertiesComposite.setLayoutData( data );

        return parentComposite;
    }

    @Override
    protected void okPressed()
    {

        try
        {
            final EntityList entityList = new EntityList( packageName.getText(), classNamePattern.getText() );
            final GraphDbServiceManager gsm = Activator.getDefault().getGraphDbServiceManager();
            final List<NodeWrapper> nodeWrapperList = gsm.getAllNodes();

            for ( NodeWrapper nodeWrapper : nodeWrapperList )
            {
                entityList.addNodeWrapper( nodeWrapper );
            }

            EntityWriter.generateEntities( new File( directory.getStringValue() ), entityList );

            ErrorMessage.showDialog( "EntityGen", "Stubs are generated at :" + directory.getStringValue() );
            close();

        }
        catch ( Exception e )
        {
            ErrorMessage.showDialog( "Entity generation problem", e );
        }
    }

    @Override
    protected void setShellStyle( int newShellStyle )
    {
        super.setShellStyle( newShellStyle | SWT.RESIZE );
    }

    private void validate()
    {
        boolean enableDisable = false;

        if ( !directory.getStringValue().trim().isEmpty() && ( validatePackageName() ) && ( validateClassName() ) )
        {
            enableDisable = true;
        }

        Button okBtn = getButton( IDialogConstants.OK_ID );
        if ( okBtn != null )
        {
            okBtn.setEnabled( enableDisable );
        }
    }

    private boolean validatePackageName()
    {
        if ( packageName.getText().isEmpty() || packageName.getText().contains( " " )
             || !packageName.getText().matches( EntityWriter.PACKAGE_REG_EX ) )
        {
            return false;
        }

        return true;
    }

    private boolean validateClassName()
    {
        if ( classNamePattern.getText().isEmpty() || classNamePattern.getText().contains( " " ) )
        {
            return false;
        }
        if ( !classNamePattern.getText().matches( EntityWriter.CLASSNAME_REG_EX ) )
        {
            return false;
        }

        return true;
    }

}

// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.views.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.AbstractTalendEditor;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty;

/**
 * bqian class global comment. Detailled comment
 */
public class SQLPatternComposite extends ScrolledComposite implements IDynamicProperty {

    // private static final String CODE_PROPERTY = "codeProperty";

    private static final String NAME_PROPERTY = "nameProperty";

    private static final int ADD_COLUMN = 0;

    private static final int CODE_COLUMN = 1;

    private List<String> comboContent;

    private Element element;

    private DynamicComboBoxCellEditor dynamicComboBoxCellEditor;

    private Button buttonAdd;

    private Button buttonRemove;

    private TableViewer tableViewer;

    private Text codeText;

    /**
     * yzhang AdvancedContextComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public SQLPatternComposite(Composite parent, int style, final Element element) {
        super(parent, style);
        this.element = element;
        setExpandHorizontal(true);
        setExpandVertical(true);

        FormData layouData = new FormData();
        layouData.left = new FormAttachment(0, 0);
        layouData.right = new FormAttachment(100, 0);
        layouData.top = new FormAttachment(0, 0);
        layouData.bottom = new FormAttachment(100, 0);
        setLayoutData(layouData);

        final Composite panel = new Composite(this, SWT.NONE);
        setContent(panel);
        panel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

        FormLayout layout = new FormLayout();
        layout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
        layout.marginHeight = ITabbedPropertyConstants.VSPACE;
        layout.spacing = ITabbedPropertyConstants.VMARGIN + 1;
        panel.setLayout(layout);

        tableViewer = new TableViewer(panel, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        FormData tableLayoutData = new FormData();
        tableLayoutData.left = new FormAttachment(10, 0);
        tableLayoutData.right = new FormAttachment(90, 0);
        tableLayoutData.top = new FormAttachment(10, 0);
        tableLayoutData.bottom = new FormAttachment(50, 0);

        final Table table = tableViewer.getTable();

        table.setLayoutData(tableLayoutData);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn columnName = new TableColumn(table, SWT.NONE, ADD_COLUMN);
        columnName.setText("SQLPattern List");
        columnName.setWidth(200);
        columnName.setResizable(true);
        columnName.setMoveable(true);

        // TableColumn columnCode = new TableColumn(table, SWT.NONE, CODE_COLUMN);
        // columnCode.setText("Code");
        // columnCode.setWidth(200);
        // columnCode.setResizable(true);
        // columnCode.setMoveable(true);

        Image addImage = ImageProvider.getImage(EImage.ADD_ICON);
        Image removeImage = ImageProvider.getImage(EImage.DELETE_ICON);

        buttonAdd = new Button(panel, SWT.NONE);
        buttonAdd.setText("Add");
        buttonAdd.setImage(addImage);

        FormData buttonAddData = new FormData();
        buttonAddData.left = new FormAttachment(table, 10, SWT.LEFT);
        buttonAddData.top = new FormAttachment(table, 2);
        buttonAdd.setLayoutData(buttonAddData);

        buttonRemove = new Button(panel, SWT.NONE);
        buttonRemove.setText("Delete");
        buttonRemove.setImage(removeImage);

        FormData buttonRemoveData = new FormData();
        buttonRemoveData.left = new FormAttachment(buttonAdd, 1, SWT.RIGHT);
        buttonRemoveData.top = new FormAttachment(table, 2);
        buttonRemove.setLayoutData(buttonRemoveData);

//       Button buttonT = new Button(panel, SWT.NONE);
//       buttonT.setText("Test");
//
//        FormData fd = new FormData();
//        fd.left = new FormAttachment(buttonAdd, 1, SWT.RIGHT);
//        fd.top = new FormAttachment(buttonAdd, 2);
//        fd.right = new FormAttachment(90, 0);
//        fd.bottom = new FormAttachment(100, 0);
        
        
//        buttonT.setLayoutData(fd);
        
        
        
        createCodeControl(panel,buttonAdd);
        setMinSize(panel.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        final List<String> legalParameters = getAllSqlPatterns();

        TableViewerProvider provider = new TableViewerProvider();
        tableViewer.setContentProvider(provider);
        tableViewer.setLabelProvider(provider);
        tableViewer.setCellModifier(new ICellModifier() {

            public boolean canModify(Object element, String property) {
                return comboContent.size() > 0;
            }

            public Object getValue(Object element, String property) {

                if (property.equals(NAME_PROPERTY)) {
                    Map map = (Map) element;
                    String text = (String) map.get(EmfComponent.SQLPATTERNLIST);
                    refreshComboContent(tableViewer);
                    comboContent.add(text);
                    dynamicComboBoxCellEditor.refresh();
                    return text;
                }
                return "";
            }

            public void modify(Object elem, String property, Object value) {

                TableItem item = null;
                if (elem instanceof Table) {
                    item = ((Table) elem).getItem(0);
                } else if (elem instanceof TableItem) {
                    item = (TableItem) elem;
                }

                if (item != null && value != null) {
                    if (property.equals(NAME_PROPERTY)) {
                        Map map = (Map) item.getData();

                        if (map.get(EmfComponent.SQLPATTERNLIST).equals(value)) {
                            return;
                        }
                        map.put(EmfComponent.SQLPATTERNLIST, value);
                        executeCommand(new Command() {
                        });
                        refreshComboContent(tableViewer);
                    }
                    tableViewer.refresh();
                }
            }

        });

        List<Map> tableContent = getTableInput(element);
        tableViewer.setInput(tableContent);

        refreshComboContent(tableViewer);
        // dynamicComboBoxCellEditor = new DynamicComboBoxCellEditor(table, comboContent,
        // comboboxCellEditorLabelProvider);
        dynamicComboBoxCellEditor = new DynamicComboBoxCellEditor(table, comboContent, comboboxCellEditorLabelProvider);
        tableViewer.setCellEditors(new CellEditor[] { dynamicComboBoxCellEditor });
        tableViewer.setColumnProperties(new String[] { NAME_PROPERTY });
        addSelectionChangeListener(tableViewer);
        // if (tableContent.size() == legalParameters.size()) {
        // buttonAdd.setEnabled(false);
        // }

        buttonAdd.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (comboContent.size() > 0) {
                    List<Map> tableInput = (List<Map>) tableViewer.getInput();
                    String parameter = comboContent.get(0);
                    Map map = new HashMap();
                    map.put(EmfComponent.SQLPATTERNLIST, parameter);
                    tableInput.add(map);

                    refreshComboContent(tableViewer);

                    // if (comboContent.size() == 0) {
                    // buttonAdd.setEnabled(false);
                    // }
                    tableViewer.refresh();
                    if (!buttonRemove.isEnabled()) {
                        buttonRemove.setEnabled(true);
                    }
                    executeCommand(new PropertyChangeCommand(element, EParameterName.SQLPATTERN_VALUE.getName(), element
                            .getElementParameter(EParameterName.SQLPATTERN_VALUE.getName()).getValue()));
                }
            }

        });
        // if (tableContent.size() == 0) {
        // buttonRemove.setEnabled(false);
        // }
        buttonRemove.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ISelection selection = tableViewer.getSelection();
                List<Map> tableViewerInput = ((List<Map>) tableViewer.getInput());

                boolean needRefresh = false;
                if (!selection.isEmpty() && selection instanceof StructuredSelection) {
                    Object[] elements = ((StructuredSelection) selection).toArray();
                    for (Object element : elements) {
                        tableViewerInput.remove(element);
                    }
                    needRefresh = true;
                } else if (!tableViewerInput.isEmpty()) {
                    int index = tableViewerInput.size() - 1;
                    tableViewerInput.remove(index);
                    needRefresh = true;
                }

                if (needRefresh) {
                    refreshComboContent(tableViewer);
                    tableViewer.refresh();
                    executeCommand(new PropertyChangeCommand(element, EParameterName.SQLPATTERN_VALUE.getName(), element
                            .getElementParameter(EParameterName.SQLPATTERN_VALUE.getName()).getValue()));
                }
            }
        });
        
      
        
    }

    /**
     * DOC bqian Comment method "createCodeControl".
     * @param panel 
     * @param  
     */
    private void createCodeControl(Composite panel, Control reference) {
        codeText = new Text(panel, SWT.BORDER | SWT.MULTI|SWT.H_SCROLL|SWT.V_SCROLL);
        FormData fd = new FormData();
        fd.left = new FormAttachment(10, 0);
        fd.right = new FormAttachment(90, 0);
        fd.top = new FormAttachment(reference, 2);
        fd.bottom = new FormAttachment(95, 0);
        codeText.setLayoutData(fd);
        
    }
    /**
     * DOC bqian Comment method "addSelectionChangeListener".
     * 
     * @param tableViewer
     */
    private void addSelectionChangeListener(final TableViewer tableViewer) {
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                Object o = ((IStructuredSelection) tableViewer.getSelection()).getFirstElement();
                if (o == null) {
                    return;
                }
                Map map = (Map) o;
                Object object = map.get(EmfComponent.SQLPATTERNLIST);
                String sqlpatternName = null;
                if (object instanceof String) {
                    sqlpatternName = (String) object;
                } else {
                    TableItem item = tableViewer.getTable().getSelection()[0];
                    sqlpatternName = item.getText();
                }

                String eltNodeName = (String) element.getElementParameter(EParameterName.SQLPATTERN_DB_NAME.getName()).getValue();

                SQLPatternItem sqlpatternItem = null;
                try {
                    List<IRepositoryObject> list = DesignerPlugin.getDefault().getRepositoryService().getProxyRepositoryFactory()
                            .getAll(ERepositoryObjectType.METADATA_SQLPATTERNS, false);

                    for (IRepositoryObject repositoryObject : list) {
                        SQLPatternItem item = (SQLPatternItem) repositoryObject.getProperty().getItem();
                        if (item.getEltName().equals(eltNodeName) && item.getProperty().getLabel().equals(sqlpatternName)) {
                            sqlpatternItem = item;
                            break;
                        }

                    }

                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }

                if (sqlpatternItem != null) {
                    codeText.setText(new String(sqlpatternItem.getContent().getInnerContent()));
                }
            }
        });

    }
    /**
     * bqian Comment method "getTableInput".
     * 
     * @param element
     * @return
     */
    private List<Map> getTableInput(Element element) {
        IElementParameter parameter = element.getElementParameter(EParameterName.SQLPATTERN_VALUE.getName());
        List<Map> value = (List<Map>) parameter.getValue();
        return value;
    }

    /**
     * yzhang AdvancedContextComposite class global comment. Detailled comment
     */
    private class DynamicComboBoxCellEditor extends ExtendedComboBoxCellEditor {

        /**
         * yzhang ComboBoxCellEditor constructor comment.
         * 
         * @param composite
         * @param list
         * @param labelProvider
         */
        public DynamicComboBoxCellEditor(Composite composite, List<?> list, ILabelProvider labelProvider) {
            super(composite, list, labelProvider);
        }

        public void refresh() {
            refreshItems("");
        }

    }

    /**
     * yzhang Comment method "executeCommand".
     * 
     * @param cmd
     */
    private void executeCommand(Command cmd) {
        if (commandStack == null) {
            IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            if (part instanceof AbstractMultiPageTalendEditor) {
                AbstractTalendEditor editor = ((AbstractMultiPageTalendEditor) part).getTalendEditor();
                commandStack = (CommandStack) editor.getAdapter(CommandStack.class);
            }
        }
        commandStack.execute(cmd);
    }

    /**
     * yzhang Comment method "refreshComboContent".
     * 
     * @param tableViewer
     * @param legalParameters
     */
    private void refreshComboContent(TableViewer tableViewer) {
        if (comboContent == null) {
            comboContent = new ArrayList<String>();
        }
        comboContent.clear();

        List<Map> tableInput = (List<Map>) tableViewer.getInput();
        List<String> content = getAllSqlPatterns();
        for (Map map : tableInput) {
            content.remove(map.get(EmfComponent.SQLPATTERNLIST));
        }

        comboContent.addAll(content);
        refreshButton();
    }

    private void refreshButton() {
        buttonAdd.setEnabled(comboContent.size() > 0);
        buttonRemove.setEnabled(((List<Map>) tableViewer.getInput()).size() > 0);
    }

    private List<String> getAllSqlPatterns() {

        String dbName = (String) element.getElementParameter(EParameterName.SQLPATTERN_DB_NAME.getName()).getValue();
        List<String> patterns = new ArrayList<String>();
        try {

            List<IRepositoryObject> list = DesignerPlugin.getDefault().getRepositoryService().getProxyRepositoryFactory().getAll(
                    ERepositoryObjectType.METADATA_SQLPATTERNS, false);
            for (IRepositoryObject repositoryObject : list) {
                SQLPatternItem item = (SQLPatternItem) repositoryObject.getProperty().getItem();
                if (item.getEltName().equals(dbName)) {
                    patterns.add(item.getProperty().getLabel());
                }
            }
        } catch (Exception e) {
        }

        return patterns;
    }

    private ILabelProvider comboboxCellEditorLabelProvider = new ILabelProvider() {

        public Image getImage(Object element) {
            return null;
        }

        public String getText(Object element) {
            return (String) element;
        }

        public void addListener(ILabelProviderListener listener) {

        }

        public void dispose() {

        }

        public boolean isLabelProperty(Object element, String property) {
            return false;
        }

        public void removeListener(ILabelProviderListener listener) {

        }

    };

    private CommandStack commandStack;

    /**
     * yzhang AdvancedContextComposite class global comment. Detailled comment
     */
    private class TableViewerProvider extends LabelProvider implements IStructuredContentProvider {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof List) {
                return ((List) inputElement).toArray();
            }
            return new Object[0];
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
         * java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
         */
        @Override
        public String getText(Object element) {
            if (element instanceof Map) {
                Map ep = (Map) element;
                return (String) ep.get(EmfComponent.SQLPATTERNLIST);
            }
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getComposite()
     */
    public Composite getComposite() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getCurRowSize()
     */
    public int getCurRowSize() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getElement()
     */
    public Element getElement() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getHashCurControls()
     */
    public BidiMap getHashCurControls() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getPart()
     */
    public AbstractMultiPageTalendEditor getPart() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getQueriesMap()
     */
    public Map<String, List<String>> getQueriesMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getRepositoryAliasName(org.talend.core.model.properties.ConnectionItem)
     */
    public String getRepositoryAliasName(ConnectionItem connectionItem) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getRepositoryConnectionItemMap()
     */
    public Map<String, ConnectionItem> getRepositoryConnectionItemMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getRepositoryQueryStoreMap()
     */
    public Map<String, Query> getRepositoryQueryStoreMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getRepositoryTableMap()
     */
    public Map<String, IMetadataTable> getRepositoryTableMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getSection()
     */
    public EComponentCategory getSection() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getTableIdAndDbSchemaMap()
     */
    public Map<String, String> getTableIdAndDbSchemaMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getTableIdAndDbTypeMap()
     */
    public Map<String, String> getTableIdAndDbTypeMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getTablesMap()
     */
    public Map<String, List<String>> getTablesMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#refresh()
     */
    public void refresh() {
        getParent().layout();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#setCurRowSize(int)
     */
    public void setCurRowSize(int i) {

    }

}

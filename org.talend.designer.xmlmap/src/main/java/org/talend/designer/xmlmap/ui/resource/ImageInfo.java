// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.ui.resource;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id: ImageInfo.java 44435 2010-06-24 01:29:35Z wchen $
 * 
 */
public enum ImageInfo {

    MINIMIZE_ICON("/icons/collapse16.png"), //$NON-NLS-1$
    RESTORE_ICON("/icons/uncollapse16.png"), //$NON-NLS-1$
    ADD("/icons/add.png"), //$NON-NLS-1$
    REFRESH("/icons/arrow_refresh.png"), //$NON-NLS-1$
    REFRESH_CACHE("/icons/arrow_refresh_cache.png"), //$NON-NLS-1$
    ACTIVATE_FILTER_ICON("/icons/addfilter.png"), //$NON-NLS-1$

    ARROW_RIGHT("/icons/arrow_right.png"), //$NON-NLS-1$
    ARROW_RIGHT_EDIT("/icons/arrow_right_edit.png"), //$NON-NLS-1$

    FIRST_MATCH("/icons/first_match.png"), //$NON-NLS-1$
    UNIQUE_MATCH("/icons/last_match.png"), //$NON-NLS-1$
    LAST_MATCH("/icons/last_match.png"), //$NON-NLS-1$
    ALL_MATCHES("/icons/all_matches.png"), //$NON-NLS-1$

    FILTER_BUTTON("/icons/button.jpg"),

    LOOKUP_KEY_ICON("/icons/lookup_key16.png"), //$NON-NLS-1$
    CONDENSED_TOOL_ICON("/icons/condensedToolItem.png"),
    CONDENSED_TOOL_ICON1("/icons/condensedToolItem1.png"),
    CONDENSED_TOOL_ICON2("/icons/condensedToolItem2.png"),
    CONDENSED_TOOL_ICON3("/icons/condensedToolItem3.png"),
    CONDENSED_TOOL_ICON4("/icons/condensedToolItem4.png"), ;

    private String path;

    ImageInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

}

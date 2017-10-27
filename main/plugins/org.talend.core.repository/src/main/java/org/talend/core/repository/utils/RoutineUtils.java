// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.repository.utils;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public final class RoutineUtils {

    public static void changeRoutinesPackage(Item item) {
        if (item == null) {
            return;
        }

        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
        if (ERepositoryObjectType.ROUTINES.equals(itemType) && item instanceof RoutineItem) {
            RoutineItem rItem = (RoutineItem) item;
            if (!rItem.isBuiltIn()) {
                //
                String routineContent = new String(rItem.getContent().getInnerContent());
                //
                // String curProjectName =
                // currentProject.getTechnicalLabel().toLowerCase();
                String oldPackage = "package(\\s)+" + JavaUtils.JAVA_ROUTINES_DIRECTORY + "\\.((\\w)+)(\\s)*;"; //$NON-NLS-1$ //$NON-NLS-2$
                // String newPackage = "package " +
                // JavaUtils.JAVA_ROUTINES_DIRECTORY + "." + curProjectName +
                // ";";

                String newPackage = "package " + JavaUtils.JAVA_ROUTINES_DIRECTORY + ";"; //$NON-NLS-1$ //$NON-NLS-2$
                try {
                    PatternCompiler compiler = new Perl5Compiler();
                    Perl5Matcher matcher = new Perl5Matcher();
                    matcher.setMultiline(true);
                    Pattern pattern = compiler.compile(oldPackage);

                    if (matcher.contains(routineContent, pattern)) {
                        // String group = matcher.getMatch().group(2);
                        // if (!curProjectName.equals(group)) { // not same
                        Perl5Substitution substitution = new Perl5Substitution(newPackage, Perl5Substitution.INTERPOLATE_ALL);
                        routineContent = Util.substitute(matcher, pattern, substitution, routineContent, Util.SUBSTITUTE_ALL);

                        rItem.getContent().setInnerContent(routineContent.getBytes());
                        ProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();

                        repFactory.save(rItem);
                        // }
                    }
                } catch (MalformedPatternException e) {
                    ExceptionHandler.process(e);
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }
}

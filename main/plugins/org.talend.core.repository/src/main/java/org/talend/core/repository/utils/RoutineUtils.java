// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import org.talend.core.runtime.services.IDesignerMavenService;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public final class RoutineUtils {

    private static final String DEFAULT_PACKAGE_REGEX = "package(\\s)+" + JavaUtils.JAVA_ROUTINES_DIRECTORY //$NON-NLS-1$
            + "\\.((\\w)+)(\\s)*;"; //$NON-NLS-1$

    private static final String DEFAULT_PACKAGE_STRING = "package " + JavaUtils.JAVA_ROUTINES_DIRECTORY + ";"; //$NON-NLS-1$ //$NON-NLS-2$

    private static final String INNER_ROUTINES_PACKAGE_REGEX = "package\\s+([^;]+);";

    public static void changeRoutinesPackage(Item item) {
        List<ERepositoryObjectType> allowedTypes = new ArrayList<ERepositoryObjectType>();
        allowedTypes.add(ERepositoryObjectType.ROUTINES);
        doChangeRoutinesPackage(item, DEFAULT_PACKAGE_REGEX, DEFAULT_PACKAGE_STRING, allowedTypes, false, true);
    }

    public static void changeInnerCodePackage(Item item, boolean avoidSave) {
        changeInnerCodePackage(item, avoidSave, true);
    }

    public static void changeInnerCodePackage(Item item, boolean avoidSave, boolean commitMode) {
        IDesignerMavenService service = IDesignerMavenService.get();
        if (service != null && item instanceof RoutineItem) {
            RoutineItem routineItem = (RoutineItem) item;
            String codesJarPackageByInnerCode = service.getCodesJarPackageByInnerCode(routineItem);
            if (StringUtils.isNotBlank(codesJarPackageByInnerCode)) {
                String newPackageString = "package " + StringUtils.replace(codesJarPackageByInnerCode, "/", ".") + ";";
                if (!routineItem.isBuiltIn()) {
                    String routineContent = new String(routineItem.getContent().getInnerContent());
                    if (routineContent != null && routineContent.contains(newPackageString)) {
                        return;
                    }
                }
                doChangeRoutinesPackage(item, INNER_ROUTINES_PACKAGE_REGEX, newPackageString,
                        ERepositoryObjectType.getAllTypesOfCodes(), avoidSave, commitMode);
            }
        }
    }

    public static void doChangeRoutinesPackage(Item item, String packageRegex, String newPackage,
            List<ERepositoryObjectType> allowedTypes, boolean avoidSave, boolean commitMode) {
        if (item == null) {
            return;
        }

        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
        if (allowedTypes != null && allowedTypes.contains(itemType) && item instanceof RoutineItem) {
            RoutineItem rItem = (RoutineItem) item;
            if (!rItem.isBuiltIn()) {
                String routineContent = new String(rItem.getContent().getInnerContent());
                try {
                    PatternCompiler compiler = new Perl5Compiler();
                    Perl5Matcher matcher = new Perl5Matcher();
                    matcher.setMultiline(true);
                    Pattern pattern = compiler.compile(packageRegex);

                    if (matcher.contains(routineContent, pattern)) {
                        // String group = matcher.getMatch().group(2);
                        // if (!curProjectName.equals(group)) { // not same
                        Perl5Substitution substitution = new Perl5Substitution(newPackage, Perl5Substitution.INTERPOLATE_ALL);
                        routineContent = Util.substitute(matcher, pattern, substitution, routineContent, Util.SUBSTITUTE_ALL);

                        rItem.getContent().setInnerContent(routineContent.getBytes());
                        ProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();

                        if (!avoidSave) {
                            if (commitMode) {
                                repFactory.save(rItem);
                            } else {
                                // avoid deadlock in git pull event listener
                                new XmiResourceManager().saveResource(rItem.eResource());
                            }
                        }
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

// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.ui.swt.proposal;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.swt.proposal.TextCellEditorContentAdapterExtended;
import org.talend.commons.ui.runtime.swt.proposal.TextContentAdapterExtended;
import org.talend.commons.ui.swt.preferences.HotKeyUtil;

/**
 * Utilities for proposals. <br/>
 * 
 * $Id: ProposalUtils.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public final class ProposalUtils {

    /**
     * Constructs a new ProposalUtils.
     */
    private ProposalUtils() {
    }

    public static ContentProposalAdapterExtended getCommonProposal(Control control, IContentProposalProvider proposalProvider) {
        IControlContentAdapter controlContentAdapter = null;
        if (control instanceof Text) {
            controlContentAdapter = new TextContentAdapterExtended();
        } else if (control instanceof StyledText) {
            controlContentAdapter = new StyledTextContentAdapterExtended();
        } else {
            throw new IllegalArgumentException(Messages.getString("ProposalUtils.CtrlProposal.ErrorMsg") + control.getClass()); //$NON-NLS-1$
        }
        final ContentProposalAdapterExtended contentProposalAdapter = getContentProposalAdapter(control, controlContentAdapter,
                proposalProvider);

        // to don't write Carriage Return when accept a proposal
        if (control instanceof StyledText) {
            final VerifyKeyListener verifyKeyListener = new VerifyKeyListener() {

                public void verifyKey(VerifyEvent verifyEvent) {
                    if (verifyEvent.character == '\r' && contentProposalAdapter.isProposalOpened()) {
                        verifyEvent.doit = false;
                    } else {
                        verifyEvent.doit = true;
                    }
                }
            };
            ((StyledText) control).addVerifyKeyListener(verifyKeyListener);
        }

        return contentProposalAdapter;
    }

    private static ContentProposalAdapterExtended getContentProposalAdapter(Control control,
            IControlContentAdapter controlContentAdapter) {
        return getContentProposalAdapter(control, controlContentAdapter, null);
    }

    private static ContentProposalAdapterExtended getContentProposalAdapter(Control control,
            IControlContentAdapter controlContentAdapter, IContentProposalProvider proposalProvider) {
        ContentProposalAdapterExtended contentProposalAdapter = null;
        try {
            KeyStroke keyStroke = HotKeyUtil.getHotKey(HotKeyUtil.contentAssist);

            contentProposalAdapter = new ContentProposalAdapterExtended(control, controlContentAdapter, proposalProvider,
                    keyStroke, null);
            contentProposalAdapter.setPropagateKeys(true);
            contentProposalAdapter.setFilterStyle(ContentProposalAdapterExtended.FILTER_CUMULATIVE_ALL_START_WORDS);
            contentProposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapterExtended.PROPOSAL_INSERT);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
        return contentProposalAdapter;
    }

    public static ContentProposalAdapterExtended getCommonProposal(Control control) {
        return getCommonProposal(control, null);
    }

    public static ContentProposalAdapterExtended getCommonProposal(CellEditor cellEditor) {
        if (cellEditor instanceof TextCellEditorWithProposal) {
            return getContentProposalAdapter(cellEditor.getControl(), new TextCellEditorContentAdapterExtended());
        } else if (cellEditor instanceof ExtendedTextCellEditorWithProposal) {
            return getContentProposalAdapter(((ExtendedTextCellEditorWithProposal) cellEditor).getTextControl(),
                    new TextCellEditorContentAdapterExtended());
        } else {
            throw new IllegalArgumentException(Messages.getString("ProposalUtils.CellProposal.Error") + cellEditor.getClass()); //$NON-NLS-1$
        }
    }

    /**
     * DOC amaumont Comment method "initializeJavaProposal".
     * 
     * @param compilationUnit
     */

}
// backup do not remove, it will be removed later
// @SuppressWarnings("restriction")
// public static List<ProblemWithLine> getProblems() {
//
// //
// // int length = workingCopy.getContents().length;
//
// // reconciler.getReconcilingStrategy(getContentDescription()).
// // getEditorInput().get
//
// IJavaProject javaProject = unit != null ? ((IJavaElement) unit).getJavaProject() : null;
// HashMap preferences = null;
// if (javaProject == null)
// preferences = new HashMap(JavaCore.getOptions());
// else
// preferences = new HashMap(javaProject.getOptions(true));
//
// // IRunProcessService service = (IRunProcessService)
// // GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
// // IProject project = service.getProject();
//
// ICompilerRequestor compilerRequestor = new ICompilerRequestor() {
//
// public void acceptResult(CompilationResult compilationResult) {
// // do nothing
// }
// };
// NameEnvironment nameEnvironment = new NameEnvironment(javaProject);
// CompilerOptions compilerOptions = new CompilerOptions(preferences);
// DefaultProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());
// IErrorHandlingPolicy errorHandlingPolicy = DefaultErrorHandlingPolicies.proceedWithAllProblems();
// org.eclipse.jdt.internal.compiler.CompilationResult compilationResult = new CompilationResult(unit, 1, 1, 100);
// org.eclipse.jdt.internal.compiler.problem.ProblemReporter problemReporter = new
// ProblemReporter(DefaultErrorHandlingPolicies
// .proceedWithAllProblems(), compilerOptions, new DefaultProblemFactory());
// CompilationUnitDeclaration compilationUnitDeclaration = new CompilationUnitDeclaration(problemReporter,
// compilationResult, unit
// .getContents().length);
// Compiler compiler = new Compiler(nameEnvironment, errorHandlingPolicy, compilerOptions, compilerRequestor,
// problemFactory);
// // CompilationResult result = problemReporter.referenceContext.compilationResult();
//
// compiler.compile(new ICompilationUnit[] { (ICompilationUnit) unit });
// compiler.resolve((org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration) compilationUnitDeclaration,
// (ICompilationUnit) unit, true, true, false);
// // CompilationResult compilationResult2 = compilationUnitDeclaration.scope.referenceContext.compilationResult;
// CompilationResult compilationResult2 = compilationUnitDeclaration.compilationResult;
// CategorizedProblem[] problems3 = compilationResult2.problems;
//
// // CompilationUnitDeclaration[] unitsToProcess = compiler.unitsToProcess;
// //
// // for (int i = 0; i < unitsToProcess.length; i++) {
// // CompilationUnitDeclaration declaration = unitsToProcess[i];
// // CompilationResult compilationResult2 = declaration.compilationResult;
// // CategorizedProblem[] problems = compilationResult2.problems;
// // for (int j = 0; j < problems.length; j++) {
// // CategorizedProblem problem = problems[j];
// // System.out.println();
// // }
// //
// // }
//
// ProblemReporter problemReporterFromCompiler = compiler.problemReporter;
//
// Map options = JavaCore.getOptions();
// options.remove(JavaCore.COMPILER_TASK_TAGS); // no need to parse task tags
//
// // CompilationUnitProblemFinder.process(compilationUnitDeclaration, unit, unit.getContents(), null,
// // workingCopyOwner, problems, true, false, monitor)
//
// LookupEnvironment lookupEnvironment = new LookupEnvironment(compiler, compilerOptions, problemReporter,
// nameEnvironment);
// compilationUnitDeclaration.scope = new CompilationUnitScope(compilationUnitDeclaration, lookupEnvironment);
// //
// // if (compilationUnitDeclaration.scope != null) {
// // compilationUnitDeclaration.resolve();
// // }
// compilationUnitDeclaration.resolve();
//
// CategorizedProblem[] allProblems = compilationResult.getAllProblems();
//
// CategorizedProblem[] problems = compilationResult.getProblems();
//
// final ArrayList<ProblemWithLine> iproblems = new ArrayList<ProblemWithLine>();
//
// // create requestor for accumulating discovered problems
// IProblemRequestor problemRequestor = new IProblemRequestor() {
//
// public void acceptProblem(IProblem problem) {
// if (problem.isError()) {
// System.out.println(problem.getID() + ": " + problem.getMessage());
// String line = extractLine(unit.getContents(), problem.getSourceStart());
// ProblemWithLine problemWithLine = new ProblemWithLine(problem, line);
// iproblems.add(problemWithLine);
// }
//
// }
//
// private String extractLine(char[] contents, int start) {
// // search previous cr
// int previousCrIndex = -1;
// int nextCrIndex = -1;
// for (int i = start, j = start; i >= 0 && j < contents.length; i--, j++) {
// if (previousCrIndex == -1) {
// if (contents[i] == '\n') {
// previousCrIndex = i + 1;
// }
// }
// if (i == 0 && previousCrIndex == -1) {
// previousCrIndex = 0;
// }
//
// if (nextCrIndex == -1) {
// if (contents[j] == '\n' || contents[j] == '\r') {
// nextCrIndex = j;
// }
// }
//
// if (j == contents.length - 1 && nextCrIndex == -1) {
// nextCrIndex = contents.length;
// }
//
// if (previousCrIndex != -1 && nextCrIndex != -1) {
// break;
// }
//
// }
//
// return String.copyValueOf(contents, previousCrIndex, nextCrIndex - previousCrIndex);
//
// }
//
// public void beginReporting() {
// }
//
// public void endReporting() {
// }
//
// public boolean isActive() {
// return true;
// } // will detect problems if active
// };
//
// // use working copy to hold source with error
// // ((IOpenable)workingCopy).getBuffer().setContents("public class X extends Zork {}");
// org.eclipse.jdt.core.ICompilationUnit workingCopy;
// try {
// workingCopy = ((org.eclipse.jdt.core.ICompilationUnit) unit).getWorkingCopy(new WorkingCopyOwner() {
// }, problemRequestor, null);
// ((IOpenable) workingCopy).getBuffer().setContents(unit.getContents());
// // workingCopy.reconcile(1, true, null, null);
// // workingCopy.reconcile(true, null);
// } catch (JavaModelException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
//
// // trigger reconciliation
//
// return iproblems;
// // return problemReporterFromCompiler.;
// //
// // unit.getC
//
// // reconciler.
// // strategy.reconcile(new Region(0, this.getDocumentProvider().getDocument(getEditorInput()).getLength()));
// // strategy.getReconcilingStrategies()[0].
// }

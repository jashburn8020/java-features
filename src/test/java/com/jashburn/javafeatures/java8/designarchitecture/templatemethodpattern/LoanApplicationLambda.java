package com.jashburn.javafeatures.java8.designarchitecture.templatemethodpattern;

/**
 * Instead of abstract methods, we've got fields that implement the {@link Criteria} functional
 * interface that checks a criterion.
 * <p>
 * The advantage of this approach over the inheritance-based approach is that instead of tying the
 * implementation of this algorithm into the {@link LoanApplicationTraditional} hierarchy, we can be
 * much more flexible about where to delegate the functionality to. For example, we may decide that
 * our {@link Company} class should be responsible for all criteria checking.
 * <p>
 * See {@link CompanyLoanApplication}
 * <p>
 * Using functional interfaces to implement the criteria doesn't preclude us from placing
 * implementation within the subclasses, either. We can explicitly use a lambda expression to place
 * implementation within these classes, or use a method reference to the current class.
 */
class LoanApplicationLambda {

    private final Criteria identity;
    private final Criteria creditHistory;
    private final Criteria incomeHistory;

    public LoanApplicationLambda(Criteria identity, Criteria creditHistory,
            Criteria incomeHistory) {
        this.identity = identity;
        this.creditHistory = creditHistory;
        this.incomeHistory = incomeHistory;
    }

    public void checkLoanApplication() throws ApplicationDenied {
        identity.check();
        creditHistory.check();
        incomeHistory.check();
        reportFindings();
    }

    private void reportFindings() {
        // report findings
    }
}

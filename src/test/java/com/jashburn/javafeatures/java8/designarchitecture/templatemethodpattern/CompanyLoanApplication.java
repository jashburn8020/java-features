package com.jashburn.javafeatures.java8.designarchitecture.templatemethodpattern;

class CompanyLoanApplication extends LoanApplicationLambda {

    /**
     * @param company We can pass in variants of Company to implement checks that differ between
     *                countries and states.
     */
    CompanyLoanApplication(Company company) {
        super(company::checkIdentity, company::checkHistoricalDebt, company::checkProfitAndLoss);
    }
}

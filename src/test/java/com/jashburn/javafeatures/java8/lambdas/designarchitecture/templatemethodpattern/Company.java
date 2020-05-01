package com.jashburn.javafeatures.java8.lambdas.designarchitecture.templatemethodpattern;

class Company {

    public void checkIdentity() throws ApplicationDenied {
        // check company identity by looking up information in a company registration database
    }

    public void checkProfitAndLoss() throws ApplicationDenied {
        // analogous to check income history for personal loans
    }

    public void checkHistoricalDebt() throws ApplicationDenied {
        // analogous to check credit history for personal loans
    }
}

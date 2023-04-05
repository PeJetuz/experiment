package my.test.rest.incomings.eureka.rules.create;

import my.test.eureka.policy.CreatePolicy;
import my.test.eureka.policy.PolicyBuilder;
import my.test.rest.incomings.eureka.rules.CreatePresenter;
import my.test.rest.incomings.eureka.rules.CreateProcess;

public class CreateProcessImpl implements CreateProcess {

    private final CreatePolicy policy;
    private final CreatePresenter presenter;

    public CreateProcessImpl(PolicyBuilder policyBuilder, CreatePresenter presenter, String userName,
            String passwordHash) {
        this.policy = policyBuilder.buildCreatePolicy(userName, passwordHash);
        this.presenter = presenter;
    }

    @Override
    public boolean createNewUser() {
        boolean isUserCreated = policy.createNewUser();
        if (isUserCreated) {
            policy.writeTokenAndExpirationDateTime(presenter::writeToken, presenter::writeExpirationDateTime);
        } else {
            presenter.initUserAlreadyExistsResponseModel();
        }
        return isUserCreated;
    }
}

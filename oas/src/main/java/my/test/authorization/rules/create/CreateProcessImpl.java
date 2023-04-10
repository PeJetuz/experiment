package my.test.authorization.rules.create;

import my.test.authorization.domain.api.CreatePolicy;
import my.test.authorization.domain.api.PolicyBuilder;
import my.test.authorization.rules.CreatePresenter;
import my.test.authorization.rules.CreateProcess;

public class CreateProcessImpl implements CreateProcess {

    private final CreatePolicy policy;
    private final CreatePresenter presenter;
    private final UserFieldValidator userFieldValidator;

    public CreateProcessImpl(PolicyBuilder policyBuilder, CreatePresenter presenter, String userName,
            String passwordHash) {
        this.policy = policyBuilder.buildCreatePolicy(userName, passwordHash);
        this.presenter = presenter;
        this.userFieldValidator = () -> validateUserField(presenter, userName, passwordHash);
    }

    @Override
    public boolean createNewUser() {
        if (isUserDataValid()) {
            boolean isUserCreated = policy.createNewUser();
            if (isUserCreated) {
                policy.writeTokenAndLastRefreshDateTime(presenter::writeToken, presenter::writeExpirationDateTime);
            } else {
                presenter.initUserAlreadyExistsResponseModel();
            }
            return isUserCreated;
        }
        return false;
    }

    /**
     * Validate user data and fill out error messages to the presenter if there is an error in the data
     *
     * @return true if the user data is valid
     */
    protected boolean isUserDataValid() {
        return userFieldValidator.validateUserFields();
    }

    interface UserFieldValidator {

        boolean validateUserFields();
    }

    private static boolean validateUserField(CreatePresenter presenter, String userName, String passwordHash) {
        boolean result = true;
        if (userName == null || userName.isBlank()) {
            presenter.invalidUserNameField();
            result = false;
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            presenter.invalidPasswordHashField();
            result = false;
        }
        return result;
    }
}

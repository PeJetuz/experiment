package my.test.authorization.rules.creation;

import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.rules.CreateUserInteractor;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.authorization.rules.creation.validator.EmptyStringChainValidator;
import my.test.authorization.rules.creation.validator.Validator;
import my.test.rest.incomings.controllers.CreationUserResponseModel;

public class CreateUserInteractorImpl implements CreateUserInteractor {

    private final CreationPolicy policy;
    private final CreationUserResponsePresenter responsePresenter;
    private final Validator validatorChain;

    public CreateUserInteractorImpl(PolicyFactory policyFactory, UserInfo userInfo,
            CreationUserResponsePresenter responsePresenter) {
        this(policyFactory, userInfo, responsePresenter,
                new EmptyStringChainValidator(userInfo.name(), responsePresenter::invalidUserNameField,
                        new EmptyStringChainValidator(userInfo.passwordHash(),
                                responsePresenter::invalidPasswordHashField, Validator.TRUE)));
    }

    public CreateUserInteractorImpl(PolicyFactory policyFactory, UserInfo userInfo,
            CreationUserResponsePresenter responsePresenter, Validator validatorChain) {
        this.policy = policyFactory.buildCreationPolicy(userInfo, responsePresenter);
        this.responsePresenter = responsePresenter;
        this.validatorChain = validatorChain;
    }

    @Override
    public CreationUserResponseModel createNewUserAndGetPresenter() {
        if (validatorChain.validate()) {
            policy.create();
        }

        return responsePresenter;
    }
}

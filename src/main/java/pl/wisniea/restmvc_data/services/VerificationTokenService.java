package pl.wisniea.restmvc_data.services;

import pl.wisniea.restmvc_data.entities.UserEntity;
import pl.wisniea.restmvc_data.entities.VerificationTokenEntity;

public interface VerificationTokenService {

    VerificationTokenEntity getByToken(String token);

    VerificationTokenEntity getByUser(UserEntity user);

    VerificationTokenEntity save(UserEntity user, String token);
}

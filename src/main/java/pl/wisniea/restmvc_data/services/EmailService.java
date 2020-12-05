package pl.wisniea.restmvc_data.services;


import pl.wisniea.restmvc_data.entities.UserEntity;

import javax.mail.MessagingException;

public interface EmailService {

    void sendHtmlMail(UserEntity user) throws MessagingException;

}

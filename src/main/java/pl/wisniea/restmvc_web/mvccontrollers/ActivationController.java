package pl.wisniea.restmvc_web.mvccontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.wisniea.restmvc_data.entities.UserEntity;
import pl.wisniea.restmvc_data.entities.VerificationTokenEntity;
import pl.wisniea.restmvc_data.services.UserService;
import pl.wisniea.restmvc_data.services.VerificationTokenService;

import java.sql.Timestamp;

@Controller
@RequestMapping("/activation")
public class ActivationController {

    private final VerificationTokenService verificationTokenService;
    private final UserService userService;

    private String message = "message";

    public ActivationController(VerificationTokenService verificationTokenService, UserService userService) {
        this.verificationTokenService = verificationTokenService;
        this.userService = userService;
    }

    @GetMapping
    public String activateAccount(@RequestParam("token") String token, Model model) {

        VerificationTokenEntity verificationToken = verificationTokenService.getByToken(token);

        if (verificationToken == null) {
            model.addAttribute(message, "Your verification token is not confirmed, please contact our support.");
            return "confirmactivation";
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if (!verificationToken.getExpirationDate().before(timestamp)) {
            UserEntity user = verificationToken.getUserEntity();
            user.setEmailVerificationStatus(true);
            userService.save(user);
            model.addAttribute(message, "Your activation has been successful.");
        } else {
            model.addAttribute(message, "Your activation token has expired.");
        }

        return "confirmactivation";
    }
}

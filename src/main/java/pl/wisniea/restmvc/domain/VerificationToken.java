package pl.wisniea.restmvc.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity(name = "verification_tokens")
@Data
public class VerificationToken implements Serializable {
    private static final long serialVersionUID = 6487947528132589427L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Timestamp expirationDate;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    //@JoinColumn(name = "user_id")
    private User user;

    public VerificationToken() {
    }

    public VerificationToken(User user, String token) {
        this.user = user;
        this.token = token;
    }
}

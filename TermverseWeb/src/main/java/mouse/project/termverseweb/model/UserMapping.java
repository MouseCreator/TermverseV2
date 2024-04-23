package mouse.project.termverseweb.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mouse.project.termverseweb.lib.service.model.IdIterable;

@Data
@Entity
@Table(name = "user_mapping")
@NoArgsConstructor
public class UserMapping implements IdIterable<String> {
    @Id
    @Column(name = "security_id")
    private String securityId;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserMapping(@Nonnull String securityId, @Nonnull User user) {
        this.securityId = securityId;
        this.user = user;
    }

    @Override
    public String getId() {
        return securityId;
    }

    @Override
    public void setId(String string) {
        this.securityId = string;
    }
}

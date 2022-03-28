package dev.azonov.giftservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "childs")
@Data
@NoArgsConstructor
public class ChildEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(length = 50)
    private String firstName;

    @Basic
    @Column(length = 50)
    private String secondName;

    @Basic
    @Column(length = 50)
    private String middleName;

    public ChildEntity(String firstName, String middleName, String secondName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.secondName = secondName;
    }

    /**
     * Combine name parts into full name
     * @return full name
     */
    public String getFullName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(firstName);
        if (StringUtils.hasText(middleName)) {
            stringBuilder.append(" ").append(middleName);
        }
        stringBuilder.append(" ").append(secondName);

        return stringBuilder.toString();
    }
}

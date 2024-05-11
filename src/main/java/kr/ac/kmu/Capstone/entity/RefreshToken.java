package kr.ac.kmu.Capstone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.ToString;

@Table(name = "refresh_token")
@Entity
@ToString
public class RefreshToken {

    @Id
    @Column(name = "refresh_key", nullable = false)
    private String refreshKey;

    @Column(name = "refresh_value")
    private String refreshValue;

    public RefreshToken(){

    }

    @Builder
    public RefreshToken(String refreshKey, String refreshValue) {
        this.refreshKey = refreshKey;
        this.refreshValue = refreshValue;
    }

    public String getRefreshKey() {
        return refreshKey;
    }

    public String getRefreshValue() {
        return refreshValue;
    }

    public RefreshToken updateValue(String token) {
        this.refreshValue = token;
        return this;
    }


}


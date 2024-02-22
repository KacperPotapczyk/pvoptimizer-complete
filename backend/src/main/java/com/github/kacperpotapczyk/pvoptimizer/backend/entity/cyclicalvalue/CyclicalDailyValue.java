package com.github.kacperpotapczyk.pvoptimizer.backend.entity.cyclicalvalue;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cyclical_daily_value", indexes = {@Index(name = "idx_cyclical_daily_value_tariff_revision", columnList = "tariff_revision")})
public class CyclicalDailyValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tariff_revision", nullable = false)
    private TariffRevision tariffRevision;

    @Column(name = "day_of_the_week", nullable = false, columnDefinition = "int")
    @Enumerated(EnumType.ORDINAL)
    private Weekdays dayOfTheWeek;

    @OneToMany(mappedBy = "cyclicalDailyValues", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyTimeValue> dailyTimeValues;

    public CyclicalDailyValue(List<DailyTimeValue> dailyTimeValues) {

        this.dayOfTheWeek = Weekdays.ALL;
        this.dailyTimeValues = dailyTimeValues;
    }
}
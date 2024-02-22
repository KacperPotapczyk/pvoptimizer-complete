package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.cyclicalvalue.CyclicalDailyValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.cyclicalvalue.DailyTimeValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.cyclicalvalue.Weekdays;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.Tariff;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TariffServiceTest {

    private final TariffService tariffService;

    @Autowired
    public TariffServiceTest(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Test
    public void testTariffAddingAndQuery() {

        String baseName = "base tariff 1";
        double defaultPrice = 3.24;

        Tariff addedTariff = tariffService.newBaseObject(getTestTariff(baseName, defaultPrice));

        assertEquals(baseName, addedTariff.getName());
        assertEquals(1, addedTariff.getRevisions().size());
        assertTrue(tariffService.existsByName(baseName));

        Tariff dbTariff = tariffService.findByName(baseName);
        assertTrue(tariffService.existsById(dbTariff.getId()));

        assertEquals(1, dbTariff.getRevisions().size());
        TariffRevision dbRevision = dbTariff.getRevisions().stream().findAny().orElseThrow();

        assertEquals(1L, dbRevision.getRevisionNumber());
        assertEquals(defaultPrice, dbRevision.getDefaultPrice());
    }

    @Test
    public void addTariffRevision() {

        double defaultValue = 13.2;
        Tariff tariff = tariffService.findByName("addRevisionTest");

        TariffRevision tariffRevision = new TariffRevision();
        tariffRevision.setDefaultPrice(defaultValue);

        List<CyclicalDailyValue> cyclicalDailyValueList = getCyclicalDailyValueList();

        tariffRevision.setCyclicalDailyValues(cyclicalDailyValueList);

        tariffService.addRevision(tariff.getName(), tariffRevision);

        assertEquals(2, tariff.getRevisions().size());
        TariffRevision dbTariffRevision = tariff.getRevision(2L).orElseThrow();

        assertEquals(defaultValue, dbTariffRevision.getDefaultPrice());
        assertEquals(1, dbTariffRevision.getCyclicalDailyValues().size());
        assertEquals(2, dbTariffRevision.getCyclicalDailyValues().get(0).getDailyTimeValues().size());
    }

    private List<CyclicalDailyValue> getCyclicalDailyValueList() {
        DailyTimeValue value1 = new DailyTimeValue();
        value1.setCurrentValue(0.1);
        value1.setStartTime(LocalTime.parse("07:00:00"));

        DailyTimeValue value2 = new DailyTimeValue();
        value2.setCurrentValue(0.15);
        value2.setStartTime(LocalTime.parse("09:00:00"));

        List<DailyTimeValue> dailyTimeValueList = new ArrayList<>();
        dailyTimeValueList.add(value1);
        dailyTimeValueList.add(value2);
        CyclicalDailyValue cyclicalDailyValue = new CyclicalDailyValue(dailyTimeValueList);

        List<CyclicalDailyValue> cyclicalDailyValueList = new ArrayList<>();
        cyclicalDailyValueList.add(cyclicalDailyValue);
        return cyclicalDailyValueList;
    }

    @Test
    public void getTariffWithRevision() {

        String tariffName = "queryOnly";
        long revisionNumber = 2L;

        Tariff tariff = tariffService.getBaseObjectWithRevision(tariffName, revisionNumber);

        assertEquals(tariffName, tariff.getName());
        assertEquals(1, tariff.getRevisions().size());

        TariffRevision tariffRevision = tariff.getRevision(revisionNumber).orElseThrow();
        assertEquals(revisionNumber, tariffRevision.getRevisionNumber());
        assertEquals(0.056, tariffRevision.getDefaultPrice());
    }

    @Test
    public void getTariffRevisionWithCyclicDailyValues() {

        String tariffName = "queryOnly";
        long revisionNumber = 1L;

        Tariff tariff = tariffService.getBaseObjectWithRevision(tariffName, revisionNumber);
        assertEquals(tariffName, tariff.getName());
        assertEquals(1, tariff.getRevisions().size());

        TariffRevision tariffRevision = tariff.getRevision(revisionNumber).orElseThrow();
        assertEquals(revisionNumber, tariffRevision.getRevisionNumber());
        assertEquals(0.02, tariffRevision.getDefaultPrice());
        assertEquals(2, tariffRevision.getCyclicalDailyValues().size());

        CyclicalDailyValue cyclicalWorkingDayValue = tariffRevision.getCyclicalDailyValues().stream()
                .filter(cyclicalDailyValue -> cyclicalDailyValue.getDayOfTheWeek() == Weekdays.MONDAY_TO_FRIDAY)
                .findAny().orElseThrow();
        assertEquals(2, cyclicalWorkingDayValue.getDailyTimeValues().size());

        List<DailyTimeValue> dailyTimeValueList1 = cyclicalWorkingDayValue.getDailyTimeValues();
        assertEquals(0.03, dailyTimeValueList1.get(0).getCurrentValue());
        assertEquals(LocalTime.parse("06:00:00"), dailyTimeValueList1.get(0).getStartTime());
        assertEquals(0.01, dailyTimeValueList1.get(1).getCurrentValue());
        assertEquals(LocalTime.parse("22:00:00"), dailyTimeValueList1.get(1).getStartTime());

        CyclicalDailyValue cyclicalWeekendValue = tariffRevision.getCyclicalDailyValues().stream()
                .filter(cyclicalDailyValue -> cyclicalDailyValue.getDayOfTheWeek() == Weekdays.WEEKEND)
                .findAny().orElseThrow();
        assertEquals(2, cyclicalWeekendValue.getDailyTimeValues().size());

        List<DailyTimeValue> dailyTimeValueList2 = cyclicalWeekendValue.getDailyTimeValues();
        assertEquals(0.025, dailyTimeValueList2.get(0).getCurrentValue());
        assertEquals(LocalTime.parse("08:00:00"), dailyTimeValueList2.get(0).getStartTime());
        assertEquals(0.005, dailyTimeValueList2.get(1).getCurrentValue());
        assertEquals(LocalTime.parse("23:30:00"), dailyTimeValueList2.get(1).getStartTime());
    }

    @Test
    public void deleteTariffRevision() {

        String baseName = "revisionToBeDeleted";

        assertThrows(IllegalStateException.class, () -> tariffService.deleteBaseObjectRevision(baseName, 1L));

        TariffRevision tariffRevision = new TariffRevision();
        tariffRevision.setDefaultPrice(1.2);

        tariffService.addRevision(baseName, tariffRevision);

        Tariff tariff = tariffService.findByName(baseName);
        assertEquals(2, tariff.getRevisions().size());

        tariffService.deleteBaseObjectRevision(baseName, 1L);
        assertEquals(1, tariff.getRevisions().size());
        assertEquals(2L, tariff.getRevision(2L).orElseThrow().getRevisionNumber());
    }

    @Test
    public void deleteTariff() {

        String baseName = "toBeDeleted";
        assertTrue(tariffService.existsByName(baseName));

        tariffService.deleteBaseObject(baseName);
        assertFalse(tariffService.existsByName(baseName));
    }

    private Tariff getTestTariff(String name, double defaultPrice) {

        TariffRevision tariffRevision = new TariffRevision(1L, defaultPrice);

        Set<TariffRevision> tariffRevisionSet = new HashSet<>();
        tariffRevisionSet.add(tariffRevision);

        Tariff tariff = new Tariff();
        tariff.setName(name);
        tariff.setRevisions(tariffRevisionSet);

        return tariff;
    }
}

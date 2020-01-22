package no.nav.helse.sparkel.sykepengeperioder.infotrygd

import no.nav.helse.sparkel.sykepengeperioder.objectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate

internal class UtbetalingshistorikkTest {

    @Test
    internal fun utbetalingshistorikk() {
        val jsonNode = readJson()
        val utbetalingshistorikk = Utbetalingshistorikk(jsonNode["sykmeldingsperioder"][1])

        assertEquals(LocalDate.of(2018, 12, 3), utbetalingshistorikk.fom)
        assertEquals(LocalDate.of(2019, 4, 11), utbetalingshistorikk.tom)
        assertEquals("050", utbetalingshistorikk.grad)
        assertNotNull(utbetalingshistorikk.inntektsopplysninger)
        assertNotNull(utbetalingshistorikk.utbetalteSykeperioder)

    }

    @Test
    internal fun inntekt() {
        val jsonNode = readJson()
        val inntektsopplysninger = Utbetalingshistorikk(jsonNode["sykmeldingsperioder"][1]).inntektsopplysninger

        assertEquals(LocalDate.of(2018, 12, 10), inntektsopplysninger[0].dato)
        assertEquals("88888888", inntektsopplysninger[0].orgnummer)
        assertEquals(12788.00, inntektsopplysninger[0].inntekt)

    }

    private fun readJson() = objectMapper.readTree(
            File("src/test/resources/infotrygdResponse.json").readText())
}
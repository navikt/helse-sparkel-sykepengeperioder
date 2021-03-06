package no.nav.helse.sparkel.sykepengeperioder

import no.nav.helse.rapids_rivers.RapidApplication
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.sparkel.sykepengeperioder.infotrygd.AzureClient
import no.nav.helse.sparkel.sykepengeperioder.infotrygd.InfotrygdClient
import java.io.File
import java.io.FileNotFoundException

fun main() {
    val app = createApp(System.getenv())
    app.start()
}

internal fun createApp(env: Map<String, String>): RapidsConnection {
    val azureClient = AzureClient(
        tenantUrl = "${env.getValue("AZURE_TENANT_BASEURL")}/${env.getValue("AZURE_TENANT_ID")}",
        clientId = "/var/run/secrets/nais.io/azure/client_id".readFile() ?: env.getValue("AZURE_CLIENT_ID"),
        clientSecret = "/var/run/secrets/nais.io/azure/client_secret".readFile() ?: env.getValue("AZURE_CLIENT_SECRET")
    )
    val infotrygdClient = InfotrygdClient(
        baseUrl = env.getValue("INFOTRYGD_URL"),
        accesstokenScope = env.getValue("INFOTRYGD_SCOPE"),
        azureClient = azureClient
    )
    val infotrygdService = InfotrygdService(infotrygdClient)

    return RapidApplication.create(env).apply {
        Sykepengehistorikkløser(this, infotrygdService)
        Utbetalingsperiodeløser(this, infotrygdService)
    }
}

private fun String.readFile() =
    try {
        File(this).readText(Charsets.UTF_8)
    } catch (err: FileNotFoundException) {
        null
    }
